package deadlybanquet.ai;

/**
 *
 * @author omega
 * This is the brain of an NPC. It contains information about the NPC which the AI needs
 * to operate on frequently in a more easily accessible form.
 * There should be nothing in this file which could not technically be expressed as a list
 * of IThought objects.
 */

import static deadlybanquet.AI.speak;
import static deadlybanquet.ai.BeingPolite.*;
import static deadlybanquet.ai.PAD.placeholderPAD;
import static deadlybanquet.ai.Say.How.*;
import deadlybanquet.model.Time;
import static deadlybanquet.model.World.getTime;
import deadlybanquet.speech.SpeechAct;
import static deadlybanquet.speech.SpeechActFactory.makeSpeechAct;
import java.util.*;
public class Brain implements IMemory{
    //Current emotion - may be changed by events.
    public static double REALCLOSE = 0.5;
    
    private String me;
    private Information memory;
    private PAD emotion = new PAD(0,0,0);
    //Personality. May not change. Emotion object regresses
    //to this in the absence of new stimuli.
    private PAD temperament= new PAD(0,0,0);   
    //Things the NPC wishes were true
    private ArrayList<Desire> desires = new ArrayList<>();
    //The NPC's goals. This list should remain sorted.
    private ArrayList<Desire> goals = new ArrayList<>();
    //This uses plan elements rather than actions in order to facilitate reevaluation of the plan.
    private ArrayList<IThought> plan = new ArrayList<>();
    private ArrayList<Opinion> opinions = new ArrayList<>(); //TODO add this to constructor
    private ArrayList<Whereabouts> whereabouts = new ArrayList<>();
    
    //this constructor replaces any bad values by defaults.
    public Brain(   SortedSet<IThought> information, 
                    PAD emotion, 
                    PAD temperament, 
                    ArrayList<Desire>desires,
                    ArrayList<Desire>goals,
                    ArrayList<IThought>plan,
                    String currentRoom,
                    String name){
        memory = new Information (information);
        if (emotion!=null) this.emotion=emotion;
        if (temperament!=null) this.temperament=temperament;
        if (desires !=null) this.desires = desires;
        if (goals!=null) this.goals = goals;
        if (plan!=null) this.plan = plan;
        me = name;
    }

    public Brain(){}
    
