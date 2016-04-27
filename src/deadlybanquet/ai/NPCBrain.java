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
import static deadlybanquet.ai.Desire.dg.GOAL;
import static deadlybanquet.ai.Do.What.*;
import deadlybanquet.ai.Say.How;
import deadlybanquet.model.TimeStamp;
import static deadlybanquet.model.World.getTimeStamp;
import deadlybanquet.speech.SpeechAct;
import static deadlybanquet.speech.SpeechActFactory.convertIThoughtToSpeechAct;
import deadlybanquet.speech.TextPropertyEnum;
import static deadlybanquet.speech.TextPropertyEnum.NEUTRAL;
import deadlybanquet.model.Debug;

import java.util.*;

public class NPCBrain implements IPerceiver, Talkable {
    
    //--------------------------------------------------------------------------
    //SECTION: DATA & CONSTRUCTORS
    //--------------------------------------------------------------------------
    public static double REALCLOSE = 0.5;
    public static double NORMALMODIFIER = 0.3;
    
    private String me;
    private Memory memory;
    private AIControler aic;
    //Current emotion - may be changed by events.
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
    private String here;
    
    
    //this constructor replaces any bad values by defaults.
    public NPCBrain(   SortedList information, 
                    PAD emotion, 
                    PAD temperament, 
                    ArrayList<Desire>desires,
                    ArrayList<Desire>goals,
                    ArrayList<IThought>plan,
                    String currentRoom,
                    String name,
                    AIControler aic){
        memory = new Memory (information);
        if (emotion!=null) this.emotion=emotion;
        if (temperament!=null) this.temperament=temperament;
        if (desires !=null) this.desires = desires;
        if (goals!=null) this.goals = goals;
        if (plan!=null) this.plan = plan;
        here=currentRoom;
        me = name;
        this.aic= aic;
    }

    //--------------------------------------------------------------------------
    //SECTION: HEAR FUNCTION WITH SUBFUNCTIONS
    //This section is written and maintained by Karin. Please
    //do not make changes to this section without express permission,
    //so as to avoid merge conflicts and wasted time trying to find things.
    //--------------------------------------------------------------------------
    //This method evaluates the content, changing both opinions and information
    //This method is also responsible for sending answers.
    public SpeechAct hear(SpeechAct act){
        ArrayList<IThought> content = act.getContent();
        ArrayList<IThought> possibleAnswers = new ArrayList<>();
        String you = act.getSpeaker();
        makeEmptyOpinion(you);
        for (IThought t : content){
            switch(t.getClass().getSimpleName()){
                case "Opinion":
                	processOpinion((Opinion) t, you, possibleAnswers);
                    break;
                    
                case "SomebodyElse":
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
                	processSay((Say)t, you, possibleAnswers);
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
                                
                default: Debug.printDebugMessage(me + " says: I didn't understand a word of that.", Debug.Channel.BRAIN);
            }
        }
        return selectResponse(possibleAnswers,you);
    }
    
    private void processOpinion(Opinion inOpinion, String you, ArrayList<IThought> ans){
        //Now we have heard of this person
        makeEmptyOpinion(inOpinion.getPerson());
        //I.e if this is the question "What do you think about X?"
        if (inOpinion.getPAD().isPlaceholder()){
            PAD ansPAD=null;
            for (Opinion o : opinions){
                if (o.getPerson()==inOpinion.getPerson())
                    ansPAD = o.getPAD();
            }
            ans.add(new Opinion(inOpinion.getPerson(), ansPAD));
            return;
        }
        //find a previous opinion the you held about this subject
        Opinion old = new Opinion(inOpinion.getPerson(), null);
        SortedList foundData;
        SomebodyElse previnfo = new SomebodyElse (old, you, null, 0.0);
        foundData = memory.find(previnfo);
        PAD inPad=inOpinion.getPAD();
        if (inOpinion.getPerson()==me){
           opinionAboutMe(inOpinion.getPAD(), you, 1.0);
           Opinion opyou = getOpinionAbout(you);
           //If opinion is negative and you are dependent on that person and what they say makes
           //you unhappy, then say so.
           if (inPad.getP()<0&&
                   opyou.getPAD().getP()>0&&
                   opyou.getPAD().getA()>0&&
                   opyou.getPAD().getD()<0&&
                   emotion.getP()<0)
                ans.add(new EmotionThought(EmotionThought.et.EMOTION, 
                                       new PAD(emotion.getP(), emotion.getA(), emotion.getD())));
           else ans.add(THANKS);
           acceptUncritically(you,inOpinion);
           return;
        }
        if (foundData.isEmpty()){
            Opinion mine = getOpinionAbout(inOpinion.getPerson());
            if(mine.getPAD().distanceTo(inPad)<REALCLOSE)
                ans.add(new Say(me, you, mine, How.AGREE));
            else ans.add(new Say(me, you, mine, How.DISAGREE));
        } else {
            ans.add(foundData.first());
        }
        acceptUncritically(you,inOpinion);
    }
    
