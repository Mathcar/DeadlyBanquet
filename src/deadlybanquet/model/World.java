package deadlybanquet.model;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import deadlybanquet.AI;
import deadlybanquet.ConversationModel;
import deadlybanquet.RenderSet;
import deadlybanquet.View;
import deadlybanquet.ai.AIControler;
import deadlybanquet.ai.Brain;
import deadlybanquet.states.States;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

/**
 * Created by Hampus on 2016-03-04.
 */
public class World implements ActionListener, TileBasedMap {
    private static HashMap<AIControler, Brain> controlerBrainMap;
    private static Time time;
    private Player player;
    private AStarPathFinder masterPathfinder;
    private ArrayList<AIControler> aiss;
    private AIControler ai;
    private boolean talk;
    private ConversationModel playerConv;

    //roomMap needs to have empty borders! [0][any], [any][0], [max][any],[any][max] all need to be unfilled
    //for the rooms to get their connections made
    private Room[][] roomMap;

    public World(){
        //Create all rooms and place them in the roomMap
        initRoomMap();
        //This must be done after all rooms that are intended to be there
        //are created and added to the roomMap!
        createDoorConnectionsInRooms();
        //Initialized all AI/NPC and their Characters
        initAIs();
        //Initialize all player related data
        initPlayer();
        //Create the time object (it's initialization is done in it's constructor)
        time = new Time();
    }

    public void initPlayer(){
        Character playerCharacter = new Character(this, "Gandalf", 9, 13);
        player = new Player(playerCharacter);
        roomMap[2][2].addCharacter(playerCharacter);
    }

    public void initAIs(){
        //Not really sure in which order these thing are supposed to be initialized, but regardless
        //it should be done in here
//    	Character npc = new Character(this, "Frido", 9, 5);
//    	ai = new AIControler(this,npc);
//    	aiss = new ArrayList<>();
//    	aiss.add(ai);
    	

        controlerBrainMap = new HashMap<AIControler, Brain>();
    	Character npc = new Character(this, "Fr�do", 9, 5);
        AIControler	ai = new AIControler(this,npc);
        //Brain brain = new Brain();
    	aiss = new ArrayList<>();
    	aiss.add(ai);

       // controlerBrainMap.put(ai, brain);

    	roomMap[2][2].addCharacter(npc);
  
    }

    public void initRoomMap(){
        roomMap = new Room[4][4];       //Needs to be updated as more rooms are added
        //add room initiations
        roomMap[2][2] = new Room("res/pictures/living_room2.tmx", "Living Room",this);
        roomMap[1][2] = new Room("res/pictures/bedroom.tmx", "Bedroom",this);
        roomMap[2][1] = new Room("res/pictures/kitchen.tmx", "Kitchen",this);
        masterPathfinder = new AStarPathFinder(this, 10, false);
    }

    //World's update function, somewhat unsure  as to what parameters are supposed
    //to be here
    public void update(GameContainer container, StateBasedGame s, int deltaTime){
        Input input = container.getInput();
        time.incrementTime(deltaTime);
        for(Room[] rs : roomMap){
            for(Room r : rs){
                if(r != null) {
                    r.update(container, s, deltaTime);
                    //-----------------DEBUG-------------------------------
                    if(r.hasCharacter(player.getCharacter())){
                        if(input.isKeyPressed(Input.KEY_T)){
                            r.debugTileOnPos(player.getCharacter().getPos());
                        }
                    }
                    //--------------------------------------------------------
                }

            }
        }
       // for(AI ai : ais){
         //   ai.update(container, s, deltaTime);
        //}
        
        if(talk){
        	s.enterState(States.talk);
        	talk = false;
        }
        
        player.update(container, s, deltaTime);

    }

    //return time in hours, DO NOT USE THIS TO SET THE TIME
    public static Time getTime(){
        return time;
    }

