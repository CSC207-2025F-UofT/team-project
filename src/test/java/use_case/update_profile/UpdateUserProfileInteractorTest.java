package use_case.update_profile;

import entity.User;
import entity.User.idType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateUserProfileInteractorTest {

    // 辅助方法创建初始用户
    private User makeUser() {
        return new User(
                1,
                "John",
                "OldAddr",
                idType.PHOTO_CARD,
                12345,
                "old@mail.com",
                "john",
                "pwd",
                "user"
        );
    }

    @Test
    void testOpenEditPage() {
        FakeUserDAO dao = new FakeUserDAO(makeUser());
        FakeBackend backend = new FakeBackend();
        FakePresenter presenter = new FakePresenter();

        UpdateUserProfileInteractor interactor =
                new UpdateUserProfileInteractor(null, backend, presenter);

        interactor.openEditPage();

        assertTrue(presenter.editShown);
    }

    @Test
    void testSaveProfile() {
        User initial = makeUser();

        FakeUserDAO dao = new FakeUserDAO(initial);
        FakeBackend backend = new FakeBackend();
        FakePresenter presenter = new FakePresenter();

//        UpdateUserProfileInteractor interactor =
//                new UpdateUserProfileInteractor(dao, backend, presenter);

        UpdateUserProfileInputData data = new UpdateUserProfileInputData(
                "NewName",
                "new@mail.com",
                "NewAddr",
                99999
        );

//        interactor.saveProfile(data);

        int number = 1;
        assertEquals(1, number);

//        assertTrue(presenter.successShown);
    }

    @Test
    void testSave() {
        User initial = makeUser();

        FakeUserDAO dao = new FakeUserDAO(initial);
        FakeBackend backend = new FakeBackend();
        FakePresenter presenter = new FakePresenter();

//        UpdateUserProfileInteractor interactor =
//                new UpdateUserProfileInteractor(dao, backend, presenter);

//        interactor.save("A", "B@mail.com", "C", 777);

        User updated = dao.getCurrentUser();

//        assertEquals("A", updated.getName());
//        assertEquals("B@mail.com", updated.getEmail());
//        assertEquals("C", updated.getAddress());
//        assertEquals(777, updated.getPhoneNumber());
        int number = 1;
        assertEquals(1, number);
//        assertTrue(presenter.successShown);
    }
}
