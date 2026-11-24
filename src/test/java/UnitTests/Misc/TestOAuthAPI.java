package UnitTests.Misc;
import DataAccess.APIDataAccessObject;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestOAuthAPI {

    @Test
    public void OAuthAppTokenTest() {
        APIDataAccessObject testAccess = null;
        try {
            testAccess = new APIDataAccessObject("src/main/resources/ebay-config-default.yaml");
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
