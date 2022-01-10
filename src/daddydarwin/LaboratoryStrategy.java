package daddydarwin;

import battlecode.common.*;

public class LaboratoryStrategy {
    public static void runLaboratory(RobotController rc) throws GameActionException {
        if (rc.getTeamLeadAmount(rc.getTeam()) > 7000 && rc.canTransmute()) {
            rc.transmute();
        }
    }

}