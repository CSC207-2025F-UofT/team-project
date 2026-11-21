package data_access;

import entity.Student;
import entity.StudentFactory;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.course_analysis.AnalysisStudentDataAccessInterface;
import use_case.job_grouping.CreateJobGroupDataAccessInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DAO for user data implemented using a File to persist the data.
 */
public class FileUserDataAccessObject implements SignupUserDataAccessInterface,
                                                 LoginUserDataAccessInterface,
                                                 ChangePasswordUserDataAccessInterface,
                                                 LogoutUserDataAccessInterface,
                                                 AnalysisStudentDataAccessInterface,
                                                 CreateJobGroupDataAccessInterface {


    private static final String HEADER = "username,password";

    private final File csvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, Student> accounts = new HashMap<>();

    private String currentUsername;

    /**
     * Construct this DAO for saving to and reading from a local file.
     * @param csvPath the path of the file to save to
     * @param studentFactory factory for creating user objects
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public FileUserDataAccessObject(String csvPath, StudentFactory studentFactory) {

        csvFile = new File(csvPath);
        headers.put("username", 0);
        headers.put("password", 1);

        if (csvFile.length() == 0) {
            save();
        }
        else {

            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                final String header = reader.readLine();

                if (!header.equals(HEADER)) {
                    throw new RuntimeException(String.format("header should be%n: %s%n but was:%n%s", HEADER, header));
                }

                String row;
                while ((row = reader.readLine()) != null) {
                    final String[] col = row.split(",");
                    final String username = String.valueOf(col[headers.get("username")]);
                    final String password = String.valueOf(col[headers.get("password")]);
                    // temporary fix, i dont want to see the big red lines
                    final Student student = studentFactory.create(username, password,
                            new ArrayList<>(), new ArrayList<>());
                    accounts.put(username, student);
                }
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void save() {
        final BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(csvFile));
            writer.write(String.join(",", headers.keySet()));
            writer.newLine();

            for (Student student : accounts.values()) {
                final String line = String.format("%s,%s,%s,%s",
                        student.getName(), student.getPassword(),
                        student.getCourses().toString(), student.getSkills().toString());
                writer.write(line);
                writer.newLine();
            }

            writer.close();

        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void save(Student student) {
        accounts.put(student.getName(), student);
        this.save();
    }

    @Override
    public Student get(String username) {
        return accounts.get(username);
    }

    @Override
    public void setCurrentUsername(String name) {
        currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public boolean existsByName(String identifier) {
        return accounts.containsKey(identifier);
    }

    @Override
    public void changePassword(Student student) {
        // Replace the User object in the map
        accounts.put(student.getName(), student);
        save();
    }

    @Override
    public void changeSkills(Student student) {
        accounts.put(student.getName(), student);
        save();
    }

    @Override
    public String getCourseDescription(String course) {
        // This would be an api call.
        return "";
    }

    @Override
    public String getSkill(String description) {
        // This would be an api call.
        return "";
    }

}
