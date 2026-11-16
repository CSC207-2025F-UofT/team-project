import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserCreationTest {

    @Test
    public void testValidUsername() {
        UserCreation account = new UserCreation(
                "tamho123",
                "Password123!",
                "tamho@mail.utoronto.ca"
        );

        assertEquals(true, account.verifiedPass());
    }

    @Test
    public void testNoUsername() {
        UserCreation account = new UserCreation(
                "",
                "Password123!",
                "tamho@mail.utoronto.ca"
        );

        assertEquals(false, account.verifiedPass());
    }

    @Test
    public void testNoPassword() {
        UserCreation account = new UserCreation(
                "tamho123",
                "",
                "tamho@mail.utoronto.ca"
        );

        assertEquals(false, account.verifiedPass());
    }

    @Test
    public void testNoEmail() {
        UserCreation account = new UserCreation(
                "tamho123",
                "Password123!",
                ""
        );

        assertEquals(true, account.verifiedPass());
    }

    @Test
    public void testNoMailUToronto() {
        UserCreation account = new UserCreation(
                "tamho123",
                "Password123!",
                "tamhowwwwwwwwww"
        );

        assertEquals(false, account.verifiedPass());
    }



}

