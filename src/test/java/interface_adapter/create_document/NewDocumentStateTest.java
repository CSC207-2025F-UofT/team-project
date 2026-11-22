package interface_adapter.create_document;

import entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NewDocumentStateTest {

    @Test
    void defaultNameIsEmpty() {
        NewDocumentState state = new NewDocumentState();

        assertEquals("", state.getName());
    }

    @Test
    void defaultNameErrorIsNull() {
        NewDocumentState state = new NewDocumentState();

        assertNull(state.getNameError());
    }

    @Test
    void defaultCreatorIsInitialized() {
        NewDocumentState state = new NewDocumentState();

        assertNotNull(state.getCreator());
        assertEquals("name", state.getCreator().getName());
    }

    @Test
    void setNameUpdatesName() {
        NewDocumentState state = new NewDocumentState();

        state.setName("My Document");

        assertEquals("My Document", state.getName());
    }

    @Test
    void setNameErrorUpdatesNameError() {
        NewDocumentState state = new NewDocumentState();

        state.setNameError("Name already exists");

        assertEquals("Name already exists", state.getNameError());
    }

    @Test
    void setCreatorUpdatesCreator() {
        NewDocumentState state = new NewDocumentState();

        User creator = new User("alice");

        state.setCreator(creator);

        assertSame(creator, state.getCreator());
        assertEquals("alice", state.getCreator().getName());
    }

    @Test
    void toStringContainsNameAndCreator() {
        NewDocumentState state = new NewDocumentState();

        state.setName("My Document");
        state.setCreator(new User("alice"));

        String result = state.toString();

        assertTrue(result.contains("My Document"));
        assertTrue(result.contains("alice"));
        assertTrue(result.startsWith("NewDocumentState{"));
        assertTrue(result.endsWith("}"));
    }

    @Test
    void toStringWorksWithDefaultState() {
        NewDocumentState state = new NewDocumentState();

        String result = state.toString();

        assertNotNull(result);
        assertTrue(result.contains("name=''"));
        assertTrue(result.contains("creator='name'"));
    }
}