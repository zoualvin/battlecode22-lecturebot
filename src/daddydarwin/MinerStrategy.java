package daddydarwin;

import battlecode.common.*;
import daddydarwin.Pathing;

import java.util.Random;

strictfp class MinerStrategy {
    static Direction exploreDir = null;
    /**
     * Run a single turn for a Miner.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runMiner(RobotController rc) throws GameActionException {
        if(rc.getHealth() < 5) {
            RobotPlayer.minerCount--;
        }
        if(exploreDir == null){
            daddydarwin.RobotPlayer.rng.setSeed(rc.getID());
            exploreDir = daddydarwin.RobotPlayer.directions[daddydarwin.RobotPlayer.rng.nextInt(daddydarwin.RobotPlayer.directions.length)];
        }
        rc.setIndicatorString(exploreDir.toString());

        // Try to mine on squares around us.
        MapLocation me = rc.getLocation();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                MapLocation mineLocation = new MapLocation(me.x + dx, me.y + dy);
                // Notice that the Miner's action cooldown is very low.
                // You can mine multiple times per turn!
                while (rc.canMineGold(mineLocation)) {
                    rc.mineGold(mineLocation);
                }
                while (rc.canMineLead(mineLocation) && rc.senseLead(mineLocation) > 1) {
                    rc.mineLead(mineLocation);
                }
            }
        }

        int visionRadius = rc.getType().visionRadiusSquared;
        MapLocation[] nearbyLocations = rc.getAllLocationsWithinRadiusSquared(me, visionRadius);

        MapLocation targetLocation = null;
        int distanceToTarget = Integer.MAX_VALUE;

        // For each nearby location
        for (MapLocation tryLocation : nearbyLocations) {
            // Is there any resource there?
            if (rc.senseLead(tryLocation) > 1 || rc.senseGold(tryLocation) > 0) {
                // Yes! We should consider going here.
                int distanceTo = me.distanceSquaredTo(tryLocation);
                if (distanceTo < distanceToTarget) {
                    targetLocation = tryLocation;
                    distanceToTarget = distanceTo;
                }
            }
        }

        // We have a target location! Let's move towards it.
        if (targetLocation != null) {
            Pathing.walkTowards(rc, targetLocation);
        } else {
            if(rc.canMove(exploreDir)){
                rc.move(exploreDir);
            } else if (!rc.onTheMap(rc.getLocation().add(exploreDir))){
                exploreDir = exploreDir.opposite();
            }
        }

        // Also try to move randomly.
        int directionIndex = daddydarwin.RobotPlayer.rng.nextInt(daddydarwin.RobotPlayer.directions.length);
        Direction dir = RobotPlayer.directions[directionIndex];
        if (rc.canMove(dir)) {
            rc.move(dir);
            System.out.println("I moved!");
        }
        
        

        
        
    }
    


}