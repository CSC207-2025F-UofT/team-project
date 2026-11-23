package entity;

import java.util.List;

public abstract class TaskSortingStrategy<T extends Task> {

    public abstract List<T> sort(List<T> tasks);
}