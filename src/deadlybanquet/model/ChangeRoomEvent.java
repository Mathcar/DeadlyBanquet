package deadlybanquet.model;

import java.awt.event.ActionEvent;

/**
 * Created by Hampus on 2016-03-21.
 */

//Actionevent extension used when a character wants to change rooms
public class ChangeRoomEvent extends ActionEvent {
    private String originRoom, targetRoom;
    //Direction is the direction in which the door the
    // character enters has its connection
    private Direction enterDirection;
    public ChangeRoomEvent(Object source, String fromRoom, String toRoom,
                            Direction enterDirection){
        super(source, EventEnum.CHANGE_ROOMS.ordinal(), "");
        targetRoom = toRoom;
        originRoom = fromRoom;
        this.enterDirection = enterDirection;
    }

    public String getTargetRoom(){
        return targetRoom;
    }

    public String getOriginRoom(){
        return originRoom;
    }

    public Direction getEnterDirection(){
        return enterDirection;
    }
}
