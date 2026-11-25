package use_case.add_side_quest;

import entity.SideQuest;

public class AddSideQuestInteractor implements AddSideQuestInputBoundary {

    private final AddSideQuestDataAccessInterface dataAccess;

    public AddSideQuestInteractor(AddSideQuestDataAccessInterface dataAccess) {
        this.dataAccess = dataAccess;
    }
    @Override
    public void execute(AddSideQuestInputData inputData){
        String budget;
        String expectedTime;

        if (inputData.getQuestDescription() == null || inputData.getQuestDescription().trim().isEmpty()){
            // outputBoundary.prepareFailView("Please enter a description for your Side Quest."))
            return;
        } else if (inputData.getBudget() < 0) {
            // outputBoundary.prepareFailView("Budget cannot be a negative value.");
            return;
        } else if (inputData.getExpectedTime() <= 0) {
            // outputBoundary.prepareFailView("Expected time (in minutes) must be greater than 0.");
            return;
        } else if (inputData.getExp() <= 0) {
            // outputBoundary.prepareFailView("Exp for the side quest must be a positive value.");
            return;
        } else if (!"indoor".equals(inputData.getIsIndoor()) && !"outdoor".equals(inputData.getIsIndoor())
                && !"either".equals(inputData.getIsIndoor())) {
            // outputBoundary.prepareFailView("Please select a valid activity type.");
            return;
        } else if (inputData.getLocation() == null) {
            // outputBoundary.prepareFailView("Please select a location in which your Side Quest occurred at.");
            return;
        } else if (inputData.getInterests() == null || inputData.getInterests().isEmpty()) {
            // outputBoundary.prepareFailView("Please select at least one interest.");
            return;
        } else if (inputData.getInterests().size() > 5) {
            // Ensure that the tags for each side quest cannot exceed 5, so it doesn't become bloated with tags.
            // outputBoundary.prepareFailView("Please select up to 5 tags.");
            return;
        }

        if (inputData.getBudget() == 0){
            budget = "free";
        } else if (inputData.getBudget() < 20) {
            budget = "less_than_twenty";
        } else if (inputData.getBudget() < 50) {
            budget = "less_than_fifty";
        } else {
            budget = "over_fifty";
        }

        if (inputData.getExpectedTime() < 10){
            expectedTime = "under_10_minutes";
        } else if (inputData.getExpectedTime() < 30) {
            expectedTime = "under_30_minutes";
        } else if (inputData.getExpectedTime() < 60) {
            expectedTime = "under_60_minutes";
        } else {
            expectedTime = "over_60_minutes";
        }

//        int generated_quest_id
//        int last_id = dataAccess.getLastId()
//        if (last_id is Null){
//            generated_quest_id = 0
//        } else {
//            String generatedQuestId = String(int(String.valueOf(dataAccess.getLastId()) + 1))
//            ;
//        }
        // TODO: uncomment the lines above for assigning the generated quest id
        SideQuest sideQuest = new SideQuest(inputData.getQuestDescription(), "0",
                inputData.getIsIndoor(), budget, expectedTime, inputData.getExp(), 0,
                inputData.getLocation(), inputData.getInterests(), false
        );

        dataAccess.save(sideQuest);
    }
}

