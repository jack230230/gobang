import java.util.ArrayList;
import java.util.List;

public class StoneUtil {
    public static Boolean checkWin(short[][] panelStatus, Boolean color) {
        List<List<Position>> fiveList = countStone(panelStatus, color, (short) 5);
        return fiveList.size() > 0;
    }

    public static Boolean checkEmpty(short[][] panelStatus, Position position) {
        if (position == null) {
            return false;
        }
        return panelStatus[position.x][position.y] == Board.STATUS_EMPTY;
    }

    // 查询连子
    public static List<List<Position>> countStone(short[][] panelStatus, Boolean white, short count) {
        List<List<Position>> positions = new ArrayList<>();

        for (short i = 0; i < Board.WIDTH; i++) {
            for (short j = 0; j < Board.WIDTH; j++) {
                short currentStatus = panelStatus[i][j];

                if (white) {
                    if (currentStatus != Board.STATUS_WHITE) {
                        continue;
                    }
                } else {
                    if (currentStatus != Board.STATUS_BLACK) {
                        continue;
                    }
                }

                // 横的连续 n 个相同
                List<Position> positionsRow = countRowSame(panelStatus, count, i, j, currentStatus);
                if (positionsRow.size() == count) {
                    positions.add(positionsRow);
                }

                // 竖的连续 n 个相同
                List<Position> positionsCol = countColSame(panelStatus, count, i, j, currentStatus);
                if (positionsCol.size() == count) {
                    positions.add(positionsCol);
                }

                // 右斜连续 n 个相同
                List<Position> positionsRightTilt = countRightTiltSame(panelStatus, count, i, j, currentStatus);
                if (positionsRightTilt.size() == count) {
                    positions.add(positionsRightTilt);
                }

                // 左斜连续 n 个相同
                List<Position> positionsLeftTilt = countLeftTiltSame(panelStatus, count, i, j, currentStatus);
                if (positionsLeftTilt.size() == count) {
                    positions.add(positionsLeftTilt);
                }
            }
        }

        return positions;
    }

    public static List<Position> countSeqOpen(short[][] panelStatus, short count, Boolean color) {
        List<Position> positions = new ArrayList<>();
        short targetStatus = color ? Board.STATUS_WHITE : Board.STATUS_BLACK;
        for (short i = 0; i < Board.WIDTH; i++) {
            for (short j = 0; j < Board.WIDTH; j++) {
                short currentStatus = panelStatus[i][j];
                if (currentStatus != targetStatus) {
                    continue;
                }
                positions.addAll(checkRowTwoOpen(panelStatus, count, i, j, currentStatus));
                positions.addAll(checkColTwoOpen(panelStatus, count, i, j, currentStatus));
                positions.addAll(checkRightTiltTwoOpen(panelStatus, count, i, j, currentStatus));
                positions.addAll(checkLeftTiltTwoOpen(panelStatus, count, i, j, currentStatus));
            }
        }

        return positions;
    }

    private static List<Position> checkRowTwoOpen(short[][] panelStatus, short count, short i, short j, short currentStatus) {
        List<Position> positions = new ArrayList<>();
        Position targetPosition1 = i - 1 < 0 ? null : new Position((short) (i - 1), j);
        Position targetPosition2 = i + count >= Board.WIDTH ? null : new Position((short) (i + count), j);
        if (targetPosition1 != null || targetPosition2 != null) {
            short c = 0;
            for (short k = 0; k < count; k++) {
                if (currentStatus != panelStatus[i + k][j]) {
                    break;
                }
                c++;
            }
            // 符合横向检查连子要求
            if (c == count) {
                // 检查端头是否为空
                if (checkEmpty(panelStatus, targetPosition1)) {
                    positions.add(targetPosition1);
                }
                if (checkEmpty(panelStatus, targetPosition2)) {
                    positions.add(targetPosition2);
                }
            }
        }
        return positions;
    }

    private static List<Position> checkColTwoOpen(short[][] panelStatus, short count, short i, short j, short currentStatus) {
        List<Position> positions = new ArrayList<>();
        Position targetPosition1 = j - 1 < 0 ? null : new Position(i, (short) (j - 1));
        Position targetPosition2 = j + count >= Board.HEIGHT ? null : new Position(i, (short) (j + count));
        if (targetPosition1 != null && targetPosition2 != null) {
            short c = 0;
            for (short k = 0; k < count; k++) {
                if (currentStatus != panelStatus[i][j + k]) {
                    break;
                }
                c++;
            }
            // 符合横向检查连子要求
            if (c == count) {
                // 检查端头是否为空
                if (checkEmpty(panelStatus, targetPosition1)) {
                    positions.add(targetPosition1);
                }
                if (checkEmpty(panelStatus, targetPosition2)) {
                    positions.add(targetPosition2);
                }
            }
        }
        return positions;
    }