    //Completely unscientific and mostly random reaction to somebody having an opinion about you.
    private void opinionAboutMe(PAD opinion, String you, double howimportant){
        double d= opinion.getD();
        double p = opinion.getP();
        if (d<0){
            //If speaker is feeling not dominant towards you, you feel more dominant.
            getOpinionAbout(me).getPAD().translateD(howimportant*NORMALMODIFIER*-d);
            getOpinionAbout(you).getPAD().translateD(howimportant*NORMALMODIFIER*-d);
            emotion.translateD(howimportant*NORMALMODIFIER*-d);
        }
        else{
            //+P+D signifies liking or love, so dominance rises.
            //-P+D means dislike or hostility, so dominance goes down.
            getOpinionAbout(me).getPAD().translateD(howimportant*NORMALMODIFIER*p);
            getOpinionAbout(you).getPAD().translateD(howimportant*NORMALMODIFIER*p);
            emotion.translateD(howimportant*NORMALMODIFIER*p);
        }
    }
    
    private void processSomebodyElse(SomebodyElse inElse, String you, ArrayList<IThought> possibleAnswers){
        IThought currentLevel = inElse.what;
        double currentCertainty = 1;
        SortedList foundData;
        acceptUncritically(you, inElse);
        while (currentLevel.getClass()==inElse.getClass()){
            SomebodyElse current = (SomebodyElse) currentLevel;
            currentCertainty*=current.getCertainty();
            acceptWithCertainty(you, current.copy(), currentCertainty);
            if(current.aboutPerson.equals(me) || current.aboutPerson==you){
                foundData = memory.find(currentLevel);
                if(foundData.isEmpty()){
                    Say c;
                    if (current.aboutPerson.equals(me)){
                        IThought ans = currentLevel.copy();
                        ans.setCertainty(-ans.getCertainty());
                        c = new Say(me, you, ans, DISAGREE, null);
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
        currentCertainty*=currentLevel.getCertainty();
        acceptWithCertainty(you, currentLevel.copy(), currentCertainty);
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
    
    private void processWhereabouts(Whereabouts inWhere, String you, ArrayList<IThought> possibleAnswers){
        //Check if I have an idea about where the person is
        SortedList foundData = new SortedList();
        if (!inWhere.isQuestion())
            acceptUncritically(you, inWhere);
        for (Whereabouts b:whereabouts){
            if (b.getCharacter()==inWhere.getCharacter())
                    foundData.add(b);
        }
        //Check if I have an idea that somebody else might know
        Whereabouts tofind = new Whereabouts(inWhere.getCharacter(), "", 0.0, null);
		if(foundData.isEmpty()) foundData=memory.find(tofind);
        //if I have no idea whatsoever about where the person is
        if(foundData.isEmpty()){
            //if this is a question
            if (inWhere.isQuestion()){
                inWhere.setPlaceHolderToNull();
                foundData.add(inWhere);
            }
            else
                foundData.add(new Say(me, you, inWhere, YESNO));
        }
        possibleAnswers.add(foundData.first());
    }
    
    private void processBeingPolite(BeingPolite t, String speaker, ArrayList<IThought> possibleAnswers){
    	Say c= new Say(me, speaker, t, AGREE);
        if(t!=THANKSANYWAY)
            possibleAnswers.add(c);
    }
    
    private void processSay(Say inSay, String you, ArrayList<IThought> possibleAnswers){
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
                                    TimeStamp time = getTimeStamp();
                                    Desire desire = new Desire(GOAL,h.content, time, 1.0);
                                    //TODO maybe add another interface to avoid such code?
                                    SomebodyElse newinfo = new SomebodyElse(desire,you, new PAD(1,0,0),1.0);
                                    memory.information.add(newinfo);
                                    possibleAnswers.add(THANKS);
                                    Opinion myOpinion = getOpinionAbout(you);
                                    myOpinion.getPAD().translateP(NORMALMODIFIER);
                                    return;
                                }
                            }
                            acceptUncritically(you, inSay.content);
                            break;
                    
                case DISAGREE:  if(inSay.content instanceof Say){
                                Say h = (Say) inSay.content;
                                //Speaker has acceded to a request we made
                                if (h.type==REQUEST){
                                    possibleAnswers.add(THANKSANYWAY);
                                    Opinion myOpinion = getOpinionAbout(you);
                                    myOpinion.getPAD().translateP(-NORMALMODIFIER);
                                    return;
                                    }
                                }
                                acceptUncritically(you, inSay.content);
                                break;
                    
                case YESNO: SortedList foundData = findAnywhere(inSay.content);
                            if(inSay.content instanceof SomebodyElse){
                                SomebodyElse content = (SomebodyElse) inSay.content;
                                if (content.aboutPerson==me)
                                    foundData = findAnywhere(content.what);
                            }
                            if (foundData.isEmpty()){
                                //Say that this is the case
                                //might want to copy content, then modify certainty
                                //Difficulty: Can't introduce placeholder.
                                IThought content = inSay.content.copy();
                                content.setCertainty(-content.getCertainty());
                                possibleAnswers.add(new Say(me, you, content, DISAGREE, null));
                            }
                            else {
                                //Say that this is the case
                                possibleAnswers.add(new Say(me, you, foundData.first(), AGREE, null));
                            }
                            break;
                    
                case REQUEST:   //this one is intimately connected
                                //with planning, so leave out until plans are constructed.
                                //Therefore, NPC refuses to do anything for anyone right now.
                                IThought content = inSay.content.copy();
                                content.setCertainty(-content.getCertainty());
                                possibleAnswers.add(new Say(me, you, content, DISAGREE, null));
                                break;
                default: Debug.printDebugMessage(me + "says: Incoming Say object is making my mind boggle.",
                                                    Debug.Channel.BRAIN);
            }
        }
        else {
            //In this case, the person is informing me
            //that somebody else said something to yet another person.
            SortedList list = memory.find(inSay);
            if (list.isEmpty()){
                if(inSay.speaker==me||inSay.hearer==me){
                    Say ans = inSay.copy();
                    ans.setCertainty(-ans.getCertainty());
                    possibleAnswers.add(new Say(me,you, ans, DISAGREE, null));
                    return;
                }
                else {
                    possibleAnswers.add(new Say(me,you, inSay, YESNO, null));
                    acceptUncritically(you, inSay);
                    return;
                }
                    
            }
            possibleAnswers.add(new Say(me, you, inSay, YESNO, null));
        }
    }
    
    private void processDo(Do inDo, String you, ArrayList<IThought> possibleAnswers){
        SortedList foundData = new SortedList();
        foundData = memory.find (inDo);
        IThought c;
        acceptUncritically (you, inDo);
        if (!foundData.isEmpty()){
            c = new Say (me, you, foundData.first(), AGREE);
            possibleAnswers.add(c);
            return;
        }
        if(inDo.isQuestion()){
            inDo.setPlaceHolderToNull();
            c=inDo;
        }
        else {
            c = new Say(me, you, inDo, YESNO);
        }
        possibleAnswers.add(c);
    }
    
    private void processEmotionThought(EmotionThought inEmotion, String you, ArrayList<IThought> possibleAnswers){
        SomebodyElse s = new SomebodyElse (inEmotion, you, null, 1.0);
        SortedList foundData = memory.find(s);
        acceptUncritically(you, inEmotion);
        if (foundData.isEmpty()){
            //If no previous information is available, return
            //I am happy/sad for you.
            s.time=getTimeStamp();
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
        acceptUncritically(you,inDesire);
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
    
    private void acceptWithCertainty(String person, IThought stateofworld, double certainty){
        SomebodyElse previnfo = new SomebodyElse(stateofworld,person,null, certainty);
        previnfo.time=getTimeStamp();
        //Find any previous information on the subject
        SortedList foundData = memory.find(previnfo);
        if (!foundData.isEmpty()){
            //If found, remove it and replace it with the new information.
            memory.information.remove(foundData.first());
            previnfo.previous= (SomebodyElse) foundData.first();      
        }
        //Otherwise, just add the information.
        memory.information.add(previnfo);
    }
    
    private void acceptUncritically(String person, IThought stateofworld){
        acceptWithCertainty(person,stateofworld,1.0);
    }
    
    //If this does not find an opinion about person,
    //creates a neutral one.
    private void makeEmptyOpinion(String about){
        PAD pad = null;
        for (Opinion i:opinions){
            if(i.getPerson()==about)
                pad=i.getPAD();
        }
        if (pad==null)
            opinions.add(new Opinion(about, new PAD(0,0,0)));
    }
    
    //selects which statement to respond to in the case that an inOpinion speech act
    //contains several pieces of information
    //responsible for sending the response and updating one's own opinion of the
    //world in accordance with how one believes the statement to change the world/
    private SpeechAct selectResponse(ArrayList<IThought> possibleResponses, String you){
        //This is the real thing
        //speak(makeSpeechAct(possibleResponses, me));
        ArrayList<IThought> ans = new ArrayList<>();
        for (IThought i : possibleResponses){
            ans.add(i.copy());
        }
        debugInfo=ans;
        //if(ans.isEmpty()) return;
        String temp = me + " says: ";
        for (IThought i: ans){
            temp+= temp.toString();
        }
        Debug.printDebugMessage(temp, Debug.Channel.BRAIN);
        return convertIThoughtToSpeechAct(ans, NEUTRAL,me,you);
        
    }
    //--------------------------------------------------------------------------
    //SECTION: Debugging stuff
    //--------------------------------------------------------------------------
    
    public void plantFalseMemory(IThought i){
        if(i instanceof Opinion){
            Opinion o = (Opinion) i;
            opinions.add(o);
            return;
        }
        memory.add(i);
        Debug.printDebugMessage("Planting IThought in " + me, Debug.Channel.BRAIN);
    }
    
    public ArrayList<IThought> debugInfo;
    
    
    //--------------------------------------------------------------------------
    //SECTION: Everything else
    //Random helper functions, stuff that should be called from outside,
    //and things that have not been handled yet.
    //--------------------------------------------------------------------------
    
    public Opinion getOpinionAbout(String who){
        Opinion result=null;
        for (Opinion i: opinions){
            if (i.getPerson()==who)
                result=i;
        }
        return result;
    }
    
    public SortedList findAnywhere(IThought i){
        if(i instanceof Opinion){
            Opinion o = (Opinion) i;
            SortedList ans = new SortedList();
            ans.add(getOpinionAbout(o.getPerson()));
            return ans;
        }
        return memory.find(i);
    }

    public SpeechAct selectPhrase(ArrayList<SpeechAct> acts){
        return aic.selectPhrase(acts);
    }

    public SpeechAct getIntendedPhrase(){
        aic.getCharacterName();
        return aic.getIntendedPhrase();}

    public AIControler getAIControler(){
        return aic;
    }

    //Returns the TextProperty that should be used in conversation with person
    public TextPropertyEnum chooseProperty(String person){
        //TODO EVALUATE MEMORIES/OPINIONS OF PERSON
        return TextPropertyEnum.COLLOQUIAL;
    }
    
    public void observeRoomChange(String person, String origin, String destination){
        Debug.printDebugMessage("NPC registered room change", Debug.Channel.BRAIN);
        SortedList res = memory.find (new Whereabouts(person, ""));
        TimeStamp t = getTimeStamp();
        t.incrementTime(-1);
        Whereabouts w = new Whereabouts(person, origin,1.0, t);
        Whereabouts n = new Whereabouts(person, destination, 1.0, getTimeStamp(), w);
        if (res.isEmpty()){           
            memory.add(n);
        }
        else {
            Whereabouts old = (Whereabouts) res.first();
            w.setPrevious(old);
            memory.replace(old, n);
        }
    }
    
    public void observeInteraction(String who, String with){
        memory.add(new Do(TALKTO, who,with, getTimeStamp()));
    }
    
    public void observePickUp(String who, String what){
        memory.add(new Do(PICKUP, who,what, getTimeStamp()));
    }
    
    public void observePutDown(String who, String what){
        memory.add(new Do(PUTDOWN, who,what, getTimeStamp()));
    }

    //Character does some thinking, cooling down, executing plan etc.
    public void update(){
        Debug.printDebugMessage("Calling update function on " + me + ", but don't expect anything to happen.",
                                    Debug.Channel.BRAIN);
    }

    //this method creates plans for any goals and puts
    private void plan(){
        
    }
    
    @Override
    public void seePeople(ArrayList<String> people) {
        TimeStamp t = getTimeStamp();
        for (String person:people){
            SortedList res = memory.find (new Whereabouts(person, ""));          
            Whereabouts w = new Whereabouts(person, here ,1.0, t);
            if (res.isEmpty()){           
                memory.add(w);
            }
            else {
                Whereabouts old = (Whereabouts) res.first();
                w.setPrevious(old);
                memory.replace(old, w);
            }
        }

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
