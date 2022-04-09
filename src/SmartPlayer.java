import java.util.*;

public class SmartPlayer extends Player {
    Random random = new Random();
    Set<Position> ourChances = new HashSet<>();
    Set<Position> opponentChances = new HashSet<>();
    short ourColorStatus;
    short opponentColorStatus;

    public SmartPlayer(String name, Boolean color) {
        this.name = name;
        this.color = color;
        ourColorStatus = color ? Board.STATUS_WHITE : Board.STATUS_BLACK;
        opponentColorStatus = color ? Board.STATUS_BLACK : Board.STATUS_WHITE;
    }

    public static Boolean checkEmpty(short[][] panelStatus, Position position) {
        if (position == null) {
            return false;
        }
        return panelStatus[position.x][position.y] == Board.STATUS_EMPTY;
    }

    @Override
    public Stone action(Board board) {

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ourChances = collectValidPositionsByColor(this.color, board.panelStatus);
        opponentChances = collectValidPositionsByColor(!this.color, board.panelStatus);

        // seq5
        for (Position p : ourChances) {
            short[][] trialPanelStatus = board.panelStatus;
            trialPanelStatus[p.x][p.y] = ourColorStatus;

            List<List<Position>> fiveList = StoneUtil.countStone(trialPanelStatus, color, (short) 5);
            trialPanelStatus[p.x][p.y] = Board.STATUS_EMPTY;
            if (fiveList.size() > 0) {
                return new Stone(p, this.color);
            }
        }

        // opponent seq5
        for (Position p : opponentChances) {
            short[][] trialPanelStatus = board.panelStatus;
            trialPanelStatus[p.x][p.y] = opponentColorStatus;

            List<List<Position>> fiveList = StoneUtil.countStone(trialPanelStatus, !color, (short) 5);
            trialPanelStatus[p.x][p.y] = Board.STATUS_EMPTY;
            if (fiveList.size() > 0) {
                return new Stone(p, this.color);
            }
        }

        // live4
        for (Position p : ourChances) {
            short[][] trialPanelStatus = board.panelStatus;
            trialPanelStatus[p.x][p.y] = ourColorStatus;
            Set<Position> positions = collectHuoNByColor(color, trialPanelStatus, (short) 4);
            trialPanelStatus[p.x][p.y] = Board.STATUS_EMPTY;
            if (positions.size() > 0) {
                return new Stone(p, this.color);
            }
        }

        // opponent live4
        for (Position p : opponentChances) {
            short[][] trialPanelStatus = board.panelStatus;
            trialPanelStatus[p.x][p.y] = opponentColorStatus;
            Set<Position> positions = collectHuoNByColor(!color, trialPanelStatus, (short) 4);
            trialPanelStatus[p.x][p.y] = Board.STATUS_EMPTY;
            if (positions.size() > 0) {
                return new Stone(p, this.color);
            }
        }

        // live3
        for (Position p : ourChances) {
            short[][] trialPanelStatus = board.panelStatus;
            trialPanelStatus[p.x][p.y] = ourColorStatus;
            Set<Position> positions = collectHuoNByColor(color, trialPanelStatus, (short) 3);
            trialPanelStatus[p.x][p.y] = Board.STATUS_EMPTY;
            if (positions.size() > 0) {
                return new Stone(p, this.color);
            }
        }

        // opponent live3
        for (Position p : opponentChances) {
            short[][] trialPanelStatus = board.panelStatus;
            trialPanelStatus[p.x][p.y] = opponentColorStatus;
            Set<Position> positions = collectHuoNByColor(!color, trialPanelStatus, (short) 3);
            trialPanelStatus[p.x][p.y] = Board.STATUS_EMPTY;
            if (positions.size() > 0) {
                return new Stone(p, this.color);
            }
        }

        // 试算自己冲4
        for (Position p : ourChances) {
            short[][] trialPanelStatus = board.panelStatus;
            trialPanelStatus[p.x][p.y] = ourColorStatus;
            Set<Position> positions = collectChongNByColor(color, trialPanelStatus, (short) 4);
            trialPanelStatus[p.x][p.y] = Board.STATUS_EMPTY;
            if (positions.size() > 0) {
                return new Stone(p, this.color);
            }
        }

        // 试算对手冲4
        for (Position p : opponentChances) {
            short[][] trialPanelStatus = board.panelStatus;
            trialPanelStatus[p.x][p.y] = opponentColorStatus;
            Set<Position> positions = collectChongNByColor(!color, trialPanelStatus, (short) 4);
            trialPanelStatus[p.x][p.y] = Board.STATUS_EMPTY;
            if (positions.size() > 0) {
                return new Stone(p, this.color);
            }
        }

        // 试算自己活2
        for (Position p : ourChances) {
            short[][] trialPanelStatus = board.panelStatus;
            trialPanelStatus[p.x][p.y] = ourColorStatus;
            Set<Position> positions = collectHuoNByColor(color, trialPanelStatus, (short) 2);
            trialPanelStatus[p.x][p.y] = Board.STATUS_EMPTY;
            if (positions.size() > 0) {
                return new Stone(p, this.color);
            }
        }

        // 试算对手活2
        for (Position p : opponentChances) {
            short[][] trialPanelStatus = board.panelStatus;
            trialPanelStatus[p.x][p.y] = opponentColorStatus;
            Set<Position> positions = collectHuoNByColor(!color, trialPanelStatus, (short) 2);
            trialPanelStatus[p.x][p.y] = Board.STATUS_EMPTY;
            if (positions.size() > 0) {
                return new Stone(p, this.color);
            }
        }

        // there's no stone.
        short x = (short) (random.nextInt(Board.WIDTH - 9) + 4);
        short y = (short) (random.nextInt(Board.HEIGHT - 9) + 4);
        while (!board.checkEmpty(x, y)) {
            x = (short) random.nextInt(Board.WIDTH);
            y = (short) random.nextInt(Board.HEIGHT);
        }
        return new Stone(new Position(x, y), color);
    }

