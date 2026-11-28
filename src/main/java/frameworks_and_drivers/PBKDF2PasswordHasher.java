package frameworks_and_drivers;
import use_case.password_hasher.PasswordHasher;

import java.security.SecureRandom;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PBKDF2PasswordHasher implements PasswordHasher {

    @Override
    public String generateSalt(){
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    @Override
    public String hash(String password, String salt) {
        byte[] saltBytes = Base64.getDecoder().decode(salt);
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 500000, 256);

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        }
        catch (GeneralSecurityException e) {
            throw new IllegalStateException("Hashing failed");
        }
        finally {
            spec.clearPassword();
        }
    }

    @Override
    public boolean verify(String password, String salt, String hash){
        return(hash.equals(hash(password, salt)));
    }
}
