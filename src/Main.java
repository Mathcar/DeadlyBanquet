import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;


public class Main {

	public static void main(String[] args) {
		
		AppGameContainer app;
		try {
			app = new AppGameContainer(new SetUpClass("SetUpTest"));
			
			app.setDisplayMode(640, 480, false);
			app.setShowFPS(true);
			app.setAlwaysRender(true);
			app.setVSync(true);
			app.setMaximumLogicUpdateInterval(60);
			app.start(); 
			
		} catch (SlickException e) {
			e.printStackTrace();
			System.exit(0);
		}

	}

}
