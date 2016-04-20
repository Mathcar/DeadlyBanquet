package deadlybanquet.ai;
import static java.lang.Math.*;
/**
 * Placeholder value is a PAD object containing nonsense values. 
 * The placeholder must have nonsense in all three fields; it is therefore
 * enough to check just one value.
 * @author omega
 * The PAD model describes a cube where points can describe 
 * either a current feeling or personality (that is, "the emotions somebody
 * usually feels" or an average of feelings over a long time period)
 * The octants of the unit cube can be labelled as follows:
 * NEW: Definitive guide to the PAD
 * PAD		Temperament			Opinion                 Emotion
 * (+P+A+D)	Exuberant		 	loves			cheerful
 * (-P-A-D) 	Bored				not interested in	bored
 * (+P+A-D) 	Dependent (needy)		relies on		wants somebody to rely on
 * (-P-A+D) 	Disdainful 			dislikes        	scornful	
 * (+P-A+D) 	Relaxed 			likes			relaxed
 * (-P+A-D) 	Anxious 			scared of		anxious
 * (+P-A-D) 	Docile 				wants to obey		cooperative
 * (-P+A+D) 	Hostile 			hates			angry
 * 
 * Mehrabian suggests that Hostile temperament probably being more 
 * descriptive of the violent antisocial personality type and 
 * Disdainful temperament being descriptive of 
 * the nonviolent white-collar criminal.
 * Therefore, a feeling that is very hostile might be used
 * as what we have so far called the "murder trait".
*/

public class PAD {
    public PAD (double p,double a, double d){
        P=p;
        A=a;
        D=d;
    }
    //Pleasantness
    private double P;
    //Arousability (excitability)
    private double A;
    //Dominance
    private double D;
    public void setP(double i){
        P=i;
    }
    public void setA(double i){
        A=i;
    }
    public void setD(double i){
        D=i;
    }
    
    public static PAD placeholderPAD(){
        return new PAD (-5, -5, -5);
    }

    public String getOpinion(){
        if(P>0&&A>0&&D>0) return "love";
        else if(P<0&&A<0&&D<0) return "am not interested in";
        else if(P>0&&A>0&&D<0) return "rely on";
        else if(P<0&&A<0&&D>0) return "dislike";
        else if(P>0&&A<0&&D>0) return "like";
        else if(P<0&&A>0&&D<0) return "am scared of";
        else if(P>0&&A<0&&D<0) return "want to obey";
        else if(P<0&&A>0&&D>0) return "hate";
        else return "can not describe my feelings for"; //todo make a better version of this method
    }
    
    public boolean isPlaceholder(){
        return (P<-1||P>1);
    }
    /** Calculates the distance to the input PAD.
     * 
     * @param pad
     * @return distance
     */
    public double distanceTo(PAD pad){
        return Math.sqrt(pow(this.P-pad.P,2)+pow(this.A-pad.A,2)+pow(this.D-pad.D,2));
    }
    /**This is currently a very simple linear model.
    No idea if this is sufficiently expressive.
    This method should be called on the PAD object
    representing a character's current emotional state.
     *
     * @param newfeeling    can be either a feeling attached to
    an event or something or a temperament (to simulate
    "calming down again" after an event)
     * @param howmuch    must be between 0 and 1, representing the percentage
    of space that should be covered.   
     */
       
    public void moveInTheDirectionOf(PAD newfeeling, double howmuch){
        if(howmuch<0||howmuch>1) throw new IllegalArgumentException();

        //Make a vector from the current values to the new ones
        double x = newfeeling.P-this.P;
        double y = newfeeling.A-this.A;
        double z = newfeeling.D-this.D;
        //Move the feeling along this vector in the direction
        //of the new one
        this.P+=x*howmuch;
        this.A+=y*howmuch;
        this.D+=z*howmuch;
    }
    public double getP(){    
        return P;
    }

    public double getA() {
        return A;
    }

    public double getD() {
        return D;
    }
    //This is really just an example method showing how one could
    //get a measure of somebody's inclination to murder somebody
    //(or other information)
    //The lower the value, the more hostile the character is feeling.
    public double murderousness() {
        return sqrt(pow(-1-P,2) + pow(1-A,2) + pow(1-D,2));
    }
    
    @Override
    public String toString(){
        return "P=" + P + ", A="+ A + ", D=" + D;
    }
    
    public void translateP(double howmuch){
        P+=howmuch;
        if(P>1) P = 1;
        if(P<1) P = -1;
    }
    
    public void translateA(double howmuch){
        A+=howmuch;
        if(A>1) A = 1;
        if(A<1) A = -1;
    }
    
    public void translateD(double howmuch){
        D+=howmuch;
        if(D>1) D = 1;
        if(D<1) D = -1;
    }
    
    public PAD copy(){
        return new PAD(P,A,D);
    }
}
