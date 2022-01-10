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
         * want this to occur near dense areas, so most likely near archons--both ours and the enemies.
         * after 20 turns of combat near archons, call charge.

        /**
         * when to call fury:
         * immediately call after coming into range of enemy turret
         * ^ we might also want to maximize the effect by getting turrets at once, but enemy will probably space them out.

        /**
         * how to read forecast of anomalies:
         */

    }


}
