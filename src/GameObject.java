import org.newdawn.slick.Graphics;

public interface GameObject {

	public int getX();
	
	public int getY();
	
	public void uppdate();

	public void draw(Graphics g);
	
	public boolean isSolid();
	
//	public TileID getTileID();
	
	enum TravaresableState{
		SOLID, SMALL, FLAT;
	}
}
