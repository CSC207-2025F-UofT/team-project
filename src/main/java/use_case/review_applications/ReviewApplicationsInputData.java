package use_case.review_applications;

public class ReviewApplicationsInputData {
    private String status;
    private String petId;

    public ReviewApplicationsInputData(String status, String petId) {
        this.status = status;
        this.petId = petId;
    }

    String getStatus() {return status;}

    String getPetId() {return petId;}
}
