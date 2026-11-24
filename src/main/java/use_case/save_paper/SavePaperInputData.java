package use_case.save_paper;

public class SavePaperInputData {
    private final String url  ;

    public SavePaperInputData(String url){
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }
}
