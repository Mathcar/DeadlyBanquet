
public interface GameObject {
	
	public int getX();
	
	public int getY();
	
	public void uppdate();
	
	public boolean isSolid();
	
//	public TileID getTileID();
	
	enum TravaresableState{
		SOLID, SMALL, FLAT;
	}
}
