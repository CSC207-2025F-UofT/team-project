package view;

import java.io.IOException;

/** Opens an application page; kept outside the use case and replaceable in UI tests. */
@FunctionalInterface
public interface LinkOpener {
  /**
   * Opens a web page without recording a saved or submitted application.
   *
   * @param url selected posting's application URL
   * @throws IOException if the link is invalid or the browser cannot be opened
   */
  void open(String url) throws IOException;
}
