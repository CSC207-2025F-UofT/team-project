package quiz_system.entity;

public final class AnswerOption {
    private final int optionId;
    private final String text;

    public AnswerOption(int optionId, String text) {
        this.optionId = optionId;
        this.text = text;
    }

    public int getOptionId() {
        return optionId;
    }

    public String getText() {
        return text;
    }
}
