package tile;

public enum Rotation {
    D0(0),
    D90(3),
    D180(2),
    D270(1);

    private final int id;
    Rotation(int id) {
        this.id = id;
    }
    public int iden() {
        return this.id;
    }
    public Rotation fromIden(int i) {
        for(Rotation r: Rotation.values())
            if(r.id == i)
                return r;
        return D0;
    }
    public Rotation next() {
        return this.fromIden((this.iden() + 1) % 4);
    }
    public int degree() {
        switch(this.iden()) {
            case 0: return 0;
            case 1: return 270;
            case 2: return 180;
            case 3: return 90;
            default: return 0;
        }
    }
}