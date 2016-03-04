package deadlybanquet;

import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import sun.java2d.pipe.ShapeSpanIterator;

/**
 * Created by Hampus on 2016-02-18.
 * A very simple, but extendable class which allows a TiledMap to utilize pathfinding.
 * The isBlocked method needs to be updated if changes to the definition of what is collision are made.
 */
public class LayerBasedTileMap implements TileBasedMap {
    private TiledMap map;
    private boolean[][] blockMap;

    public LayerBasedTileMap(TiledMap baseMap){
        map = baseMap;
        //create a blockMap for manual blocking, and intialize it to not block anything.
        blockMap = new boolean[getWidthInTiles()][getHeightInTiles()];
        for(int i = 0; i< blockMap.length; i++){
            for(int j=0; j<blockMap[i].length; j++){
                blockMap[i][j] = false;
            }
        }
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
    public boolean blocked(PathFindingContext pfx, int x, int y) {
        return isBlocked(x,y);

    }

    //Update the whole blockmap by supplying a new one to overwrite the old.
    public void updateBlockmap(boolean[][] newBlockMap){
        if(newBlockMap.length == blockMap.length && newBlockMap[0].length == blockMap.length){
            blockMap = newBlockMap;
        }

    }

    //Update the manual blocking of a single tile.
    public void updateBlockStatus(int x, int y, boolean blocked){
        blockMap[x][y] = blocked;
    }

    @Override
    public float getCost(PathFindingContext pfx, int x, int y) {
        return 1;
    }

    public boolean isBlocked(int x, int y){
        if(blockMap[x][y]){
            return true;        //Tile is manually blocked
        }
        for(int i = map.getLayerCount()-1; i>=0;i--){
            if(i!=0) {
                if (map.getTileId(x, y, i) != 0) {
                    return true;       //Tile has something in the blocked layers
                }
            }
        }
        return false;       //Tile is unblocked, both manually and in the tilemap
    }
}
