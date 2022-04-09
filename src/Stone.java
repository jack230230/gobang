public class Stone {
    public static Boolean STONE_COLOR_WHITE = true;
    public static Boolean STONE_COLOR_BLACK = false;
    // 横纵坐标轴位置
    Position position;
    // true 是白色，false 是黑色
    Boolean color;

    private Stone() {
    }
    public Stone(Position position, Boolean color) {
        this.position = position;
        this.color = color;
    }

}