    //This class evaluates the content, changing both opinions and information
    //This method is also responsible for sending answers.
    public void hear(SpeechAct act){
        ArrayList<IThought> content = act.getContent();
        ArrayList<IThought> possibleAnswers = new ArrayList<>();
        String you = act.getSpeaker();
        for (IThought t : content){
            SortedSet<IThought> foundData = new TreeSet<>(); 
            //A say object for responses
            Say c;
            switch(t.getClass().getSimpleName()){
                case "Opinion": possibleAnswers.add(processOpinion((Opinion) t, you));
                                break;
                    
                case "SomebodyElse":SomebodyElse inElse = (SomebodyElse) t;
                                    if (inElse.aboutPerson==me){
                                        //IThought response = whatAboutMe(inElse.what);
                                    }
                                    else {
                                        //TODO: what if person is saying x thinks that you...
                                        //Find your own matching info about this person, if any.
                                        foundData = memory.find(inElse);
                                        //If none is found, make polite response.
                                        if(foundData.isEmpty()){
                                            c= new Say(me, you, inElse, YESNO, null);
                                            possibleAnswers.add(c);
                                            break;
                                        }
                                        possibleAnswers.add(foundData.first());
                                    }
                                    break;
                    
                case "BackStory": BackStory inBack = (BackStory) t;
                                  Say.How a;
                                  if (memory.find(inBack).isEmpty()) {
                                      a =YESNO;
                                      memory.information.add(t);
                                  }
                                  else {
                                      a=AGREE;
                                  }
                                  c = new Say(me, you, inBack, a, null);
                                  possibleAnswers.add(c);
                                  break;
                    
                case "Whereabouts": Whereabouts inWhere = (Whereabouts) t;
                                    //Check if I have an idea about where the person is
                                    for (Whereabouts b:whereabouts){
                                        if (b.character==inWhere.character)
                                                foundData.add(b);
                                    }
                                    //Check if I have an idea that somebody else might know
                                    Whereabouts tofind = new Whereabouts(inWhere.character,"",null,0.0, null);
									if(foundData.isEmpty()) foundData=memory.find(tofind);
                                    //if I have no idea whatsoever about where the person is
                                    if(foundData.isEmpty()){
                                        foundData.add(new Say(me, you,inWhere, YESNO,null));
                                    }
                                    possibleAnswers.add(foundData.first());
                                    break;
                    
                case "BeingPolite": c= new Say(me, you,t,AGREE,null);
                                    possibleAnswers.add(c);
                                    break;
                
                case "Say": Say inSay = (Say) t;
                            if (inSay.when==null){
                                //This means that speaker is performing speech act
                                //by saying it. Can therefore assume that me is recipient
                                //and speaker is current speaker.
                                switch (inSay.type){
                                    case SAY:   //This must obviously be a question
                                                //since if it were information you would 
                                                //just give the info instead of saying I hereby inform you that...
                                                foundData= memory.find(inSay.content);
                                                if (foundData.isEmpty()){
                                                    possibleAnswers.add(inSay.content);
                                                }
                                                possibleAnswers.add(foundData.first());
                                                break;
                             
                                        
                                    case AGREE: if(inSay.content instanceof Say){
                                                    Say h = (Say) inSay.content;
                                                    //Speaker has acceded to a request we made
                                                    if (h.type==REQUEST){
                                                        //content of a request should be either a do or a say object.
                                                        Time time = getTime();
                                                        time.incrementTime(60);
                                                        //TODO maybe add another interface to avoid such code?
                                                        if(h.content instanceof Say){
                                                            Say z = (Say) h.content;
                                                            z.when=time;
                                                            memory.information.add(z);
                                                        }
                                                        if (h.content instanceof Do){
                                                            Do z = (Do) h.content;
                                                            z.when=time;
                                                            memory.information.add(z);
                                                        }
                                                    }
                                                    possibleAnswers.add(THANKS);
                                                }
                                                else {
                                                    acceptUncritically(you, inSay.content);
                                                }
                                                break;
                                        
                                    case DISAGREE:  if(inSay.content instanceof Say){
                                                    Say h = (Say) inSay.content;
                                                    //Speaker has refused to accomodate a request we made
                                                    if (h.type==REQUEST){
                                                        //TODO get angry
                                                    }
                                                    possibleAnswers.add(THANKSANYWAY);
                                                }
                                                else {
                                                    acceptUncritically(you, inSay.content);
                                                }
                                                break;
                                        
                                    case YESNO: foundData = memory.find(inSay.content);
                                                //TODO need to check other relevant registers as well
                                                if (foundData.isEmpty()){
                                                    //Say that this is the case
                                                    possibleAnswers.add(new Say(me, you, inSay.content, DISAGREE, null));
                                                }
                                                else {
                                                    //Say that this is not the case
                                                    possibleAnswers.add(new Say(me, you, inSay.content, AGREE, null));
                                                }
                                                break;
                                        
                                    case REQUEST:   //this one is intimately connected
                                                    //with planning, so leave out until plans are constructed.
                                                    //Therefore, NPC refuses to do anything for anyone right aMomentAgo.
                                                    possibleAnswers.add(new Say(me, you, inSay, DISAGREE, null));
                                                    break;
                                    default: System.out.println(me + "says: Incoming Say object is making my mind boggle.");
                                }
                            }
                            else {
                                //In this case, the person is informing me
                                //that somebody else said something yet another person.
                                //TODO: Look for relevant information,
                                //first in history, then in information
                                possibleAnswers.add(new Say(me, you, inSay, YESNO, null));
                            }
                            break;
                    
                case "Do":  Do inDo = (Do) t;
                            //Check if I saw this happen
                            foundData = memory.find (inDo);
                            if (!foundData.isEmpty()){
                                //I did see this happen
                                c = new Say (me, you, inDo, AGREE, null);
                                possibleAnswers.add(c);
                                break;
                            }
                            
                            //Check if I have heard from somebody else 
                            //that this has happened
                            foundData = memory.find (inDo);
                            if (foundData.isEmpty()){
                                c= new Say(me, you, inDo, YESNO, null);
                                possibleAnswers.add(c);
                                break;
                            }
                            possibleAnswers.add(foundData.first());
                            break;
                    
                case "EmotionThought":  EmotionThought inEmotion = (EmotionThought) t;
                                        SomebodyElse s = new SomebodyElse (inEmotion, you, null, 1.0);
                                        foundData = memory.find(s);
                                        if (foundData.isEmpty()){
                                            //If no previous information is available, return
                                            //I am happy/sad for you.
                                            s.time=getTime();
                                            memory.information.add(s);
                                            PAD opinion = new PAD(inEmotion.pad.getP(), 0, 0);
                                            s.opinion = opinion;
                                            possibleAnswers.add(s);
                                            memory.information.add(s);
                                        }
                                        else {
                                            //If we already have information, return previous info.
                                            SomebodyElse b = (SomebodyElse) foundData.first();
                                            memory.information.remove(b);
                                            s.previous=b;
                                            memory.information.add(s);
                                            possibleAnswers.add(b);
                                        }
                                        break;
                    
                case "Desire":  Desire inDesire = (Desire) t;
                                Desire ownd = null;
                                //Find any goal
                                for (Desire i : goals){
                                    if(inDesire.what.contains(i.what))
                                        ownd=i;
                                }
                                //If this is a goal of yours, say so.
                                if (ownd!=null){
                                    possibleAnswers.add(ownd);
                                    break;
                                }
                                //Find any own desire
                                for (Desire i : desires){
                                    if(inDesire.what.contains(i.what))
                                        ownd=i;
                                }
                                //If I do not have a desire, or if my desire
                                //is positive when opponent's is positive (or the reverse)
                                if (ownd==null || ownd.strength*inDesire.strength>=0){
                                    SomebodyElse offer = new SomebodyElse(inDesire.what, me, placeholderPAD(),1.0);
                                    possibleAnswers.add(new Say(me, you, offer, SAY, null));
                                }
                                else {
                                    possibleAnswers.add(ownd);
                                }
                                break;
                                
                default: System.out.println(me + " says: I didn't understand a word of that.");
            }
            selectResponse(possibleAnswers);
        }
    }
    
