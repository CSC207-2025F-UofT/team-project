package use_case.SecretCodeGenerator;


import entities.CandleType;
import entities.Guess;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SecretCodeFeedbackTest {
    @Test
    void allBright() {

        Guess guess = new Guess("12345");
        List<CandleType> feedback = guess.generateFeedback("12345");

        assertEquals(List.of(CandleType.BRIGHT,
                CandleType.BRIGHT,
                CandleType.BRIGHT,
                CandleType.BRIGHT,
                CandleType.BRIGHT), feedback);
    };
    @Test
    void allDim() {

        Guess guess = new Guess("12345");
        List<CandleType> feedback = guess.generateFeedback("12345");

        assertEquals(List.of(CandleType.BRIGHT,
                CandleType.BRIGHT,
                CandleType.BRIGHT,
                CandleType.BRIGHT,
                CandleType.BRIGHT), feedback);
    }
};



