package use_case.logout;

import entity.UserSession;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LogoutInteractorTest {
    @Test
    void logoutSuccess(){
        UserSession session = new UserSession();
        session.login("Alex");

        final boolean[] successCalled = {false};

        LogoutOutputBoundary presenter = new LogoutOutputBoundary(){
            @Override
            public void prepareSuccessView(){
                successCalled[0] = true;
            }
        };
        LogoutInteractor interactor = new LogoutInteractor(presenter, session);
        interactor.execute();
        assertTrue(successCalled[0]);
        assertNull(session.getCurrentUsername());
    }
}
