package deadlybanquet.model;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Hampus on 2016-03-04.
 */
public class Room implements TileBasedMap {
    private TiledMap map;
    private AStarPathFinder pathFinder;
    private ArrayList<Character> characters;


    public Room(String tilemapURL){
        try {
            map = new TiledMap(tilemapURL);
        }catch(SlickException se){
            se.printStackTrace();
        }
        pathFinder = new AStarPathFinder(this, 50, false);
        characters = new ArrayList<Character>();

    }

    public ArrayList<Character> getCharactersInRoom(){
        return characters;
    }

    private boolean isBlocked(int x, int y){
        for(Character c : characters){
            if(c.getX() == x && c.getY() == y){
                return true;                    //A Characters is occupying this tile
            }
        }
        for(int i = map.getLayerCount()-1; i>=0;i--){
            if(i!=0) {
                if (map.getTileId(x, y, i) != 0) {
                    return true;       //Tile has something static in the blocked layers
                }
            }
        }
        return false;       //Tile is unblocked;
    }

    @Override
    public int getWidthInTiles() {
        return map.getWidth();
    }

    @Override
    public int getHeightInTiles() {
        return map.getHeight();
    }

    @Override
    public void pathFinderVisited(int i, int i1) {
    }

    @Override
    //Return a boolean as to whether a tile is blocked or not, x and y coordinates should be in tile-format
    //Collision detection needs to be added!!
    public boolean blocked(PathFindingContext pathFindingContext, int x, int y) {
        return isBlocked(x,y);
    }

    @Override
    public float getCost(PathFindingContext pathFindingContext, int i, int i1) {
        return 1;
    }


}
