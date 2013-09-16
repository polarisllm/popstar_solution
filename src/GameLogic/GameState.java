/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import java.util.ArrayList;

/**
 *
 * @author wssccc
 */
class Pt {

    int x;
    int y;
    int clear;

    @Override
    public String toString() {
        return "{" + (x + 1) + "," + (GameState.MAP_SIZE - y) + '#' + clear + '}';
    }

    public Pt(int x, int y, int clear) {
        this.x = x;
        this.y = y;
        this.clear = clear;
    }

    public Pt(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class GameState {

    static final int[] RULE = {0, 0, 20, 45, 80, 125, 180, 245, 320, 405, 500, 605};
    public static final int MAP_SIZE = 10;
    public byte[] mdata;
    int score;

    public String state() {
        String str = "";
        for (int i = 0; i < mdata.length; i++) {
            str = str + mdata[i] + ",";
            if ((i + 1) % 10 == 0) {
                str = str + "\r\n";
            }
        }
        return str;
    }

    public int evalMaxScore() {
        int[] table = new int[6];
        for (int i = 0; i < mdata.length; i++) {
            ++table[mdata[i]];
        }
        int evalScore = 0;
        for (int i = 1; i <= 5; i++) {
            if (table[i] < RULE.length) {
                evalScore += RULE[table[i]];
            } else {
                evalScore += table[i] * 100;
            }
        }
        return evalScore;
    }

    @Override
    public GameState clone() {
        return new GameState(this.mdata.clone(), this.score);
    }

    public GameState(byte[] mdata, int score) {
        this.mdata = mdata;
        this.score = score;
    }

    public int count() {
        int count = 0;
        for (int i = 0; i < mdata.length; i++) {
            if (mdata[i] != 0) {
                ++count;
            }
        }
        return count;
    }

    public void tap(int x, int y) {
        byte t = mdata[y * MAP_SIZE + x];
        //check
        if (t == 0) {
            System.out.println("tap on empty!");
        }
        //flood fill
        int n = floodAndClear(x, y);
        if (n < RULE.length) {
            this.score += RULE[n];
        } else {
            System.out.println("goooosh! n=" + n);
            this.score += n * 80;
        }
        //cleard
        //veritical move
        for (int mapx = 0; mapx < GameState.MAP_SIZE; mapx++) {
            int putter = GameState.MAP_SIZE - 1;
            for (int scanner = GameState.MAP_SIZE - 1; scanner >= 0; scanner--) {
                if (mdata[scanner * MAP_SIZE + mapx] != 0) {
                    if (scanner != putter) {
                        mdata[putter * MAP_SIZE + mapx] = mdata[scanner * MAP_SIZE + mapx];
                    }
                    --putter;
                }
            }
            for (int i = putter; i >= 0; --i) {
                mdata[ i * MAP_SIZE + mapx] = (byte) 0;
            }
        }
        //horizonal move
        int putter = 0;
        for (int scanner = 0; scanner < GameState.MAP_SIZE; ++scanner) {
            if (mdata[ (GameState.MAP_SIZE - 1) * MAP_SIZE + scanner] != 0) {
                if (scanner != putter) {
                    //copy scanner col to putter col
                    for (int i = 0; i < GameState.MAP_SIZE; i++) {
                        mdata[  i * MAP_SIZE + putter] = mdata[  i * MAP_SIZE + scanner];
                    }
                }
                ++putter;
            }
        }
        for (int i = putter; i < GameState.MAP_SIZE; ++i) {
            for (int j = 0; j < GameState.MAP_SIZE; j++) {
                mdata[ j * MAP_SIZE + i] = (byte) 0;
            }
        }
    }

    int floodAndClear(int fx, int fy) {

        byte t = mdata[  fy * MAP_SIZE + fx];
        mdata[  fy * MAP_SIZE + fx] = 0;
        int count = 0;
        int[] xs = new int[100];
        int[] ys = new int[100];
        int p = 0;

        xs[p] = fx;
        ys[p] = fy;
        ++p;
        int x, y;
        while (p != 0) {
            --p;
            x = xs[p];
            y = ys[p];
            ++count;
            //flood 
            if (x + 1 < MAP_SIZE) {
                if (mdata[  y * MAP_SIZE + x + 1] == t) {
                    mdata[  y * MAP_SIZE + x + 1] = 0;
                    xs[p] = x + 1;
                    ys[p] = y;
                    ++p;
                }
            }
            if (y + 1 < MAP_SIZE) {
                if (mdata[  (y + 1) * MAP_SIZE + x] == t) {
                    mdata[  (y + 1) * MAP_SIZE + x] = 0;
                    xs[p] = x;
                    ys[p] = y + 1;
                    ++p;
                }
            }
            if (x - 1 >= 0) {
                if (mdata[ y * MAP_SIZE + x - 1] == t) {
                    mdata[  y * MAP_SIZE + x - 1] = 0;
                    xs[p] = x - 1;
                    ys[p] = y;
                    ++p;
                }
            }
            if (y - 1 >= 0) {
                if (mdata[  (y - 1) * MAP_SIZE + x] == t) {
                    mdata[  (y - 1) * MAP_SIZE + x] = 0;
                    xs[p] = x;
                    ys[p] = y - 1;
                    ++p;
                }
            }

        }
        return count;
    }

    ArrayList<Pt> getTaps() {
        ArrayList<Pt> entries = new ArrayList<>();
        //backup mdata;  

        byte[] nmdata = mdata.clone();
        for (int i = 0; i < GameState.MAP_SIZE; i++) {
            for (int j = 0; j < GameState.MAP_SIZE; j++) {
                if (mdata[i * MAP_SIZE + j] != 0) {
                    int clear = floodAndClear(j, i);
                    if (clear > 1) {
                        entries.add(new Pt(j, i, clear));
                    }
                }
            }
        }
        //restore
        mdata = nmdata;
        return entries;
    }
}
