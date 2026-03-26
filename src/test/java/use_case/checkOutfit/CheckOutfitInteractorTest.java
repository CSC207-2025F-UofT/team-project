package use_case.checkOutfit;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CheckOutfitInteractorTest {

    /** Presenter spy: capture what interactor outputs. */
    static class CapturingPresenter implements CheckOutfitOutputBoundary {
        CheckOutfitOutputData successData;
        String failMessage;
        int goBackCalls = 0;

        @Override
        public void prepareSuccessView(CheckOutfitOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void prepareFailView(String error) {
            this.failMessage = error;
        }

        @Override
        public void goBack() {
            goBackCalls++;
        }
    }

    // -------------------- Robust helpers (high correctness) --------------------

    /**
     * Build CheckOutfitInputData WITHOUT assuming constructor parameter order.
     * We try:
     *  1) every 3-String constructor
     *  2) all 6 permutations of (outfitId, weatherMain, temperature)
     *  3) validate by getters used in Interactor: getOutfitId/getWeatherMain/getTemperature
     *  4) fallback: no-arg + setters (if exists)
     */
    private static CheckOutfitInputData makeInput(String outfitId, String weatherMain, String temperature) {
        List<String> vals = Arrays.asList(outfitId, weatherMain, temperature);
        int[][] perms = {
                {0, 1, 2}, {0, 2, 1},
                {1, 0, 2}, {1, 2, 0},
                {2, 0, 1}, {2, 1, 0}
        };

        // Try all (String,String,String) constructors + permutations
        for (Constructor<?> c : CheckOutfitInputData.class.getDeclaredConstructors()) {
            if (c.getParameterCount() == 3
                    && c.getParameterTypes()[0] == String.class
                    && c.getParameterTypes()[1] == String.class
                    && c.getParameterTypes()[2] == String.class) {
                c.setAccessible(true);

                for (int[] p : perms) {
                    try {
                        CheckOutfitInputData in =
                                (CheckOutfitInputData) c.newInstance(vals.get(p[0]), vals.get(p[1]), vals.get(p[2]));

                        boolean ok = outfitId.equals(in.getOutfitId())
                                && weatherMain.equals(in.getWeatherMain())
                                && ((temperature == null && in.getTemperature() == null)
                                || (temperature != null && temperature.equals(in.getTemperature())));

                        if (ok) return in;
                    } catch (Exception ignored) {
                        // try next permutation/constructor
                    }
                }
            }
        }

        // Fallback: no-arg + setters (if the class is mutable)
        try {
            Constructor<?> noArg = CheckOutfitInputData.class.getDeclaredConstructor();
            noArg.setAccessible(true);
            CheckOutfitInputData in = (CheckOutfitInputData) noArg.newInstance();

            Method setOutfitId = findMethod(CheckOutfitInputData.class, "setOutfitId", String.class);
            Method setWeatherMain = findMethod(CheckOutfitInputData.class, "setWeatherMain", String.class);
            Method setTemperature = findMethod(CheckOutfitInputData.class, "setTemperature", String.class);

            if (setOutfitId != null) setOutfitId.invoke(in, outfitId);
            if (setWeatherMain != null) setWeatherMain.invoke(in, weatherMain);
            if (setTemperature != null) setTemperature.invoke(in, temperature);

            // last validation
            assertEquals(outfitId, in.getOutfitId(), "Fallback setters didn't set outfitId correctly.");
            assertEquals(weatherMain, in.getWeatherMain(), "Fallback setters didn't set weatherMain correctly.");
            assertEquals(temperature, in.getTemperature(), "Fallback setters didn't set temperature correctly.");
            return in;

        } catch (Exception e) {
            throw new AssertionError(
                    "Cannot construct CheckOutfitInputData reliably. " +
                            "Your InputData likely has a different constructor/setters.", e);
        }
    }

    private static Method findMethod(Class<?> cls, String name, Class<?>... params) {
        try {
            Method m = cls.getMethod(name, params);
            m.setAccessible(true);
            return m;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Extract the message string from CheckOutfitOutputData robustly.
     * Works even if getter name is unknown (scans String-returning getters/fields).
     */
    private static String extractMessage(CheckOutfitOutputData outputData) {
        if (outputData == null) return null;

        // 1) common getter names
        List<String> names = Arrays.asList(
                "getMessage", "getText", "getResult", "getOutput", "getFeedback", "getOutfitMessage"
        );
        for (String n : names) {
            try {
                Method m = outputData.getClass().getMethod(n);
                if (m.getReturnType() == String.class && m.getParameterCount() == 0) {
                    String s = (String) m.invoke(outputData);
                    if (s != null) return s;
                }
            } catch (Exception ignored) {}
        }

        // 2) scan all public no-arg String getters; prefer one containing "Weather:"
        String best = null;
        for (Method m : outputData.getClass().getMethods()) {
            if (m.getParameterCount() == 0 && m.getReturnType() == String.class && !m.getName().equals("toString")) {
                try {
                    String s = (String) m.invoke(outputData);
                    if (s == null) continue;
                    if (s.contains("Weather:")) return s;
                    if (best == null || s.length() > best.length()) best = s;
                } catch (Exception ignored) {}
            }
        }
        if (best != null) return best;

        // 3) scan String fields; prefer one containing "Weather:"
        try {
            for (Field f : outputData.getClass().getDeclaredFields()) {
                if (f.getType() == String.class) {
                    f.setAccessible(true);
                    String s = (String) f.get(outputData);
                    if (s == null) continue;
                    if (s.contains("Weather:")) return s;
                    if (best == null || s.length() > best.length()) best = s;
                }
            }
        } catch (Exception ignored) {}

        // 4) last resort
        return outputData.toString();
    }

    private static void assertSuccessOnly(CapturingPresenter p) {
        assertNull(p.failMessage, "Interactor ended in FAIL unexpectedly: " + p.failMessage);
        assertNotNull(p.successData, "Expected prepareSuccessView to be called.");
    }

    private static void assertFailOnly(CapturingPresenter p) {
        assertNull(p.successData, "Interactor ended in SUCCESS unexpectedly.");
        assertNotNull(p.failMessage, "Expected prepareFailView to be called.");
    }

    // ------------------------------ Tests ------------------------------

    @Test
    void execute_failsWhenOutfitUnknown() {
        CapturingPresenter presenter = new CapturingPresenter();
        CheckOutfitInteractor interactor = new CheckOutfitInteractor(presenter);

        CheckOutfitInputData in = makeInput("NOT_A_REAL_OUTFIT", "Clear", "20");
        interactor.execute(in);

        assertFailOnly(presenter);
        assertEquals("Missing outfit or temperature.", presenter.failMessage);
    }

    @Test
    void execute_failsWhenTempNull() {
        CapturingPresenter presenter = new CapturingPresenter();
        CheckOutfitInteractor interactor = new CheckOutfitInteractor(presenter);

        CheckOutfitInputData in = makeInput("HOODIE", "Clear", null);
        interactor.execute(in);

        assertFailOnly(presenter);
        assertEquals("Missing outfit or temperature.", presenter.failMessage);
    }

    @Test
    void execute_failsWhenTempHasNoNumber() {
        CapturingPresenter presenter = new CapturingPresenter();
        CheckOutfitInteractor interactor = new CheckOutfitInteractor(presenter);

        CheckOutfitInputData in = makeInput("HOODIE", "Clear", "abc");
        interactor.execute(in);

        assertFailOnly(presenter);
        assertEquals("Missing outfit or temperature.", presenter.failMessage);
    }

    @Test
    void execute_wetRule_blocksLightOutfitInSnow_successEarly() {
        CapturingPresenter presenter = new CapturingPresenter();
        CheckOutfitInteractor interactor = new CheckOutfitInteractor(presenter);

        CheckOutfitInputData in = makeInput("T_SHIRT", "Snow", " -5°C ");
        interactor.execute(in);

        assertSuccessOnly(presenter);
        String msg = extractMessage(presenter.successData);
        assertNotNull(msg);

        String low = msg.toLowerCase();
        assertTrue(low.contains("not recommended for wet/snowy"), msg);
        assertTrue(low.contains("try:"), msg);
        assertTrue(low.contains("puffer"), msg); // "Try: Coat / Trench / Puffer + Jeans."
    }

    @Test
    void execute_goodChoice_whenWarmthMatchesTemperature() {
        CapturingPresenter presenter = new CapturingPresenter();
        CheckOutfitInteractor interactor = new CheckOutfitInteractor(presenter);

        // temp 20 -> recommended 2; HOODIE warmth 2 => abs(diff)<=1 => good choice
        CheckOutfitInputData in = makeInput("HOODIE", "Clear", "Temp: 20°C");
        interactor.execute(in);

        assertSuccessOnly(presenter);
        String msg = extractMessage(presenter.successData);
        assertNotNull(msg);

        assertTrue(msg.contains("Weather:"), msg);
        assertTrue(msg.contains("Temperature:"), msg);
        assertTrue(msg.toLowerCase().contains("good choice"), msg);
    }

    @Test
    void execute_tooLight_suggestsCoatFor5C() {
        CapturingPresenter presenter = new CapturingPresenter();
        CheckOutfitInteractor interactor = new CheckOutfitInteractor(presenter);

        // temp 5 -> recommended 4; T_SHIRT chosen 1 => diff = -3 (<-1) => too light -> suggestWarmer(4) => Coat/Trench
        CheckOutfitInputData in = makeInput("T_SHIRT", "Clear", "5.0°C");
        interactor.execute(in);

        assertSuccessOnly(presenter);
        String msg = extractMessage(presenter.successData);
        assertNotNull(msg);

        String low = msg.toLowerCase();
        assertTrue(low.contains("too light"), msg);
        assertTrue(low.contains("suggestion:"), msg);
        assertTrue(low.contains("coat"), msg);
    }

    @Test
    void execute_tooLight_veryCold_suggestsPuffer() {
        CapturingPresenter presenter = new CapturingPresenter();
        CheckOutfitInteractor interactor = new CheckOutfitInteractor(presenter);

        // temp -1 -> recommended 5; T_SHIRT chosen 1 => diff=-4 => suggestWarmer(5) => Puffer Jacket
        CheckOutfitInputData in = makeInput("T_SHIRT", "Clear", "Temp: -1°C");
        interactor.execute(in);

        assertSuccessOnly(presenter);
        String msg = extractMessage(presenter.successData);
        assertNotNull(msg);

        String low = msg.toLowerCase();
        assertTrue(low.contains("too light"), msg);
        assertTrue(low.contains("puffer"), msg);
    }

    @Test
    void execute_tooWarm_hotDay_suggestsTShirtShorts() {
        CapturingPresenter presenter = new CapturingPresenter();
        CheckOutfitInteractor interactor = new CheckOutfitInteractor(presenter);

        // temp 30 -> recommended 1; PUFFER chosen 5 => diff=4 (>1) => too warm -> suggestCooler(1) => T-Shirt/Shorts
        CheckOutfitInputData in = makeInput("COAT", "Clear", "30 C"); // COAT warmth 4 also diff=3 -> too warm
        interactor.execute(in);

        assertSuccessOnly(presenter);
        String msg = extractMessage(presenter.successData);
        assertNotNull(msg);

        String low = msg.toLowerCase();
        assertTrue(low.contains("too warm"), msg);
        assertTrue(low.contains("t-shirt"), msg);
        assertTrue(low.contains("shorts"), msg);
    }

    @Test
    void execute_tooWarm_whenRecommended2_suggestsHoodieJeans() {
        CapturingPresenter presenter = new CapturingPresenter();
        CheckOutfitInteractor interactor = new CheckOutfitInteractor(presenter);

        // temp 20 -> recommended 2; COAT chosen 4 => diff=2 => too warm -> suggestCooler(2) => Hoodie/Jeans
        CheckOutfitInputData in = makeInput("COAT", "Clear", "20");
        interactor.execute(in);

        assertSuccessOnly(presenter);
        String msg = extractMessage(presenter.successData);
        assertNotNull(msg);

        String low = msg.toLowerCase();
        assertTrue(low.contains("too warm"), msg);
        assertTrue(low.contains("hoodie"), msg);
        assertTrue(low.contains("jeans"), msg);
    }

    @Test
    void execute_tooWarm_whenRecommended3_suggestsSweater() {
        CapturingPresenter presenter = new CapturingPresenter();
        CheckOutfitInteractor interactor = new CheckOutfitInteractor(presenter);

        // temp 12 -> recommended 3; COAT chosen 4 => diff=1 => actually good choice (abs<=1)
        // We need diff > 1 so pick PUFFER(5): 5-3 = 2 => too warm -> suggestCooler(3) => Sweater
        CheckOutfitInputData in = makeInput("PUFFER", "Clear", "12");
        interactor.execute(in);

        assertSuccessOnly(presenter);
        String msg = extractMessage(presenter.successData);
        assertNotNull(msg);

        String low = msg.toLowerCase();
        assertTrue(low.contains("too warm"), msg);
        assertTrue(low.contains("sweater"), msg);
    }

    @Test
    void goBack_delegatesToPresenter() {
        CapturingPresenter presenter = new CapturingPresenter();
        CheckOutfitInteractor interactor = new CheckOutfitInteractor(presenter);

        interactor.goBack();

        assertEquals(1, presenter.goBackCalls);
    }
}
