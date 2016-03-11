package deadlybanquet;

import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;

/**
 * Created by Hampus on 2016-03-11.
 */
public class RenderSet {
    private ArrayList<RenderObject> renderObjects;
    private TiledMap map;

    public RenderSet(TiledMap map, ArrayList<RenderObject> ros){
        this.map = map;
        renderObjects = ros;
    }

    public TiledMap getMap(){
        return map;
    }

    public ArrayList<RenderObject> getRenderObjects(){
        return renderObjects;
    }
}
