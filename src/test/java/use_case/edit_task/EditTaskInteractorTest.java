package use_case.edit_task;

import data_access.InMemoryTaskDataAccess;
import entity.StudentTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EditTaskInteractorTest {
    private static final int USER_ID = 7;
    private static final int TASK_ID = 3;
    private static final LocalDate DEADLINE = LocalDate.of(2026, 12, 10);

    private final StudentTask original = new StudentTask(
            TASK_ID, USER_ID, "CSC207 assignment", "Write tests", DEADLINE, 4.5, (byte) 20, (byte) 2);
    private final InMemoryTaskDataAccess dao = new InMemoryTaskDataAccess(List.of(original));
    private final RecordingPresenter presenter = new RecordingPresenter();
    private final EditTaskInteractor interactor = new EditTaskInteractor(dao, presenter);

    private EditTaskInputData editProgress(int userId, int taskId, int progress) {
        return new EditTaskInputData(taskId, userId, original.getTitle(), original.getDescription(),
                original.getDeadline(), original.getEstimatedHoursToCompleteTask(),
                progress, original.getDifficultyLevel());
    }

    @ParameterizedTest
    @ValueSource(ints = {50, 0, 100})
    void savesEditedProgressAndPresentsSavedFields(int progress) {
        interactor.execute(editProgress(USER_ID, TASK_ID, progress));

        StudentTask saved = dao.findById(USER_ID, TASK_ID).orElseThrow();
        assertEquals(progress, saved.getCompletionProgress());
        assertEquals(TASK_ID, saved.getUniqueTaskId());
        assertEquals(USER_ID, saved.getUniqueUserId());
        assertEquals("CSC207 assignment", saved.getTitle());
        assertEquals("Write tests", saved.getDescription());
        assertEquals(DEADLINE, saved.getDeadline());
        assertEquals(4.5, saved.getEstimatedHoursToCompleteTask());
        assertEquals(2, saved.getDifficultyLevel());

        assertNull(presenter.error);
        EditTaskOutputData result = presenter.result;
        assertNotNull(result);
        assertEquals(saved.getUniqueTaskId(), result.getUniqueTaskId());
        assertEquals(saved.getUniqueUserId(), result.getUniqueUserId());
        assertEquals(saved.getTitle(), result.getTitle());
        assertEquals(saved.getDescription(), result.getDescription());
        assertEquals(saved.getDeadline(), result.getDeadline());
        assertEquals(saved.getEstimatedHoursToCompleteTask(), result.getEstimatedHoursToCompleteTask());
        assertEquals(saved.getCompletionProgress(), result.getCompletionProgress());
        assertEquals(saved.getDifficultyLevel(), result.getDifficultyLevel());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 101, 256})
    void rejectsInvalidProgressBeforeConvertingToByte(int progress) {
        interactor.execute(editProgress(USER_ID, TASK_ID, progress));

        assertEquals("Completion progress must be an integer between 0 and 100.", presenter.error);
        assertNull(presenter.result);
        assertEquals(20, dao.findById(USER_ID, TASK_ID).orElseThrow().getCompletionProgress());
    }

    @Test
    void reportsMissingTask() {
        int missingTaskId = TASK_ID + 1;
        interactor.execute(editProgress(USER_ID, missingTaskId, 50));

        assertEquals("Task not found.", presenter.error);
        assertNull(presenter.result);
        assertTrue(dao.findById(USER_ID, missingTaskId).isEmpty());
        assertEquals(20, dao.findById(USER_ID, TASK_ID).orElseThrow().getCompletionProgress());
    }

    @Test
    void cannotEditAnotherUsersTask() {
        interactor.execute(editProgress(USER_ID + 1, TASK_ID, 50));

        assertEquals("Task not found.", presenter.error);
        assertNull(presenter.result);
        assertEquals(20, dao.findById(USER_ID, TASK_ID).orElseThrow().getCompletionProgress());
    }

    @Test
    void reportsFailureWhenDaoCannotSave() {
        InMemoryTaskDataAccess rejectingDao = new InMemoryTaskDataAccess(List.of(original)) {
            @Override
            public boolean update(StudentTask updatedTask) {
                return false;
            }
        };
        EditTaskInteractor edit = new EditTaskInteractor(rejectingDao, presenter);

        edit.execute(editProgress(USER_ID, TASK_ID, 50));

        assertEquals("Task could not be updated.", presenter.error);
        assertNull(presenter.result);
        assertEquals(20, rejectingDao.findById(USER_ID, TASK_ID).orElseThrow().getCompletionProgress());
    }

    private static class RecordingPresenter implements EditTaskOutputBoundary {
        private EditTaskOutputData result;
        private String error;

        @Override
        public void prepareSuccessView(EditTaskOutputData outputData) {
            result = outputData;
        }

        @Override
        public void prepareFailView(String message) {
            error = message;
        }
    }
}
