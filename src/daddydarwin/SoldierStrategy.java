package daddydarwin;

import battlecode.common.*;

import static daddydarwin.RobotPlayer.directions;
import static daddydarwin.RobotPlayer.rng;

public class SoldierStrategy {

    static void runSoldier(RobotController rc) throws GameActionException {
        // Try to attack someone
        int radius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
        MapLocation target = null;
        Direction dir = null;
        
        if (enemies.length > 0) {
        	MapLocation toAttack = enemies[0].location;
        	dir = rc.getLocation().directionTo(enemies[rng.nextInt(enemies.length)].location);
        	
        	//attack priority: enemy archons, soldiers, watchtowers, labs, then whatever's closest. take into account rubble later.
        	for (int i = 0; i < enemies.length; i++) {
        		if(enemies[i].getType() == battlecode.common.RobotType.ARCHON) {
        			 toAttack = enemies[i].location;
        			 target = enemies[i].location;
        			break;
        		} else if (enemies[i].getType() == battlecode.common.RobotType.SOLDIER) {
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
        		
        
        }

    
        
        //if archon is sensed, move towards it with pathing
        //if not, move in direction of random enemy on the map.
        //if no enemies detected, move in a random direction
        if(target != null) {
        	Pathing.walkTowards(rc, target);
        } else if (dir != null){
        	if (rc.canMove(dir)) {
        		rc.move(dir);
        		System.out.println("I moved!");
        	}
        } else {
        	dir = directions[rng.nextInt(directions.length)];
        	if (rc.canMove(dir)) {
                rc.move(dir);
                System.out.println("I moved!");}
        }
        
    }
}
