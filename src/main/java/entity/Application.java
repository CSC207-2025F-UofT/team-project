package entity;

import java.time.LocalDate;

/**
 * An adopter's application to adopt one specific pet.
 */
public class Application {

    private final String id;
    private final String adopterId;
    private final String petId;
    private final String housingType;
    private final String petExperience;
    private final String householdMembers;
    private final String contactInfo;
    private final String note;
    private final LocalDate submittedAt;
    private ApplicationStatus status;
    private String adminNote;

    /**
     * Creates a new application with status PENDING.
     * @throws IllegalArgumentException if a required value is missing
     */
    public Application(String id, String adopterId, String petId, String housingType, String petExperience,
                       String householdMembers, String contactInfo, String note, LocalDate submittedAt) {
        this(id, adopterId, petId, housingType, petExperience, householdMembers, contactInfo, note, submittedAt,
                ApplicationStatus.PENDING, "");
    }

    /**
     * Creates an application with a given status, for example when loading saved data.
     * @throws IllegalArgumentException if a required value is missing
     */
    public Application(String id, String adopterId, String petId, String housingType, String petExperience,
                       String householdMembers, String contactInfo, String note, LocalDate submittedAt,
                       ApplicationStatus status, String adminNote) {
        this.id = require(id, "Application id");
        this.adopterId = require(adopterId, "Adopter");
        this.petId = require(petId, "Pet");
        this.housingType = require(housingType, "Housing type");
        this.petExperience = require(petExperience, "Pet experience");
        this.householdMembers = require(householdMembers, "Household members");
        this.contactInfo = require(contactInfo, "Contact information");
        this.note = note == null ? "" : note.trim();
        if (submittedAt == null || status == null) {
            throw new IllegalArgumentException("Date and status are required.");
        }
        this.submittedAt = submittedAt;
        this.status = status;
        this.adminNote = adminNote == null ? "" : adminNote;
    }

    private static String require(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        return value.trim();
    }

    /**
     * Returns whether the shelter is still considering this application.
     * @return true if the status is PENDING or INTERVIEW
     */
    public boolean isActive() {
        return status == ApplicationStatus.PENDING || status == ApplicationStatus.INTERVIEW;
    }

    /**
     * Withdraws this application.
     * @throws IllegalStateException if the application is not PENDING
     */
    public void withdraw() {
        if (status != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Only a pending application can be withdrawn.");
        }
        status = ApplicationStatus.WITHDRAWN;
    }

    /**
     * Records the shelter's decision.
     * @param newStatus the new status
     * @param newAdminNote a note for the adopter, may be empty
     */
    public void updateStatus(ApplicationStatus newStatus, String newAdminNote) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status is required.");
        }
        this.status = newStatus;
        this.adminNote = newAdminNote == null ? "" : newAdminNote;
    }

    public String getId() {
        return id;
    }

    public String getAdopterId() {
        return adopterId;
    }

    public String getPetId() {
        return petId;
    }

    public String getHousingType() {
        return housingType;
    }

    public String getPetExperience() {
        return petExperience;
    }

    public String getHouseholdMembers() {
        return householdMembers;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getNote() {
        return note;
    }

    public LocalDate getSubmittedAt() {
        return submittedAt;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public String getAdminNote() {
        return adminNote;
    }
}
