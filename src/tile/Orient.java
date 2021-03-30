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
};
