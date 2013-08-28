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

import herzog3d.Game;
import herzog3d.HZObject;
import herzog3d.Player;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import physics.Shell;
import util.GameMath;
import util.ReadableVector3f;
import util.Vector3f;
import ai.UnitAI;
import ai.UnitAI.UnitMove;
import ai.UnitAI.UnitTurn;
import effects.Projectile;

/**
 * @author Shannon Smith
 */
public abstract class Unit extends HZObject {

    private static final int REFIRE_RATE = 1;
    private static final int MAX_SPEED = 10;
    
    protected UnitAI    ai;
    protected Player    owner;
    protected UnitStats stats;
    
    protected int energy;
    protected int armour;
    protected int missiles;
    protected int ammo;
    private String name;

    public Unit(Player owner, String name, UnitStats stats){
    	super(new Vector3f());
    	this.owner = owner;
    	this.stats = stats;        

    	this.name = name;
    	energy = stats.getEnergy();
    	armour = stats.getArmour();
    	ammo = stats.getAmmo();
    	missiles = stats.getMissiles();
    }
    
    public boolean isFlying(){
    	return false;
    }
    
    public void setStats(UnitStats stats){
    	this.stats = stats;
    }
    
    public String getName(){
    	return name;
    }
    
    public void setOwner(Player owner){
    	this.owner = owner;
    }
    
    public Player getOwner(){
    	return owner;
    }
    
    public void dammage(int dammage){
    	armour -= dammage;
    }
    
    public void setAI(UnitAI ai){
        this.ai = ai;
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

    public void update(Game game, float timeStep){
    	Vector3f move = new Vector3f(0,0,0);
    	ai.update(game,timeStep);
    	if (ai.getMove() != UnitMove.MOVE_STOP){
    		move.x = (float)(stats.getSpeed()*timeStep*Math.cos(dir)*ai.getMove().value/MAX_SPEED);
    		move.y = (float)(stats.getSpeed()*timeStep*Math.sin(dir)*ai.getMove().value/MAX_SPEED);
    	}
    	if (ai.getTurn() != UnitTurn.TURN_STOP){
    		dir = GameMath.normaliseAngle(dir + stats.getTurnSpeed()*timeStep*ai.getTurn().value);
    	}
    	if (isFlying()){
    		pos.z = 5.0f;
    	} else {
    		pos.z = game.getMap().getHeightAt(pos.x,pos.y) + 0.1f;
    	}
    	move(move);
    	if (ai.getFireCannon()){
    		fire(game);
    	}
    }
    
    public Shell getShell(){
    	return shell;
    }
    
    public Vector3f intersects(Unit unit){
    	return shell.intersects(unit.getShell());
    }
    
    public void fire(Game game){
        Vector3f vel = new Vector3f();
        vel.x = (float)Math.cos(getFireDir())*10;
        vel.y = (float)Math.sin(getFireDir())*10;
        vel.z = 0;
       	game.addEffect(new Projectile(game, this, getFirePos(), vel));
    }
    
    public abstract float getFireDir();
    
    public abstract Vector3f getFirePos();
    
    public void draw(){
    	Color playerColour = owner.getColour();
    	GL11.glColor3f(playerColour.getRed()/255.0f, playerColour.getGreen()/255.0f, playerColour.getBlue()/255.0f);
    	skin.bind();
        GL11.glPushMatrix();
        GL11.glTranslatef(pos.x,pos.y,pos.z);
        GL11.glRotatef(dir*GameMath.DEGREES_PER_RADIAN,0,0,1);
//        shell.draw();
        model.draw(state);
        GL11.glPopMatrix();
    }
    
    public Vector3f testCollision(ReadableVector3f v1, ReadableVector3f v2){
    	return shell.testCollision(v1,v2);
    }
    
}
