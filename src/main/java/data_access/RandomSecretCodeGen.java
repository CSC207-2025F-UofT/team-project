package data_access;

import use_case.SecretCodeGenerator.SecretCodeGenerator;


public class RandomSecretCodeGen implements SecretCodeGenerator {
    private final String AlphaNumericString;
    private final int length;

    public RandomSecretCodeGen(StringBuilder secretCode) {
        this.AlphaNumericString = "0123456789";
        this.length = 5;
    }

    @Override
    public String generate() {
        StringBuilder secretCode = new StringBuilder();

        for (int i = 0; i < length; i++) {

            // pick a random position in AlphaNumeric position
            int index
                    = (int) (AlphaNumericString.length() * Math.random());

            // add Character one by one in end of the secretCode
            secretCode.append(AlphaNumericString.charAt(index));
        }

        return secretCode.toString();
    }
}