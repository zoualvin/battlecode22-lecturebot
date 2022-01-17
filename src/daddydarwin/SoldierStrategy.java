package daddydarwin;

import battlecode.common.*;

import static battlecode.common.Clock.getBytecodeNum;
import static daddydarwin.RobotPlayer.directions;
import static daddydarwin.RobotPlayer.rng;
import daddydarwin.ArchonStrategy;
public class SoldierStrategy {
	// have a separate method for defense and offense soldiers.
	// keeping track of identities over turns is hard. maybe make it so that if soldier is certain distance from 
	//archon then it moves randomly
	//but if soldier moves outside a certain radius from archon it becomes offensive and goes to explore! the main difference
	//will be their movement. defense will move randomly and not target archons, but offense will (eventually)
	// move to explore
	
	//gives soldier identity of offense or defense.
	//returns 69 = offense returns 420 = defense

	/**
	static int identifySoldier(RobotController rc) {
		RobotInfo[] friends = rc.senseNearbyRobots(4, rc.getTeam()); //get all robots in 4 space radius
		boolean archon = false;
		if(friends.length != 0) {
			for(RobotInfo friend: friends) {
				if(friend.getType() == battlecode.common.RobotType.ARCHON) {
					archon = true;
					break;
				}
			}
		} else {
			return 69;
		}
		
		if(archon = true) {
			return 420;
		}
		else {return 69;}
		
	}
	
	
	//run a defense soldier
	static void defenseMove(RobotController rc) throws GameActionException {
		//on defense, just move randomly.
		Direction dir = directions[rng.nextInt(directions.length)];
        	if (rc.canMove(dir)) {
                rc.move(dir);
                System.out.println("moved randomly");}
        }
	
	
	//run an offense soldier
	static void offenseMove(RobotController rc, MapLocation ml, RobotInfo[] enemies) throws GameActionException {
		Direction dir = rc.getLocation().directionTo(enemies[rng.nextInt(enemies.length)].location);
		MapLocation targetLocation = null;
        int locationInt = rc.readSharedArray(0);
        if(locationInt != 0) {targetLocation = RobotPlayer.convertToLocation(locationInt);}
       
        //if attacking an archon, stay in place!!!
        //if there's an archon to attack, move towards it
        if(targetLocation != null) {
        	Pathing.walkTowards(rc, targetLocation);
        	System.out.println("moved towards archon!");
        } else if (dir != null){ //if no archon sensed yet, but enemies detected, move towards a random enemy
        	if (rc.canMove(dir)) {
        		rc.move(dir);
        		System.out.println("moved towards enemy!");
        	}
        } else { //MOVE RANDOMLY. THIS IS IMPORTANT EDIT LATER
        	dir = directions[rng.nextInt(directions.length)];
        	if (rc.canMove(dir)) {
                rc.move(dir);
                System.out.println("moved randomly");}
        }
		
	}
	**/
	
	
	

