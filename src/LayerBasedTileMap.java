import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

/**
 * Created by Hampus on 2016-02-18.
 * A very simple, but extendable class which allows a TiledMap to utilize pathfinding.
 * The isBlocked method needs to be updated if changes to the definition of what is collision are made.
 */
public class LayerBasedTileMap implements TileBasedMap {
    private TiledMap map;
    //private int blockingIndex = 2;   //can be used if a single layer can represent collision to simplify the code

    public LayerBasedTileMap(TiledMap baseMap){
        map = baseMap;
    }

   /* public LayerBasedTileMap(TiledMap baseMap, int bi){
        map = baseMap;
        blockingIndex = bi;
    }*/

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

    @Override
    public float getCost(PathFindingContext pfx, int x, int y) {
        return 1;
    }

    public boolean isBlocked(int x, int y){
        for(int i = map.getLayerCount()-1; i>=0;i--){
            if(i%2 == 0 && i != 0) {
                if (map.getTileId(x, y, i) != 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
