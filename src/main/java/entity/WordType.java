package entity;

/**
 * What kind of word a pictogram represents on the board.
 */
public enum WordType {

    /** A verb, shown on the main board (e.g. "play", "eat"). */
    ACTION,

    /** A noun, linked to one or more actions (e.g. "ball", "apple"). */
    THING
}