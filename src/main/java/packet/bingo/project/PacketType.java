package packet.bingo.project;

/**
 * Created by incognito on 2016-12-02.
 */

// Protocol
public enum PacketType {

    MESSAGE,
    MESSAGE_TO,
    SIGN_UP,
    SIGN_IN,
    BINGO_NUM,
    CLICKED_NUM,
    READY,
    DUPLICATION_CHECK,
    REQUEST_USER_LIST,
    BINGO_COMPLETE,
    WIN

}