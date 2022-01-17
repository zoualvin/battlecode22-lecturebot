package daddydarwin;

import java.util.Arrays;
import java.lang.Math;
import battlecode.common.*;
import daddydarwin.RobotPlayer;

import static battlecode.common.Clock.getBytecodeNum;

//access shared array; update number of each robot/builder in the array (modify buildTowardsLowRubble)
//keep soldier spawning in a x^2+y^2=16 circle where the archon is the origin
public class ArchonStrategy {

    //static int miners = 0, soldiers = 0, builders = 0, sages = 0, watchtowers = 0, labs = 0;
    //in array 2^6-4 is miner --> 2^6-3 soldier --> 2^6 - 2 archon --> 2^6 - 1 builders
    /**
     * Run a single turn for an Archon.
     * This code is wrapped inside the infinite loop in run(), so it is called once
     * per turn.
     */
    static MapLocation archonPlace = null;
    static void runArchon(RobotController rc) throws GameActionException { //throw out static ints above instead read from awway each time
        archonPlace = rc.getLocation();
        RobotInfo[] friendlyRobots = rc.senseNearbyRobots(archonPlace, rc.getType().actionRadiusSquared, rc.getTeam());
        int numSoldiersAround = 0;
        for (int i = 0; i < friendlyRobots.length; i++) {
            if (friendlyRobots[i].equals(RobotType.SOLDIER)) {
                numSoldiersAround++;

            }
        }
        if (numSoldiersAround < 5) {
            for (int i = 0; i < 5; i++) {
                buildTowardsLowRubble(rc, RobotType.SOLDIER);
            }
        }
        if (RobotPlayer.turnCount > 800 && RobotPlayer.turnCount < 1000) {
            buildTowardsLowRubble(rc, RobotType.SOLDIER);
            if (rc.getTeamLeadAmount(rc.getTeam()) > 100) {
                buildTowardsLowRubble(rc, RobotType.MINER);
            }
        }
        if (RobotPlayer.turnCount > 1700 && RobotPlayer.turnCount < 1900) {
            buildTowardsLowRubble(rc, RobotType.SOLDIER);
        }
        if(rc.readSharedArray((int)(Math.pow(2,6))-4) < 20){
            buildTowardsLowRubble(rc, RobotType.MINER);
            int ran = RobotPlayer.rng.nextInt(3);
            if(ran == 1) {
            	buildTowardsLowRubble(rc, RobotType.SOLDIER);}
        } else if (rc.readSharedArray((int)(Math.pow(2,6))-3) < 25){
            buildTowardsLowRubble(rc, RobotType.SOLDIER);
        } else if (rc.readSharedArray((int)(Math.pow(2,6))-1) < 5){
            buildTowardsLowRubble(rc, RobotType.BUILDER);
        }
        else if (rc.readSharedArray((int)(Math.pow(2,6))-4) < rc.readSharedArray((int)(Math.pow(2,6))-3) && rc.getTeamLeadAmount(rc.getTeam()) < 5000){
            buildTowardsLowRubble(rc, RobotType.MINER);
        }
        else if (rc.readSharedArray((int)(Math.pow(2,6))-1) < rc.readSharedArray((int)(Math.pow(2,6))-3) / 20){
            buildTowardsLowRubble(rc, RobotType.BUILDER);
        }  //else if (sages < 1) {
            //buildTowardsLowRubble(rc, RobotType.SAGE); sage counter NOT needed here
        //}
        else {
            int choose = 0+(int)(Math.random() * ((1-0)+1)); //evaluate based on array how many soldiers or miners we needed; whichever count is lower is what will spawn
            if (choose == 0) { //use random step if the counts are equal in array
                buildTowardsLowRubble(rc, RobotType.MINER);
            } else {
                buildTowardsLowRubble(rc, RobotType.SOLDIER);
            }
        }
        if (getBytecodeNum() == 20000) {
            Clock.yield();
        }
    }

    static void buildTowardsLowRubble(RobotController rc, RobotType type) throws GameActionException {
        Direction[] dirs = Arrays.copyOf(RobotPlayer.directions, RobotPlayer.directions.length);
        Arrays.sort(dirs, (a, b) -> getRubble(rc, a) - getRubble(rc, b));
        for (Direction d : dirs){
            if(rc.canBuildRobot(type, d)){
                rc.buildRobot(type, d);
                switch(type){
                    case MINER: rc.writeSharedArray((int)(Math.pow(2,6))-4, rc.readSharedArray((int)(Math.pow(2,6))-4)+1); break;
                    case SOLDIER: rc.writeSharedArray((int)(Math.pow(2,6))-3, rc.readSharedArray((int)(Math.pow(2,6))-3)+1);  break;
                    case BUILDER: rc.writeSharedArray((int)(Math.pow(2,6))-1,rc.readSharedArray((int)(Math.pow(2,6))-1)+1); break;
                    //case SAGE: sages++; break;
                    //case WATCHTOWER: watchtowers++; break;
                    //case LABORATORY: labs++; break;
                    default: break;
                }
            }
        }
    }

    static int getRubble(RobotController rc, Direction d){
        try {
            MapLocation loc = rc.getLocation().add(d);
            return rc.senseRubble(loc);
        } catch (GameActionException e){
            e.printStackTrace();
            return 0;
        }
    }




}

