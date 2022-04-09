package gobang;
import java.util.*;

public class StupidPlayer extends Player {
    Random random = new Random();

    private List<List<Position>> tryList = new ArrayList<>();

    public StupidPlayer(String name, Boolean color) {
        this.name = name;
        this.color = color;
    }

    // 下棋动作，玩家根据台面情况，给出一个合法的棋子
    @Override
    public Stone action(Board board) {
        Position targetPosition;

        List<Position> positions4 = StoneUtil.countSeqOpen(board.panelStatus, (short) 4, this.color);
        targetPosition = getBest(positions4);

        if (targetPosition == null) {
            List<Position> positions3 = StoneUtil.countSeqOpen(board.panelStatus, (short) 3, this.color);
            targetPosition = getBest(positions3);
        }

        if (targetPosition == null) {
            List<Position> positions2 = StoneUtil.countSeqOpen(board.panelStatus, (short) 2, this.color);
            targetPosition = getBest(positions2);
        }

        if (targetPosition == null) {
            List<Position> positions1 = StoneUtil.countSeqOpen(board.panelStatus, (short) 1, this.color);
            targetPosition = getBest(positions1);
        }

        if (targetPosition == null) {
            // 台面上没有棋子，随机生成一个合法的位置
            short x = (short) (random.nextInt(Board.WIDTH - 9) + 4);
            short y = (short) (random.nextInt(Board.HEIGHT - 9) + 4);
            while (!board.checkEmpty(x, y)) {
                x = (short) random.nextInt(Board.WIDTH);
                y = (short) random.nextInt(Board.HEIGHT);
            }
            targetPosition = new Position(x, y);
        }
        return new Stone(targetPosition, this.color);
    }

    private Position getBest(List<Position> positions) {
        if (positions == null || positions.size() == 0) {
            return null;
        }

        Map<Position, Integer> pmap = new HashMap<>();

        for (Position p : positions) {
            if (pmap.containsKey(p)) {
                pmap.put(p, pmap.get(p) + 1);
            } else {
                pmap.put(p, 1);
            }
        }


        int max = 0;
        for (Position p : pmap.keySet()) {
            if (pmap.get(p) > max) {
                max = pmap.get(p);
            }
        }

        List<Position> candidates = new ArrayList<>();
        for (Position p : pmap.keySet()) {
            if (pmap.get(p) == max) {
                candidates.add(p);
            }
        }

        return candidates.get(random.nextInt(candidates.size()));
    }
}
