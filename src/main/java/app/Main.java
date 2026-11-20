package app;

public class Main {
    public static void main(String[] args) {
        System.out.println("GIPHY key set: " + (System.getenv("GIPHY_API_KEY") != null));
        System.out.println("Firebase URL: " + System.getenv("FIREBASE_DB_URL"));
    }
}