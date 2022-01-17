package daddydarwin;

import battlecode.common.*;
import daddydarwin.Pathing;

import java.util.Random;
import daddydarwin.WatchTowerStrategy;
import daddydarwin.ArchonStrategy;

import static battlecode.common.Clock.getBytecodeNum;

// implement mutation methods to mutate the watchtowers to level 3 if applicable
strictfp class BuilderStrategy {

    static int turn = 0;
    static int numWatchTowers = 0;
    static int numLabs = 0;
    static void runBuilder(RobotController rc) throws GameActionException{

        turn ++;
        //repair neighboring building
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

        int directionIndex = daddydarwin.RobotPlayer.rng.nextInt(daddydarwin.RobotPlayer.directions.length);
        Direction dir = RobotPlayer.directions[directionIndex];
        if (rc.canMove(dir)) {
            rc.move(dir);
            System.out.println("I moved!");
        }

        if(rc.getTeamLeadAmount(rc.getTeam()) > 550 && rc.canBuildRobot(RobotType.LABORATORY, dir)){
            rc.buildRobot(RobotType.LABORATORY, dir);
            numLabs++;
        } else if(rc.getTeamLeadAmount(rc.getTeam()) > 1000 && rc.canBuildRobot(RobotType.WATCHTOWER, dir) && numWatchTowers <= 2){ // && numWatchTower per archon <= 2
            int xcoord = ArchonStrategy.lol.x + 4;
            int ycoord = ArchonStrategy.lol.y - 3;
            MapLocation goToArchon = new MapLocation(xcoord, ycoord);
            Pathing.walkTowards(rc,goToArchon);
            rc.buildRobot(RobotType.WATCHTOWER, dir);
            numWatchTowers++;
            // implement code to build the robot in space near the archon
        }

        if (getBytecodeNum() == 7500) {
            Clock.yield();
        }
        //transmutation methods if there is at least 200pB mutate both watchtowers to level 2; same for laboratory

        if (rc.getTeamLeadAmount(rc.getTeam()) > 500 && rc.canMutate(WatchTowerStrategy.loc)) {
            Pathing.walkTowards(rc, WatchTowerStrategy.loc);
            rc.mutate(WatchTowerStrategy.loc);
        }



    }
}