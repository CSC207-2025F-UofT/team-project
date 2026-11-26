package entity;

import java.util.List;

public class StudentTaskUser extends TaskUser<StudentTask> {
    public static final Country DEFAULT_COUNTRY_CODE = Country.CA;

    private Country countryCode = DEFAULT_COUNTRY_CODE;

    public final StudentTaskFactory studentTaskFactory;

    public StudentTaskUser(String username, String password, int uniqueUserId) {
        super(username, password, uniqueUserId);

        this.studentTaskFactory =
                new StudentTaskFactory(1, getUniqueUserId());
    }

    public StudentTaskUser(String username, String password, int uniqueUserId,
                           List<StudentTask> tasks, int nextTaskUniqueId) {
        super(username, password, uniqueUserId, tasks);

        if (nextTaskUniqueId <= 0) {
            throw new IllegalArgumentException(
                    "The next task unique ID must be positive.");
        }

        for (StudentTask task : getTasks()) {
            if (nextTaskUniqueId <= task.getUniqueTaskId()) {
                throw new IllegalArgumentException(
                        "The next task unique ID must be greater than "
                                + "every existing task's unique ID.");
            }
        }

        this.studentTaskFactory =
                new StudentTaskFactory(nextTaskUniqueId, getUniqueUserId());
    }

    public Country getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(Country countryCode) {
        if (countryCode == null) {
            throw new IllegalArgumentException("Country code cannot be null.");
        }

        this.countryCode = countryCode;
    }
}