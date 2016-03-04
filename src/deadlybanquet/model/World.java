package deadlybanquet.model;

import deadlybanquet.Player;

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
        roomMap[0][0] = new Room();

    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
