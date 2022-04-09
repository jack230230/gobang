package gobang;

public interface BoardStatusHandler {
    void handleVictory(boolean color);

    void handlePeace();

    void handleAction(Stone stone);

    void handleReset();
}
