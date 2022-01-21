package zaddydarwin;

import battlecode.common.*;
import zaddydarwin.ArchonStrategy;
import zaddydarwin.Pathing;
import java.util.Random;

strictfp class BuilderStrategy {

    static int turn = 0;
    static int numLabs = 0;
    static int numWatchTowers = 0;
    static void runBuilder(RobotController rc) throws GameActionException{

        turn ++;
        //repair neighboring buiulding
        MapLocation me = rc.getLocation();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                MapLocation repLocation = new MapLocation(me.x + dx, me.y + dy);
                while (rc.canRepair(repLocation)) {
                    rc.repair(repLocation);
                }
            }
        }

        RobotInfo[] robots = rc.senseNearbyRobots();
        int distance = Integer.MAX_VALUE;
        MapLocation targetLocation = null;
        for(RobotInfo robot : robots){
            if(robot.getTeam().equals(rc.getTeam()) && robot.type.isBuilding()  &&robot.health < robot.type.getMaxHealth(robot.level)){
                if(rc.getLocation().distanceSquaredTo(robot.location) < distance){
                    targetLocation = robot.getLocation();
                    distance = rc.getLocation().distanceSquaredTo(robot.location);
                }
            }
        }

        if (targetLocation != null) {
            Pathing.walkTowards(rc, targetLocation);
        }

        int directionIndex = RobotPlayer.rng.nextInt(RobotPlayer.directions.length);
        Direction dir = RobotPlayer.directions[directionIndex];
        if (rc.canMove(dir)) {
            rc.move(dir);
            System.out.println("I moved!");
        }

        if (rc.getTeamLeadAmount(rc.getTeam()) > 200) {
            int choose = (int)(Math.random()*(5-0+1)) + 0;
            if (choose == 3) {
                if (rc.canBuildRobot(RobotType.LABORATORY, dir)) {
                    rc.buildRobot(RobotType.LABORATORY, dir);
                }
            }
            else {
                if (rc.canBuildRobot(RobotType.WATCHTOWER, dir)) {
                    rc.buildRobot(RobotType.WATCHTOWER, dir);
                }
            }
        }
    }
}

/**
 * if(rc.getTeamLeadAmount(rc.getTeam()) > 10000 && turn % 150 == 0 && rc.canBuildRobot(RobotType.LABORATORY, dir)){
 *             rc.buildRobot(RobotType.LABORATORY, dir);
 *         } else if(rc.getTeamLeadAmount(rc.getTeam()) > 7000 && turn % 100 == 0 && rc.canBuildRobot(RobotType.WATCHTOWER, dir)){
 *             rc.buildRobot(RobotType.WATCHTOWER, dir);
 *         }
 */