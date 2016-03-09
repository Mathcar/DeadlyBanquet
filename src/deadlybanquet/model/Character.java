package deadlybanquet.model;
import deadlybanquet.ai.Opinion;
import deadlybanquet.GameObject;
import deadlybanquet.RenderObject;
import deadlybanquet.Renderable;
import deadlybanquet.Trait;
import deadlybanquet.TraitInfo;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Character implements Renderable{

    /*
    Counter to set specific ID for each character.
     */
    private static int idCounter =0;

    private int id;
	private int xPos;
	private int yPos;
	private Direction direction;
	
	private List<Trait> traits;
	
	private String name;
	private TraitInfo traitInfo;//???
	
	private Image imageS;
	private Image imageE;
	private Image imageW;
	private Image imageN;

    private Map opinions = new HashMap<Integer, Opinion>();
	
	
	public Character(ActionListener al, String name, int xPos, int yPos){
        //set ID
        idCounter++;
        this.id=idCounter;
        //done with ID set
		this.name = name;
		this.xPos = xPos;
		this.yPos = yPos;
		this.direction = Direction.SOUTH;
		
		try {
			imageS = new Image("res/pictures/lookingdown.png");
			imageN = new Image("res/pictures/lookingup.png");
			imageW = new Image("res/pictures/lookingleft.png");
			imageE = new Image("res/pictures/lookingright.png");

		} catch (SlickException e) {
			e.printStackTrace();
		}

		traits = new LinkedList<Trait>();
	}

    public void meetNewCharacter(Character person){
        //opinions.put(new Opinion(),person.getId());
        opinions.put(person.getId(), new Opinion());
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

    //todo this is just being tested.
    public Opinion getOpinion(Character person){
        int pid = person.getId();
        Opinion temp = (Opinion) opinions.get(pid);
        return temp;
    }

    public void setxPos(int x){
        this.xPos=x;
    }

    public void setyPos(int y){
        this.yPos=y;
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
	
	public int getX() {
		return xPos;
	}

	public int getY() {
		return yPos;
	}
	
	public void moveE(){
		this.xPos++;
	}
	
	public void moveW(){
		this.xPos--;
	}
	
	public void moveN(){
		this.yPos--;
	}
	
	public void moveS(){
		this.yPos++;
	}

	/*
	Should probably be different methods for NPC and player.
	 */

	/* I want to save this to but it should not be here, but please DONT remove.

	public void reciveSpeachAct(SpeechAct s, Character c){
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
				return new RenderObject(xPos,yPos, imageS);
			case EAST:
				return new RenderObject(xPos,yPos, imageE);
			case WEST:
				return new RenderObject(xPos,yPos, imageW);
			case NORTH:
				return new RenderObject(xPos,yPos, imageN);
		}
		return new RenderObject(xPos,yPos, imageS); //
	}

}
