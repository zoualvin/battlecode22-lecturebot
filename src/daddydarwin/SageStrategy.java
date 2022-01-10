package daddydarwin;

import battlecode.common.*;

import java.util.Random;

import static daddydarwin.RobotPlayer.directions;
import static daddydarwin.RobotPlayer.rng;

public class SageStrategy {

    static void runSage(RobotController rc) throws GameActionException {
        AnomalyScheduleEntry[] forecast = rc.getAnomalySchedule();
        int radius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
        MapLocation target = null;
        Direction dir = null;

        if (enemies.length > 0 && enemies.length > 10) {
            int chooseType = 0+(int)(Math.random() * ((10-0)+1));
            if (chooseType % 3 == 0 && rc.canEnvision(AnomalyType.ABYSS)) {
                rc.envision(AnomalyType.ABYSS);
            }
            else if (chooseType % 2 == 0 && rc.canEnvision(AnomalyType.CHARGE)) {
                rc.envision(AnomalyType.CHARGE);
            }
            else {
                if (rc.canEnvision(AnomalyType.FURY)) {
                    rc.envision(AnomalyType.FURY);
                }
            }
        }
        else {
            dir = directions[rng.nextInt(directions.length)];
            if (rc.canMove(dir)) {
                rc.move(dir);
                System.out.println("I moved!");}
        }
    }


}
