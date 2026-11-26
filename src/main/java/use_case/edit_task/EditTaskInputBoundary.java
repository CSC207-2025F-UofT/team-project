package use_case.edit_task;

public interface EditTaskInputBoundary {

    /**
     * Executes the edit task use case.
     * @param editTaskInputData the input data.
     */
    void execute(EditTaskInputData editTaskInputData);

}
