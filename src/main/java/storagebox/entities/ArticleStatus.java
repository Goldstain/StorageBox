package storagebox.entities;

public enum ArticleStatus {
    IN_STOCK("В наявності"),
    OUT_OF_STOCK("Немає в наявності"),
    ON_THE_WAY("В дорозі");

    private final String alias;

    private ArticleStatus(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }
}
