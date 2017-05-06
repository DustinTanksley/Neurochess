package chesscs5400;

public class Action implements Comparable<Action> {

    public int promotion = -1;
    public int xfrom, yfrom, xto, yto;
    public boolean attack;
    public int[] kings;
    public boolean[] castle;
    public int[] EnPassant;
    public boolean special;
    public boolean Q = false; //Quiescential
    public CP TempPiece;
    public int score = 0;

    public Action(int x1, int y1, int x2, int y2, boolean attack) {
        xfrom = x1;
        yfrom = y1;
        xto = x2;
        yto = y2;
        this.attack = attack;
    }

    Action(Action a) { //does not deep copy, only shallow, but should be fine. This is really for promotion
        promotion = a.promotion;
        xfrom = a.xfrom;
        yfrom = a.yfrom;
        xto = a.xto;
        yto = a.yto;
        attack = a.attack;
        kings = a.kings;
        castle = a.castle;
        EnPassant = a.EnPassant;
        special = a.special;
    }

    public boolean equals(Action a) {
        return a.xfrom == xfrom && a.yfrom == yfrom && a.xto == xto && a.yto == yto;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + this.promotion;
        hash = 73 * hash + this.xfrom * 4;
        hash = 73 * hash + this.yfrom * 2;
        hash = 73 * hash + this.xto * 8;
        hash = 73 * hash + this.yto * 16;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Action other = (Action) obj;
        if (this.promotion != other.promotion) {
            return false;
        }
        if (this.xfrom != other.xfrom) {
            return false;
        }
        if (this.yfrom != other.yfrom) {
            return false;
        }
        if (this.xto != other.xto) {
            return false;
        }
        if (this.yto != other.yto) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Action o) {
        if (o.score > score) {
            return -1;
        } else if (o.score < score) {
            return 1;
        } else {
            return 0;
        }
    }
}
