
public interface GameObject {

	public int getX();
	
	public int getY();
	
	public void uppdate();

	public void draw();
	
	public boolean isSolid();
	
	enum TravaresableState{
		SOLID, SMALL, FLAT;
	}
}
