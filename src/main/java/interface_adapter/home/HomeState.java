package interface_adapter.home;

public record HomeState(String signedInAs, String handle) {
    public static HomeState empty() {
        return new HomeState("", "");
    }
}
