package deadlybanquet.model;
import deadlybanquet.ai.Opinion;
import deadlybanquet.GameObject;
import deadlybanquet.SpeechAct;
import deadlybanquet.Trait;
import deadlybanquet.TraitInfo;
import org.newdawn.slick.Graphics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Character implements GameObject {

    /*
    Counter to set specific ID for each character.
     */
    private static int idCounter =0;

    private int id;
	private int xPos;
	private int yPos;
	
	private List<Trait> traits;
	
	private String name;
	private TraitInfo traitInfo;

    private Map opinions = new HashMap<Integer, Opinion>();
	
	private TravaresableState state = TravaresableState.SOLID;
	
	public Character(String name){
        //set ID
        idCounter++;
        this.id=idCounter;
        //done with ID set
		this.name = name;
		this.xPos = 0;
		this.yPos = 0;

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
	
	@Override
	public int getX() {
		return xPos;
	}

	@Override
	public int getY() {
		return yPos;
	}

	@Override
	public void uppdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSolid() {
		if(state==TravaresableState.SOLID){
			return true;
		}
		return false;
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
		// something like:  deadlybanquet.SpeechAct choosenAnswer=chooseAnswer(answers);


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

}
