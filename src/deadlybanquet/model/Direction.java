package deadlybanquet.model;

public enum Direction {
	NORTH,SOUTH,WEST,EAST;

	public static Direction getOppositeDirection(Direction dir){
        if(dir == NORTH)
            return SOUTH;
        else if(dir  == WEST)
            return EAST;
        else if(dir == SOUTH)
            return NORTH;
        else
            return WEST;
    }
}
