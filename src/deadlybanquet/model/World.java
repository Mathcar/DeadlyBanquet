package deadlybanquet.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import deadlybanquet.RenderSet;

/**
 * Created by Hampus on 2016-03-04.
 */
public class World implements ActionListener {
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


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getID() == EventEnum.MOVE.ordinal()) {
            for (Room[] rm : roomMap) {
                for (Room r : rm) {
                    r.moveWithCollision(e);
                }
            }
        }
        if(e.getID() == EventEnum.CHANGE_ROOMS.ordinal()){
            //Check if new room has space, remove from old room, add to new
            //if(entranceIsBlocked(Direction.EAST)) //todo commented out by Tom
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
