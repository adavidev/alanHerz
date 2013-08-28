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
import unit.Unit;

/**
 * @author Shannon Smith
 */
public class RandomAI extends UnitAI {

	private UnitMove move;
	private UnitTurn turn;
	private UnitTurn turretTurn;
	private boolean fire;
	
	public RandomAI(Unit unit) {
		super(unit);
		move = UnitMove.values()[(int)(Math.random()*3)]; 
		turn = UnitTurn.values()[(int)(Math.random()*3)];
		turretTurn = UnitTurn.values()[(int)(Math.random()*3)];
		fire = false;
	}
	
	public void update(Game game, float timeStep){
		if (Math.random() < 0.01){
			move = UnitMove.values()[(int)(Math.random()*3)]; 
		}
		if (Math.random() < 0.01){
			turn = UnitTurn.values()[(int)(Math.random()*3)]; 
		}
		if (Math.random() < 0.01){
			turretTurn = UnitTurn.values()[(int)(Math.random()*3)]; 
		}
		if (Math.random() < 0.01){
			fire = true; 
		} else {
			fire = false;
		}
		
	}

	public UnitTurn getTurretTurn(){
		return turretTurn;
	}
	
	public boolean getFireCannon(){
		return fire;
	}
	
	public UnitMove getMove(){
		return move;
	}

	public UnitTurn getTurn(){
		return turn;
	}

    
}
