package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addLoggedInView()
                .addCreateJobGroupView()
                .addAddCourseView()
                .addSkillListView()
                .addJobListView()
                .addSignupUseCase()
                .addLoginUseCase()
                .addLogoutUseCase()
                .addCreateJobGroupUseCase()
                .addAddCourseUseCase()
                .addCourseAnalysisUseCase()
                .addJobMatchUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
