package entity;
import java.util.Date;

public class SharedRecipe {
    private String shareId;
    private final int recipeId;
    private final String sharedBy;
    private final Date dateShared;

    public SharedRecipe(int recipeId, String sharedBy) {
        this.recipeId = recipeId;
        this.sharedBy = sharedBy;
        this.dateShared = new Date();
    }

    public String getShareId() {
        return shareId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getSharedBy() {
        return sharedBy;
    }

    public Date getDateShared() {
        return dateShared;
    }

    public void generateShareId(String sharedBy, int recipeId) {
        this.shareId = sharedBy + "-" + recipeId + "-" + dateShared.getTime();
    }

}