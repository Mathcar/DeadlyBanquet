package deadlybanquet.model;


import deadlybanquet.RenderObject;
import deadlybanquet.RenderSet;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Hampus on 2016-03-04.
 */
public class Room implements TileBasedMap {
    private String name;
    private TiledMap map;
    private AStarPathFinder pathFinder;
    private ArrayList<Character> characters;
    private ArrayList<Door> doors;
    public static final int DOOR_LAYER = 3;
    

    public Room(String tilemapURL, String name){
        try {
            map = new TiledMap(tilemapURL);
        }catch(SlickException se){
            se.printStackTrace();
        }
        this.name = name;
        pathFinder = new AStarPathFinder(this, 50, false);
        characters = new ArrayList<Character>();
        createDoors();

    }

    public void update(GameContainer container, StateBasedGame s, int deltaTime){
        //Maybe something wants to update in each specific room?
        //Like possibly gameobject/pickups or whatever if taht is added
        // Will leave this like this for now, it is called for each room in world!

    }

    public void createDoors(){
        doors = new ArrayList<>();
        for(int x = 0; x<getWidthInTiles(); x++){
            for(int y = 0; y<getHeightInTiles(); y++){
                int tileID = map.getTileId(x,y, DOOR_LAYER);

                if(tileID != 0){
                    //A door was found, create door object
                    Door tempDoor = new Door(new Position(x,y));
                    doors.add(tempDoor);
                }
            }
        }
    }
    //Creates the doors connections to the correct rooms.
    //Presumes that doors have a valid connection!
    public void assignDoorConnections(String northRoom, String southRoom,
                                      String eastRoom, String westRoom){
        //Create the connection for every initiated door
        for(Door d : doors){
            if(d.getX() == 0){
                //Door is on the left wall, create connection to west neighbour
                d.createConnection(name, westRoom, Direction.WEST);
            } else if(d.getY() == 1){
                //Door is on the top wall, create connection to north neighbour
                d.createConnection(name, northRoom, Direction.NORTH);
            }else if(d.getX() == getWidthInTiles()-1){
                //Door is on the right wall, create connection to east neighbour
                d.createConnection(name, eastRoom, Direction.EAST);
            }else{
                //Door is on the bottom wall, create connection to south neighbour
                d.createConnection(name, southRoom, Direction.SOUTH);
            }
        }
        //----------------------------------Debug-----------------------------
        String debugMsg = "In room : " + getName() + " these doors have been made";
        for(Door d : doors){
            debugMsg = debugMsg + "\n Door at " + d.getPos().toString()+ " has: ";
            debugMsg = debugMsg + "\n Origin Room :  " + d.getOriginRoom();
            debugMsg = debugMsg + "\n Destination Room : " + d.getDestinationRoom();
            debugMsg = debugMsg + "\n With the direction : " + d.getDirection();
        }
        Debug.printDebugMessage(debugMsg, Debug.Channel.WORLD);
        //--------------------------------------------------------------------
    }

    public Path createPathToDoor(Character c, String target){
    	Position temp = new Position(0,0);
        for(Door d : doors){
            if(d.getDestinationRoom().equals(target)){
            	temp= d.getPos();
            	switch(d.getDirection()){
            	case EAST:            		
            		temp.x = temp.getX()-1;
            		return getPath(c, c.getPos(), temp);	
            	case WEST:
            		temp.x = temp.getX()+1;
            		return getPath(c, c.getPos(), temp);
            	case NORTH:
            		temp.y = temp.getY()+1;
            		return getPath(c, c.getPos(), temp);
            	case SOUTH: 
            		temp.y = temp.getY()-1;
            		return getPath(c, c.getPos(), temp);
            	default:
            		return getPath(c, c.getPos(), d.getPos());
            	}
               
            }
        }
        //No such door was found, or no path to it could be found
        return null;
    }

    //Checks if the room has a door with connection to a specified room
    public boolean hasConnectionTo(String room){
        Debug.printDebugMessage("Checking connection from " + this.name + " to " + room, Debug.Channel.WORLD);
        for(Door d : doors){
            Debug.printDebugMessage("Destination room of door = " + d.getDestinationRoom(), Debug.Channel.WORLD);
            if(d.getDestinationRoom().equals(room)) {
                Debug.printDebugMessage("A door was found with connection to that room", Debug.Channel.WORLD);
                return true;
            }
        }
        Debug.printDebugMessage("No door found with that connection", Debug.Channel.WORLD);
        return false;
    }

    public String getName(){
        return name;
    }
    
    public Direction getAdjacentDoorDirection(Position pos){
    	for(Door d : doors){
			if(Position.getAdjacentPositionInDirection(pos, Direction.EAST).equals(d.getPos())){
				return Direction.EAST;
			}else if(Position.getAdjacentPositionInDirection(pos, Direction.WEST).equals(d.getPos())){
				return Direction.WEST;
			}else if(Position.getAdjacentPositionInDirection(pos, Direction.SOUTH).equals(d.getPos())){
				return Direction.SOUTH;
			}else if(Position.getAdjacentPositionInDirection(pos, Direction.NORTH).equals(d.getPos())){
				return Direction.NORTH;
			}
    	}
    	return null;
    }

    public ArrayList<Character> getCharactersInRoom(){
        return characters;
    }
    
    public boolean hasCharacter(Character searchChar){
    	for(Character c: characters){
    		if(c.equals(searchChar)) return true; //.equals instead
    	}
    	return false;
    }

    public boolean hasCharacter(String searchName){
        for(Character c: characters){
            if(c.getName().equals(searchName)) return true; //.equals instead
        }
        return false;
    }


