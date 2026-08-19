package se.demo.todo.domain;

public enum Priority {
    LAG("Låg"), NORMAL("Normal"), HOG("Hög"), KRITISK("Kritisk");

    private final String label;

    Priority(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
