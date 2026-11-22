package view;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

/** Opens only web URLs using the operating system's browser integration. */
public final class DesktopLinkOpener implements LinkOpener {
  @Override
  public void open(String url) throws IOException {
    final URI uri;
    try {
      uri = URI.create(url);
    } catch (IllegalArgumentException exception) {
      throw new IOException("This application link is invalid.", exception);
    }
    if (uri.getHost() == null
        || !("https".equalsIgnoreCase(uri.getScheme())
            || "http".equalsIgnoreCase(uri.getScheme()))) {
      throw new IOException("Only HTTP and HTTPS application links can be opened.");
    }
    if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
      throw new IOException("A browser could not be opened. Copy the application URL instead.");
    }
    Desktop.getDesktop().browse(uri);
  }
}
