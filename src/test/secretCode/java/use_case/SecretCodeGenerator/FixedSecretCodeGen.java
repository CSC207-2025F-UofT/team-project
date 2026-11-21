package use_case.SecretCodeGenerator;


public class FixedSecretCodeGen implements SecretCodeGenerator {
    private final String secretCode;

    public FixedSecretCodeGen(String secretCode){
        this.secretCode = secretCode;
    }

    @Override
    public String generate() {
        return secretCode;
    }
}
