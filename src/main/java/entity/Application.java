package entity;

import org.json.JSONObject;

import java.time.LocalDate;

public class Application {
    public enum Status { PENDING, APPROVED, REJECTED }

    private final int id;
    private final int userId;
    private final int petId;
    private final LocalDate date;
    private Status status;

    public Application(int applicationId, int userId, int petId) {
        this.id = applicationId;
        this.userId = userId;
        this.petId = petId;
        this.date = LocalDate.now();
        this.status = Status.PENDING;
    }

    public int getId() {
        return id;
    }

    public int getPetId() {
        return petId;
    }

    public int getUserId() {
        return userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void approve() {
        this.status = Status.APPROVED;
    }

    public void reject() {
        this.status = Status.REJECTED;
    }

    public Application(JSONObject applicationJson) {
        this.id = applicationJson.getInt("id");
        this.userId = applicationJson.getInt("userId");
        this.petId = applicationJson.getInt("petId");
        this.date = LocalDate.parse(applicationJson.getString("date"));
        this.status = applicationJson.getEnum(Status.class, "status");
    }

    public JSONObject toJson() {
        JSONObject applicationJson = new JSONObject();

        applicationJson.put("id", id);
        applicationJson.put("userId", userId);
        applicationJson.put("petId", petId);
        applicationJson.put("date", date);
        applicationJson.put("status", status.toString());
        return applicationJson;
    }
}