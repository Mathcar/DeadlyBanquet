package deadlybanquet.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Hampus on 2016-03-04.
 */
public class World implements ActionListener {
    private ArrayList ais;
    private Player player;
    private Room[][] roomMap;

    public World(){
        roomMap = new Room[2][2];       //Needs to be updated as more rooms are added
        //add room initiations
        Character playerCharacter = new Character(this, "Gandalf", 3, 3);
        roomMap[0][0] = new Room("res/pictures/living_room2.tmx", "Living Room");
        player = new Player(playerCharacter);
        roomMap[0][0].addCharacter(playerCharacter);
        

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        for(Room[] rm : roomMap){
            for(Room r : rm) {
                r.moveWithCollision(e);
            }
        }
    }
}
