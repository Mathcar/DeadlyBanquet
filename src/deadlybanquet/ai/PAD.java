package deadlybanquet.ai;
import static java.lang.Math.*;
/**
 * Placeholder value is a PAD object containing nonsense values. 
 * The placeholder must have nonsense in all three fields; it is therefore
 * enough to check just one value.
 * @author omega
 */
/*The PAD model describes a cube where points can describe 
either a current feeling or personality (that is, "the emotions somebody
usually feels" or an average of feelings over a long time period)
The octants of the unit cube can be labelled as follows:
(In brackets our own words)
Exuberant (i.e. cheerful) (+P+A+D) loves
Bored (-P-A-D) does not care about
Dependent (needy)(+P+A-D) i.e. tries to make them do something for him
Disdainful (-P-A+D) dislikes, finds disagreeable
Relaxed (+P-A+D) (is friends with, likes)
Anxious (-P+A-D) (scared of)
Docile (+P-A-D) looks up to/does as they are told
Hostile (-P+A+D) (hates)
Mehrabian suggests that Hostile temperament probably being more 
descriptive of the violent antisocial personality type and 
Disdainful temperament being descriptive of 
the nonviolent white-collar criminal.
Therefore, a feeling that is very hostile might be used
as what we have so far called the "murder trait".
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
}