    static void runSoldier(RobotController rc) throws GameActionException {
        // Try to attack someone
		int radius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
		MapLocation target = null; //newcode
		Direction dir = null; //newcode
		//RobotInfo archon = null;
        
        //int identity = identifySoldier(rc);
        
        
        if (enemies.length > 0) {
        	MapLocation toAttack = enemies[0].location;
        	dir = rc.getLocation().directionTo(enemies[rng.nextInt(enemies.length)].location); //newcode
        	
        	//attack priority: enemy archons, soldiers, watchtowers, labs, then whatever's closest. take into account rubble later.
        	for (int i = 0; i < enemies.length; i++) {
        		if(enemies[i].getType() == battlecode.common.RobotType.SOLDIER) {
        			 toAttack = enemies[i].location;
        			break;
        		} else if (enemies[i].getType() == battlecode.common.RobotType.ARCHON) {
        			//archon = enemies[i];
        			toAttack = enemies[i].location;
					target = enemies[i].location;

					// RobotPlayer.addLocationToArray(rc, toAttack);
        			 //System.out.println("found an archon!");
        			 
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
        		}
        		
        	}
           
        	if (rc.canAttack(toAttack)) {
                rc.attack(toAttack);
            }
			if(target != null) {
				Pathing.walkTowards(rc, target);
			} else if (dir != null){
				if (rc.canMove(dir)) {
					rc.move(dir);
					System.out.println("I moved!");
				}
			} else {
				dir = directions[rng.nextInt(directions.length)];
				if (rc.canMove(dir)) {
					rc.move(dir);
					System.out.println("I moved!");}
			}
			/**
			if (rc.getHealth() < 6) {
				System.out.println("yo im dead");
				//int soldierCount = rc.readSharedArray(61);
				//soldierCount -= 1;
				rc.writeSharedArray(61, rc.readSharedArray(61)-1);
				//System.out.println("there are " +rc.readSharedArray(61)+ " soldiers now!");//to check updated numbers

			}
			 **/
			if (rc.getHealth() < 6) {
				System.out.println("yo im dead");
				ArchonStrategy.soldiers -= 1;

			}
			if (getBytecodeNum() == 10000) {
				Clock.yield();
			}
        		
        
        }
        /**
        //MOVE
        
        if(identity == 420) {
        	defenseMove(rc);
        } else {
        	offenseMove(rc, rc.getLocation(), enemies);
        }
        
        
        
      //if the archon is about to die, assume it's dead, and change array accordingly.
        //this part assumes that if the soldier's near a very low health archon, that archon's being attacked at the moment
        
        if(archon.getHealth() < 6) { 
        	int locationInt = RobotPlayer.convertToOneInt(archon.getLocation());
        	int index = 0;
        	int elementAfter = 0;
        	int lastIndex = 0;
        	
        	for(int j = 0; j < 9; j++) {
   			 	int loc = rc.readSharedArray(j);
   			 	//if the locations match, note the index
   			 	if(loc == locationInt) {index = j;}
   			 	//once you hit end of location list in shared array, exit and note index
   			 	if (loc == 0) {
   			 		lastIndex = j;
   			 		break;
   			 	}
   			  
        	}
        	//if location list isnt empty
        	if(lastIndex != 0) {
        		//if the location element is last one on list
	        	if(lastIndex == index + 1) {
	        		rc.writeSharedArray(index, 0);
	        		System.out.println("archon almost dead!");
	        	} else if (index == 0){ //if dying archon is first and only one on list, just erase it
	        		rc.writeSharedArray(0, 0);
	        		System.out.println("archon almost dead!");
	        	} else {
	        		int movedElement = rc.readSharedArray(lastIndex-1); //take last one on list
	        		rc.writeSharedArray(index, movedElement); //replace dying archon location with ^^
	        		System.out.println("archon almost dead!");
	        	}
        	}
		 **/
        	
        }

      
        
        /**
        if (getBytecodeNum() == 10000) {
			Clock.yield();
		}
		 **/
        
        
        //report death sequence
	/**
        if (rc.getHealth() < 6) {
        	System.out.println("yo im dead");
        	int soldierCount = rc.readSharedArray(61);
        	soldierCount--;
        	rc.writeSharedArray(61, soldierCount);
        	System.out.println("there are " +rc.readSharedArray(61)+ " soldiers now!");//to check updated numbers
        	
        }
	 **/

    }
    
    
    
  //have ALL soldier and miners report enemy archon locations if they find it. CHECK FOR DUPLICATES. DONE
    //have OFFENSE soldiers automatically target archons if the array reports them. start with the first index. DONE
    //MAYBE shave ALL soldiers store the enemy locations in a static array every turn????
    //have ALL soldiers detect when archon is killed and modify array accordingly so that all remaining locations move to the front of array DONE?
    //have ALL soldiers/miners report their death EVERY turn if one dies and it's not reported, that would be bad. CHECK
    
    //if archon is sensed, move towards it with pathing
    //if not, move in direction of random enemy on the map.
    //if no enemies detected, move in a direction as given by an epic function that sends them to unexplored/faraway areas of map
    //maybe make a function that can copy array if needed in robotplayer
    //maybe have all of the array writing functions take into account number of archons on map before doing for loop
    
    

