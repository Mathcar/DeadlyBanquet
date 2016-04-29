package deadlybanquet.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import deadlybanquet.RenderSet;
import deadlybanquet.ai.AIControler;
import deadlybanquet.ai.NPCBrain;
import deadlybanquet.ai.BrainFactory;
import deadlybanquet.ai.PlayerBrain;
import deadlybanquet.ai.TaskExecuter;
import deadlybanquet.ai.*;
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

public class World implements TileBasedMap, TaskExecuter {
    private static World current;
    private static PlayerBrain playerBrain;
    private static HashMap<AIControler, NPCBrain> controlerBrainMap;
    private static Time time;

    private Player player;
    //Pathfinding variables
    private AStarPathFinder masterPathfinder;
    private Position targetPosition;

    private ArrayList<AIControler> aiss;
    private AIControler ai;
    private boolean talk;
    //Conversation data
    private ConversationModel playerConv;
    private ArrayList<ConversationModel> npcConversations;

    

    //roomMap needs to have empty borders! [0][any], [any][0], [max][any],[any][max] all need to be unfilled
    //for the rooms to get their connections made
    private Room[][] roomMap;

    public World(){
        //init the Debug class, use the control over channels to make debug a bit more ordered!
        Debug.init();
        //Set which npc will be able to print messages to debug, if none is set then all of them will be able to send
        Debug.setDebugNPC("BURT");
        Debug.setChannelStatus(Debug.Channel.SPEECH_ACTS, false);
        Debug.setChannelStatus(Debug.Channel.PATHFINDING, false);
        current=this;

        npcConversations = new ArrayList<>();
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

        //add all the items and such.
        ArrayList<String>temp = getAllCharacterNames();
        ItemsAndNameSingelton.getInstance().setAllNames(temp);
        temp.remove(player.getName());
        ItemsAndNameSingelton.getInstance().setAllNPCNames(temp);
        temp.clear();
        temp = ItemsAndNameSingelton.getInstance().readFile();
        ItemsAndNameSingelton.getInstance().setItems(temp);
        // All the items and names are added in the singelton class.
        //Create initialization memories (like who is in the same room and such)
        initBasicMemories();
    }

    //plant basic memories like the positions of the people in the same room in all the AIs
    private void initBasicMemories(){
        for(AIControler aic : aiss){
            Room r = getRoomOfCharacter(aic.getCharacter());
            for(Character c : r.getAllCharacters()){
                if(!aic.getCharacterName().equals("BURT")) {
                    getControlerBrain(aic).plantFalseMemory(new Whereabouts(c.getName(),
                            r.getName()));
                }
            }
        }
    }

    private void initPlayer(){
        Character playerCharacter = new Character("NoName", 9, 13);
        playerBrain = new PlayerBrain(playerCharacter.getName());
        player = new Player(playerCharacter, playerBrain);
        roomMap[2][2].addCharacter(playerCharacter);
    }

    private void initAIs(){
        //Not really sure in which order these thing are supposed to be initialized, but regardless
        //it should be done in here
    	aiss = new ArrayList<>();
    	controlerBrainMap = new HashMap<>();
    	createNpc(new Position(9,5), "Frido", "Living Room" );
    	createNpc(new Position(10,13), "Candy", "Living Room" );
    	createNpc(new Position(2,2), "BURT", "Living Room" );
    	createNpc(new Position(7,12), "Cindy", "Kitchen" );
    	createNpc(new Position(9,7), "Aragorn", "Kitchen" );
    	createNpc(new Position(9,3), "Daisy", "Bedroom" );
    }
    //always use when creating an npc 
    private void createNpc (Position pos, String name, String room){
    	Character npc = new Character(name, pos.getX(), pos.getY());
    	AIControler	ai = new AIControler(npc);
        NPCBrain brain = BrainFactory.makeBrain(room, npc.getName(), ai);
    	aiss.add(ai);
    	Debug.printDebugMessage(name + " created in " +room, Debug.Channel.WORLD);
        controlerBrainMap.put(ai, brain);
        Position p = getRoomPosition(room);
    	roomMap[p.getX()][p.getY()].addCharacter(npc);
    }

    private void initRoomMap(){
        roomMap = new Room[4][4];       //Needs to be updated as more rooms are added
        //add room initiations
        roomMap[2][2] = new Room("res/pictures/living_room2.tmx", "Living Room");
        roomMap[1][2] = new Room("res/pictures/bedroom.tmx", "Bedroom");
        roomMap[2][1] = new Room("res/pictures/kitchen.tmx", "Kitchen");
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
        ArrayList<ConversationModel> cleanup = new ArrayList<>();
        //Update all ongoing conversations
        for(ConversationModel cm : npcConversations){
            cm.runConversation();
            if(cm.isConversationOver())
                cleanup.add(cm);
        }
        for(ConversationModel cm : cleanup) {
            cleanUpConversation(cm);
        }
        if(talk){
        	s.enterState(States.talk);
        	talk = false;
        }else if(playerConv != null && playerConv.isConversationOver()){
            cleanUpConversation(playerConv);
        }
        
        player.update(this, container, s, deltaTime);

    }

