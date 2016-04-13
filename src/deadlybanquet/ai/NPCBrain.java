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

import deadlybanquet.Talkable;
import deadlybanquet.ai.Say.How;
import deadlybanquet.model.Time;
import static deadlybanquet.model.World.getTime;
import deadlybanquet.speech.SpeechAct;
import static deadlybanquet.speech.SpeechActFactory.makeSpeechAct;
import java.util.*;

public class NPCBrain implements IPerceiver, Talkable {
    //Current emotion - may be changed by events.
    public static double REALCLOSE = 0.5;
    public static double NORMALMODIFIER = 0.3;
    
    private String me;
    private Memory memory;
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
    //Program should guarantee that anybody who has been met or heard about is in
    //this list of opinions.
    //TODO this should include an opinion about oneself
    private ArrayList<Opinion> opinions = new ArrayList<>(); //TODO add this to constructor
    private ArrayList<Whereabouts> whereabouts = new ArrayList<>();
    
    public ArrayList<IThought> debugInfo;
    
    //this constructor replaces any bad values by defaults.
    public NPCBrain(   SortedList information, 
                    PAD emotion, 
                    PAD temperament, 
                    ArrayList<Desire>desires,
                    ArrayList<Desire>goals,
                    ArrayList<IThought>plan,
                    String currentRoom,
                    String name){
        memory = new Memory (information);
        if (emotion!=null) this.emotion=emotion;
        if (temperament!=null) this.temperament=temperament;
        if (desires !=null) this.desires = desires;
        if (goals!=null) this.goals = goals;
        if (plan!=null) this.plan = plan;
        me = name;
    }

    public Opinion getOpinionAbout(String who){
        Opinion result=null;
        for (Opinion i: opinions){
            if (i.person==who)
                result=i;
        }
        return result;
    }
    
    //If this does not find an opinion about person,
    //creates a neutral one.
    public void makeEmptyOpinion(String about){
        PAD pad = null;
        for (Opinion i:opinions){
            if(i.person==about)
                pad=i.getPAD();
        }
        if (pad==null)
            opinions.add(new Opinion(about, new PAD(0,0,0)));
    }
    
    //This class evaluates the content, changing both opinions and information
    //This method is also responsible for sending answers.
    //THIS METHOD AND ALL PRIVATE METHODS CALLED BY THIS
    //ARE WRITTEN AND MAINTAINED BY KARIN.
    //DO NOT MAKE CHANGES WITHOUT EXPRESS PERMISSION.
    public void hear(SpeechAct act){
        ArrayList<IThought> content = act.getContent();
        ArrayList<IThought> possibleAnswers = new ArrayList<>();
        String you = act.getSpeaker();
        makeEmptyOpinion(you);
        for (IThought t : content){
            acceptUncritically(you, t);
            SortedList foundData = new SortedList(); 
            switch(t.getClass().getSimpleName()){

                case "Opinion":
                	processOpinion((Opinion) t, you, possibleAnswers);
                    break;
                    
                case "SomebodyElse":
                    //TODO
                	processSomebodyElse((SomebodyElse)t,you, possibleAnswers);
                    break;
                    
                case "BackStory": 
                	processBackStory((BackStory)t, you, possibleAnswers);
                	break;
                    
                case "Whereabouts":
                	processWhereabouts((Whereabouts)t, you, possibleAnswers);
                    break;
                    
                case "BeingPolite": 
                	processBeingPolite((BeingPolite)t, you, possibleAnswers);
                    break;
                
                case "Say": 
                    //TODO
                	processSay((Say)t, you, foundData, possibleAnswers);
                    break;
                    
                case "Do":  
                        processDo((Do)t, you, possibleAnswers);
                    break;
                    
                case "EmotionThought":  
                        processEmotionThought((EmotionThought) t, you, possibleAnswers);
                    break;
                    
                case "Desire":  
                        processDesire((Desire) t, you, possibleAnswers);
                    break;
                                
                default: System.out.println(me + " says: I didn't understand a word of that.");
            }
            selectResponse(possibleAnswers);
        }
    }
    
