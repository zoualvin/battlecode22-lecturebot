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
        if(RobotPlayer.minerCount < 20){
            buildTowardsLowRubble(rc, RobotType.MINER);
        } else if (RobotPlayer.soldierCount < 35){
            buildTowardsLowRubble(rc, RobotType.SOLDIER);
        } else if (builders < 8){
            buildTowardsLowRubble(rc, RobotType.BUILDER);
        }
        else if (RobotPlayer.minerCount < RobotPlayer.soldierCount * 9/10 && rc.getTeamLeadAmount(rc.getTeam()) < 5000){
            buildTowardsLowRubble(rc, RobotType.MINER);
        }
        else if (builders < RobotPlayer.soldierCount / 10){
            buildTowardsLowRubble(rc, RobotType.BUILDER);
        }  else if (sages < 3) {
            buildTowardsLowRubble(rc, RobotType.SAGE);
        }
        else {
            buildTowardsLowRubble(rc, RobotType.SOLDIER);
        }
    }

    static void buildTowardsLowRubble(RobotController rc, RobotType type) throws GameActionException {
        Direction[] dirs = Arrays.copyOf(RobotPlayer.directions, RobotPlayer.directions.length);
        Arrays.sort(dirs, (a, b) -> getRubble(rc, a) - getRubble(rc, b));
        for (Direction d : dirs){
            if(rc.canBuildRobot(type, d)){
                rc.buildRobot(type, d);
                switch(type){
                    case MINER: miners++; RobotPlayer.minerCount++; break;
                    case SOLDIER: soldiers++; RobotPlayer.soldierCount++; break;
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

