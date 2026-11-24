package use_case.port;

import java.util.Map;

/** A shared job source that can also retrieve one employer's public board. */
public interface CompanyBoardSource extends JobSource {
  /** Returns configured board identifiers and their display names in selection order. */
  Map<String, String> getCompanies();

  /** Retrieves only the requested board, given its identifier or public Greenhouse URL. */
  JobFetchResult fetchCompany(String board) throws JobSourceException;
}
