package app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import data_access.JobBoard;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class BoardConfigurationTest {
  @Test
  void loadsLabelsInDeterministicOrder() throws Exception {
    List<JobBoard> boards = BoardConfiguration.load(input("z=Company Z\na=Company A\n"));
    assertEquals("a", boards.get(0).getToken());
    assertEquals("Company Z", boards.get(1).getCompany());
  }

  @Test
  void rejectsEmptyAndInvalidConfiguration() {
    assertThrows(IllegalArgumentException.class, () -> BoardConfiguration.load(input("# empty")));
    assertThrows(
        IllegalArgumentException.class, () -> BoardConfiguration.load(input("../escape=Bad")));
  }

  private ByteArrayInputStream input(String value) {
    return new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8));
  }
}