    Set<Position> collectChongNByColor(Boolean color, short[][] panelStatus, short count) {
        short targetStatus = color ? Board.STATUS_WHITE : Board.STATUS_BLACK;
        Set<Position> positionSet = new HashSet<>();

        for (short i = 0; i < Board.WIDTH; i++) {
            for (short j = 0; j < Board.WIDTH; j++) {
                // 横冲N
                positionSet.addAll(collectRowChongN(panelStatus, count, targetStatus, i, j));
                // 竖冲N
                positionSet.addAll(collectColChongN(panelStatus, count, targetStatus, i, j));
                // 右斜冲N
                positionSet.addAll(collectRightTiltChongN(panelStatus, count, targetStatus, i, j));
                // 左斜冲N
                positionSet.addAll(collectLeftTiltChongN(panelStatus, count, targetStatus, i, j));
            }
        }
        return positionSet;
    }

    private Set<Position> collectLeftTiltChongN(short[][] panelStatus, short count, short targetStatus, short i, short j) {
        Set<Position> positionSet = new HashSet<>();

        if (targetStatus != panelStatus[i][j]) {
            return positionSet;
        }

        Position targetPosition1 = i - 1 < 0 || j + 1 >= Board.HEIGHT ? null : new Position((short) (i - 1), (short) (j + 1));
        Position targetPosition2 = j + count >= Board.WIDTH || i - count < 0 ? null : new Position((short) (i - count), (short) (j + count));

        Boolean p1Empty = checkEmpty(panelStatus, targetPosition1);
        Boolean p2Empty = checkEmpty(panelStatus, targetPosition2);

        if ((p1Empty && !p2Empty) || (!p1Empty && p2Empty)) {

            short c = 0;
            for (short k = 0; k < count; k++) {
                if (i - k < 0 || j + k >= Board.HEIGHT || targetStatus != panelStatus[i - k][j + k]) {
                    break;
                }
                c++;
            }
            // 符合横向检查连子要求
            if (c == count) {
                positionSet.add(targetPosition1);
                positionSet.add(targetPosition2);
            }
        }
        return positionSet;
    }

    private Collection<? extends Position> collectRightTiltChongN(short[][] panelStatus, short count, short targetStatus, short i, short j) {
        Set<Position> positionSet = new HashSet<>();

        if (targetStatus != panelStatus[i][j]) {
            return positionSet;
        }

        Position targetPosition1 = j - 1 < 0 || i - 1 < 0 ? null : new Position((short) (i - 1), (short) (j - 1));
        Position targetPosition2 = j + count >= Board.WIDTH || i + count >= Board.WIDTH ? null : new Position((short) (i + count), (short) (j + count));

        Boolean p1Empty = checkEmpty(panelStatus, targetPosition1);
        Boolean p2Empty = checkEmpty(panelStatus, targetPosition2);

        if ((p1Empty && !p2Empty) || (!p1Empty && p2Empty)) {

            short c = 0;
            for (short k = 0; k < count; k++) {
                if (j + k >= Board.HEIGHT || i + k >= Board.WIDTH || targetStatus != panelStatus[i + k][j + k]) {
                    break;
                }
                c++;
            }
            // 符合横向检查连子要求
            if (c == count) {
                if (p1Empty)
                    positionSet.add(targetPosition1);
                if (p2Empty)
                    positionSet.add(targetPosition2);
            }
        }
        return positionSet;
    }

