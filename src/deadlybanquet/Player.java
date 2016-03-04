package deadlybanquet;

import deadlybanquet.states.GameWindow;
import org.newdawn.slick.*;

/**
 * Created by Hampus on 2016-02-28.
 */
public class Player {
    private Image playerdown,playerup,playerleft,playerright;
    private int playerx = 100;
    private int playery = 100;
    private Boolean playerDown;
    private Boolean playerUp;
    private Boolean playerLeft;
    private Boolean playerRight;
    private Boolean playerLookingDown = true;
    private Boolean playerLookingUp = false;
    private Boolean playerLookingLeft = false;
    private Boolean playerLookingRight = false;
    private Boolean leftStop;
    private Boolean rightStop;
    private Boolean upStop;
    private Boolean downStop;
    private SpriteSheet moveUpSheet;
    private Animation moveUpAni;
    private Boolean playerMovingUp;
    private SpriteSheet moveDownSheet;
    private Animation moveDownAni;
    private Boolean playerMovingDown;
    private SpriteSheet moveLeftSheet;
    private Animation moveLeftAni;
    private Boolean playerMovingLeft;
    private SpriteSheet moveRightSheet;
    private Animation moveRightAni;
    private Boolean playerMovingRight;

    public Player() throws SlickException {
        moveUpSheet = new SpriteSheet("res/pictures/upanimation.png",32,32);
        moveUpAni = new Animation(moveUpSheet,300);
        moveDownSheet = new SpriteSheet("res/pictures/downanimation.png",32,32);
        moveDownAni = new Animation(moveDownSheet,300);
        moveLeftSheet = new SpriteSheet("res/pictures/leftanimation.png",32,32);
        moveLeftAni = new Animation(moveLeftSheet,300);
        moveRightSheet = new SpriteSheet("res/pictures/rightanimation.png",32,32);
        moveRightAni = new Animation(moveRightSheet,300);

        playerdown = new Image("res/pictures/lookingdown.png");
        playerup = new Image("res/pictures/lookingup.png");
        playerleft = new Image("res/pictures/lookingleft.png");
        playerright = new Image("res/pictures/lookingright.png");



    }

    public void update(Input input, int delta, GameWindow world) {
        if ((playerx / 32 == 5 && playery / 32 == 1 && playerLookingUp && input.isKeyPressed(Input.KEY_E)) || (input.isKeyPressed(Input.KEY_2))) {
            System.out.println("Should be changing room now!");
            playerx = 9 * 32;
            playery = 2 * 32;
            world.swapRooms(2);     //Request the world to change active room
            playerLookingDown = true;
            playerLookingUp = false;
        } else if (playerx / 32 == 9 && playery / 32 == 1 && playerLookingUp && input.isKeyPressed(Input.KEY_E) || input.isKeyPressed(Input.KEY_1)) {
            System.out.println("Should be changing room now!");
            playerx = 5 * 32;
            playery = 2 * 32;
            world.swapRooms(1);    //Request the world to change active room
            playerLookingDown = true;
            playerLookingUp = false;
        }
        else if (playerx / 32 == 9 && playery / 32 == 1 && playerLookingUp && input.isKeyPressed(Input.KEY_E) || input.isKeyPressed(Input.KEY_3)) {
        	System.out.println("Should be changing room now!");
        	playerx = 9 * 32;
        	playery = 4 * 32;
        	world.swapRooms(3);    //Request the world to change active room
        	playerLookingDown = true;
        	playerLookingUp = false;
        }
    

        playerMovement(input, delta, world.getMap());
    }

    public void playerMovement(Input input, int delta, LayerBasedTileMap pathfindingMap){
        playerDown = input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN);
        playerUp = input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP);
        playerLeft = input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT);
        playerRight = input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT);

        playerMovingUp = false;
        playerMovingDown = false;
        playerMovingLeft = false;
        playerMovingRight = false;

        leftStop = pathfindingMap.isBlocked((playerx)/32,(playery+21)/32);
        rightStop = pathfindingMap.isBlocked((playerx+32)/32,(playery+21)/32);
        upStop = pathfindingMap.isBlocked((playerx+16)/32,(playery+20)/32);
        downStop = pathfindingMap.isBlocked((playerx+16)/32,(playery+33)/32);


        if (playerDown){
            playerLookingDown = true;
            playerLookingUp = false;
            playerLookingLeft = false;
            playerLookingRight = false;
            playerMovingDown = true;
            moveDownAni.update(delta);
            if(!downStop){
                playery += 1;
            }
        }else if (playerLeft){
            playerLookingLeft = true;
            playerLookingUp = false;
            playerLookingDown = false;
            playerLookingRight = false;
            playerMovingLeft = true;
            moveUpAni.update(delta);
            if(!leftStop){
                playerx -= 1;
            }
        }else if (playerUp){
            playerLookingUp = true;
            playerLookingDown = false;
            playerLookingLeft = false;
            playerLookingRight = false;
            playerMovingUp = true;
            moveUpAni.update(delta);
            if(!upStop){
                playery -= 1;
            }
        }else if (playerRight){
            playerLookingRight = true;
            playerLookingUp = false;
            playerLookingDown = false;
            playerLookingLeft = false;
            playerMovingRight = true;
            moveUpAni.update(delta);

            if(!rightStop){
                playerx += 1;
            }
        }

    }
    //Renamed from playerAnimation to render for consistency on rendering methods
    public void render(){
        if (playerLookingDown){
            if(playerMovingDown){
                moveDownAni.draw(playerx,playery);
                moveDownAni.setPingPong(true);
            }else{
                playerdown.draw(playerx,playery);
            }
        }else if (playerLookingLeft){
            if(playerMovingLeft){
                moveLeftAni.draw(playerx,playery);
                moveLeftAni.setPingPong(true);
            }else{
                playerleft.draw(playerx,playery);
            }
        }else if (playerLookingUp){
            if(playerMovingUp){
                moveUpAni.draw(playerx,playery);
                moveUpAni.setPingPong(true);
            }else{
                playerup.draw(playerx,playery);
            }
        }else if (playerLookingRight){
            if(playerMovingRight){
                moveRightAni.draw(playerx,playery);
                moveRightAni.setPingPong(true);
            }else{
                playerright.draw(playerx,playery);
            }
        }
    }

    //Test for facing and talking to the testNPC
    public boolean nextToNPC(NPCMover testNPC){
        if((playerx+16)/32 == testNPC.x/32 && ((playery+21)/32 - testNPC.y/32) == 1 && playerLookingUp){
            return true;
        }else if((playerx+16)/32 == testNPC.x/32 && ((playery+21)/32 - testNPC.y/32) == -1 && playerLookingDown){
            return true;
        }else if((playerx+16)/32 - testNPC.x/32 == 1 && ((playery+21)/32 == testNPC.y/32) && playerLookingLeft){
            return true;
        }else if((playerx+16)/32 - testNPC.x/32 == -1 && ((playery+21)/32 == testNPC.y/32) && playerLookingRight){
            return true;
        }else{
            return false;
        }
    }



}
