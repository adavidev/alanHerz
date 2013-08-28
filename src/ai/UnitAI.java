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
package ai;

import herzog3d.Game;

import org.lwjgl.opengl.GL11;

import resource.Material;
import unit.Unit;

public abstract class UnitAI {

	public enum UnitAction {
		MOVE,
		TURN,
		TURRET_TURN,
		FIRE_CANNON,
		FIRE_MISSILE,
		RESUPPLY,
	}
	
	public enum UnitTurn {
		TURN_LEFT(1),
		TURN_RIGHT(-1),
		TURN_STOP(0);

		public final int value;
		
		private UnitTurn(int value){
			this.value = value;
		}
	}
	
	public enum UnitMove {
		MOVE_FORWARD(1),
		MOVE_BACKWARD(-1),
		MOVE_STOP(0);

		public final int value;
		
		private UnitMove(int value){
			this.value = value;
		}
	}
	
	protected Unit unit;
	protected Material icon;
	
	public UnitAI(Unit unit){
		this.unit = unit;
	}
	
	public void setUnit(Unit unit){
		this.unit = unit;
	}
	
    public void update(Game game, float step){
    	
    }
    
    public UnitTurn getTurn(){
    	return UnitTurn.TURN_STOP;
    }
    
    public UnitTurn getTurretTurn(){
    	return UnitTurn.TURN_STOP;
    }
    
    public UnitMove getMove(){
    	return UnitMove.MOVE_STOP;
    }
    
    public boolean getFireCannon(){
    	return false;
    }
    
    public boolean getLaunchMissile(){
    	return false;
    }
    
    public boolean getResupply(){
    	return false;
    }
    
    public void draw(){
    	icon.bind();
    	GL11.glBegin(GL11.GL_QUADS);
    	GL11.glTexCoord2f(0,0);
    	GL11.glVertex2i(0,0);
    	GL11.glTexCoord2f(0,1);
    	GL11.glVertex2i(0,64);
    	GL11.glTexCoord2f(1,1);
    	GL11.glVertex2i(64,64);
    	GL11.glTexCoord2f(1,0);
    	GL11.glVertex2i(64,0);
    	GL11.glEnd();
    }
}
