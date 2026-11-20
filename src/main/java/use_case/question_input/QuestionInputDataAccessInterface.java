package use_case.question_input;

import entity.Question;
import entity.Team;

public interface QuestionInputDataAccessInterface {
    Question getCurrentQuestion();
    Team getCurrentTeam();
}