    private void setUpConversation(AIControler aic1, AIControler aic2){
        npcConversations.add(new ConversationModel(getControlerBrain(aic1), getControlerBrain(aic2)));
        aic1.getCharacter().setTalking(true);
        aic2.getCharacter().setTalking(true);
    }

    //Cleans up all the conversation vy notifying the characters and then removing the conversation
    private void cleanUpConversation(ConversationModel cm){
        Debug.printDebugMessage("Conversation with " + cm.getIPerceiver1().getName() + " and " +
                            cm.getIPerceiver2().getName() + " is now being cleaned up...", Debug.Channel.CONVERSATION);
        getCharacterRef(cm.getIPerceiver1().getName()).setTalking(false);
        getCharacterRef(cm.getIPerceiver2().getName()).setTalking(false);
        if(cm == playerConv) {
            playerConv = null;
            //Debug.printDebugMessage();("Playerconversation cleaned up!");
        }
        else {
            npcConversations.remove(cm);
            //Debug.printDebugMessage();("Npc conversation cleaned up!");
        }
        Debug.printDebugMessage(cm.printConversation(), Debug.Channel.CONVERSATION);
    }

    private void removeFromConversation(IPerceiver p){

    }

    //return time in hours, DO NOT USE THIS TO SET THE TIME
    public static Time getTime(){
        return time;
    }

    //Return current time in a timestamp
    public static TimeStamp getTimeStamp(){ return time.time();}

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
                    Debug.printDebugMessage();(debugMsg);
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
    
    public boolean attemptTurnToAdjacentCharacter(Character c, Character c2){

    	int x = c.getPos().getX();
    	int y = c.getPos().getY();
    	int targetX  = c2.getPos().getX();
    	int targetY  = c2.getPos().getY();
    	if(x == targetX && y == targetY-1){
    		c.setDirection(Direction.SOUTH);
    		return true;
    	}else if(x == targetX && y == targetY+1){
    		c.setDirection(Direction.NORTH);
    		return true;
    	}else if(x == targetX-1 && y == targetY){
    		c.setDirection(Direction.EAST);
    		return true;
    	}else if(x == targetX+1 && y == targetY){
    		c.setDirection(Direction.WEST);
    		return true;
    	}
    	
    	return false;
    	
    }

    public boolean attemptTalk(Character chr){
        Room temp = getRoomOfCharacter(chr);
        Debug.printDebugMessage(chr.getName() + " attempts to talk...", Debug.Channel.WORLD);
    	if(temp.isCharacterOn(chr.getFacedTilePos())){
            Character c = temp.getCharacterOnPos(chr.getFacedTilePos());
            AIControler target = getRelatedControler(c);
    		if(player.isCharacter(chr)){
                c.setDirection(Direction.getOppositeDirection(chr.getDirection()));
                playerConv = new ConversationModel(playerBrain,controlerBrainMap.get(target), player.getCharacter().getDefaultImage(),
                        target.getCharacter().getDefaultImage());
                player.getCharacter().setTalking(true);
                target.getCharacter().setTalking(true);
               // attemptCreatePathToPerson(target, "Frido");
              //  attemptCreateMasterPath(a, "Kitchen");
              //  createMasterPathTo(getRoomOfCharacter(a.getCharacter()).getName(), "Bedroom");
               // attemptCreatePathToDoor(a, "Bedroom");
                talk = true;
                return true;

    		}
            else{       //Character attempting to talk is an npc
                AIControler attempter = getRelatedControler(chr);
                c.setDirection(Direction.getOppositeDirection(chr.getDirection()));
                setUpConversation(attempter, target);
                Debug.printDebugMessage(attempter.getCharacterName() + " started a conversation with " +
                                    target.getCharacterName(), Debug.Channel.WORLD);
                return true;
            }
    	}
    	return false;
    }
    