    private void processOpinion(Opinion inOpinion, String you, ArrayList<IThought> ans){
        //Now we have heard of this person
        if(getOpinionAbout(inOpinion.person)==null){
            opinions.add(new Opinion (inOpinion.person, new PAD(0,0,0)));
        }
        //I.e if this is the question "What do you think about X?"
        if (inOpinion.pad.isPlaceholder()){
            PAD ansPAD=null;
            for (Opinion o : opinions){
                if (o.person==inOpinion.person)
                    ansPAD = o.getPAD();
            }
            ans.add(new Opinion(inOpinion.person, ansPAD));
            return;
        }
        //find a previous opinion the you held about this subject
        Opinion old = new Opinion(inOpinion.person, null);
        SortedList foundData;
        SomebodyElse previnfo = new SomebodyElse (old, you, null, 0.0);
        foundData = memory.find(previnfo);
        acceptUncritically(you,inOpinion);
        PAD inPad=inOpinion.getPAD();
        if (inOpinion.person==me){
           opinionAboutMe(inOpinion.getPAD(), you, 1.0);
           Opinion opyou = getOpinionAbout(you);
           //If opinion is negative and you are dependent on that person and what they say makes
           //you unhappy, then say so.
           if (inPad.getP()<0&&
                   opyou.pad.getP()>0&&
                   opyou.pad.getA()>0&&
                   opyou.pad.getD()<0&&
                   emotion.getP()<0)
                ans.add(new EmotionThought(EmotionThought.et.EMOTION, 
                                       new PAD(emotion.getP(), emotion.getA(), emotion.getD())));
           else ans.add(THANKS);
           return;
        }
        if (foundData.isEmpty()){
            Opinion mine = getOpinionAbout(inOpinion.person);
            if(mine.pad.distanceTo(inPad)<REALCLOSE)
                ans.add(new Say(me, you, mine, How.AGREE));
            else ans.add(new Say(me, you, mine, How.DISAGREE));
        } else {
            ans.add(foundData.first());
        }
    }
    
    //Completely unscientific and mostly random reaction to somebody having an opinion about you.
    private void opinionAboutMe(PAD opinion, String you, double howimportant){
        double d= opinion.getD();
        double p = opinion.getP();
        if (d<0){
            //If speaker is feeling not dominant towards you, you feel more dominant.
            getOpinionAbout(me).pad.translateD(howimportant*NORMALMODIFIER*-d);
            getOpinionAbout(you).pad.translateD(howimportant*NORMALMODIFIER*-d);
            emotion.translateD(howimportant*NORMALMODIFIER*-d);
        }
        else{
            //+P+D signifies liking or love, so dominance rises.
            //-P+D means dislike or hostility, so dominance goes down.
            getOpinionAbout(me).pad.translateD(howimportant*NORMALMODIFIER*p);
            getOpinionAbout(you).pad.translateD(howimportant*NORMALMODIFIER*p);
            emotion.translateD(howimportant*NORMALMODIFIER*p);
        }
    }
    
    private void processSomebodyElse(SomebodyElse inElse, String you, ArrayList<IThought> possibleAnswers){
        IThought currentLevel = inElse;
        SortedList foundData;
        while (currentLevel.getClass()==inElse.getClass()){
            SomebodyElse current = (SomebodyElse) currentLevel;
            if(current.aboutPerson.equals(me) || current.aboutPerson==you){
                foundData = memory.find(currentLevel);
                if(foundData.isEmpty()){
                    Say c;
                    if (current.aboutPerson.equals(me)){
                        c = new Say(me, you, currentLevel, DISAGREE, null);
                    }
                    else {
                        c= new Say(me, you, currentLevel, YESNO, null);
                    }
                    possibleAnswers.add(c);
                    return;
                }
                SomebodyElse c = (SomebodyElse) foundData.first();
                c.opinion = inElse.opinion;
                possibleAnswers.add(c);
                return;
            }
            currentLevel = current.what;           
        }
        //At this point, we have arrived at the lowest level, which contains the actual 
        //content
        foundData = memory.find(currentLevel);
        //If none is found, make polite response.
        if(foundData.isEmpty()){
            Say c= new Say(me, you, inElse, YESNO, null);
            possibleAnswers.add(c);
            return;
        }
        possibleAnswers.add(foundData.first());
    }

    private void processBackStory(BackStory inBack, String speaker, ArrayList<IThought> possibleAnswers){
        Say.How a;
        if (memory.find(inBack).isEmpty()) {
            a =YESNO;
            memory.information.add(inBack);
        }
        else {
            a=AGREE;
        }
        Say c = new Say(me, speaker, inBack, a);
        possibleAnswers.add(c);
    }
    
    private void processWhereabouts(Whereabouts inWhere, String speaker, ArrayList<IThought> possibleAnswers){
        //Check if I have an idea about where the person is
        SortedList foundData = new SortedList();
        for (Whereabouts b:whereabouts){
            if (b.getCharacter()==inWhere.getCharacter())
                    foundData.add(b);
        }
        //Check if I have an idea that somebody else might know
        Whereabouts tofind = new Whereabouts(inWhere.getCharacter(), "",null, 0.0, null);
		if(foundData.isEmpty()) foundData=memory.find(tofind);
        //if I have no idea whatsoever about where the person is
        if(foundData.isEmpty()){
            //if this is a question
            if (inWhere.isQuestion()){
                inWhere.setPlaceHolderToNull();
                foundData.add(inWhere);
            }
            else
                foundData.add(new Say(me, speaker, inWhere, YESNO));
        }
        possibleAnswers.add(foundData.first());
    }
    
