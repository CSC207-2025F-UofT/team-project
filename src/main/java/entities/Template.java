package entities;

import java.util.ArrayList;
import java.util.List;

public class Template {
    private String name;
    private final List<ExerciseSetGroup> groups = new ArrayList<>();

    public Template(){
        this("");
    }

    public Template(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void addGroup(ExerciseSetGroup group){
        this.groups.add(group);
    }

    public void removeGroup(int index){
        this.groups.remove(index);
    }

    public ExerciseSetGroup getGroup(int index){
        return this.groups.get(index);
    }

    public int getGroupCount(){
        return this.groups.size();
    }

    public List<ExerciseSetGroup> getGroups(){
        return List.copyOf(this.groups);
    }
}
