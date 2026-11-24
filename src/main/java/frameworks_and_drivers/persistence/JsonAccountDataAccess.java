package frameworks_and_drivers.persistence;
import use_case.create_account.AccountDataAccessInterface;
import entity.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

/** JSON gateway for accounts stored inside each user's profile record. */
public final class JsonAccountDataAccess implements AccountDataAccessInterface {
    private final Path filePath;
    public JsonAccountDataAccess(Path filePath){this.filePath=filePath;}
    private JSONObject read(){
        try{return new JSONObject(Files.readString(filePath));}
        catch(IOException e){throw new IllegalStateException("Unable to read profile data.",e);}
    }
    private void write(JSONObject data){
        try{if(filePath.getParent()!=null)Files.createDirectories(filePath.getParent());Files.writeString(filePath,data.toString(2));}
        catch(IOException e){throw new IllegalStateException("Unable to save profile data.",e);}
    }
    @Override public UserProfile getUserProfile(int id){
        JSONArray profiles=read().getJSONArray("profiles");
        for(int i=0;i<profiles.length();i++){
            JSONObject row=profiles.getJSONObject(i);
            if(row.getInt("id")==id){
                UserProfile profile=new UserProfile(id,row.getString("name"),row.getString("email"),
                    row.getString("passwordHash"),LocalDateTime.parse(row.optString("createdAt",LocalDateTime.now().toString())));
                JSONArray accounts=row.optJSONArray("accounts");
                if(accounts!=null)for(int j=0;j<accounts.length();j++)profile.addAccount(readAccount(accounts.getJSONObject(j),id));
                return profile;
            }
        }
        return null;
    }
    private Account readAccount(JSONObject row,int ownerId){
        int id=row.getInt("id");String name=row.getString("name");
        BigDecimal balance=new BigDecimal(row.getString("balance"));String currency=row.getString("currency");
        LocalDateTime created=LocalDateTime.parse(row.getString("createdAt"));
        switch(AccountType.valueOf(row.getString("type"))){
            case SAVINGS:return new SavingsAccount(id,ownerId,name,balance,currency,new BigDecimal(row.optString("annualInterestRate","0")),created);
            case CHEQUING:return new ChequingAccount(id,ownerId,name,balance,currency,new BigDecimal(row.optString("overdraftLimit","0")),created);
            case CASH:return new CashAccount(id,ownerId,name,balance,currency,row.optString("storageLocation","Wallet"),created);
            default:throw new IllegalArgumentException("Unknown account type.");
        }
    }
    @Override public boolean accountNameExists(int userId,String name){
        UserProfile profile=getUserProfile(userId);
        return profile!=null&&profile.getAccounts().stream().anyMatch(a->a.getAccountName().equalsIgnoreCase(name.trim()));
    }
    @Override public int nextAccountId(){return read().optInt("nextAccountId",1);}
    @Override public void saveAccount(Account account,UserProfile updatedProfile){
        JSONObject data=read();JSONArray profiles=data.getJSONArray("profiles");boolean found=false;
        for(int i=0;i<profiles.length();i++){
            JSONObject profile=profiles.getJSONObject(i);
            if(profile.getInt("id")==updatedProfile.getId()){
                JSONArray accounts=new JSONArray();
                for(Account a:updatedProfile.getAccounts()){
                    JSONObject row=new JSONObject().put("id",a.getAccountID()).put("name",a.getAccountName())
                        .put("type",a.getAccountType().name()).put("balance",a.getBalance().toPlainString())
                        .put("currency",a.getCurrency()).put("createdAt",a.getCreatedAt().toString());
                    if(a instanceof SavingsAccount)row.put("annualInterestRate",((SavingsAccount)a).getAnnualInterestRate().toPlainString());
                    if(a instanceof ChequingAccount)row.put("overdraftLimit",((ChequingAccount)a).getOverdraftLimit().toPlainString());
                    if(a instanceof CashAccount)row.put("storageLocation",((CashAccount)a).getStorageLocation());
                    accounts.put(row);
                }
                profile.put("accounts",accounts);found=true;break;
            }
        }
        if(!found)throw new IllegalArgumentException("User profile could not be found.");
        data.put("nextAccountId",Math.max(data.optInt("nextAccountId",1),account.getAccountID()+1));write(data);
    }
}
