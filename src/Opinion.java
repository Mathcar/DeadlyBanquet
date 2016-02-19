/**
 * Created by Tom on 2016-02-19.
 */
public class Opinion {
    /*
    To simplify this at first, ppl will only have one opinion meter,
    let's call that love. love is an int between 0 and 100 where 100 is
    perfect love, and 0 is absolute hate.
     */
    private int love;

    public Opinion(){
        this.love=30;
    }

    public void setLove(int newLove){
        this.love=newLove;
    }
    public int getLove(){
        return this.love;
    }
}