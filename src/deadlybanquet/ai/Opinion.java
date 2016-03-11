package deadlybanquet.ai;

/**
 * Created by Tom on 2016-02-19.
 * This IThought expresses the fact that (implied person) has opinion pad about aboutPersonRoomObject
 */
public class Opinion implements IThought{
    public String aboutPersonRoomObject;
    public PAD pad;
    public Opinion(String about, PAD pad){
        this.pad = pad;
        aboutPersonRoomObject = about;
    }
    @Override
    public String toString(){
        return "feeling " + pad + " about " + aboutPersonRoomObject;
    }
}