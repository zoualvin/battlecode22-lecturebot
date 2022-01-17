package daddydarwin;

import battlecode.common.*;

import static battlecode.common.Clock.getBytecodeNum;

//watchtower should remain in turret mode for most of round; don't move them instead focus on upgrading them; maybe add priority first
strictfp class WatchTowerStrategy{
	static MapLocation loc = null;
    static void runWatchTower(RobotController rc) throws GameActionException {
        // Try to attack someone
		loc = rc.getLocation();
		if (rc.getMode() == RobotMode.PORTABLE && rc.canTransform()) {
			rc.transform();
		}
        int radius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
        if (enemies.length > 0) {
            MapLocation toAttack = enemies[0].location;
            
            for (int i = 0; i < enemies.length; i++) {
        		if(enemies[i].getType() == battlecode.common.RobotType.SOLDIER) {
        			 toAttack = enemies[i].location;
        			 
        			break;
        		} else if (enemies[i].getType() == battlecode.common.RobotType.MINER) {
        			 toAttack = enemies[i].location;
        			break;
        			
        		} else if (enemies[i].getType() == battlecode.common.RobotType.WATCHTOWER) {
        			 toAttack = enemies[i].location;
        			break;
        			
        		} else if (enemies[i].getType() == battlecode.common.RobotType.LABORATORY) {
        			 toAttack = enemies[i].location;
        			break;
        		} else if (enemies[i].getType() == battlecode.common.RobotType.SAGE) {
        			 toAttack = enemies[i].location;
        			break;
        		}
        		
        	}
            
            if (rc.canAttack(toAttack)) {
                rc.attack(toAttack);
            }
			if (getBytecodeNum() == 10000) {
				CLock.yield();
			}
        }
    }
}