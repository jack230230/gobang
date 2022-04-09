package gobang;

public class Board {
    public static int STONE_COUNT_MAX = 15 * 15;
    public static short STATUS_WHITE = 1;
    public static short STATUS_BLACK = 2;
    public static short STATUS_EMPTY = 0;
    public static short NOT_STATED = 0;
    public static short STARTED = 1;
    public static short WHITE_WIN = 2;
    public static short BLACK_WIN = 3;
    public static short PEACE_END = 4;
    public static short WIDTH = 15;
    public static short HEIGHT = WIDTH;
    public static short WHITE_TURN = 1;
    public static short BLACK_TURN = -1;
    // -1 black
    // 0 empty
    // 1 white
    public short[][] panelStatus;
    public int stoneCount = 0;
    public short status = NOT_STATED;
    public Player playerWhite;
    public Player playerBlack;
    public short turn;
    public BoardStatusHandler boardStatusHandler;

    public Board(Player white, Player black, BoardStatusHandler boardStatusHandler) {
        this.playerWhite = white;
        this.playerBlack = black;
        this.panelStatus = new short[WIDTH][HEIGHT];
        this.boardStatusHandler = boardStatusHandler;
    }

    public short checkStatus(Stone stone) {
        if (status == NOT_STATED || status == WHITE_WIN || status == BLACK_WIN) {
            return status;
        }

        if (stone == null) {
            return status;
        }

        Boolean win = StoneUtil.checkWin(panelStatus, stone.color);
        if (win) {
            if (stone.color == Stone.STONE_COLOR_WHITE) {
                this.status = WHITE_WIN;
            } else {
                this.status = BLACK_WIN;
            }
        } else {
            return status = STARTED;
        }
        return status;
    }

    public void start() {
        this.turn = BLACK_TURN;
        this.status = STARTED;

        do {
            short status;

            Stone stoneBlack = playerBlack.action(this);
            boardStatusHandler.handleAction(stoneBlack);
            stoneCount++;
            this.panelStatus[stoneBlack.position.x][stoneBlack.position.y] = STATUS_BLACK;
            this.turn = WHITE_TURN;
            status = checkStatus(stoneBlack);
            if (status == BLACK_WIN) {
                boardStatusHandler.handleVictory(Stone.STONE_COLOR_BLACK);
                break;
            }

            if (stoneCount == STONE_COUNT_MAX) {
                // End
                boardStatusHandler.handlePeace();
                this.status = PEACE_END;
                break;
            }

            Stone stoneWhite = playerWhite.action(this);
            boardStatusHandler.handleAction(stoneWhite);
            stoneCount++;
            this.panelStatus[stoneWhite.position.x][stoneWhite.position.y] = STATUS_WHITE;
            this.turn = BLACK_TURN;
            status = checkStatus(stoneWhite);
            if (status == WHITE_WIN) {
                boardStatusHandler.handleVictory(Stone.STONE_COLOR_WHITE);
                break;
            }

        } while (this.status == STARTED);
    }

    public void reset() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                this.panelStatus[i][j] = STATUS_EMPTY;
            }
        }
        this.status = NOT_STATED;
        this.turn = BLACK_TURN;
        this.boardStatusHandler.handleReset();
    }

    public Boolean checkEmpty(short x, short y) {
        return panelStatus[x][y] == STATUS_EMPTY;
    }
}