    private static List<Position> checkRightTiltTwoOpen(short[][] panelStatus, short count, short i, short j, short currentStatus) {
        List<Position> positions = new ArrayList<>();
        Position targetPosition1 = j - 1 < 0 || i - 1 < 0 ? null : new Position((short) (i - 1), (short) (j - 1));
        Position targetPosition2 = j + count >= Board.HEIGHT || i + count >= Board.WIDTH ? null : new Position((short) (i + count), (short) (j + count));
        if (targetPosition1 != null && targetPosition2 != null) {
            short c = 0;
            for (short k = 0; k < count; k++) {
                if (currentStatus != panelStatus[i + k][j + k]) {
                    break;
                }
                c++;
            }
            // 符合横向检查连子要求
            if (c == count) {
                // 检查端头是否为空
                if (checkEmpty(panelStatus, targetPosition1)) {
                    positions.add(targetPosition1);
                }
                if (checkEmpty(panelStatus, targetPosition2)) {
                    positions.add(targetPosition2);
                }
            }
        }
        return positions;
    }

    private static List<Position> checkLeftTiltTwoOpen(short[][] panelStatus, short count, short i, short j, short currentStatus) {
        List<Position> positions = new ArrayList<>();
        Position targetPosition1 = j - 1 < 0 || j + 1 >= Board.HEIGHT ? null : new Position((short) (i - 1), (short) (j + 1));
        Position targetPosition2 = i - count < 0 || j + count >= Board.HEIGHT ? null : new Position((short) (i - count), (short) (j + count));
        if (targetPosition1 != null && targetPosition2 != null) {
            short c = 0;
            for (short k = 0; k < count; k++) {
                if (currentStatus != panelStatus[i - k][j + k]) {
                    break;
                }
            }
            // 符合横向检查连子要求
            if (c == count) {
                // 检查端头是否为空
                if (checkEmpty(panelStatus, targetPosition1)) {
                    positions.add(targetPosition1);
                }
                if (checkEmpty(panelStatus, targetPosition2)) {
                    positions.add(targetPosition2);
                }
            }
        }
        return positions;
    }


    private static List<Position> countRowSame(short[][] panelStatus, short count, short i, short j, short currentStatus) {
        List<Position> positionsRow = new ArrayList<>();
        for (short k = 0; k < count; k++) {
            if (i + k >= Board.WIDTH || currentStatus != panelStatus[i + k][j]) {
                // 坐标超过长度或者当前不是目标棋子
                break;
            }
            positionsRow.add(new Position((short) (i + k), j));
        }

        return positionsRow;
    }

    private static List<Position> countColSame(short[][] panelStatus, short count, short i, short j, short currentStatus) {
        List<Position> positionsCol = new ArrayList<>();
        for (short k = 0; k < count; k++) {
            if (j + k >= Board.WIDTH || currentStatus != panelStatus[i][j + k]) {
                // 坐标超过长度或者当前不是目标棋子
                break;
            }
            positionsCol.add(new Position(i, (short) (j + k)));
        }
        return positionsCol;
    }


    private static List<Position> countRightTiltSame(short[][] panelStatus, short count, short i, short j, short currentStatus) {
        List<Position> positions = new ArrayList<>();
        for (short k = 0; k < count; k++) {
            if (i + k >= Board.WIDTH || j + k >= Board.WIDTH || currentStatus != panelStatus[i + k][j + k]) {
                // 坐标超过长度或者当前不是目标棋子
                break;
            }
            positions.add(new Position((short) (i + k), (short) (j + k)));
        }
        return positions;
    }


    private static List<Position> countLeftTiltSame(short[][] panelStatus, short count, short i, short j, short currentStatus) {
        List<Position> positions = new ArrayList<>();
        for (short k = 0; k < count; k++) {
            if (i - k < 0 || j + k >= Board.WIDTH || currentStatus != panelStatus[i - k][j + k]) {
                // 坐标超过长度或者当前不是目标棋子
                break;
            }
            positions.add(new Position((short) (i - k), (short) (j + k)));
        }
        return positions;
    }
}
