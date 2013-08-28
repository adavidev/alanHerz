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
package effects;

import herzog3d.Game;
import map.Base;
import map.GameMap;

import org.lwjgl.opengl.GL11;

import resource.Material;
import unit.Unit;
import util.ReadableVector3f;
import util.Vector3f;

public class Projectile extends Effect {

	private Unit firer;
    private Vector3f vel;
    private int dammage;
    private Material material;
    
    public Projectile(Game game, Unit firer, ReadableVector3f pos, Vector3f vel){
    	super(pos);
    	this.firer = firer;
        this.vel = new Vector3f(vel);
        this.material = game.getResources().getMaterial("white");
        this.dammage = 10;
    }
    
    public void update(Game g, float step){
        Vector3f oldPos = new Vector3f(pos);
        pos.add(step*vel.x, step*vel.y, step*vel.z);
        Vector3f col;
        for (Unit unit : g.getUnits()){
        	if (unit != firer){
	        	col = unit.testCollision(oldPos,pos);
	            if (col != null){
	                setActive(false);
	                Vector3f explosionPos = new Vector3f(col);
	                explosionPos.sub(unit.getPos());
	                g.addEffect(new Explosion(g.getResources(), col,explosionPos));
	                unit.dammage(dammage);
	                return;
	            }
        	}
        }
        
        for(Base base : g.getBases()){
        	col = base.testCollision(oldPos,pos);
        	if (col != null){
                setActive(false);
                Vector3f v = new Vector3f();
                g.addEffect(new Explosion(g.getResources(), col, v));
               	base.dammage(dammage);
        	}
        }
        GameMap map = g.getMap();
        col = map.testCollision(oldPos,pos);
        if (col != null){
            setActive(false);
            Vector3f v = new Vector3f();
            map.getNormalAt(col.x,col.y,v);
            g.addEffect(new Explosion(g.getResources(), col, v));
        } else if (!map.isInMap(pos.x,pos.y)){
            setActive(false);
        }
    }

    public Vector3f getPos(){
        return pos;
    }
    
    public Vector3f getVel(){
        return vel;
    }
    
    public void draw(){
    	GL11.glDisable(GL11.GL_LIGHTING);
    	material.bind();
    	GL11.glColor3f(1.0f,1.0f,1.0f);
        GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex3f(pos.x,pos.y,pos.z);
            GL11.glVertex3f(pos.x + vel.x/10, pos.y + vel.y/10, pos.z + vel.z/10);
        GL11.glEnd();
    	GL11.glEnable(GL11.GL_LIGHTING);
    }

}
