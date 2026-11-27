package data_access;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import use_case.port.JobFetchResult;
import use_case.port.JobSource;
import use_case.port.JobSourceException;

/** Explicit offline sample data for demonstrations; never presented as live listings. */
public final class DemoJobSource implements JobSource {
  @Override
  public JobFetchResult fetch() throws JobSourceException {
    try (InputStream input = getClass().getResourceAsStream("/demo-jobs.json")) {
      if (input == null) {
        throw new JobSourceException("Sample data was not found.");
      }
      String json = new String(input.readAllBytes(), StandardCharsets.UTF_8);
      return new JobFetchResult(
          new GreenhouseJobMapper().parse(json, new JobBoard("sample", "Sample company")),
          List.of("sample"),
          List.of());
    } catch (IOException exception) {
      throw new JobSourceException("Sample data could not be read.");
    }
  }
}
