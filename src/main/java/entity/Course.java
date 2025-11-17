//package entity;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Objects;
//
//
//public class Course {
//    private final String id;
//    private String name;
//    private final List<Task> tasks = new ArrayList<>();
//
//    public Course(String id, String name) {
//        if (name == null || name.trim().isEmpty()) {
//            throw new IllegalArgumentException("Course name cannot be empty");
//        }
//        this.id = Objects.requireNonNull(id);
//        this.name = name;
//    }
//
//    public String getId() { return id; }
//
//    public String getName() { return name; }
//    public void setName(String name) {
//        if (name == null || name.trim().isEmpty()) {
//            throw new IllegalArgumentException("Course name cannot be empty");
//        }
//        this.name = name;
//    }
//
//    public List<Task> getTasks() {
//        // Return unmodifiable view to protect internal list
//        return Collections.unmodifiableList(tasks);
//    }
//
//    public boolean addTask(Task t) {
//        Objects.requireNonNull(t);
//        boolean exists = tasks.stream().anyMatch(x -> x.getId().equals(t.getId()));
//        if (exists) return false;
//        tasks.add(t);
//        return true;
//    }
//
//    public Task findTaskById(String taskId) {
//        return tasks.stream().filter(t -> t.getId().equals(taskId)).findFirst().orElse(null);
//    }
//}
