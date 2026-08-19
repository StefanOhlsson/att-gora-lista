package se.demo.todo.domain;

public enum TodoStatus {
    REGISTRERAD("Registrerad"), STARTAD("Startad"), AVSLUTAD("Avslutad");

    private final String label;

    TodoStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
