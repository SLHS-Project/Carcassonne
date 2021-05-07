package tile;

public enum Orient {
    N(0), W(1), S(2), E(3);

    private final int id;
    Orient(int id) {
        this.id = id;
    }
    public int iden() {
        return this.id;
    }
    public Orient fromId(int i) {
        for(Orient o: Orient.values())
            if(o.id == i)
                return o;
        return null;
    }
    public Orient opposite() {
        return this.fromId((this.id + 2)%4);
    }
    public Orient rotate(Rotation r) {
        return this.fromId((this.id - r.iden() + 4) % 4);
    }
    public String toString() {
        switch (this.id) {
            case 0: return "N";
            case 1: return "W";
            case 2: return "S";
            case 3: return "E";
            default: return "no";
        }
    }
    public int[] getVector() {
        switch (this.id) {
            //                        x  y
            case 0: return new int[] {0, -1};
            case 1: return new int[] {-1, 0};
            case 2: return new int[] {0, 1};
            case 3: return new int[] {1, 0};
            default: return new int[] {0, 0};
        }
    }
};
