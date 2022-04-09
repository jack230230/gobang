import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GobangFrame extends JFrame implements BoardStatusHandler {

    public static int PANEL_STONE_SIZE = 28;
    public static int PANEL_CELL_SIZE = 32;
    public static int OFFSET_X = 21;
    public static int OFFSET_Y = 21;

    public static int PANEL_PIXEL = 500;

    public JPanel stonePanel = new JPanel();
    public GobangJPanel panel = new GobangJPanel();
    Board fiveStoneBoard;

    public GobangFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(PANEL_PIXEL, PANEL_PIXEL + 20);
        setResizable(false);
        stonePanel.setLayout(null);
        stonePanel.setOpaque(false);
        this.setContentPane(panel);
        panel.add(stonePanel);

        panel.setBounds(0, 0, PANEL_PIXEL, PANEL_PIXEL);

        stonePanel.setBounds(0, 0, PANEL_PIXEL, PANEL_PIXEL);

        setVisible(true);

        Player whitePlayer = new SmartPlayer("电脑1", Stone.STONE_COLOR_WHITE);
        Player blackPlayer = new UserPlayer("玩家", stonePanel);
//        Player blackPlayer = new SmartPlayer("电脑2", Stone.STONE_COLOR_BLACK);

        fiveStoneBoard = new Board(whitePlayer, blackPlayer, this);
        fiveStoneBoard.start();
    }

    @Override
    public void handleVictory(boolean color) {
        JOptionPane.showMessageDialog(this, color ? "白色获胜!" : "黑色获胜!");
        int value = JOptionPane.showConfirmDialog(this, "再来一局?");
        if (value == JOptionPane.YES_OPTION) {
            fiveStoneBoard.reset();
            fiveStoneBoard.start();
        } else {
            System.exit(0);
        }
    }

    @Override
    public void handlePeace() {
        JOptionPane.showMessageDialog(this, "和局结束！");
        int value = JOptionPane.showConfirmDialog(this, "再来一局?");

        if (value == JOptionPane.YES_OPTION) {
            fiveStoneBoard.reset();
            fiveStoneBoard.start();
        } else {
            System.exit(0);
        }
    }

    @Override
    public void handleAction(Stone stone) {
        // 绘制棋子
        StoneJPanel panel = new StoneJPanel(stone.color ? Color.WHITE : Color.BLACK);
        panel.setBounds((stone.position.y + 1) * PANEL_CELL_SIZE - OFFSET_X, (stone.position.x + 1) * PANEL_CELL_SIZE - OFFSET_Y, 28, 28);
        stonePanel.add(panel);
        panel.revalidate();
        panel.repaint();
    }

    @Override
    public void handleReset() {
        stonePanel.removeAll();
        panel.revalidate();
        panel.repaint();
    }
}

class GobangJPanel extends JLabel {
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Image image = new ImageIcon("resource/panel.jpg").getImage();
        g.drawImage(image, 0, 0, GobangFrame.PANEL_PIXEL, GobangFrame.PANEL_PIXEL, this);
    }
}

class StoneJPanel extends JPanel {
    Color color;

    public StoneJPanel(Color color) {
        this.color = color;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(color);
        g.fillOval(0, 0, GobangFrame.PANEL_STONE_SIZE, GobangFrame.PANEL_STONE_SIZE);
    }
}

class UserPlayer extends Player {

    JPanel stonePanel;
    private Position position;

    public UserPlayer(String name, JPanel stonePanel) {
        this.color = Stone.STONE_COLOR_BLACK;
        this.name = name;
        this.stonePanel = stonePanel;
        this.position = new Position((short) -1, (short) -1);

        stonePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 计算位置
                short x = (short) (Math.floor((e.getY() + GobangFrame.PANEL_CELL_SIZE / 2) / GobangFrame.PANEL_CELL_SIZE) - 1);
                short y = (short) (Math.floor((e.getX() + GobangFrame.PANEL_CELL_SIZE / 2) / GobangFrame.PANEL_CELL_SIZE) - 1);
                position.x = x;
                position.y = y;
            }
        });
    }

    @Override
    public Stone action(Board board) {

        do {
            position = getPosition();
            // 空位置才能下子
        } while (!board.checkEmpty(position.x, position.y));

        Stone stone = new Stone(position, this.color);
        return stone;
    }

    private Position getPosition() {
        position.x = -1;
        while (position.x < 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return position;
    }
}
