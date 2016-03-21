package deadlybanquet.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import deadlybanquet.RenderSet;

/**
 * Created by Hampus on 2016-03-04.
 */
public class World implements ActionListener {
    private static Time time;
    private ArrayList ais;
    private Player player;
    private Room[][] roomMap;

    public World(){
        roomMap = new Room[2][2];       //Needs to be updated as more rooms are added
        //add room initiations
        Character playerCharacter = new Character(this, "Gandalf", 3, 3);
        roomMap[0][0] = new Room("res/pictures/living_room2.tmx", "Living Room");
        player = new Player(playerCharacter);
        roomMap[0][0].addCharacter(playerCharacter);
        
        //This must be done after all rooms that are intended to be there
        //are created and added to the roomMap!
        createDoorsInRooms();
        time = new Time();

    }

    //World's update function, somewhat unsure  as to what parameters are supposed
    //to be here
    public void update(float deltaTime){
        time.incrementTime(deltaTime);
    }

    //return time in hours, DO NOT USE THIS TO SET THE TIME
    public static Time getTime(){
        return time;
    }

    public void createDoorsInRooms(){
        for(int i = 0; i< roomMap.length; i++){
            for(int j= 0; j<roomMap.length; j++){
                if(roomMap[i][j] != null){
                    String north = "";
                    String south = "";
                    String east = "";
                    String west = "";
                    if(roomMap[i-1][j] != null)	// i-1 gives indexOutOfBound when i=0
                        west = roomMap[i-1][j].getName();
                    if(roomMap[i+1][j] != null)
                        east = roomMap[i+1][j].getName();
                    if(roomMap[i][j+1] != null)
                        north = roomMap[i][j+1].getName();
                    if(roomMap[i][j-1] != null)	//j-1 gives indexOutOfBound when j=0
                        south = roomMap[i][j-1].getName();
                    //All neighbours have been compiled, now notify the room
                    roomMap[i][j].assignDoorConnections(north, south, east, west);
                }
            }
        }
    }

    //Returns position of a room in the roomMap by its name
    public Position  getRoomPosition(String name){
        for(int i=0; i<roomMap.length;i++){
            for(int j = 0; j<roomMap[i].length;j++){
                if(roomMap != null && roomMap[i][j].getName().equals(name)){
                    return new Position(i,j);
                }
            }
        }
        return null;
    }

    public Room getRoomRef(String name){
        for(int i=0; i<roomMap.length;i++){
            for(int j = 0; j<roomMap[i].length;j++){
                if(roomMap != null && roomMap[i][j].getName().equals(name)){
                    return roomMap[i][j];
                }
            }
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //ActionEvent fired when a character wished to move
        if(e.getID() == EventEnum.MOVE.ordinal()) {
            for (Room[] rm : roomMap) {
                for (Room r : rm) {
                    r.moveWithCollision(e);
                }
            }
        }
        //ActionEvent fired when a character wants to change room/walk through a door
        if(e.getID() == EventEnum.CHANGE_ROOMS.ordinal()) {
            ChangeRoomEvent ce = (ChangeRoomEvent)e;
            //Check if new room has space, remove from old room, add to new
            Room targetRoom = getRoomRef(ce.getTargetRoom());
            Room originRoom = getRoomRef(ce.getOriginRoom());
            if (!targetRoom.entranceIsBlocked(ce.getEnterDirection())) { //todo commented out by Tom
                originRoom.removeCharacter((Character)ce.getSource());
                targetRoom.addCharacterToRoom((Character)e.getSource(),ce.getEnterDirection());
            }
        }
    }
    
    public RenderSet getRenderSet(){
    	for(Room[] roomList: roomMap){
    		for(Room room: roomList){
    			if(room != null && room.hasCharacter(player.getCharacter())){
    				return room.getRenderSet();
    			}
    		}
    	}
		return null;
    }
}
