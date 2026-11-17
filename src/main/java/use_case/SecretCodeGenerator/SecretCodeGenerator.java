package use_case.SecretCodeGenerator;

//     two class will implement this interface; RandomSecretCodeGenerator and FixedSecretCodeGenerator
//     first one will be used for the actual game and the latter will be used in testing

public interface SecretCodeGenerator {
    /** generate secret codes for the Candle game
     * @return the secret code as String of digits
     */
    String generate();
}
