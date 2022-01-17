package daddydarwin;

import battlecode.common.*;

import static battlecode.common.Clock.getBytecodeNum;

public class LaboratoryStrategy {
    public static void runLaboratory(RobotController rc) throws GameActionException {
        
    	//check how many bots are in vision radius. if over 30, dont transmute.
    	int visionRadius = rc.getType().visionRadiusSquared;
    	RobotInfo [] nearbyBots = rc.senseNearbyRobots(visionRadius, rc.getTeam());

    	if (rc.getTeamLeadAmount(rc.getTeam()) > 200 && rc.canTransmute() && nearbyBots.length < 30) {
            rc.transmute();
        }
        if (getBytecodeNum() == 5000) {
            Clock.yield();
        }
    }

}