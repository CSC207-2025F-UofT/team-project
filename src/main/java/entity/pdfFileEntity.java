package entity;

import java.time.LocalDateTime;

/** Entity: PdfFile
 * Represents one PDF user has uploaded
 * Stores information about the file (name, size, owner, date)
 *
 * Used by: UploadPdf, AssociatePdfToPaper, OpenPdf, DeletePdf
 */
public class pdfFileEntity {
    public static final long MAX_SIZE_BYTES = 50L * 1024 * 1024; // 50 MB Max Size

    private final String id;
    private final String ownerId;
    private final String fileName;
    private final long sizeBytes;
    private final LocalDateTime uploadedAt;

    // Constructor
    public pdfFileEntity(String id, String ownerId, String fileName, long sizeBytes, LocalDateTime uploadedAt) {
        // Check Business Rules
        if (!isPdf(fileName)) {
            throw new IllegalArgumentException("Only PDF files are supported.");
        }
        if (!isWithinSizeLimit(sizeBytes)) {
            throw new IllegalArgumentException("File must be under 50 MB.");
        }
        this.id = id;
        this.ownerId = ownerId;
        this.fileName = fileName;
        this.sizeBytes = sizeBytes;
        this.uploadedAt = uploadedAt;
    }

    /**
     * Business Rules:
     * - isPdf() checks if file ends with .pdf and returns true if it does
     * - isWithinSizeLimit() checks if file is within the 50MB size limit
     * - belongsTo() checks if PDF belongs to the user, which is 4.3
     * /**
     *      * Business Rules:
     *      * - isPdf() checks if file ends with .pdf and returns true if it does
     *      * - isWithinSizeLimit() checks if file is within the 50MB size limit
     *      * - belongsTo() checks if PDF belongs to the user, which is 4.3
     *      *
     *      * Getter methods:
     *      * - getId()
     *      * - getOwnerId()
     *      * - getFileName()
     *      * - getSizeBytes()
     *      * - getUploadedAt()
     *
     * Getter methods:
     * - getId()
     * - getOwnerId()
     * - getFileName()
     * - getSizeBytes()
     * - getUploadedAt()
     */
    // Check if file is a PDF file
    public static boolean isPdf(String fileName) {
        return fileName != null && fileName.toLowerCase().endsWith(".pdf");
    }
    // Check if PDF file is within size limit
    public static boolean isWithinSizeLimit(long sizeBytes) {
        return sizeBytes > 0 && sizeBytes <= MAX_SIZE_BYTES;
    }

    // Checks if the PDF file belongs to the User
    public boolean belongsTo(String userId) {
        return ownerId.equals(userId);
    }

    // Getter methods
    public String getId() { return id; }
    public String getOwnerId() { return ownerId; }
    public String getFileName() { return fileName; }
    public long getSizeBytes() { return sizeBytes; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
}