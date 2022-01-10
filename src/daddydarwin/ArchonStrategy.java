package daddydarwin;

import java.util.Arrays;

import battlecode.common.*;
import daddydarwin.RobotPlayer;

public class ArchonStrategy {

    static int miners = 0, soldiers = 0, builders = 0, sages = 0, watchtowers = 0, labs = 0;
    /**
     * Run a single turn for an Archon.
     * This code is wrapped inside the infinite loop in run(), so it is called once
     * per turn.
     */
    static void runArchon(RobotController rc) throws GameActionException {
        if (RobotPlayer.turnCount > 800 && RobotPlayer.turnCount < 1000) {
            buildTowardsLowRubble(rc, RobotType.SOLDIER);
            if (rc.getTeamLeadAmount(rc.getTeam()) > 100) {
                buildTowardsLowRubble(rc, RobotType.MINER);
            }
        }
        if (RobotPlayer.turnCount > 1800 && RobotPlayer.turnCount < 1900) {
            buildTowardsLowRubble(rc, RobotType.SOLDIER);
        }
        if(miners < 20){
            buildTowardsLowRubble(rc, RobotType.MINER);
        } else if (soldiers < 25){
            buildTowardsLowRubble(rc, RobotType.SOLDIER);
        } else if (builders < 5){
            buildTowardsLowRubble(rc, RobotType.BUILDER);
        }
        else if (miners < soldiers && rc.getTeamLeadAmount(rc.getTeam()) < 5000){
            buildTowardsLowRubble(rc, RobotType.MINER);
        }
        else if (builders < soldiers / 20){
            buildTowardsLowRubble(rc, RobotType.BUILDER);
        }  else if (sages < 1) {
            buildTowardsLowRubble(rc, RobotType.SAGE);
        }
        else {
            int choose = 0+(int)(Math.random() * ((1-0)+1));
            if (choose == 0) {
                buildTowardsLowRubble(rc, RobotType.MINER);
            } else {
                buildTowardsLowRubble(rc, RobotType.SOLDIER);
            }
        }
    }

    static void buildTowardsLowRubble(RobotController rc, RobotType type) throws GameActionException {
        Direction[] dirs = Arrays.copyOf(RobotPlayer.directions, RobotPlayer.directions.length);
        Arrays.sort(dirs, (a, b) -> getRubble(rc, a) - getRubble(rc, b));
        for (Direction d : dirs){
            if(rc.canBuildRobot(type, d)){
                rc.buildRobot(type, d);
                switch(type){
                    case MINER: miners++; break;
                    case SOLDIER: soldiers++;  break;
                    case BUILDER: builders++; break;
                    case SAGE: sages++; break;
                    case WATCHTOWER: watchtowers++; break;
                    case LABORATORY: labs++; break;
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

