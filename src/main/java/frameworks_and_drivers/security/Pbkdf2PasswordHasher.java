package frameworks_and_drivers.security;
import use_case.signup.PasswordHasher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/** JDK PBKDF2-HMAC-SHA256 adapter with a fresh random salt per password. */
public final class Pbkdf2PasswordHasher implements PasswordHasher {
    private static final int ITERATIONS=210_000, KEY_LENGTH=256;
    private final SecureRandom random=new SecureRandom();
    @Override public String hash(String rawPassword){
        if(rawPassword==null)throw new IllegalArgumentException("Password is required.");
        byte[] salt=new byte[16]; random.nextBytes(salt);
        return ITERATIONS+":"+Base64.getEncoder().encodeToString(salt)+":"+Base64.getEncoder().encodeToString(derive(rawPassword.toCharArray(),salt,ITERATIONS));
    }
    @Override public boolean matches(String rawPassword,String encodedHash){
        if(rawPassword==null||encodedHash==null)return false;
        try{
            String[] parts=encodedHash.split(":");
            int iterations=Integer.parseInt(parts[0]);
            byte[] salt=Base64.getDecoder().decode(parts[1]), expected=Base64.getDecoder().decode(parts[2]);
            return MessageDigest.isEqual(expected,derive(rawPassword.toCharArray(),salt,iterations));
        }catch(RuntimeException exception){return false;}
    }
    private byte[] derive(char[] password,byte[] salt,int iterations){
        PBEKeySpec spec=new PBEKeySpec(password,salt,iterations,KEY_LENGTH);
        try{return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();}
        catch(Exception exception){throw new IllegalStateException("Password hashing is unavailable.",exception);}
        finally{spec.clearPassword();}
    }
}
