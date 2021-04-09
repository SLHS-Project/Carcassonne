package tile;

import java.util.Arrays;

public class Side {
    TerrainType[] sidetypes;
    String[] side;

    public Side(TerrainType l, TerrainType m, TerrainType r) {
        sidetypes = new TerrainType[3];
        sidetypes[0] = l;
        sidetypes[1] = m;
        sidetypes[2] = r;
    }

    public TerrainType[] getSide() {
        return this.sidetypes;
    }
    
    public String[] getSideStr()
    {
    		return side;
    }

    public Side getReversedSide() {
        return new Side(
                this.sidetypes[2],
                this.sidetypes[1],
                this.sidetypes[0]
        );
    }

    public boolean equals(Object obj) {
        return Arrays.equals(this.sidetypes, ((Side) obj).getSide());
    }
}