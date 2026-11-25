package use_case.complete_quest;

/**
 * What the controller calls. The controller only knows this interface,
 * never the interactor class itself.
 *
 * Java note: an interface is a list of methods a class promises to have.
 * Closest Python idea is an abstract base class with only abstract methods.
 */
public interface CompleteQuestInputBoundary {
    void execute(CompleteQuestInputData inputData);
}
