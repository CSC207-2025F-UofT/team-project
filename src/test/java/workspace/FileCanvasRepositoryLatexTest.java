package workspace;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import workspace.data_access.FileCanvasRepository;
import workspace.domain.CanvasState;
import workspace.domain.SavedCanvas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileCanvasRepositoryLatexTest {
    @TempDir
    Path temp;

    private static CanvasState.Ink ink() {
        return new CanvasState.Ink(List.of(new CanvasState.Point(0, 0, 1), new CanvasState.Point(10, 40, 2)),
                0xff000000);
    }

    @Test
    void typesetAnswerKeepsItsTex() throws IOException {
        var formula = new CanvasState.Formula(List.of(0), "1/3", false, 50, 40, 30, 90, 62, 0xff000000, List.of(),
                "\\frac{1}{3}");
        var state = new CanvasState(List.of(ink()), List.of(formula), List.of(), 1, 0, 0);
        var repository = new FileCanvasRepository(temp);
        repository.write(new SavedCanvas("00000000-0000-0000-0000-00000000000a", "fractions", Instant.EPOCH, state));
        var loaded = repository.find("00000000-0000-0000-0000-00000000000a").orElseThrow().state().formulas().get(0);
        assertEquals("\\frac{1}{3}", loaded.latex());
        assertEquals("1/3", loaded.answer());
    }

    // file from before latex was saved, written byte for byte like the v3 writer did
    @Test
    void v3FilesStillOpenWithNoTex() throws IOException {
        Path file = temp.resolve("00000000-0000-0000-0000-0000000000b3.mlcanvas");
        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(file))) {
            out.writeInt(0x4d4c4333);
            out.writeUTF("00000000-0000-0000-0000-0000000000b3");
            out.writeUTF("old canvas");
            out.writeLong(0);
            out.writeDouble(1);
            out.writeDouble(0);
            out.writeDouble(0);
            out.writeInt(1);
            out.writeInt(0xff000000);
            out.writeFloat(4f);
            out.writeInt(2);
            for (int i = 0; i < 2; i++) {
                out.writeInt(i * 10);
                out.writeInt(i * 40);
                out.writeLong(i);
            }
            out.writeInt(1);
            out.writeInt(1);
            out.writeInt(0);
            out.writeUTF("3x2");
            out.writeBoolean(false);
            out.writeDouble(50);
            out.writeDouble(40);
            out.writeDouble(30);
            out.writeDouble(20);
            out.writeFloat(16);
            out.writeInt(0xff000000);
            out.writeInt(2);
            out.writeUTF("3x");
            out.writeBoolean(false);
            out.writeUTF("2");
            out.writeBoolean(true);
            out.writeInt(0);
        }
        var repository = new FileCanvasRepository(temp);
        var formula = repository.find("00000000-0000-0000-0000-0000000000b3").orElseThrow().state().formulas().get(0);
        assertEquals("", formula.latex());
        assertEquals(List.of(new CanvasState.TextPart("3x", false), new CanvasState.TextPart("2", true)),
                formula.textParts());
    }
}
