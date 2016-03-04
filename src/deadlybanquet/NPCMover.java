package deadlybanquet;

import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;

/**
 * Created by Hampus on 2016-02-18.
 * A very basic template class which contains the things needed to utilize the inherent pathfinding
 *
 */
public class NPCMover implements Mover {
    public int x,y;
    private Path currentPath;
    private int pathCounter = 0;
    public int prevTilex,prevTiley;

    public NPCMover(int startX, int startY){
        x = startX;
        y = startY;
        prevTilex = 0;
        prevTiley = 0;
    }

    //Small template on an update method regarding movement along a path
    public void update(){
        //Move along the current path if there is one
        if(currentPath!=null) {
        	if(pathCounter != 0){
        		prevTilex = currentPath.getStep(pathCounter-1).getX();
        		prevTiley = currentPath.getStep(pathCounter-1).getY();
        	}
            int targetX = currentPath.getStep(pathCounter).getX()*32;
            int targetY = currentPath.getStep(pathCounter).getY()*32;
            if (targetX != x) {
                x += (targetX - x) / Math.abs(targetX - x); //Move one pixel per update in the correct x direction
            } else if (targetY != y) {
                y += (targetY - y) / Math.abs(targetY - y); //Move one pixel per update in the correct x direction
            } else if (currentPath.getLength() > pathCounter+1) {
                pathCounter++;  //If a grid has been reached, increment the path so the next grid will be the target
            } else{
                currentPath = null; //Remove current path if destination has been reached
            }
        }
    }
    

    public void addPath(Path p){
        pathCounter = 0;
        currentPath = p;
    }


}
