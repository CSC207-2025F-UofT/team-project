package usecase.savenotebook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import data.InMemoryLabDataAccessObject;
import entity.LabModule;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class SaveNotebookInteractorTest {
  private final LabModule module = new LabModule("Pre-lab", LocalDate.of(2026, 11, 30), "",
      List.of(), List.of(), null, List.of());
  private SaveNotebookOutputData output;
  private String error;
  private final SaveNotebookInteractor interactor = new SaveNotebookInteractor(
      new InMemoryLabDataAccessObject(List.of(module)), new SaveNotebookOutputBoundary() {
        @Override
        public void prepareSuccessView(SaveNotebookOutputData out) {
          output = out;
        }

        @Override
        public void prepareFailView(String message) {
          error = message;
        }
      });

  @Test
  void savesTheNotebookAndMarksTheModuleEdited() {
    interactor.execute(new SaveNotebookInputData("Pre-lab", "<b>0.52 g</b>"));
    assertEquals("Pre-lab", output.getModuleTitle());
    assertEquals("<b>0.52 g</b>", module.getNotebook());
    assertTrue(module.getLastEdited().isPresent());
    assertNull(error);
  }

  @Test
  void unknownModuleFailsAndSavesNothing() {
    interactor.execute(new SaveNotebookInputData("Pre-lab 9", "notes"));
    assertEquals("There is no module called Pre-lab 9.", error);
    assertNull(output);
    assertEquals("", module.getNotebook());
  }
}
