package entities;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private String prompt;
    private List<String> choices;
    private int correctIndex;
    //We could potentially make an "answer" class with a "correct" boolean attribute

    public Question(String prompt, List<String> choices, int correctIndex) {
        this.prompt = prompt;
        List<String> copy_choices = new ArrayList<>(choices);
        if(copy_choices.size() < 2) {
            while(copy_choices.size() < 2) {
                copy_choices.add("Placeholder");
            }
            // add placeholder answers until we get at least two answer options

            System.out.println("Please enter a list of at least two answers when creating a question");
        }
        if(correctIndex < 0 || correctIndex >= choices.size()) {
            throw new IllegalArgumentException("Correct index is out of range for the provided choices.");
            //may want to change what happens when we input a correctIndex that does not work
        }

        this.correctIndex = correctIndex;
        this.choices = copy_choices;

        //The parameter answers can be empty and have any length
    }
    public String getPrompt() {
        return prompt;
    }
    public ArrayList<String> getChoices() {
        return new ArrayList<>(choices);
    }
    public int getCorrectIndex() {
        return correctIndex;
    }
    public String getCorrectAnswer() {
        return choices.get(correctIndex);
    }
}
