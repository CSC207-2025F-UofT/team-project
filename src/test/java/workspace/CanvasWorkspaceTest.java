package workspace;

import org.junit.jupiter.api.Test;
import workspace.domain.*;
import workspace.use_case.*;
import java.io.IOException;
import java.time.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class CanvasWorkspaceTest {
    private static final CanvasState EMPTY = new CanvasState(List.of(),List.of(),List.of(),1,0,0);
    private static final class MemoryRepository implements CanvasRepository {
        final Map<String,SavedCanvas> saved=new LinkedHashMap<>();
        @Override public List<SavedCanvas> list() {return List.copyOf(saved.values());}
        @Override public Optional<SavedCanvas> find(String id) {return Optional.ofNullable(saved.get(id));}
        @Override public void write(SavedCanvas item) {saved.put(item.id(),item);}
    }
    @Test void saveThenSaveAgainUpdatesTheSameNamedDocument() throws IOException {
        var repo=new MemoryRepository();var uc=new CanvasWorkspace(repo);
        var first=uc.save(EMPTY,"Lecture 1",false);
        var updated=uc.save(new CanvasState(List.of(),List.of(),List.of(),1,10,20),"Lecture 1",false);
        assertEquals(first.id(),updated.id());assertEquals(1,repo.saved.size());
        assertEquals(10,uc.open(first.id()).state().offsetX());
    }
    @Test void saveAsCreatesIndependentNamedSnapshot() throws IOException {
        var repo=new MemoryRepository();var uc=new CanvasWorkspace(repo);
        var first=uc.save(EMPTY,"Chapter 1",false);
        var second=uc.save(EMPTY,"Chapter 3",true);
        assertNotEquals(first.id(),second.id());assertEquals("Chapter 1",uc.open(first.id()).name());
        assertEquals(2,uc.all().size());
    }
    @Test void mostRecentlySavedIsSelectedOnRestart() throws IOException {
        var repo=new MemoryRepository();var clock=Clock.fixed(Instant.parse("2026-11-12T12:00:00Z"),ZoneOffset.UTC);
        var one=new CanvasWorkspace(repo,clock).save(EMPTY,"One",false);
        var newer=new CanvasWorkspace(repo,Clock.offset(clock,Duration.ofMinutes(1))).save(EMPTY,"Two",false);
        var restarted=new CanvasWorkspace(repo);
        assertEquals(newer.id(),restarted.mostRecent().orElseThrow().id());
        assertTrue(restarted.currentId().isEmpty());
        assertEquals(newer.id(),restarted.open(newer.id()).id());
    }
    @Test void emptyNameAndUnknownDocumentAreRejected() throws IOException {
        var uc=new CanvasWorkspace(new MemoryRepository());
        assertThrows(IllegalArgumentException.class,()->uc.save(EMPTY,"  ",true));
        assertThrows(IOException.class,()->uc.open(UUID.randomUUID().toString()));
    }
    @Test void newCanvasDetachesWithoutDestroyingPreviousFile() throws IOException {
        var repo = new MemoryRepository(); var uc = new CanvasWorkspace(repo);
        var original = uc.save(EMPTY, "Original", false);
        uc.newCanvas();
        assertTrue(uc.currentId().isEmpty());
        var fresh = uc.save(EMPTY, "Fresh", false);
        assertNotEquals(original.id(), fresh.id());
        assertEquals(2, repo.saved.size());
    }
    @Test void renameKeepsIdStateAndActiveSelection() throws IOException {
        var repo = new MemoryRepository(); var uc = new CanvasWorkspace(repo);
        var first = uc.save(EMPTY, "Before", false);
        var second = uc.save(EMPTY, "Another", true);
        var renamed = uc.rename(first.id(), "After");
        assertEquals(first.id(), renamed.id());
        assertEquals(EMPTY, renamed.state());
        assertEquals(second.id(), uc.currentId().orElseThrow());
        assertEquals("After", uc.open(first.id()).name());
        assertEquals(2, repo.saved.size());
    }
    @Test void failedStorageDoesNotChangeCurrentId() {
        CanvasRepository broken=new CanvasRepository(){
            public List<SavedCanvas> list(){return List.of();}
            public Optional<SavedCanvas> find(String id){return Optional.empty();}
            public void write(SavedCanvas value) throws IOException {throw new IOException("disk full");}
        };
        var uc=new CanvasWorkspace(broken);
        assertThrows(IOException.class,()->uc.save(EMPTY,"Unwritten",false));
        assertTrue(uc.currentId().isEmpty());
    }
}