    public boolean attemptChangeRooms(Character ce) {
    	Door d = getRoomOfCharacter(ce).getFacedDoor(ce.getFacedTilePos());
    	if(d != null){
	    	Room targetRoom = getRoomRef(d.getDestinationRoom());
	        Room originRoom = getRoomRef(d.getOriginRoom());
	        /*//----------------------------------DEBUG--------------------------------
	        Debug.printDebugMessage();(originRoom.getName() + "   to   " + targetRoom.getName()
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
	        return true;
    	}else{
    		return false;
    	}
        /*//----------------------------------DEBUG--------------------------------
        else{
            Debug.printDebugMessage();(ce.getTargetRoom() + " Was blocked from " + ce.getEnterDirection().toString());
        }
        //-----------------------------------------------------------------------*/
    
    }


    public boolean attemptMove(Character c, Direction dir){
        c.setDirection(dir);
        if(getRoomOfCharacter(c).attemptMove(c)){
        	c.setBlocked(true);
        	return true; 
        }else{
        	return false;
        }
    }

    //Attempt to create a path to a person within the same room
    public boolean attemptCreatePathToPerson(AIControler aic, String targetPerson){
        Character c = aic.getCharacter();
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
        Character c = aic.getCharacter();
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
        Character c = aic.getCharacter();
        Room r = getRoomOfCharacter(c);
        Debug.printDebugMessage("Attempting to create a MasterPath for " + r.getName(), Debug.Channel.WORLD);
        MasterPath mp = createMasterPathTo(r.getName(), targetRoom);
        if(mp!=null){               //Was a masterPath actually made?
            aic.setMasterPath(mp);  //Send created MasterPath to AI
            return true;        //MasterPath successfully created and sent
        }
        return false;   //Something went wrong and no MasterPath was created

    }

    //Returns a list with the names of all the current characters
    public ArrayList<String> getAllCharacterNames(){
        ArrayList<String> names = new ArrayList<>();
        for(AIControler aic : aiss){
            names.add(aic.getCharacterName());
        }
        return names;
    }

    private AIControler getRelatedControler(Character c){
        for(AIControler aic : aiss){
            if(aic.getCharacterName().equals(c.getName()))
                return aic;
        }
        Debug.printDebugMessage("No related aicontroller could be found", Debug.Channel.WORLD);
        return null;

    }

    private AIControler getRelatedControler(String name){
        //Debug.printDebugMessage();("Name to get related controller for : " + name);
        for(AIControler aic : aiss){
            if(aic.getCharacterName().equals(name))
                return aic;
        }
        Debug.printDebugMessage("No related aicontroller could be found for this name : " + name, Debug.Channel.WORLD);
        return null;

    }

    public Room getRoomOfCharacter(Character c){
        for(Room[] rs :  roomMap){
            for(Room r : rs){
                if(r!=null){
                    if(r.hasCharacter(c))
                        return r;
                }
            }
        }
        Debug.printDebugMessage("Character of AIC doesnt exist in any room... error...", Debug.Channel.WORLD);
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
        //Needs to be correct for pathfinding to work
        targetPosition = targ;
        Debug.printDebugMessage(org.getX() + " "+ org.getY() +" "+ targ.getX() + " "+ targ.getY(),
                                                Debug.Channel.MASTER_PATH);
        Path p = masterPathfinder.findPath(null, org.getX(), org.getY(), targ.getX(), targ.getY());
        MasterPath mp = new MasterPath();
       if(p != null){
    	   for(int i = 1; i<p.getLength(); i++){
            	mp.addStep(roomMap[p.getX(i)][p.getY(i)].getName());
            	Debug.printDebugMessage(" Step " + i + "   is " + roomMap[p.getX(i)][p.getY(i)].getName(),
                                            Debug.Channel.MASTER_PATH );
        	}
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
        /*Debug.printDebugMessage();(new Position(x,y).toString()  +  "       source = " +
                                new Position(pfc.getSourceX(), pfc.getSourceY()).toString() + "    current = "
                        + new Position(masterPathfinder.getCurrentX(), masterPathfinder.getCurrentY()).toString());*/
        if(targetPosition.getX() == x && targetPosition.getY() == y)
            return false;               //Override so pathfinding doesnt interpret target as blocked
        if(roomMap[x][y] != null && roomMap[pfc.getSourceX()][pfc.getSourceY()].hasConnectionTo(roomMap[x][y].getName())){
            //Debug.printDebugMessage();(roomMap[x][y].getName() +  " is free");
            return false;
        }else
            //Debug.printDebugMessage();(new Position(x,y).toString()+ " is blocked");
            return true;
    }

    @Override
    public float getCost(PathFindingContext pathFindingContext, int i, int i1) {
        return 1;
    }
    
    public ConversationModel getPlayerConv(){
    	return playerConv;
    }

    private Character getCharacterRef(String name){
        if(name.equals(player.getName()))
            return player.getCharacter();
        for(AIControler aic : aiss){
            if(aic.getCharacterName().equals(name))
                return aic.getCharacter();
        }
        Debug.printDebugMessage("getCharacterRef() couldnt find any character with that name!", Debug.Channel.WORLD);
        return null;
    }

    public static NPCBrain getControlerBrain(AIControler aic){
        return controlerBrainMap.get(aic);
    }

    public static IPerceiver stringToIPerceiver(String name){
        if(name == current.player.getName())
            return playerBrain;
        else {
            AIControler aic = current.getRelatedControler(name);
            aic.getCharacterName();
            NPCBrain npcbrain = controlerBrainMap.get(aic);
            npcbrain.getName();
            return npcbrain;
        }
    }
    public Player getPlayer(){
    	return player;
    }
    public PlayerBrain getPlayerBrain(){
    	return playerBrain;
    }
    
}
