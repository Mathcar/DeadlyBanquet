package deadlybanquet.ai;

import deadlybanquet.model.Debug;
import deadlybanquet.model.World;
/**
 * This class is the memory of the NPC or player. 
 * Contains everything which the NPC and player have in common.
 * @author omega
 */
import deadlybanquet.speech.SpeechAct;
import java.util.*;

import deadlybanquet.speech.SpeechActFactory;
import deadlybanquet.speech.TextPropertyEnum;
import jdk.nashorn.internal.objects.annotations.Where;
import org.newdawn.slick.Image;
public class PlayerBrain implements IPerceiver {
    private Memory information;
    private World world;
    private String name;
    private ArrayList<Whereabouts> whereabouts;
    private ArrayList<Do> dos;



    //TODO CONSTRUCTOR ONLY TEMPORARILY ADDED, NOT SURE IF EMPTY MEMORY IS SUPPPOSED TO BE MADE
    public PlayerBrain(String name){
        information = new Memory(new SortedList());
        this.name = name;
        whereabouts = new ArrayList<>();
    }
    public PlayerBrain(SortedList s, World world){
        information = new Memory(s);
        this.world = world;
        whereabouts = new ArrayList<>();

    }
    
    //this method accepts a speech act and adds the information to its memory.
    //This method should not evaluate memory as that is the (human) player's or brains job.
    @Override
    public SpeechAct hear(SpeechAct act){
        SpeechAct answer = null;
        IThought answerContent = null;
    	for(IThought i: act.getContent()){
            switch(i.getClass().getSimpleName()){
                case "Whereabouts" :
                    IThought intent = processWhereabouts((Whereabouts)i);
                    if(intent != null)
                        answerContent = intent;
                    break;
                case "Do" :
                    answerContent = processDo((Do)i);
                    break;
            }


    		Opinion compOpinion = new Opinion(act.getSpeaker(), new PAD(0,0,0));
    		SortedList tempSet = information.find(compOpinion);
    		
    		if(tempSet.size()==0){//if the characters hven't met before
    			information.add(compOpinion);
    			tempSet.add(compOpinion);
    		}
    		Opinion speeker = (Opinion)tempSet.first(); //find should only return Opinions
    		
    		
    		this.information.add(new SomebodyElse(i, act.getSpeaker(), speeker.getPAD(), 1));
    	}

        if(answerContent != null) {
            Debug.printDebugMessage(answerContent.toString() + " is the ithought answer", Debug.Channel.PLAYER);
            return SpeechActFactory.convertIThoughtToSpeechAct(answerContent, TextPropertyEnum.NEUTRAL,
                    act.getListener(), act.getSpeaker());
        }
        //TODO Return something sensible
        return null;
    }

    private IThought processWhereabouts(Whereabouts wa ){
        Debug.printDebugMessage("processWhereabouts in playerBrain is attempted!", Debug.Channel.PLAYER);
        for(Whereabouts w : whereabouts){
            if(w.getCharacter().equals(wa.getCharacter())){
                Debug.printDebugMessage("Corresponding memory found! " + w.toString(), Debug.Channel.PLAYER);
                return w;
            }
        }
        return null;
    }

    private IThought processDo(Do doQ){
        Debug.printDebugMessage("processDo in playerBrain not done yet!", Debug.Channel.PLAYER);
        /*
        for(Do d : dos){
            if(d.isQuestion()){

            }
        }*/
        return null;

    }

    public void plantWhereabout(Whereabouts w){
        whereabouts.add(w);
    }


    
    //Called on every person in origin and destination rooms on room change.
    @Override
    public void observeRoomChange(String character, String origin, String destination){
        //how does Wherabouts work?
    }
    
    //Called on every person in the room when somebody strikes up a conversation with somebody
    //else or examines (but does not pick up) an object in the room. (Other than the person
    //with whom the person is talking, obviously)
    @Override
    public void observeInteraction(String who, String with){
        //witch IThought should be generated?
    }

    @Override
    public TextPropertyEnum chooseProperty(String person) {
        return TextPropertyEnum.PROPER;
    }

    //Called on every person in the room (apart from who) when somebody picks up an object.
    @Override
    public void observePickUp(String who, String what){
        //It is not possible to pick up things
    }
    
    //Called on every person in the room (apart from who) when somebody puts down an object.
    @Override
    public void observePutDown(String who, String what){
    	//It is not possible to put down things
    }
    
    //called on entering a room
    @Override
    public void seePeople (ArrayList<String> people){
        
    }

    @Override
    public SpeechAct getIntendedPhrase(){
        System.err.println("getIntendedPhrase should not be called on the player!");
        return null;
    }

	@Override
	public String getName() {
		return name;
	}
    
    @Override
    public Memory getMemory(){
        return this.information;
    }
	@Override
	public SpeechAct selectPhrase(ArrayList<SpeechAct> acts) {
		// TODO Auto-generated method stub
		return null;
	}
	public void setName(String name){
		this.name = name;
	}

    @Override
    public ArrayList<Whereabouts> getWhereabouts() {
        throw new UnsupportedOperationException("No whereabouts in player brain. Observation functions not implemented."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Opinion> getOpinions() {
        throw new UnsupportedOperationException("No Opinions in player brain."); //To change body of generated methods, choose Tools | Templates.
    }
    public boolean isPlayer(){return true;}
}
