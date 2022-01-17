package examplefuncsplayer;

import battlecode.common.*;

class Communication {
    private static final int MIN_ENEMY_IDX = 21;
    private static final int NUM_TYPES = 7;

    static void reportAlive(RobotController rc) {
        final int typeIdx = typeToIndex(rc.getType());

        try {
            // Zero out in-progress counts if necessary
            if (rc.readSharedArray(0) != rc.getRoundNum()) {
                final int thisRound = rc.getRoundNum() % 2;
                for (int i = 0; i < NUM_TYPES; i++) {
                    if (rc.readSharedArray(thisRound * NUM_TYPES + i + 1) != 0) {
                        rc.writeSharedArray(thisRound * NUM_TYPES + i + 1, 0);
                    }
                }
                rc.writeSharedArray(0, rc.getRoundNum());
            }

            // Increment alive counter
            final int arrayIdx = (rc.getRoundNum() % 2) * NUM_TYPES + typeIdx + 1;
            rc.writeSharedArray(arrayIdx, rc.readSharedArray(arrayIdx) + 1);
        } catch (GameActionException e) {
            e.printStackTrace();
        }
    }

    static int getAlive(RobotController rc, RobotType type) {
        final int typeIdx = typeToIndex(type);

        // Read from previous write cycle
        final int arrayIdx = ((rc.getRoundNum() + 1) % 2) * NUM_TYPES + typeIdx + 1;
        try {
            return rc.readSharedArray(arrayIdx);
        } catch (GameActionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    static void clearObsoleteEnemies(RobotController rc) {
        for (int i = MIN_ENEMY_IDX; i < GameConstants.SHARED_ARRAY_LENGTH; i++) {
            final int value;
            try {
                value = rc.readSharedArray(i);
            } catch (GameActionException e) {
                continue;
            }
            final MapLocation m = intToLocation(rc, value);
            if (m == null || !rc.canSenseLocation(m)) { // We might want a stronger check than this
                continue;
            }
            try {
                final RobotInfo r = rc.senseRobotAtLocation(m);
                if (r == null || r.team == rc.getTeam()) {
                    rc.writeSharedArray(i, locationToInt(rc, null));
                }
            } catch (GameActionException e) {
                e.printStackTrace();
            }
        }
    }

    static void reportEnemy(RobotController rc, MapLocation enemy) {
        int slot = -1;
        for (int i = MIN_ENEMY_IDX; i < GameConstants.SHARED_ARRAY_LENGTH; i++) {
            final int value;
            try {
                value = rc.readSharedArray(i);
            } catch (GameActionException e) {
                continue;
            }
            final MapLocation m = intToLocation(rc, value);
            if (m == null && slot == -1) {
                slot = i;
            } else if (m != null && enemy.distanceSquaredTo(m) <= 10) {
                return;
            }
        }
        if (slot != -1) {
            try {
                rc.writeSharedArray(slot, locationToInt(rc, enemy));
            } catch (GameActionException e) {
                e.printStackTrace();
            }
        }
    }

    static MapLocation getClosestEnemy(RobotController rc) {
        MapLocation answer = null;
        for (int i = MIN_ENEMY_IDX; i < GameConstants.SHARED_ARRAY_LENGTH; i++) {
            final int value;
            try {
                value = rc.readSharedArray(i);
            } catch (GameActionException e) {
                continue;
            }
            final MapLocation m = intToLocation(rc, value);
            if (m != null && (answer == null || rc.getLocation().distanceSquaredTo(m) < rc.getLocation().distanceSquaredTo(answer))) {
                answer = m;
            }
        }
        return answer;
    }

    private static int typeToIndex(RobotType type) {
        switch (type) {
            case ARCHON:     return 0;
            case MINER:      return 1;
            case SOLDIER:    return 2;
            case LABORATORY: return 3;
            case WATCHTOWER: return 4;
            case BUILDER:    return 5;
            case SAGE:       return 6;
            default: throw new RuntimeException("Unknown type: " + type);
        }
    }

    private static int locationToInt(RobotController rc, MapLocation m) {
        if (m == null) {
            return 0;
        }
        return 1 + m.x + m.y * rc.getMapWidth();
    }

    private static MapLocation intToLocation(RobotController rc, int m) {
        if (m == 0) {
            return null;
        }
        m--;
        return new MapLocation(m % rc.getMapWidth(), m / rc.getMapWidth());
    }
}