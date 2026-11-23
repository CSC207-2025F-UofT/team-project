package use_case.search_sticker;

import entity.Sticker;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SearchStickerInteractorTest {

    /** Records what the interactor sent to the presenter. */
    private static class RecordingPresenter implements SearchStickerOutputBoundary {
        SearchStickerOutputData success;
        String error;
        public void prepareSuccessView(SearchStickerOutputData out) { success = out; }
        public void prepareFailView(String message) { error = message; }
    }

    @Test
    void successReturnsStickers() {
        SearchStickerDataAccessInterface fake = query -> List.of(
                new Sticker("a1", "cat", "https://example.com/a1.gif"),
                new Sticker("b2", "cat 2", "https://example.com/b2.gif"));
        RecordingPresenter presenter = new RecordingPresenter();

        new SearchStickerInteractor(fake, presenter).execute(new SearchStickerInputData("cat"));

        assertNull(presenter.error);
        assertEquals(List.of("a1", "b2"), presenter.success.getStickerIds());
    }

    @Test
    void emptyQueryFails() {
        SearchStickerDataAccessInterface fake = query -> fail("Should not search");
        RecordingPresenter presenter = new RecordingPresenter();

        new SearchStickerInteractor(fake, presenter).execute(new SearchStickerInputData("   "));

        assertEquals("Please enter a search term", presenter.error);
    }

    @Test
    void serviceDownFails() {
        SearchStickerDataAccessInterface fake = query -> {
            throw new StickerSearchException("down");
        };
        RecordingPresenter presenter = new RecordingPresenter();

        new SearchStickerInteractor(fake, presenter).execute(new SearchStickerInputData("cat"));

        assertEquals("Couldn't reach sticker search. Try again.", presenter.error);
    }

    @Test
    void noResultsFails() {
        SearchStickerDataAccessInterface fake = query -> List.of();
        RecordingPresenter presenter = new RecordingPresenter();

        new SearchStickerInteractor(fake, presenter).execute(new SearchStickerInputData("zzzz"));

        assertEquals("No stickers found for zzzz", presenter.error);
    }
}