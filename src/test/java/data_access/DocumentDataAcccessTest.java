
package data_access;

import entity.Document;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DocumentDataAccessTest {

    @TempDir
    Path tempDir;

    private UserDataAccess userDataAccess;
    private DocumentDataAccess documentDataAccess;

    private User alice;
    private User bob;

    private Path userFile;
    private Path documentFile;

    @BeforeEach
    void setUp() {
        userFile = tempDir.resolve("users.csv");
        documentFile = tempDir.resolve("documents.csv");

        userDataAccess = new UserDataAccess(userFile.toString());
        documentDataAccess = new DocumentDataAccess(documentFile.toString());

        alice = new User("alice", new ArrayList<>(), new ArrayList<>());
        bob = new User("bob", new ArrayList<>(), new ArrayList<>());

        userDataAccess.save(alice);
        userDataAccess.save(bob);

        documentDataAccess.setUserDataAccess(userDataAccess);
    }

    @Test
    void saveDocumentAndRetrieveIt() {
        Document document = new Document("notes", alice);
        document.addAccessedBy(bob);

        documentDataAccess.save(document);

        assertTrue(documentDataAccess.existsByName("notes"));
        assertSame(document, documentDataAccess.get("notes"));
    }

    @Test
    void saveMultipleDocuments() {
        Document document1 = new Document("notes1", alice);
        Document document2 = new Document("notes2", bob);

        documentDataAccess.save(document1);
        documentDataAccess.save(document2);

        assertTrue(documentDataAccess.existsByName("notes1"));
        assertTrue(documentDataAccess.existsByName("notes2"));

        assertSame(document1, documentDataAccess.get("notes1"));
        assertSame(document2, documentDataAccess.get("notes2"));
    }

    @Test
    void documentDoesNotExistWhenNotSaved() {
        assertFalse(documentDataAccess.existsByName("missing"));
        assertNull(documentDataAccess.get("missing"));
    }

    @Test
    void loadRestoresDocumentAndUserRelationships() {
        Document document = new Document("shared", alice);
        document.addAccessedBy(bob);

        documentDataAccess.save(document);

        // Load users first, then documents.
        UserDataAccess loadedUsers =
                new UserDataAccess(userFile.toString());
        loadedUsers.load();

        DocumentDataAccess loadedDocuments =
                new DocumentDataAccess(documentFile.toString());
        loadedDocuments.setUserDataAccess(loadedUsers);
        loadedDocuments.load();

        Document loadedDocument = loadedDocuments.get("shared");
        User loadedAlice = loadedUsers.get("alice");
        User loadedBob = loadedUsers.get("bob");

        assertNotNull(loadedDocument);
        assertSame(loadedAlice, loadedDocument.getCreator());

        assertTrue(loadedDocument.getAccessedBy().contains(loadedBob));
        assertTrue(loadedAlice.getDocumentsCreated().contains(loadedDocument));
        assertTrue(loadedBob.getDocumentsAccessed().contains(loadedDocument));
    }

    @Test
    void contentsWithCommasArePreservedAfterLoading() {
        String contents = "Hello, world, this is a document.";

        Document document = new Document("commaDoc", alice, new ArrayList<>(), contents);
        documentDataAccess.save(document);

        assertEquals("Hello, world, this is a document.", documentDataAccess.get("commaDoc").getContents());
    }
}