    private IThought processOpinion(Opinion inOpinion, String you){
        //find a previous opinion the you held about this subject
        Opinion old = new Opinion(inOpinion.person, null);
        SortedSet<IThought> foundData;
        SomebodyElse previnfo = new SomebodyElse (old, you, null, 0.0);
        foundData = memory.find(previnfo);
        acceptUncritically(you,inOpinion);
        if (foundData.isEmpty()){
            //the person has not previously mentioned anything about this.
            Opinion response = new Opinion (inOpinion.person, null);
            if (inOpinion.person==me){
                
            }
            for (Opinion i : opinions){
                if (i.person==inOpinion.person)
                    response.pad=i.pad;
            }
            return response;
        } else {
            return foundData.first();
        }
    }
    
    
    
    private void acceptUncritically(String person, IThought stateofworld){
        SomebodyElse previnfo = new SomebodyElse(stateofworld,person,null, 1.0);
        previnfo.time=getTime();
        SortedSet<IThought> foundData = memory.find(previnfo);
        if (!foundData.isEmpty()){
            memory.information.remove(foundData.first());
            previnfo.previous= (SomebodyElse) foundData.first();      
        }
        memory.information.add(previnfo);
    }
    
    //selects which statement to respond to in the case that an inOpinion speech act
    //contains several pieces of information
    //responsible for sending the response and updating one's own opinion of the
    //world in accordance with how one believes the statement to change the world/
    private void selectResponse(ArrayList<IThought> possibleResponses){
        //This is the real thing
        //speak(makeSpeechAct(possibleResponses, me));
        makeSpeechAct(possibleResponses, me);
    }
    
    public void observeRoomChange(String person, String origin, String destination){
    }
    
    public void observeInteraction(String who, String with){
        
    }
    
    public void observePickUp(String who, String what){
        
    }
    
    public void observePutDown(String who, String what){
        
    }

    //Character does some thinking, cooling down, executing plan etc.
    public void update(){
        
    }

    //this method creates plans for any goals and puts
    private void plan(){
        
    }
    
    @Override
    public void seePeople(ArrayList<String> people) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
   
}
