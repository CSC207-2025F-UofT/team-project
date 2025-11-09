package interface_adapter.home;

/**
 * Controller for the Signup Use Case.
 */
public class HomeController {

    private final HomeViewModel homeViewModel;

    public HomeController(HomeViewModel homeViewModel) {
        this.homeViewModel = homeViewModel;
    }

    // Placeholder print statements to confirm buttons work until we can implement the actual pages
    public void openBestTeamPage() {
        System.out.println("Navigating to Best Team Page...");
    }

    public void openTeamInputPage() {
        System.out.println("Navigating to Team Input Page...");
    }

    public void openReplacementPage() {
        System.out.println("Navigating to Replacement Suggestions Page...");
    }

    public void openBestPlayersPage() {
        System.out.println("Navigating to Best Players Page...");
    }

    public void openTransferPage() {
        System.out.println("Navigating to Transfer Page...");
    }

    public void openStatsPage() {
        System.out.println("Navigating to Player Stats Page...");
    }

    public void openLineupPage() {
        System.out.println("Navigating to Starting Lineup Page...");
    }
}
