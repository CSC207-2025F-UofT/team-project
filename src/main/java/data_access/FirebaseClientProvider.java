package data_access;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class FirebaseClientProvider {

    private static Firestore instance;

    private FirebaseClientProvider() {}

    public static synchronized Firestore getFirestore() {
        if (instance == null) {
            try (InputStream serviceAccount =
                         new FileInputStream("src/main/resources/serviceAccountKey.json")) {

                GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

                FirestoreOptions options = FirestoreOptions.newBuilder()
                        .setCredentials(credentials)
                        .setProjectId("gochat-1c0fc")
                        .build();

                instance = options.getService();
            } catch (IOException e) {
                throw new RuntimeException("Failed to init Firestore", e);
            }
        }
        return instance;
    }
}
