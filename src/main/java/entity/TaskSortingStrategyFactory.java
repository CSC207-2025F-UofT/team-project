package entity;

public abstract class TaskSortingStrategyFactory<T extends Task> {

    public abstract TaskSortingStrategy<T> createStrategy();
}
