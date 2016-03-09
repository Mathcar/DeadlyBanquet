package deadlybanquet.model;

/**
 * Created by Hampus on 2016-03-09.
 */
public class Position {
    public int x,y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public Position(Position p){
    	this.x = p.getX();
    	this.y = p.getY();
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setX(int newX){
        this.x = newX;
    }

    public void setY(int newY){
        this.y = newY;
    }
    
    public void incX(){
    	x++;
    }
    
    public void decX(){
    	x--;
    }
    
    public void incY(){
    	y++;
    }
    
    public void decY(){
    	y--;
    }
    
    public Position copy(){
    	return new Position(this);
    }
}
