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

        if(rc.getTeamLeadAmount(rc.getTeam()) > 550 && rc.canBuildRobot(RobotType.LABORATORY, dir)){
            rc.buildRobot(RobotType.LABORATORY, dir);
            numLabs++;
        } else if(rc.getTeamLeadAmount(rc.getTeam()) > 180 && rc.canBuildRobot(RobotType.WATCHTOWER, dir)){ // && numWatchTower per archon <= 2
            int xcoord = zaddydarwin.ArchonStrategy.lol.x + 4;
            int ycoord = zaddydarwin.ArchonStrategy.lol.y - 3;
            MapLocation goToArchon = new MapLocation(xcoord, ycoord);
            zaddydarwin.Pathing.walkTowards(rc,goToArchon);
            rc.buildRobot(RobotType.WATCHTOWER, dir);
            numWatchTowers++;
            // implement code to build the robot in space near the archon
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