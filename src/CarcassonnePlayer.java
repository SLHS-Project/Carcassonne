/*
 * TODO
 * -Takes care of Meeples
 * -Keep track of individual score
 * -Serves as identification for Game class
 */

import tile.Meeple;

import java.awt.image.*;

public class CarcassonnePlayer {
    private String name;
    private int score;
    private Meeple[] meeples;

    public CarcassonnePlayer(String n) {
        name = n;
        score = 0;

        meeples = new Meeple[7]; //7 meeples, and 1 other for scoring placement
        for (int i = 0; i < meeples.length; i++)
            meeples[i] = new Meeple(name);
    }

    public void addScore(int num) {
        score += num;
    }

    public Meeple[] getMeeples() {
        return meeples;
    }

    public void receiveMp() {
        for (Meeple mp : meeples) {
            if (!mp.available()) {
                mp.lift();
                break;
            }
        }
    }

    public void placeMp() {
        for (Meeple mp : meeples) {
            if (mp.available()) {
                mp.place();
                break;
            }
        }
    }

    public BufferedImage getMpImg() {
        return meeples[0].getImg();
    }

    public int availableMp() {
        int i = 0;
        for (Meeple mp : meeples) {
            if (mp.available())
                i++;
        }
        return i;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String toString() {
        return this.getName();
    }
}
