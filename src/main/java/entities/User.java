package entities;

import java.util.List;

public class User {
    private String id;
    private String name;
    private List<BankStatement> statements;

    public User(String id, String name, List<BankStatement> statements) {
        this.id = id;
        this.name = name;
        this.statements = statements;
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addStatement(BankStatement statement) {
        statements.add(statement);
    }

    public List<BankStatement> getStatements() {
        return statements;
    }
}
