package use_case.accept_quest;

/**
 * What the "Do this quest" buttons call. Those buttons live on other people's
 * screens (catalogue, recommendations, map), so they all go through this.
 */
public interface AcceptQuestInputBoundary {
    void execute(AcceptQuestInputData inputData);
}
