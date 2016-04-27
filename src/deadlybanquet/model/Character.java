package deadlybanquet.model;
import deadlybanquet.ai.Opinion;
import deadlybanquet.GameObject;
import deadlybanquet.RenderObject;
import deadlybanquet.Renderable;
import deadlybanquet.Trait;
import deadlybanquet.TraitInfo;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.pathfinding.Mover;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Character implements Renderable, Mover{

    /*
    Counter to set specific ID for each character.
     */
    private static int idCounter =0;
    
//    private boolean blocked;		NOW OBSOLETE

    private int id;
//	private int xPos;
//	private int yPos;
	private Position pos, prevPos;
	private Direction direction;
	private int distance;
	private String room;
	private boolean talking;
	private boolean blocked;
	
	private ActionListener actList;
	
	private List<Trait> traits;
	
	private String name;
	private TraitInfo traitInfo;//???
	
	private Image imageS;
	private Image imageE;
	private Image imageW;
	private Image imageN;
	private Animation aniS,aniN,aniW,aniE;
	private boolean moving = false;

    private Map opinions = new HashMap<Integer, Opinion>();
	
    public Character(ActionListener al, Character c){
    	this.id = c.getId();
    	this.name = c.getName();
    	this.pos = c.getPos();
    	this.direction = c.getDirection();
    	this.talking = false;
    		
    }
	
	public Character(ActionListener al, String name, int xPos, int yPos){
        //set ID
        
        this.id=idCounter;
        //done with ID set
		this.name = name;
		this.pos = new Position(xPos, yPos);
		this.direction = Direction.SOUTH;
		distance = 32;
		
		this.talking = false;
		
		this.actList = al;
		
		try {
			if(id == 0){
				imageS = new Image("res/pictures/lookingdown.png");
				imageN = new Image("res/pictures/lookingup.png");
				imageW = new Image("res/pictures/lookingleft.png");
				imageE = new Image("res/pictures/lookingright.png");
				aniS = new Animation(new SpriteSheet("res/pictures/downanimation.png",32,32), 300);
				aniN = new Animation(new SpriteSheet("res/pictures/upanimation.png",32,32), 300);
				aniW = new Animation(new SpriteSheet("res/pictures/leftanimation.png",32,32), 300);
				aniE = new Animation(new SpriteSheet("res/pictures/rightanimation.png",32,32), 300);
			}else{
				imageS = new Image("res/pictures/npc_"+id+"_down.png");
				imageN = new Image("res/pictures/npc_"+id+"_up.png");
				imageW = new Image("res/pictures/npc_"+id+"_left.png");
				imageE = new Image("res/pictures/npc_"+id+"_right.png");
				aniS = new Animation(new SpriteSheet("res/pictures/npc_"+id+"_downAnimation.png",32,32), 300);
				aniN = new Animation(new SpriteSheet("res/pictures/npc_"+id+"_upAnimation.png",32,32), 300);
				aniW = new Animation(new SpriteSheet("res/pictures/npc_"+id+"_leftAnimation.png",32,32), 300);
				aniE = new Animation(new SpriteSheet("res/pictures/npc_"+id+"_rightAnimation.png",32,32), 300);
			}
		} catch (SlickException e) {
			e.printStackTrace();
		}
		traits = new LinkedList<Trait>();
		idCounter++;
		//this.blocked=false;		OBOLETE
	}

    public void meetNewCharacter(Character person){
        //opinions.put(new Opinion(),person.getId());
        //opinions.put(person.getId(), new Opinion()); //todo commented out by Tom
    }

    /*
    Set the trait option, for the first time.
    Has to be ALL the stuff that is in TraitOption
     */
    public void initTraitInfo(TraitInfo.Sex sex){
        this.traitInfo = new TraitInfo(sex);
    }

    public void draw(Graphics g){

    }

	public String getName(){
		return name;
	}

    //todo this is just being tested.
    public Opinion getOpinion(Character person){
        int pid = person.getId();
        Opinion temp = (Opinion) opinions.get(pid);
        return temp;
    }

    public void setPos(Position p){
        pos.setX(p.getX());
        pos.setY(p.getY());
    }

	public void addTrait(Trait t){
		for(Trait trait: traits){
			if(t.equals(trait)){
				break;
			}	
		}
		traits.add(t);
	}

    public int getId() {
        return id;
    }

	public Position getFacedTilePos(){
		return Position.getAdjacentPositionInDirection(pos, direction);

	}
    /*
    Do NOT use this
     */
	public List<Trait> getTraits(){
		LinkedList<Trait> returnList = new LinkedList<Trait>();
		returnList.addAll(traits); // still a shallow copy. Needs to be fixed.
		return returnList;
	}

    /*
    Check if trait exists
    return true if it dose, and false if not.
     */
    public Boolean traitExist(Trait t){
        if(traits.contains(t)){
            return true;
        }else{
            return false;
        }
    }
	
	public Position getPos() {
		return new Position(this.pos);
	}
	
	public Position getPrevPos(){
		return prevPos;
	}
	
	public Direction getDirection(){
		return direction;
	}
	
	public void setDirection(Direction dir){
		direction = dir;
	}

	/* ---------------OBSOLETE, ACTIONEVENTS ARE NO LONGER USED!-------------
	public void moveE(){
		this.direction = Direction.EAST;
		this.actList.actionPerformed(new ActionEvent(this, 0, "move"));
	}
	
	public void moveW(){
		this.direction = Direction.WEST;
		this.actList.actionPerformed(new ActionEvent(this, 0, "move"));
	}
	
	public void moveN(){
		this.direction = Direction.NORTH;
		this.actList.actionPerformed(new ActionEvent(this, 0, "move"));
	}
	
	public void moveS(){
		this.direction = Direction.SOUTH;
		this.actList.actionPerformed(new ActionEvent(this, 0, "move"));
	}
	----------------------------------------------------------------*/
	
	public void executeMove(){
        Position newPos = getFacedTilePos();
        moving = true;
        setPos(newPos);
        /*      Functionality held in getFacedTilePos renders this obsolete
		switch(this.direction){
			case NORTH:
				pos.decY();
				moving = true;
			case SOUTH:
				pos.incY();
				moving = true;
			case WEST:
				pos.decX();
				moving = true;
			case EAST:
				pos.incX();
				moving = true;
			default:
				break;
		}
		*/
	}

	/*
	Should probably be different methods for NPC and player.
	 */

	/* I want to save this to but it should not be here, but please DONT remove.

	public void receiveSpeechAct(SpeechAct s, Character c){
		this.getOpinion(c).changeLove(s.getLoveChange());
		SpeechAct[] answers = s.getAproprietAnswers(s);
		boolean endConversation = false;
		// Here for player choose an answer, and NPC have to decide as well
		// is there any method in the dialouge state that takes a list of answers??
		// something like:  deadlybanquet.speech.SpeechAct choosenAnswer=chooseAnswer(answers);


		//if(choosenAnswer.isEndConversation()){
		//	endConversation=true;
		//}


		if(!endConversation){
			//sendSpeachAct(choosenAnswer,c);
		}else{
			// Change the state to Normal, the conversation is now ended.
		}

		//Should a NPC wait for answer here??
		//Is the game paused, or do we need a semaphore here to make sure the NPC dosnt just walk away?

	}


	//Makes Character C run his/hers reciveSpeachActFunktion.

	public void sendSpeachAct(SpeechAct s, Character c){

		//Here should there probably be some checks to see if there actually is a conversation and stuff like that

		c.reciveSpeachAct(s,this);
	}*/

	
	/**
	 * returns a RenderObject different image depending on
	 * witch direction the character is facing.
	 */
	@Override
	public RenderObject getRenderObject() {
		switch(this.direction){
			case SOUTH:
				if(moving){
					if(distance == 1){
						moving = false;
						distance = 32;
						return new RenderObject(pos.getX(), pos.getY(), imageS, moving);
					}
					distance--;
					return new RenderObject(pos.getX(), pos.getY(), aniS, moving, 0, distance);
					
				}else{
					return new RenderObject(pos.getX(), pos.getY(), imageS, moving);
				}
			case EAST:
				if(moving){
					if(distance == 1){
						moving = false;
						distance = 32;
						return new RenderObject(pos.getX(), pos.getY(), imageE, moving);
					}
					distance--;
					return new RenderObject(pos.getX(), pos.getY(), aniE, moving, distance, 0);
				}else{
					return new RenderObject(pos.getX(), pos.getY(), imageE, moving);
				}
			case WEST:
				if(moving){
					
					if(distance == 1){
						moving = false;
						distance = 32;
						return new RenderObject(pos.getX(), pos.getY(), imageW, moving);
					}
					distance--;
					return new RenderObject(pos.getX(), pos.getY(), aniW, moving, -1*distance, 0 );
				}else{
					return new RenderObject(pos.getX(), pos.getY(), imageW, moving);
				}
			case NORTH:
				if(moving){
					
					if(distance == 1){
						moving = false;
						distance = 32;
						return new RenderObject(pos.getX(), pos.getY(), imageN, moving);
					}
					distance--;
					return new RenderObject(pos.getX(), pos.getY(), aniN, moving, 0, -1*distance);
				}else{
					return new RenderObject(pos.getX(), pos.getY(), imageN, moving);
				}
			default:
				return new RenderObject(pos.getX(), pos.getY(), imageS, moving);
		}
		//return new RenderObject(pos.getX(), pos.getY(), imageS, moving); //
	}

	public boolean equals(Character c){
		return c.getName().equals(name);
	}

	/*-------------OBSOLETE, BLOCKED STATUS IS IN AIC INSTEAD----------
	public void notifyBlocked() {
		this.blocked=true;
	}
	
	public boolean isBlocked() {
		return this.blocked;
	}
	
	public void unblock(){
		this.blocked = false;
	}
	--------------------------------------------------------------------*/

	/*------------------OBSOLETE, ACTIONEVENTS ARE NO LONGER USED!---------------------
	//Request a path to a door withing the current room which leads to targRoom
	//OBS! This yields NOTHING if no door connects to the targRoom
	public void reqPathToDoor(String targRoom){
		actList.actionPerformed(new ActionEvent(this, EventEnum.REQUEST_PATH_TO_DOOR.ordinal(), targRoom));
	}

	//Requests a masterpath in between rooms
	public void reqMasterPathToRoom(String targRoom){
		actList.actionPerformed(new ActionEvent(this, EventEnum.REQUEST_PATH_TO_ROOM.ordinal(), targRoom));
	}

	//Requests a path to a specific person
	public void reqPathToPerson(String personName){
		actList.actionPerformed(new ActionEvent(this, EventEnum.REQUEST_PATH_TO_PERSON.ordinal(), personName));
	}
	
	public void attemptRoomChange(){
		this.actList.actionPerformed(new ActionEvent(this, EventEnum.CHECK_DOOR.ordinal(), ""));
	}
	
	public void tryTalk(){
		this.actList.actionPerformed(new ActionEvent(this, EventEnum.TALK_TO.ordinal(), ""));
	}

	public void enterDoor(String toRoom, String fromRoom){
		this.actList.actionPerformed(new ChangeRoomEvent(this, fromRoom, toRoom, this.direction));
	}*/

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public Image getDefaultImage(){
		return imageS;
	}

	public boolean isTalking() {
		return talking;
	}

	public void setTalking(boolean talking) {
		this.talking = talking;
	}
	
	public void setBlocked(boolean blocked){
		this.blocked=blocked;
	}
	
	public boolean isBlocked(){
		return blocked;
	}
	public boolean isMoving(){
		return moving;
	}
	public void setName(String playerName){
		name = playerName;
	}
}
