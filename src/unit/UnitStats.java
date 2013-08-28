/*
 *	 Herzog3D - 3D Real Time Strategy game.
 *   Copyright (C) 2005  Shannon Smith
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package unit;

public class UnitStats {

	private final int cost;		// Build cost
    private final int buildTime;		// Build time

    private final int armour;			// Armour
    private final int energy;

    private final int ammo;				// Number of ammo rounds
    private final int missiles;			// Number of missiles
    
    private final int speed;			// Movement speed
    private final int turnSpeed;
    
    private final int dammage;			// Dammaged dished out
    
    public UnitStats (int cost,
    				  int buildTime,
    				  int armour,
    				  int energy,
    				  int ammo,
    				  int missiles,
    				  int speed,
    				  int turnSpeed,
    				  int dammage){
    	this.cost = cost;
    	this.buildTime = buildTime;
    	this.armour = armour;
    	this.energy = energy;
    	this.ammo = ammo;
    	this.missiles = missiles;
    	this.speed = speed;
    	this.turnSpeed = turnSpeed;
    	this.dammage = dammage;
    }
    
    public int getCost(){
    	return cost;
    }

    public int getBuildTime(){
    	return buildTime;
    }

    public int getArmour(){
        return armour;
    }
    
    public int getEnergy(){
        return energy;
    }

    public int getAmmo(){
        return ammo;
    }

    public int getMissiles(){
        return missiles;
    }

    public int getSpeed(){
    	return speed;
    }

    public int getTurnSpeed(){
    	return turnSpeed;
    }

    public int getDammage(){
    	return dammage;
    }
    
       
	
}
