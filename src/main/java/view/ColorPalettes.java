package view;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class ColorPalettes {
    private ColorPalettes() {}

    public static final List<Color> ALL = List.of(
            new Color(0x1F77B4), new Color(0xFF7F0E), new Color(0x2CA02C),
            new Color(0xD62728), new Color(0x9467BD), new Color(0x8C564B),
            new Color(0xE377C2), new Color(0x7F7F7F), new Color(0xBCBD22),
            new Color(0x17BECF), new Color(0x0E4D92), new Color(0xF28E2B),
            new Color(0x59A14F), new Color(0xE15759), new Color(0xB07AA1),
            new Color(0x9C755F), new Color(0xBAB0AC), new Color(0x76B7B2),
            new Color(0xF1CE63), new Color(0x4E79A7), new Color(0xA0CBE8),
            new Color(0xFF9DA7), new Color(0x86BCB6), new Color(0xFABFD2)
    );

    public static List<List<Color>> pages(int n) {
        List<List<Color>> out = new ArrayList<>();
        for (int i = 0; i < ALL.size(); i += n) {
            out.add(ALL.subList(i, Math.min(ALL.size(), i + n)));
        }
        return out;
    }
}
