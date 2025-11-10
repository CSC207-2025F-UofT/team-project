package entities;

public class OriginalPost extends User{

    public OriginalPost(String full_name, String username, String email, String password) {
        super(full_name, username, email, password);
    }

    public OriginalPost createPost(String title, String username){
        //TODO: implement OriginalPost
        return null;
    }

    public void deletePost(OriginalPost post){
        //TODO: implement deletePost
    }

    public OriginalPost editPost(OriginalPost post, String text){
        //TODO: implement editPost
        return null;
    }

    public void referencePost(Post post){
        //TODO: implement referencePost
    }

}
