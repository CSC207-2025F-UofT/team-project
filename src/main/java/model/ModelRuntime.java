package model;

import ai.djl.MalformedModelException;
import ai.djl.Model;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.NoopTranslator;
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

/** 内部基础设施：加载一份 .pt，管理张量和调用器；不编排网络计算流程。 */
final class ModelRuntime implements AutoCloseable {
    private static final Set<String> PIXEL_METHODS = Set.of("cnn");
    private static final Set<String> MINI_METHODS = Set.of(
            "encoder1", "encoder2", "decoder1", "decoder2", "decoder3", "decoder4", "decoder5", "final_norm", "head",
            "judge_decoder", "judge_norm", "process", "silu", "test", "decoder_start", "decoder_step");

    private final Model model;
    private final Predictor<NDList, NDList> predictor;
    private final NDManager manager;
    private boolean closed;

    ModelRuntime(Path filename) throws IOException, MalformedModelException {
        Path path = filename.toAbsolutePath();
        if (!Files.isRegularFile(path)) throw new IOException("TorchScript 文件不存在: " + path);
        if (System.getProperty("ENGINE_CACHE_DIR") == null && System.getenv("ENGINE_CACHE_DIR") == null
                && Files.isDirectory(Path.of(".runtime", "djl"))) {
            System.setProperty("ENGINE_CACHE_DIR", Path.of(".runtime", "djl").toAbsolutePath().toString());
        }
        Model loaded = Model.newInstance("model_ops", InferenceDevice.automatic(), "PyTorch");
        try {
            loaded.load(path.getParent(), path.getFileName().toString());
            manager = loaded.getNDManager().newSubManager();
            predictor = loaded.newPredictor(new NoopTranslator());
            model = loaded;
        } catch (IOException | MalformedModelException | RuntimeException | Error e) {
            loaded.close();
            throw e;
        }
    }

    NDManager manager() {
        if (closed) throw new IllegalStateException("模型运行时已关闭");
        return manager;
    }

    NDArray copyInput(NDManager scope, NDArray input) {
        NDArray copy = input.toDevice(manager().getDevice(), true);
        copy.attach(scope);
        return copy;
    }

    NDArray callPixel(NDManager scope, String method, NDArray... arguments) throws TranslateException {
        if (!PIXEL_METHODS.contains(method)) throw new IllegalArgumentException("不是 vital_pixel 的层: " + method);
        return single(method, invoke(scope, method, arguments));
    }

    NDArray callMini(NDManager scope, String method, NDArray... arguments) throws TranslateException {
        return single(method, callMiniList(scope, method, arguments));
    }

    /** 返回多个 Tensor 的方法（如 decoder_start / decoder_step），按导出时的元组顺序。 */
    NDList callMiniList(NDManager scope, String method, NDArray... arguments) throws TranslateException {
        if (!MINI_METHODS.contains(method)) throw new IllegalArgumentException("不是 vital_mini 的层: " + method);
        return invoke(scope, method, arguments);
    }

    private static NDArray single(String method, NDList outputs) {
        if (outputs.size() != 1) {
            outputs.close();
            throw new IllegalStateException(method + " 应返回一个 Tensor");
        }
        return outputs.singletonOrThrow();
    }

    private NDList invoke(NDManager scope, String method, NDArray... arguments) throws TranslateException {
        manager();
        try (NDArray marker = scope.create(0)) {
            marker.setName("module_method:" + method);
            NDList inputs = new NDList(marker);
            for (NDArray argument : arguments) inputs.add(argument);
            NDList outputs = predictor.predict(inputs);
            outputs.attach(scope);
            return outputs;
        }
    }

    @Override
    public void close() {
        if (!closed) {
            closed = true;
            try { predictor.close(); }
            finally {
                try { manager.close(); }
                finally { model.close(); }
            }
        }
    }
}
