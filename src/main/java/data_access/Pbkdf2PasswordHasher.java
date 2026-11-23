package data_access;

import use_case.user.PasswordHasher;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Pbkdf2PasswordHasher implements PasswordHasher{
    private static final int ITERATIONS = 600000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;

    @Override
    public String hash(String password){
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);

        byte[] hash = generateHash(password, salt, ITERATIONS);

        return ITERATIONS + ":" + Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }

    @Override
    public boolean verify(String password, String storedHash){
        try{
            String[] parts = storedHash.split(":");

            if (parts.length != 3){
                return false;
            }

            int iterations = Integer.parseInt(parts[0]);
            if (iterations < 1 || iterations > ITERATIONS){
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[1]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[2]);

            if (salt.length != SALT_LENGTH || expectedHash.length != KEY_LENGTH /8){
                return false;
            }
            byte[] actualHash = generateHash(password, salt, iterations);

            return java.security.MessageDigest.isEqual(expectedHash, actualHash);
        }catch (IllegalArgumentException| NullPointerException e){
            return false;
        }
    }

    private byte[] generateHash(String password, byte[] salt, int iterations){
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, KEY_LENGTH);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return factory.generateSecret(spec).getEncoded();
        }catch (Exception e){
            throw new IllegalStateException("Password hashing failed", e);
        }finally{
            ((PBEKeySpec) spec).clearPassword();
        }
    }
}
