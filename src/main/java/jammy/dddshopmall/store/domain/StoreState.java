package jammy.dddshopmall.store.domain;

public enum StoreState {

    RUNNING,
    SUSPENDED;

    public boolean isRunning() {
        return this == RUNNING;
    }
    public boolean isSuspended() {
        return this == SUSPENDED;
    }
}
