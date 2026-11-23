package interface_adapter.challenge;

import java.util.ArrayList;
import java.util.List;
import use_case.challenge.ChallengeCourse;
import use_case.challenge.ChallengeOutputBoundary;
import use_case.challenge.ChallengeOutputData;

public final class ChallengePresenter implements ChallengeOutputBoundary {
    private final ChallengeViewModel model;
    public ChallengePresenter(ChallengeViewModel model) { this.model = model; }

    @Override public void present(ChallengeOutputData output) {
        List<ChallengeViewModel.CourseItem> courses = new ArrayList<>();
        for (ChallengeCourse course : output.overview.getCourses()) {
            String status = course.getResult().map(result -> "Completed — "
                    + result.getPreparationPoints() + " preparation points").orElse("Not completed");
            courses.add(new ChallengeViewModel.CourseItem(course.getId(), course.getName() + " — " + status));
        }
        String result = output.result == null ? "" : output.result.getCorrectCount() + " / "
                + output.result.getQuestionCount() + " correct\nPreparation points for this course: "
                + output.result.getPreparationPoints() + "\nThis challenge is complete. Viewing it again "
                + "does not spend energy or award extra points.";
        String instructions = output.questionCount + " questions · " + output.energyCost
                + " energy · 1 preparation point per correct answer.\n"
                + "Confirm to load questions and start. If loading fails, no energy is spent.\n"
                + "Leaving after the challenge starts keeps the energy cost and awards no points.";
        model.setState(new ChallengeViewModel.State(ChallengeViewModel.Screen.valueOf(output.stage.name()),
                "Energy: " + output.overview.getEnergy(), courses,
                output.course == null ? "" : output.course.getId(),
                output.course == null ? "" : output.course.getName(), output.attemptId,
                output.questionIndex, output.questionCount, instructions, output.questionText,
                output.options, output.feedback, result, ""));
    }

    @Override public void presentError(String message) { model.setState(model.getState().withError(message)); }
}
