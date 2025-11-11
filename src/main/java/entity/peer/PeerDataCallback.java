package entity.peer;


@FunctionalInterface
public interface PeerDataCallback {
  void onData(String data);
}
