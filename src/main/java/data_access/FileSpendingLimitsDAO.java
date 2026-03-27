package data_access;

import use_case.spending_limits.SpendingLimitsDataAccessInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileSpendingLimitsDAO implements SpendingLimitsDataAccessInterface {

    private static final String FILE_PATH = "spending_limits.csv";
    private static final List<String> HEADERS = List.of(
            "username",
            "Groceries",
            "Dining Out",
            "Entertainment",
            "Shopping",
            "Utilities",
            "Fitness",
            "Transportation",
            "Miscellaneous"
    );

    @Override
    public void saveUserSpendingLimits(String username, Map<String, Double> spendingLimits) {
        List<String[]> rows = new ArrayList<>();

        // read existing rows
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = br.readLine(); // skip headers
            while ((line = br.readLine()) != null) {
                rows.add(line.split(","));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        boolean updated = false;

        for (String[] row : rows) {
            if (row[0].equals(username)) {
                for (int i = 1; i < HEADERS.size(); i++) {
                    row[i] = String.valueOf(spendingLimits.getOrDefault(HEADERS.get(i), 0.0));
                }
                updated = true;
                break;
            }
        }

        if (!updated) {
            String[] newRow = new String[HEADERS.size()];
            newRow[0] = username;
            for (int i = 1; i < HEADERS.size(); i++) {
                newRow[i] = String.valueOf(spendingLimits.getOrDefault(HEADERS.get(i), 0.0));
            }
            rows.add(newRow);
        }

        // write all rows back
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            pw.println(String.join(",", HEADERS));
            for (String[] row : rows) {
                pw.println(String.join(",", row));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Double> getSpendingLimitsByUsername(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            br.readLine(); // skip headers
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                if (row[0].equals(username)) {
                    Map<String, Double> map = new HashMap<>();
                    for (int i = 1; i < HEADERS.size(); i++) {
                        map.put(HEADERS.get(i), Double.parseDouble(row[i]));
                    }
                    return map;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Map.of();
    }
}
