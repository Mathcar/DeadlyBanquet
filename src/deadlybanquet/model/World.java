package deadlybanquet.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import deadlybanquet.AI;
import deadlybanquet.RenderSet;
import deadlybanquet.View;
import deadlybanquet.ai.AIControler;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by Hampus on 2016-03-04.
 */
public class World implements ActionListener {
    private static Time time;
    private Player player;
    private AIControler ai;
    private ArrayList<AIControler> aiss;

    //roomMap needs to have empty borders! [0][any], [any][0], [max][any],[any][max] all need to be unfilled
    //for the rooms to get their connections made
    private Room[][] roomMap;

    public World(){
        //Create all rooms and place them in the roomMap
        initRoomMap();
        //This must be done after all rooms that are intended to be there
        //are created and added to the roomMap!
        createDoorsInRooms();
        //Initialized all AI/NPC and their Characters
        initAIs();
        //Initialize all player related data
        initPlayer();
        //Create the time object (it's initialization is done in it's constructor)
        time = new Time();
    }

    public void initPlayer(){
        Character playerCharacter = new Character(this, "Gandalf", 3, 3);
        player = new Player(playerCharacter);
        roomMap[1][1].addCharacter(playerCharacter);
    }

    public void initAIs(){
        //Not really sure in which order these thing are supposed to be initialized, but regardless
        //it should be done in here
    	Character npc = new Character(this, "Frådo", 9, 5);
    	ai = new AIControler(this,npc);
    	aiss = new ArrayList<>();
    	aiss.add(ai);
    	roomMap[1][1].addCharacter(npc);
  
    }

    public void initRoomMap(){
        roomMap = new Room[4][4];       //Needs to be updated as more rooms are added
        //add room initiations
        roomMap[1][1] = new Room("res/pictures/living_room2.tmx", "Living Room");
    }

    //World's update function, somewhat unsure  as to what parameters are supposed
    //to be here
    public void update(GameContainer container, StateBasedGame s, int deltaTime){

        time.incrementTime(deltaTime);
        for(Room[] rs : roomMap){
            for(Room r : rs){
                if(r != null)
                    r.update(container,  s, deltaTime);
            }
        }
       // for(AI ai : ais){
         //   ai.update(container, s, deltaTime);
        //}
        player.update(container, s, deltaTime);
    }

    //return time in hours, DO NOT USE THIS TO SET THE TIME
    public static Time getTime(){
        return time;
    }

    public void createDoorsInRooms(){
        for(int i = 1; i< roomMap.length-1; i++){
            for(int j= 1; j<roomMap.length-1; j++){
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
                    if(r!= null)
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