    private Collection<? extends Position> collectColChongN(short[][] panelStatus, short count, short targetStatus, short i, short j) {

        Set<Position> positionSet = new HashSet<>();

        if (targetStatus != panelStatus[i][j]) {
            return positionSet;
        }

        Position targetPosition1 = j - 1 < 0 ? null : new Position(i, (short) (j - 1));
        Position targetPosition2 = j + count >= Board.HEIGHT ? null : new Position(i, (short) (j + count));
        Boolean p1Empty = checkEmpty(panelStatus, targetPosition1);
        Boolean p2Empty = checkEmpty(panelStatus, targetPosition2);

        if ((p1Empty && !p2Empty) || (!p1Empty && p2Empty)) {
            short c = 0;
            for (short k = 0; k < count; k++) {
                if (j + k >= Board.HEIGHT || targetStatus != panelStatus[i][j + k]) {
                    break;
                }
                c++;
            }
            // 符合横向检查连子要求
            if (c == count) {
                if (p1Empty)
                    positionSet.add(targetPosition1);
                if (p2Empty)
                    positionSet.add(targetPosition2);
            }
        }
        return positionSet;
    }

    private Set<Position> collectRowChongN(short[][] panelStatus, short count, short targetStatus, short i, short j) {
        Set<Position> positionSet = new HashSet<>();

        if (targetStatus != panelStatus[i][j]) {
            return positionSet;
        }

        Position targetPosition1 = i - 1 < 0 ? null : new Position((short) (i - 1), j);
        Position targetPosition2 = i + count >= Board.WIDTH ? null : new Position((short) (i + count), j);
        Boolean p1Empty = checkEmpty(panelStatus, targetPosition1);
        Boolean p2Empty = checkEmpty(panelStatus, targetPosition2);

        if ((p1Empty && !p2Empty) || (!p1Empty && p2Empty)) {
            short c = 0;
            for (short k = 0; k < count; k++) {
                if (i + k >= Board.WIDTH || targetStatus != panelStatus[i + k][j]) {
                    break;
                }
                c++;
            }
            // 符合横向检查连子要求
            if (c == count) {
                if (p1Empty)
                    positionSet.add(targetPosition1);
                if (p2Empty)
                    positionSet.add(targetPosition2);
            }
        }
        return positionSet;
    }

    Set<Position> collectHuoNByColor(Boolean color, short[][] panelStatus, short count) {
        short targetStatus = color ? Board.STATUS_WHITE : Board.STATUS_BLACK;
        Set<Position> positionSet = new HashSet<>();

        for (short i = 0; i < Board.WIDTH; i++) {
            for (short j = 0; j < Board.WIDTH; j++) {
                // 横活N
                positionSet.addAll(collectRowHuoN(panelStatus, count, targetStatus, i, j));
                // 竖活N
                positionSet.addAll(collectColHuoN(panelStatus, count, targetStatus, i, j));
                // 右斜活N
                positionSet.addAll(collectRightTiltHuoN(panelStatus, count, targetStatus, i, j));
                // 左斜活N
                positionSet.addAll(collectLeftTiltHuoN(panelStatus, count, targetStatus, i, j));

            }
        }
        return positionSet;
    }

    private Set<Position> collectRowHuoN(short[][] panelStatus, short count, short targetStatus, short i, short j) {
        Set<Position> positionSet = new HashSet<>();

        if (targetStatus != panelStatus[i][j]) {
            return positionSet;
        }

        Position targetPosition1 = i - 1 < 0 ? null : new Position((short) (i - 1), j);
        Position targetPosition2 = i + count >= Board.WIDTH ? null : new Position((short) (i + count), j);

        if (checkEmpty(panelStatus, targetPosition1)
                && checkEmpty(panelStatus, targetPosition2)
        ) {
            short c = 0;
            for (short k = 0; k < count; k++) {
                if (targetStatus != panelStatus[i + k][j]) {
                    break;
                }
                c++;
            }
            // 符合横向检查连子要求
            if (c == count) {
                positionSet.add(targetPosition1);
                positionSet.add(targetPosition2);
            }
        }
        return positionSet;
    }

    private Set<Position> collectColHuoN(short[][] panelStatus, short count, short targetStatus, short i, short j) {
        Set<Position> positionSet = new HashSet<>();

        if (targetStatus != panelStatus[i][j]) {
            return positionSet;
        }

        Position targetPosition1 = j - 1 < 0 ? null : new Position(i, (short) (j - 1));
        Position targetPosition2 = j + count >= Board.WIDTH ? null : new Position(i, (short) (j + count));

        if (checkEmpty(panelStatus, targetPosition1)
                && checkEmpty(panelStatus, targetPosition2)
        ) {
            short c = 0;
            for (short k = 0; k < count; k++) {
                if (targetStatus != panelStatus[i][j + k]) {
                    break;
                }
                c++;
            }
            // 符合横向检查连子要求
            if (c == count) {
                positionSet.add(targetPosition1);
                positionSet.add(targetPosition2);
            }
        }
        return positionSet;
    }

