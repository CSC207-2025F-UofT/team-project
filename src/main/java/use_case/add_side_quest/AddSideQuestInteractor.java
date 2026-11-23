package use_case.add_side_quest;

import entity.SideQuest;

public class AddSideQuestInteractor implements AddSideQuestInputBoundary {

    private final AddSideQuestDataAccessInterface dataAccess;

    public AddSideQuestInteractor(AddSideQuestDataAccessInterface dataAccess) {
        this.dataAccess = dataAccess;
    }
    @Override
    public void execute(AddSideQuestInputData inputData){
        if (inputData.getName() == null || inputData.getName().trim().isEmpty()){
            // outputBoundary.prepareFailView("Please enter a Side Quest name.")
            return;
        } else if (inputData.getDescription() == null || inputData.getDescription().trim().isEmpty()) {
            // outputBoundary.prepareFailView("Please enter a description for your Side Quest.");
            return;
        } else if (inputData.getBudget() < 0) {
            // outputBoundary.prepareFailView("Budget cannot be a negative value.");
            return;
        } else if (inputData.getExpectedTime() <= 0) {
            // outputBoundary.prepareFailView("Expected time (in hours) must be greater than 0.");
            return;
        } else if (inputData.getExp() < 0) {
            // outputBoundary.prepareFailView("Exp for the side quest cannot be negative.");
            return;
        } else if (inputData.getLocation() == null) {
            // outputBoundary.prepareFailView("Please select a location in which your Side Quest occurred at.");
            return;
        } else if (inputData.getInterests() == null || inputData.getInterests().isEmpty()) {
            // outputBoundary.prepareFailView("Please select at least one interest.");
            return;
        }

        SideQuest sideQuest = new SideQuest(inputData.getName(), inputData.getDescription(), inputData.getPhotoUrl(),
                inputData.isIndoor(), inputData.getBudget(), inputData.getExpectedTime(), inputData.getExp(),
                inputData.getLocation(), inputData.getInterests()
        );

        dataAccess.save(sideQuest);
    }
}