    public void createDoorConnectionsInRooms(){
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
                    /*//--------------------DEBUG------------------------------------
                    //Debugmessage to see if the correct interpretation of the roomMap
                    // is made
                    String debugMsg = "North : " + north + "\n South : " + south
                                        + "\n West : " + west + "\n East : " + east;
                    debugMsg = "Room namned : " + roomMap[i][j].getName() +
                                "\n Room at position (" + i + ", " + j + ") \n" + debugMsg ;
                    System.out.println(debugMsg);
                    //---------------------------------------------------------------*/
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
                if(roomMap[i][j] != null && roomMap[i][j].getName().equals(name)){
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
            /*//----------------------------------DEBUG--------------------------------
            System.out.println(originRoom.getName() + "   to   " + targetRoom.getName()
                                + " entering from " + ce.getEnterDirection().toString() );
            //-----------------------------------------------------------------------*/

            if (!targetRoom.entranceIsBlocked(ce.getEnterDirection())) {
                Character c = (Character)ce.getSource();
                originRoom.removeCharacter(c);
                //Tell the affected rooms to notify all characters so that this can be added
                //to memory
                originRoom.notifyRoomChange(c.getName(), ce.getOriginRoom(), ce.getTargetRoom());
                targetRoom.notifyRoomChange(c.getName(), ce.getOriginRoom(), ce.getTargetRoom());
                targetRoom.addCharacterToRoom((Character)e.getSource(),ce.getEnterDirection());
            }
            /*//----------------------------------DEBUG--------------------------------
            else{
                System.out.println(ce.getTargetRoom() + " Was blocked from " + ce.getEnterDirection().toString());
            }
            //-----------------------------------------------------------------------*/
        }
        
        if(e.getID() == EventEnum.CHECK_DOOR.ordinal()){
        	Character characterTemp = (Character) e.getSource();
        	for (Room[] rm : roomMap) {
        		for (Room r : rm) {
                    if(r != null && r.hasCharacter(characterTemp)){
                    	r.checkDoor(e);
                    }
                }
        	}
        }
        if(e.getID() == EventEnum.TALK_TO.ordinal()){
        	Character characterTemp = (Character) e.getSource();
        	for (Room[] rm : roomMap) {
                for (Room r : rm) {
                    if(r !=null && r.hasCharacter(characterTemp)){
                    	if(r.isCharacterOn(characterTemp.getFacedTilePos())){
                    		if(player.isCharacter(characterTemp)){
                    			
	                    		for(AIControler a : aiss){
	                    			if(a.getCharacterId() == (r.getCharacterOnPos(characterTemp.getFacedTilePos())).getId()){
	                    				playerConv = new ConversationModel(player,a.getNpc());
	                    				talk = true;
	                    			}
	                    		}	      
                    		}
                    		//change to talk state between cahracterTemp and r.getCharacterOnPos(characterTemp.getFacedTilePos()
                    	}
                    }	
                }
        	}
        }
        if(e.getID() == EventEnum.REQUEST_PATH_TO_PERSON.ordinal()){
            Room targetRoom= null;
            Room originRoom = null;
            for(Room[] rs : roomMap){
                for(Room r : rs){
                    if(r!=null){
                        if(r.hasCharacter(e.getActionCommand()))
                            targetRoom = r;
                        else  if (r.hasCharacter((Character)e.getSource()))
                            originRoom = r;
                    }
                }
            }
            if(originRoom.equals(targetRoom)){
                Path p = originRoom.createPathToPerson((NPC)e.getSource(), e.getActionCommand());
                sendPathToAI(((Character)e.getSource()).getName(), p);
                //Send path to correct AIController
            }else{
                createMasterPathTo(originRoom.getName(), targetRoom.getName());
            }
        }
        //The actioncommand in this event is presumed to contain the name of the room you wish to enter
        if(e.getID() == EventEnum.REQUEST_PATH_TO_DOOR.ordinal()){
            Path p;
            for(Room[] rs : roomMap){
                for(Room r : rs){
                    if(r!=null && r.hasCharacter((Character)e.getSource()) && r.hasConnectionTo(e.getActionCommand())){
                        p = r.createPathToDoor((Character)e.getSource(), e.getActionCommand());
                    }
                }
            }
            //send the path to the correct AI
        //    sendPathToAI(((Character)e.getSource()).getName();
        }
        if(e.getID() == EventEnum.REQUEST_PATH_TO_ROOM.ordinal()){
            Character c = (Character)e.getSource();
            MasterPath mp = new MasterPath();
            for(Room[] rs : roomMap){
                for(Room r : rs){
                    if(r != null && r.hasCharacter(c)){
                        mp = createMasterPathTo(r.getName(), e.getActionCommand());
                    }
                }
            }
            //Send masterpath to correct AI
            sendMasterPathToAI(c.getName(), mp);
        }
    }

    public void sendPathToAI(String charName, Path p){
        for(AIControler ai : aiss){
            if(ai.getCharacterName().equals(charName))
                ai.setPath(p);
        }
    }

    public void sendMasterPathToAI(String charName, MasterPath mp){
        for(AIControler ai : aiss){
            if(ai.getCharacterName().equals(charName))
                ai.setMasterPath(mp);
        }
    }

    public MasterPath createMasterPathTo(String origin, String target){
        Position org = getRoomPosition(origin);
        Position targ = getRoomPosition(target);
        Path p = masterPathfinder.findPath(null, org.getX(), org.getY(), targ.getX(), targ.getY());
        MasterPath mp = new MasterPath();
        for(int i = 0; i<p.getLength(); i++){
            mp.addStep(roomMap[p.getX(i)][p.getY(i)].getName());
        }
        return mp;
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

    @Override
    public int getWidthInTiles() {
        return roomMap[0].length;
    }

    @Override
    public int getHeightInTiles() {
        return roomMap.length;
    }

    @Override
    public void pathFinderVisited(int i, int i1) {

    }

    @Override
    public boolean blocked(PathFindingContext pfc, int x, int y) {
        if(roomMap[x][y] != null && roomMap[pfc.getSourceX()][pfc.getSourceY()].hasConnectionTo(roomMap[x][y].getName()))
            return false;
        else
            return true;
    }

    @Override
    public float getCost(PathFindingContext pathFindingContext, int i, int i1) {
        return 1;
    }
    
    public ConversationModel getPlayerConv(){
    	return playerConv;
    }
    
}
