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
import deadlybanquet.ai.NPCBrain;
import deadlybanquet.ai.BrainFactory;
import deadlybanquet.ai.PlayerBrain;
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
    private static HashMap<AIControler, NPCBrain> controlerBrainMap;
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
        
        //Initialize all player related data
        initPlayer();
        //Initialized all AI/NPC and their Characters
        initAIs();
        
        
        //Create the time object (it's initialization is done in it's constructor)
        time = new Time();
    }

    public void initPlayer(){
        Character playerCharacter = new Character(this, "Gandalf", 9, 13);
        PlayerBrain playerBrain = new PlayerBrain();
        player = new Player(playerCharacter, playerBrain);
        roomMap[2][2].addCharacter(playerCharacter);
    }

    public void initAIs(){
        //Not really sure in which order these thing are supposed to be initialized, but regardless
        //it should be done in here
    	aiss = new ArrayList<>();
    	controlerBrainMap = new HashMap<AIControler, NPCBrain>();
    	createNpc(new Position(9,5), "Frido", "Living Room" );
    	createNpc(new Position(13,9), "Candy", "Living Room" );
    	createNpc(new Position(2,2), "BURT", "Living Room" );
    	createNpc(new Position(7,12), "Cindy", "Kitchen" );
    	createNpc(new Position(9,7), "Aragorn", "Kitchen" );
    	createNpc(new Position(9,3), "Daisy", "Bedroom" );
    }
    //always use when creating an npc 
    public void createNpc (Position pos, String name, String room){
    	Character npc = new Character(this, name, pos.getX(), pos.getY());
    	AIControler	ai = new AIControler(this,npc);
        NPCBrain brain = BrainFactory.makeBrain(room, npc.getName());
    	aiss.add(ai);
    	System.out.println(room);
        controlerBrainMap.put(ai, brain);
    	System.out.println(room);
        Position p = getRoomPosition(room);
    	roomMap[p.getX()][p.getY()].addCharacter(npc);
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

        for(AIControler ai : aiss){
            ai.update(this, deltaTime);
        }
        
        if(talk){
        	s.enterState(States.talk);
        	talk = false;
        }
        
        player.update(this, container, s, deltaTime);
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
                if(roomMap[i][j] != null && roomMap[i][j].getName().equals(name)){
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
    
    public boolean attemptChangeRooms(Character ce) {
    	Room targetRoom = getRoomRef(getRoomOfCharacter(ce).getFacedDoor(ce.getFacedTilePos()).getDestinationRoom());
        Room originRoom = getRoomRef(getRoomOfCharacter(ce).getFacedDoor(ce.getFacedTilePos()).getOriginRoom());
        /*//----------------------------------DEBUG--------------------------------
        System.out.println(originRoom.getName() + "   to   " + targetRoom.getName()
                            + " entering from " + ce.getEnterDirection().toString() );
        //-----------------------------------------------------------------------*/

        if (!targetRoom.entranceIsBlocked(ce.getDirection())) {
            originRoom.removeCharacter(ce);
            //Tell the affected rooms to notify all characters so that this can be added
            //to memory
            targetRoom.addCharacterToRoom(ce,ce.getDirection());
            notifyRoomChange(originRoom,targetRoom,ce);
            seePeople(ce, targetRoom);


        }
        /*//----------------------------------DEBUG--------------------------------
        else{
            System.out.println(ce.getTargetRoom() + " Was blocked from " + ce.getEnterDirection().toString());
        }
        //-----------------------------------------------------------------------*/
    

    	return true;
    }


    public boolean attemptMove(Character c, Direction dir){
        c.setDirection(dir);
        return getRoomOfCharacter(c).attemptMove(c);
    }

    //Attempt to create a path to a person within the same room
    public boolean attemptCreatePathToPerson(AIControler aic, String targetPerson){
        Character c = aic.getNpc();
        Path p;
        Room r = getRoomOfCharacter(c);
        p = r.createPathToPerson(c, targetPerson);
        if(p != null){
            //Send the path to the AIControler requesting it
            aic.setPath(p);
            return true;    //Path successfully created
        }
        return false; //Something failed along the way and no path was made and sent
    }

    //Attempt to create a path to a door leading to the specified room.
    //Door must be in the same room, meaning targetRoom must be adjacent to current room
    public boolean attemptCreatePathToDoor(AIControler aic, String targetRoom){
        Character c = aic.getNpc();
        Path p;
        Room r = getRoomOfCharacter(c);
        if(r.hasConnectionTo(targetRoom)){
            p = r.createPathToDoor(c, targetRoom);
            aic.setPath(p);     //Send path to correct AIControler
            return true;        //Path was successfully made
        }
        return false;       //Something went wrong and no path was created and sent
    }

    //Attempt to create a MasterPath from current room to any specified room
    public boolean attemptCreateMasterPath(AIControler aic, String targetRoom){
        Character c = aic.getNpc();
        MasterPath mp = new MasterPath();
        Room r = getRoomOfCharacter(c);
        mp = createMasterPathTo(r.getName(), targetRoom);
        if(mp!=null){               //Was a masterPath actually made?
            aic.setMasterPath(mp);  //Send created MasterPath to AI
            return true;        //MasterPath successfully created and sent
        }
        return false;   //Something went wrong and no MasterPath was created
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //ActionEvent fired when a character wished to move
        /*      OBSOLETE, use attempMove() instead!
        if(e.getID() == EventEnum.MOVE.ordinal()) {
            for (Room[] rm : roomMap) {
                for (Room r : rm) {
                    if(r!= null)
                        r.moveWithCollision(e);
                }
            }
        }
        */
       
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
                targetRoom.addCharacterToRoom(c,ce.getEnterDirection());
                notifyRoomChange(originRoom,targetRoom,c);
                seePeople(c, targetRoom);


            }
            /*//----------------------------------DEBUG--------------------------------
            else{
                System.out.println(ce.getTargetRoom() + " Was blocked from " + ce.getEnterDirection().toString());
            }
            //-----------------------------------------------------------------------*/
        }
        /*      OBSOLETE
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
                    			Character c = r.getCharacterOnPos(characterTemp.getFacedTilePos());
	                    		for(AIControler a : aiss){
	                    			if(a.getCharacterId() == c.getId()){
	                    				c.setDirection(Direction.getOppositeDirection(characterTemp.getDirection()));
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
        }*/
        /*---------------------OBSOLETE, REPLACED BY attemptCreatePathTo... methods found above---------
        if(e.getID() == EventEnum.REQUEST_PATH_TO_PERSON.ordinal()){
            Path p;
            for(Room[] rs : roomMap){
                for(Room r : rs){
                    if(r!=null){
                        if (r.hasCharacter((Character)e.getSource())) {
                            p = r.createPathToPerson((NPC) e.getSource(), e.getActionCommand());
                            sendPathToAI(((Character) e.getSource()).getName(), p);
                        }
                    }
                }
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
        */
    }

    private AIControler getRelatedControler(Character c){
        for(AIControler aic : aiss){
            if(aic.getCharacterName().equals(c.getName()))
                return aic;
        }

        return null;

    }

    private Room getRoomOfCharacter(Character c){
        for(Room[] rs :  roomMap){
            for(Room r : rs){
                if(r!=null){
                    if(r.hasCharacter(c))
                        return r;
                }
            }
        }
        System.out.println("Character of AIC doesnt exist in any room... error...");
        return null; //THIS SHOULD NEVER HAPPEN
    }

    //Tells all affected AIcontrollers/player that a person has went into a room and left another
    private void notifyRoomChange(Room originRoom, Room targetRoom, Character person){
        for(Character c : originRoom.getCharactersInRoom()){
            if(originRoom.hasCharacter(player.getCharacter()))
                player.observeRoomChange(person.getName(), originRoom.getName(), targetRoom.getName());
            else
                getRelatedControler(c).observeRoomChange(person.getName(), originRoom.getName(), targetRoom.getName());
        }
        for(Character c : targetRoom.getCharactersInRoom()){
            if(player.isCharacter(c) )
                player.observeRoomChange(person.getName(), originRoom.getName(), targetRoom.getName());
            else
                getRelatedControler(c).observeRoomChange(person.getName(), originRoom.getName(), targetRoom.getName());
        }
    }

    //Called on every person in the room when somebody strikes up a conversation with somebody
    //else or examines (but does not pick up) an object in the room. (Other than the person
    //with whom the person is talking, obviously)
    private void observeInteraction(Character who, Character with, Room room){
        for(Character c : room.getCharactersInRoom()){
            if(room.hasCharacter(player.getCharacter()))
                player.observeInteraction(who.getName(), with.getName());
            else
                getRelatedControler(c).observeInteraction(who.getName(), with.getName());
        }
    }

    //called on entering a room
    private void seePeople (Character whoSees, Room whatRoom){
        ArrayList<String> names = new ArrayList<>();
        for(Character c : whatRoom.getCharactersInRoom()){
            names.add(c.getName());
        }
        //Either notify the corresponding AIControler or the player
        if(player.isCharacter(whoSees))
            player.seePeople(names);
        else
            getRelatedControler(whoSees).seePeople(names);
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

    public static NPCBrain getControlerBrain(AIControler aic){
        return controlerBrainMap.get(aic);
    }
    
}
