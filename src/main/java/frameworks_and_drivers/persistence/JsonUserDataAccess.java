package frameworks_and_drivers.persistence;
import entity.UserProfile;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.login.LoginDataAccessInterface;
import use_case.signup.SignUpUserDataAccessInterface;
import use_case.signup.PasswordHasher;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

/** Single JSON gateway for signup and login; stores password hashes, never raw passwords. */
public final class JsonUserDataAccess implements SignUpUserDataAccessInterface, LoginDataAccessInterface {
    private final Path path;
    private final PasswordHasher passwordHasher;
    public JsonUserDataAccess(Path path,PasswordHasher passwordHasher){this.path=path;this.passwordHasher=passwordHasher;}
    private JSONObject read(){
        try{
            if(!Files.exists(path)){
                JSONObject initial=new JSONObject().put("nextUserId",1).put("nextAccountId",1).put("profiles",new JSONArray());
                write(initial);return initial;
            }
            return new JSONObject(Files.readString(path));
        }catch(IOException exception){throw new IllegalStateException("Unable to read user profiles.",exception);}
    }
    private void write(JSONObject data){
        try{if(path.getParent()!=null)Files.createDirectories(path.getParent());Files.writeString(path,data.toString(2));}
        catch(IOException exception){throw new IllegalStateException("Unable to save user profiles.",exception);}
    }
    @Override public boolean emailExists(String email){
        JSONArray rows=read().getJSONArray("profiles");
        for(int i=0;i<rows.length();i++)if(rows.getJSONObject(i).getString("email").equalsIgnoreCase(email.trim()))return true;
        return false;
    }
    @Override public int nextUserId(){return read().optInt("nextUserId",1);}
    @Override public void saveUserProfile(UserProfile profile){
        JSONObject data=read();JSONArray rows=data.getJSONArray("profiles");
        for(int i=0;i<rows.length();i++)if(rows.getJSONObject(i).getString("email").equalsIgnoreCase(profile.getEmail()))
            throw new IllegalArgumentException("An account with this email already exists.");
        JSONObject row=new JSONObject().put("id",profile.getId()).put("name",profile.getName())
            .put("email",profile.getEmail()).put("passwordHash",profile.getPasswordHash())
            .put("createdAt",profile.getCreatedAt().toString()).put("accounts",new JSONArray());
        rows.put(row);data.put("nextUserId",Math.max(data.optInt("nextUserId",1),profile.getId()+1));write(data);
    }
    @Override public UserProfile findUserByUsername(String username){
        if(username==null)return null;
        JSONArray rows=read().getJSONArray("profiles");
        for(int i=0;i<rows.length();i++){
            JSONObject row=rows.getJSONObject(i);
            if(row.getString("email").equalsIgnoreCase(username.trim()))
                return new UserProfile(row.getInt("id"),row.getString("name"),row.getString("email"),
                    row.getString("passwordHash"),LocalDateTime.parse(row.optString("createdAt",LocalDateTime.now().toString())));
        }
        return null;
    }
    @Override public boolean passwordMatches(UserProfile profile,String rawPassword){
        return passwordHasher.matches(rawPassword,profile.getPasswordHash());
    }
}
