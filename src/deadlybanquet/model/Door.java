package deadlybanquet.model;

/**
 * Created by Hampus on 2016-03-11.
 */
public class Door {
    private String origin, destination;
    private Direction direction;
    private Position pos;
    private boolean initiationComplete;

    public Door(Position pos){
        this.pos = pos;
        initiationComplete = false;
    }

    public Door(Position pos, Direction direction, String org, String dest){
        origin = org;
        destination = dest;
        this.direction = direction;
        initiationComplete = true;
    }

    public Direction getDirection(){
        return direction;
    }

    public String getDestinationRoom(){
        if(initiationComplete)
            return destination;
        else
            return "Door not initialized";
    }
    
    public String getOriginRoom(){
        if(initiationComplete)
            return origin;
        else
            return "Door not initialized";
    }
    //Can only be called once for a single door object
    public void createConnection(String org, String dest, Direction dir){
        if(!initiationComplete) {
            direction = dir;
            origin = org;
            destination = dest;
            initiationComplete = true;
        }
        //Otherwise initation has already been done!
    }

    public Position getPos(){
        return pos.copy();
    }

    public int getX(){
        return pos.getX();
    }
    public int getY(){
        return pos.getY();
    }
}
