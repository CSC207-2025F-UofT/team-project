package entity;

import java.util.ArrayList;
import java.util.List;

public class PaperLibrary {

    private final List<String> savedUrls;

    public PaperLibrary(){
        this.savedUrls = new ArrayList<>();
    }

    public boolean savePaper(String url){
        if (savedUrls.contains(url)) {
            return false;
        }
        savedUrls.add(url);
        return true;
    }
}
