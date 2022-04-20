package de.hochtaunusschule.mathpuzzle.max;

/**
 * @author David (_Esel)
 */
public interface HexDisplay {
    int UP_LEFT = 1;
    int UP_MID = 2;
    int UP_RIGHT = 4;
    int CENTER = 8;
    int DOWN_LEFT = 16;
    int DOWN_MID = 32;
    int DOWN_RIGHT = 64;
    int FULL_LEFT = DOWN_LEFT | UP_LEFT;
    int FULL_RIGHT = DOWN_RIGHT | UP_RIGHT;

    int[] DIGITS = new int[] {
        FULL_LEFT | FULL_RIGHT | UP_MID | DOWN_MID,
        FULL_RIGHT,
        UP_MID | UP_RIGHT | CENTER | DOWN_LEFT | DOWN_MID,
        FULL_RIGHT | CENTER | UP_MID | DOWN_MID,
        UP_LEFT | FULL_RIGHT | CENTER,
        UP_MID | UP_LEFT | CENTER | DOWN_RIGHT | DOWN_MID,
        FULL_LEFT | DOWN_MID | UP_MID | CENTER | DOWN_RIGHT,
        FULL_RIGHT | UP_MID,
        FULL_RIGHT | FULL_LEFT | UP_MID | DOWN_MID | CENTER,
        FULL_RIGHT | UP_MID | DOWN_MID | CENTER | UP_LEFT,
        FULL_RIGHT | FULL_LEFT | UP_MID | CENTER,
        FULL_LEFT | CENTER | DOWN_MID | DOWN_RIGHT,
        FULL_LEFT | UP_MID | DOWN_MID,
        FULL_RIGHT | DOWN_LEFT | CENTER | DOWN_MID,
        FULL_LEFT | UP_MID | DOWN_MID | CENTER,
        FULL_LEFT | UP_MID | CENTER
    };
}
