package data_access;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import entity.Library;
import entity.QuizResult;
import use_case.save_result.SaveResultDataAccessInterface;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Saves and loads quiz results in Firebase Firestore (collection "quizResults").
 */
public class FirestoreQuizResultDAO implements SaveResultDataAccessInterface {
    private static final String COLLECTION = "quizResults";
    private final Firestore db;

    // Set up Firestore DB with JSON key
    public FirestoreQuizResultDAO(String serviceAccountPath) throws IOException {
        try (FileInputStream key = new FileInputStream(serviceAccountPath)) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(key))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        }
        this.db = FirestoreClient.getFirestore();
    }

    // Save name, result in a DB row
    @Override
    public void save(QuizResult result) {
        Map<String, Object> doc = new HashMap<>();
        doc.put("userName", result.getUserName());
        doc.put("dateTaken", result.getDateTaken().toString());
        doc.put("chosenIndices", result.getChosenIndices());
        doc.put("matchedLibrary", result.getMatchedLibrary().name());
        try {
            db.collection(COLLECTION).add(doc).get();
        } catch (InterruptedException | ExecutionException e) {
            // For example when your wifi dies
            throw new RuntimeException("Could not save result to Firestore", e);
        }
    }

    // Retrieve QuizResult by username
    public List<QuizResult> findByUserName(String userName) {
        try {
            List<QueryDocumentSnapshot> docs = db.collection(COLLECTION)
                    .whereEqualTo("userName", userName)
                    .get().get().getDocuments();
            List<QuizResult> results = new ArrayList<>();
            for (QueryDocumentSnapshot doc : docs) {
                results.add(toEntity(doc));
            }
            return results;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Could not load results from Firestore", e);
        }
    }

    // Make compiler shut up about unsafe down-casting (Object -> List)
    @SuppressWarnings("unchecked")
    // Convert QueryDocumentSnapshot -> QuizResult
    private QuizResult toEntity(DocumentSnapshot doc) {
        List<Long> raw = (List<Long>) doc.get("chosenIndices"); // Firestore stores numbers as Long
        List<Integer> chosenIndices = new ArrayList<>();
        for (Long n : raw) {
            chosenIndices.add(n.intValue());
        }
        return new QuizResult(
                doc.getString("userName"),
                LocalDate.parse(doc.getString("dateTaken")),
                chosenIndices,
                Library.valueOf(doc.getString("matchedLibrary")));
    }
}