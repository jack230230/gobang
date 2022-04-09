package gobang;
public abstract class Player {
    public String name;
    public Boolean color;

    protected Player() {
    }

    public abstract Stone action(Board board);
}