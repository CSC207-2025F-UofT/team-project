package use_case.port;

/** Shared retrieval boundary for Finder and recommendations; contains no HTTP or UI types. */
public interface JobSource {
  /**
   * Retrieves snapshots from the configured job boards.
   *
   * @return immutable jobs and failed-board details; a successful empty board counts as success
   * @throws JobSourceException if no board succeeded or retrieval was interrupted
   */
  JobFetchResult fetch() throws JobSourceException;
}
