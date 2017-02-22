package packet.bingo.project;

import lombok.*;

import java.io.Serializable;

/**
 * Created by incognito on 2016-12-02.
 * Packet class represents a packet.
 * Protocol is easily simplified by Serializable interface.
 * If you want to re-design protocol, modify this class.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Packet implements Serializable {

    private static final long serialVersionUID = 4894946798145447397L;
    private PacketType protocol;
    private String mainField;
    private String subField;

}