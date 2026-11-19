package pdf_editor.adapter;

import data_access.InMemoryFunctionDataAccessObject;
import interface_adapter.MathLabState;
import interface_adapter.MathLabViewModel;
import interface_adapter.evaluate.EvaluateController;
import interface_adapter.evaluate.EvaluatePresenter;
import org.junit.jupiter.api.Test;
import use_case.evaluate.EvaluateInteractor;

import static org.junit.jupiter.api.Assertions.*;

class HistoryFormulaCalculatorTest {
    @Test void existingUs2CalculatorRecomputesEditedFormulaWithoutResultPrompt() {
        MathLabViewModel model = new MathLabViewModel();
        var calculate = new HistoryFormulaCalculator(new EvaluateController(new EvaluateInteractor(
                new InMemoryFunctionDataAccessObject(), new EvaluatePresenter(model))), model);
        String result = calculate.calculate("2+3");
        assertEquals("5", result);
        assertEquals("9", calculate.calculate("4+5"));
    }

    @Test void handlesErrorAndDefinitionWithoutReusingStaleAnswer() {
        MathLabState state = new MathLabState();
        state.setResultText("= 12");
        assertEquals("12", HistoryFormulaCalculator.formattedResult(state));
        state.setErrorTitle("Invalid formula");
        assertEquals("", HistoryFormulaCalculator.formattedResult(state));
        state.setErrorTitle("");
        state.setDefinition(true);
        assertEquals("", HistoryFormulaCalculator.formattedResult(state));
    }
}