    private void processBeingPolite(BeingPolite t, String speaker, ArrayList<IThought> possibleAnswers){
    	Say c= new Say(me, speaker, t, AGREE);
        if(t!=THANKSANYWAY)
            possibleAnswers.add(c);
    }
    
    private void processSay(Say inSay, String speaker, SortedList foundData, ArrayList<IThought> possibleAnswers){
        if (inSay.when==null){
            //This means that speaker is performing speech act
            //by saying it. Can therefore assume that me is recipient
            //and speaker is current speaker.
            switch (inSay.type){
                    
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
                                acceptUncritically(speaker, inSay.content);
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
                                acceptUncritically(speaker, inSay.content);
                            }
                            break;
                    
                case YESNO: foundData = memory.find(inSay.content);
                            //TODO need to check other relevant registers as well
                            if (foundData.isEmpty()){
                                //Say that this is the case
                                possibleAnswers.add(new Say(me, speaker, inSay.content, DISAGREE, null));
                            }
                            else {
                                //Say that this is not the case
                                possibleAnswers.add(new Say(me, speaker, inSay.content, AGREE, null));
                            }
                            break;
                    
                case REQUEST:   //this one is intimately connected
                                //with planning, so leave out until plans are constructed.
                                //Therefore, NPC refuses to do anything for anyone right now.
                                possibleAnswers.add(new Say(me, speaker, inSay, DISAGREE, null));
                                break;
                default: System.out.println(me + "says: Incoming Say object is making my mind boggle.");
            }
        }
        else {
            //In this case, the person is informing me
            //that somebody else said something yet another person.
            //TODO: Look for relevant information,
            //first in history, then in information
            possibleAnswers.add(new Say(me, speaker, inSay, YESNO, null));
        }
    }
    
    private void processDo(Do inDo, String you, ArrayList<IThought> possibleAnswers){
        SortedList foundData = new SortedList();
        foundData = memory.find (inDo);
        Say c;
        if (!foundData.isEmpty()){
            c = new Say (me, you, foundData.first(), AGREE);
            possibleAnswers.add(c);
            return;
        }
        c = new Say(me, you, inDo, YESNO);
        possibleAnswers.add(c);
    }
    
    private void processEmotionThought(EmotionThought inEmotion, String you, ArrayList<IThought> possibleAnswers){
        SomebodyElse s = new SomebodyElse (inEmotion, you, null, 1.0);
        SortedList foundData = memory.find(s);
        if (foundData.isEmpty()){
            //If no previous information is available, return
            //I am happy/sad for you.
            s.time=getTime();
            memory.information.add(s);
            PAD opinion = new PAD(inEmotion.pad.getP(), 0, 0);
            s.opinion = opinion;
            possibleAnswers.add(s);
        }
        else {
            //If we already have information, return previous info.
            SomebodyElse b = (SomebodyElse) foundData.first();
            memory.information.remove(b);
            s.previous=b;
            memory.information.add(s);
            possibleAnswers.add(b);
        }
    }
    
    private void processDesire(Desire inDesire, String you, ArrayList<IThought>possibleAnswers){
        Desire ownd = null;
        //Find any goal
        for (Desire i : goals){
            if(inDesire.what.contains(i.what))
                ownd=i;
        }
        //If this is a goal of yours, say so.
        if (ownd!=null){
            possibleAnswers.add(ownd);
            return;
        }
        //Find any own desire
        for (Desire i : desires){
            if(inDesire.what.contains(i.what))
                ownd=i;
        }
        //If I do not have a desire, or if my desire
        //is positive when opponent's is positive (or the reverse),
        //that is, I want the opposite of what you want
        if (ownd==null || ownd.strength*inDesire.strength>=0){
            SomebodyElse offer = new SomebodyElse(inDesire.what, me, placeholderPAD(),1.0);
            possibleAnswers.add(offer);
        }
        else {
            possibleAnswers.add(ownd);
        }
    }
    
    public void plantFalseMemory(IThought i){
        memory.add(i);
    }
    
    public void plantFalseOpinion (Opinion o){
        opinions.add(o);
    }
    
    
    private void acceptUncritically(String person, IThought stateofworld){
        SomebodyElse previnfo = new SomebodyElse(stateofworld,person,null, 1.0);
        previnfo.time=getTime();
        SortedList foundData = memory.find(previnfo);
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
        debugInfo=possibleResponses;
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

    @Override
    public String getName(){
        return this.me;
    }
    
    //@Override
    public Memory getMemory(){
        return memory;
    }
    
   
}
