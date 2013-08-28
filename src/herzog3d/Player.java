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
package herzog3d;

import java.awt.Color;

import map.Base;
import unit.Mech;
import ai.UnitAI;

/**
 * @author Shannon Smith
 */
public class Player extends UnitAI {

	private String name;
    private UnitTurn turn;
    private UnitMove move;
    private boolean fire;
    private boolean transform;
    private Mech mech;
    private Base homeBase;
    private int credits;
    private UnitOrder order;
    private Color colour;
    
    public Player(String name, Mech unit){
    	super(unit);
        this.name = name;
        move = UnitMove.MOVE_STOP;
        turn = UnitTurn.TURN_STOP;
        colour = new Color(name.hashCode());
    }
    
    public UnitOrder getUnitOrder(){
    	return order;
    }
    
    public void setUnitOrder(UnitOrder order){
    	this.order = order;
    }
    
    public int getCredits(){
    	return credits;
    }
    
    public Base getHomeBase(){
        return homeBase;
    }
    
    public Mech getMech(){
        return mech;
    }

    public void setColour(Color colour){
    	this.colour = colour;
    }
    
    public Color getColour(){
    	return colour;
    }
    
    public void setHomeBase(Base homeBase){
        this.homeBase = homeBase;
    }
    
    public void setMech(Mech mech){
        this.mech = mech;
    }
    
    
    public UnitTurn getTurn(){
        return turn;
    }
    
    public UnitMove getMove(){
        return move;
    }
    
    public void setTurn(UnitTurn turn){
        this.turn = turn;
    }
    
    public void setMove(UnitMove move){
        this.move = move;
    }
    
    public void setTransform(boolean transform){
        this.transform = transform;
    }
    
    public void setFire(boolean fire){
        this.fire = fire;
    }
    
    public boolean getTransform(){
        if (transform){
            transform = false;
            return true;
        } else {
            return false;
        }
    }

    public boolean getFireCannon() {
        if (fire){
            fire = false;
            return true;
        } else {
            return false;
        }
    }
    
    public int hashCode(){
    	return name.hashCode();
    }
    
}