    private Set<Position> collectRightTiltHuoN(short[][] panelStatus, short count, short targetStatus, short i, short j) {
        Set<Position> positionSet = new HashSet<>();

        if (targetStatus != panelStatus[i][j]) {
            return positionSet;
        }

        Position targetPosition1 = j - 1 < 0 || i - 1 < 0 ? null : new Position((short) (i - 1), (short) (j - 1));
        Position targetPosition2 = j + count >= Board.WIDTH || i + count >= Board.WIDTH ? null : new Position((short) (i + count), (short) (j + count));

        if (checkEmpty(panelStatus, targetPosition1)
                && checkEmpty(panelStatus, targetPosition2)
        ) {
            short c = 0;
            for (short k = 0; k < count; k++) {
                if (targetStatus != panelStatus[i + k][j + k]) {
                    break;
                }
                c++;
            }
            // 符合横向检查连子要求
            if (c == count) {
                positionSet.add(targetPosition1);
                positionSet.add(targetPosition2);
            }
        }
        return positionSet;
    }

    private Set<Position> collectLeftTiltHuoN(short[][] panelStatus, short count, short targetStatus, short i, short j) {
        Set<Position> positionSet = new HashSet<>();

        if (targetStatus != panelStatus[i][j]) {
            return positionSet;
        }

        Position targetPosition1 = i - 1 < 0 || j + 1 >= Board.HEIGHT ? null : new Position((short) (i - 1), (short) (j + 1));
        Position targetPosition2 = i + count >= Board.WIDTH || j - count < 0 ? null : new Position((short) (i + count), (short) (j - count));

        if (checkEmpty(panelStatus, targetPosition1)
                && checkEmpty(panelStatus, targetPosition2)
        ) {
            short c = 0;
            for (short k = 0; k < count; k++) {
                if (targetStatus != panelStatus[i + k][j - k]) {
                    break;
                }
                c++;
            }
            // 符合横向检查连子要求
            if (c == count) {
                positionSet.add(targetPosition1);
                positionSet.add(targetPosition2);
            }
        }
        return positionSet;
    }

    Set<Position> collectValidPositionsByColor(Boolean color, short[][] panelStatus) {
        short targetStatus = color ? Board.STATUS_WHITE : Board.STATUS_BLACK;
        Set<Position> positionSet = new HashSet<>();
        for (short i = 0; i < Board.WIDTH; i++) {
            for (short j = 0; j < Board.WIDTH; j++) {
                if (panelStatus[i][j] == targetStatus) {
                    // 周围8个中的空位置
                    // 左上
                    if (i - 1 >= 0 && j - 1 >= 0 && panelStatus[i - 1][j - 1] == Board.STATUS_EMPTY) {
                        positionSet.add(new Position((short) (i - 1), (short) (j - 1)));
                    }

                    // 右下
                    if (i + 1 < Board.WIDTH && j + 1 < Board.HEIGHT && panelStatus[i + 1][j + 1] == Board.STATUS_EMPTY) {
                        positionSet.add(new Position((short) (i + 1), (short) (j + 1)));
                    }

                    // 右上
                    if (i - 1 >= 0 && j + 1 < Board.HEIGHT && panelStatus[i - 1][j + 1] == Board.STATUS_EMPTY) {
                        positionSet.add(new Position((short) (i - 1), (short) (j + 1)));
                    }

                    // 左下
                    if (i + 1 < Board.WIDTH && j - 1 >= 0 && panelStatus[i + 1][j - 1] == Board.STATUS_EMPTY) {
                        positionSet.add(new Position((short) (i + 1), (short) (j - 1)));
                    }

                    // 上方
                    if (i - 1 >= 0 && panelStatus[i - 1][j] == Board.STATUS_EMPTY) {
                        positionSet.add(new Position((short) (i - 1), j));
                    }

                    // 下方
                    if (i + 1 < Board.WIDTH && panelStatus[i + 1][j] == Board.STATUS_EMPTY) {
                        positionSet.add(new Position((short) (i + 1), j));
                    }

                    // 左方
                    if (j - 1 >= 0 && panelStatus[i][j - 1] == Board.STATUS_EMPTY) {
                        positionSet.add(new Position(i, (short) (j - 1)));
                    }

                    // 右方
                    if (j + 1 < Board.HEIGHT && panelStatus[i][j + 1] == Board.STATUS_EMPTY) {
                        positionSet.add(new Position(i, (short) (j + 1)));
                    }
                }
            }
        }
        return positionSet;
    }
}
