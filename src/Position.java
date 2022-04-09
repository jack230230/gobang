public class Position {
    short x, y;

    public Position(short x, short y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        Position p = (Position) obj;
        return p.y == this.y && this.x == p.x;
    }

    @Override
    public int hashCode() {
        return (String.valueOf(x)+":"+String.valueOf(y)).hashCode();
    }
}
