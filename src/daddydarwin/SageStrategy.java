package daddydarwin;

import battlecode.common.*;

import java.util.Random;

public class SageStrategy {

    static void runSage(RobotController rc) throws GameActionException {
        AnomalyScheduleEntry[] forecast = rc.getAnomalySchedule();
        /**
         * when to call an abyss:
         */
        int chooseType = 0+(int)(Math.random() * ((10-0)+1));
        if (chooseType % 3 == 0) {
            rc.envision(AnomalyType.ABYSS);
        }
        else if (chooseType % 2 == 0) {
            rc.envision(AnomalyType.CHARGE);
        }
        else {
            rc.envision(AnomalyType.FURY);
        }

        /**
         * when to call charge:
         */

        /**
         * when to call fury:
         */

        /**
         * how to read forecast of anomalies:
         */

    }


}
