import org.newdawn.slick.Graphics;

public interface GameObject {

	public int getX();
	
	public int getY();
	
	public void uppdate();

	public void draw(Graphics g);
	
	public boolean isSolid();
	
	enum TravaresableState{
		SOLID, SMALL, FLAT;
	}
}
