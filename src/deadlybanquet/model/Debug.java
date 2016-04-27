package deadlybanquet.model;

import java.util.ArrayList;

/**
 * Created by Hampus on 2016-04-27.
 * This class is used instead of System.out so that output can be organized into channels.
 * You can then select which you want to listen to, and which ones you dont, by opening or closing them.
 *
 */
public class Debug {
    private static int nrOfChannels;
    private static boolean debugActivated;
    private static boolean[] channelOpen;
    private static ArrayList<String>[] channelHistory;
    //Used to define which npc to actually debug since debugging all at the same time will become a bit hard!
    //An empty string ("") will let all npcs print their messages
    private static String debugNPC;


    public enum Channel{
        PLAYER,
        NPC,
        BRAIN,
        OBSERVING,
        SPEECH_ACTS,
        CONVERSATION,
        WORLD,
        MASTER_PATH,
        PATHFINDING
    }

    public static void init(){
        nrOfChannels = Channel.values().length;
        debugActivated = true;
        channelOpen = new boolean[nrOfChannels];
        channelHistory = new ArrayList[nrOfChannels];
        debugNPC = "";
        for(int i = 0; i<nrOfChannels; i++){
            channelHistory[i] = new ArrayList<>();
            channelOpen[i] = true;
        }
    }

    public static void printDebugMessage(String message, Channel channel){
        if(!debugActivated)
            return;
        if(channel== Channel.NPC)
            System.out.println("NPCs need to use the print specific to them, not printDebugMessage!");
        else {
            int channelNr = channel.ordinal();
            if (channelOpen[channelNr]) {
                channelHistory[channelNr].add(message);
                System.out.println(channel.toString() + "   : " + message);
            }
        }
    }

    public static void printDebugMessage(String message, Channel channel, String sender){
        if(!debugActivated)
            return;
        if( channel!=Channel.NPC)
            System.out.println("This is only for the npcs!");
        else if(debugNPC.equals("") || debugNPC.equals(sender)){
            int channelNr = channel.ordinal();
            if (channelOpen[channelNr]) {
                channelHistory[channelNr].add(message);
                System.out.println(channel.toString() + " :   " + message);
            }
        }
    }

    public static void openAllChannels(){
        for(int i = 0; i<Channel.values().length; i++)
            channelOpen[i] = true;
    }

    public static void closeAllChannels(){
        for(int i = 0; i<Channel.values().length; i++)
            channelOpen[i] = false;
    }

    public static void setDebugActivated(boolean b){
        debugActivated = b;
    }

    public static void setChannelStatus(Channel c, boolean b){
        channelOpen[c.ordinal()] = b;
    }

    public static void setDebugNPC(String name){
        debugNPC = name;
    }


}
