package data_access;

import entity.Answer;
import entity.Board;
import entity.Question;
import entity.Team;
import use_case.load_question.LoadQuestionDataAccessInterface;
import use_case.question_input.QuestionInputDataAccessInterface;
import use_case.start_game.StartGameDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class DataAccessObject implements LoadQuestionDataAccessInterface, StartGameDataAccessInterface, QuestionInputDataAccessInterface {

    // TEMPORARY IMPLEMENTATION FOR TESTING PURPOSES
    @Override
    public boolean existsUnusedQuestion() {
        return false;
    }

    // TEMPORARY IMPLEMENTATION FOR TESTING PURPOSES
    @Override
    public Question getNextQuestion() {
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Balloon", null, 44));
        answers.add(new Answer("Tire", null, 25));
        answers.add(new Answer("Beach ball", null, 10));
        answers.add(new Answer("Ego", null, 7));
        answers.add(new Answer("Raft", null, 5));
        return new Question("Name something that can be inflated or deflated", answers);
    }
  
    // TODO: Implement StartGame use case functions
    @Override
    public boolean teamExists(String teamName){
        return false;
    }

    @Override
    public Team getTeam(String teamName){
        return new Team("TEMP.");
    }

    public Board getCurrentBoard() {
        return null;
    }
}
