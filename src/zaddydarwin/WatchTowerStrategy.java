package zaddydarwin;

import battlecode.common.*;

import java.util.Random;

import static battlecode.common.Clock.getBytecodeNum;

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
                Clock.yield();
            }
        }

    }
}

/**
 * int radius = rc.getType().actionRadiusSquared;
 *         Team opponent = rc.getTeam().opponent();
 *         RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
 *         if (enemies.length > 0) {
 *             MapLocation toAttack = enemies[0].location;
 *             if (rc.canAttack(toAttack)) {
 *                 rc.attack(toAttack);
 *             }
 *         }
 **/
