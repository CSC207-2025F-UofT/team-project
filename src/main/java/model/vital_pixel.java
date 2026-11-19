package model;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.nio.file.Path;

/** model.py 的 vital_pixel；整块调用导出的 cnn 方法（含残差块和二维位置编码）。单线程使用。 */
public final class vital_pixel implements AutoCloseable {
    private final ModelRuntime runtime;
    private final boolean ownsRuntime;
    private boolean closed;

    public vital_pixel(Path filename) throws IOException, MalformedModelException {
        this(new ModelRuntime(filename), true);
    }

    /** vital_mini 注入同一份模型；此 CNN 不负责关闭共享运行时。 */
    vital_pixel(ModelRuntime runtime) {
        this(runtime, false);
    }

    private vital_pixel(ModelRuntime runtime, boolean ownsRuntime) {
        this.runtime = runtime;
        this.ownsRuntime = ownsRuntime;
    }

    public NDManager manager() {
        if (closed) throw new IllegalStateException("vital_pixel 已关闭");
        return runtime.manager();
    }

    public Device device() { return manager().getDevice(); }

    /** 已预处理的输入：[B,3,H,W]，H 和 W 都是 16 的倍数（多行公式更高）；不重复缩放或反色。 */
    public NDArray forward(NDArray image) throws TranslateException {
        try (NDManager scope = manager().newSubManager()) {
            NDArray output = forwardIn(scope, image);
            output.attach(manager());
            return output;
        }
    }

    NDArray forwardIn(NDManager scope, NDArray image) throws TranslateException {
        manager();
        Shape shape = image.getShape();
        if (image.getDataType() != DataType.FLOAT32 || shape.dimension() != 4
                || shape.get(0) < 1 || shape.get(1) != 3
                || shape.get(2) < 16 || shape.get(2) % 16 != 0
                || shape.get(3) < 16 || shape.get(3) % 16 != 0) {
            throw new IllegalArgumentException("image 必须为 FLOAT32 [B,3,H,W]，H 和 W 都是 16 的正整数倍");
        }
        // 整个 Python cnn.forward（卷积、残差块、池化、二维位置编码）作为一个导出方法调用。
        return runtime.callPixel(scope, "cnn", runtime.copyInput(scope, image));
    }

    @Override
    public void close() {
        if (!closed) {
            closed = true;
            if (ownsRuntime) runtime.close();
        }
    }
}
