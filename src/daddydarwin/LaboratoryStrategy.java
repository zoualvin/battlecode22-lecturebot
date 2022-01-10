package daddydarwin;

import battlecode.common.*;

public class LaboratoryStrategy {
    public static void runLaboratory(RobotController rc) throws GameActionException {
        if (rc.getTeamLeadAmount(rc.getTeam()) > 200 && rc.canTransmute()) {
            rc.transmute();
        }
    }

}