    private boolean isBlocked(int x, int y){
        for(Character c : characters){
            if(c.getPos().getX() == x && c.getPos().getY() == y){
                Debug.printDebugMessage("Character is blocking tile " + c.getName(), Debug.Channel.PATHFINDING);
                return true;                    //A Characters is occupying this tile
            }
        }
        for(int i = map.getLayerCount()-1; i>=0;i--){
            if(i!=0) {
                if (map.getTileId(x, y, i) != 0) {
                    Debug.printDebugMessage("tile at " + x + ", " + y +" was blocked on layer " +
                                        i, Debug.Channel.PATHFINDING);
                    return true;       //Tile has something static in the blocked layers
                }
            }
        }
        return false;       //Tile is unblocked;
    }

    public RenderSet getRenderSet(){
        ArrayList<RenderObject> ros = new ArrayList<>();
        for(Character c : characters){
            ros.add(c.getRenderObject());
        }
        return new RenderSet(map, ros);

    }

    public Path createPathToPerson(Character mover, String targetName){
        Position target = new Position(0,0);
        for(Character c : characters){
            if(c.getName().equals(targetName)){
                target = c.getPos();
                if(!isBlocked(target.getX(), target.getY()+1)){
                	target.setY(target.getY()+1);
                }else if(!isBlocked(target.getX()-1, target.getY())){
                	target.setX(target.getX()-1);	
                }else if(!isBlocked(target.getX(), target.getY()-1)){
                	target.setY(target.getY()-1);	
                }else if(!isBlocked(target.getX()+1, target.getY())){
                	target.setX(target.getX()+1);
                }
            }
        }
        return getPath(mover,mover.getPos(), target);
    }

    public Path getPath(Mover mover, Position origin, Position target){
        return pathFinder.findPath(mover, origin.getX(), origin.getY(),
                                target.getX(), target.getY());

    }

    //Answers: "Is this positions x and y within the limits of the room?"
    public boolean isInBoundaries(Position pos){
        return pos.getY()<getHeightInTiles() && pos.getX()<getWidthInTiles() &&
                pos.getX() >=0 && pos.getY() >=0;
    }
    public Door getFacedDoor(Position p){
    	for(Door d : doors){
    		if(d.getX() == p.getX() && d.getY() == p.getY()){
    			return d;
    		}
    	} 
    	return null;
    }

    //Attempt to move passed character in their current facing.
    //Returns TRUE on succesfull move, otherwise FALSE.
    public boolean attemptMove(Character c){
        Position newPos = c.getFacedTilePos();
        if(!isInBoundaries(newPos) || isBlocked(newPos.getX(), newPos.getY())){
            //tile is blocked, send notification to related ai/character?
            return false;
        }else{
            c.executeMove(); //character can move
            return true;
        }
    }
    
    public boolean entranceIsBlocked(Direction origin){
        for(Door d : doors) {
            if(d.getDirection()==Direction.getOppositeDirection(origin)){
                Position pos = Position.getAdjacentPositionInDirection(d.getPos(),origin);
                Debug.printDebugMessage(pos.toString(), Debug.Channel.WORLD);
                return isBlocked(pos.getX(), pos.getY());
            }
        }
        return true;
    }


    ///Debug function, used to print out a list of all layers which are occupied on a tile
    public void debugTileOnPos(Position pos){
        Debug.printDebugMessage("Tile at " + pos.toString() + "  has something on layers: ", Debug.Channel.WORLD);
        for(int i = 0; i<map.getLayerCount();i++){
            if(map.getTileId(pos.getX(), pos.getY(), i)!=0)
                Debug.printDebugMessage( i + ",          ", Debug.Channel.WORLD);
        }
    }

    @Override
    public int getWidthInTiles() {
        return map.getWidth();
    }

    @Override
    public int getHeightInTiles() {
        return map.getHeight();
    }

    @Override
    public void pathFinderVisited(int i, int i1) {
    }

    @Override
    //Return a boolean as to whether a tile is blocked or not, x and y coordinates should be in tile-format
    //Collision detection needs to be added!!
    public boolean blocked(PathFindingContext pfc, int x, int y) {
        /*Debug.printDebugMessage(new Position(x,y).toString()  +  "       source = " +
                new Position(pfc.getSourceX(), pfc.getSourceY()).toString() + "    current = "
                + new Position(pathFinder.getCurrentX(), pathFinder.getCurrentY()).toString());*/
        return isBlocked(x,y);
    }

    @Override
    public float getCost(PathFindingContext pathFindingContext, int i, int i1) {
        return 1;
    }
    
    public void addCharacter(Character character){
    	this.characters.add(character);
    }

    public void removeCharacter(Character character){
        characters.remove(character);
    }

    //Character to enter room and the direction in which he is entering the room
    public void addCharacterToRoom(Character character, Direction dir){
        for(Door d : doors){
            if(d.getDirection() == Direction.getOppositeDirection(dir)){
                //Set position of character to just inside the door
                character.setPos(Position.getAdjacentPositionInDirection(d.getPos(),dir));
                addCharacter(character);
            }
        }
    }

    public boolean isCharacterOn(Position p){
    	for(Character c: characters){
    		if(c.getPos().equals(p)){
    			return true;
    		}
    			
    	}
		return false;
    }

    public boolean equals(Room r){
        return r.getName().equals(name);
    }
    /*
     * these should be in the same method chain to avoid multiple unececery searches in lists.
     * 
     */
    public Character getCharacterOnPos(Position p){
    	for(Character c: characters){
    		if(c.getPos().equals(p)){
    			return c;
    		}
    			
    	}
		return null;
    }
    
    public ArrayList<Character> getAllCharacters(){
    	return this.characters;
    }
    
    public Collection<? extends Door> getAllDoors() {
		return this.doors;
	}

}
