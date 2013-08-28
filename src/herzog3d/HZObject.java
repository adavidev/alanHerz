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

import model.Model;
import model.ModelState;
import physics.Shell;
import resource.Material;
import unit.Unit;
import util.ReadableVector3f;
import util.Vector3f;

public abstract class HZObject {

    protected Shell shell;
    protected boolean active;
    protected final Vector3f pos;
    protected float dir;
    protected float radius;
    protected Model model;
    protected ModelState state;
    protected Material skin;
    
    public HZObject(ReadableVector3f pos){
        active = true;
        this.pos = new Vector3f(pos);
        dir = 0;
        radius = 0.6f;
    }
    
    public float getColRadius(){
    	return radius;
    }
    
    public void setModel(Model model){
    	this.model = model;
    }
    
    public void setSkin(Material skin){
    	this.skin = skin;
    }
    
    public void setState(ModelState state){
        this.state = state;
    }

    public void setActive(boolean active){
        this.active = active;
    }
    
    public Vector3f getPos(){
    	return pos;
    }
    
    public void setPos(float x, float y, float z){
    	pos.set(x,y,z);
    }
    
    public void setPos(ReadableVector3f v){
    	pos.set(v);
    }
    
    public void move(float x, float y, float z){
    	pos.add(x,y,z);
    }
    
    public void move(Vector3f move){
    	pos.add(move);
    }

    public Vector3f intersects(HZObject other){
    	return shell.intersects(other.shell);
    }
    
    public float getDir(){
    	return dir;
    }
    
    public float getRadius(){
    	return radius;
    }
    
    public boolean isActive(){
        return active;
    }
    
    public Vector3f testCollision(ReadableVector3f v1, ReadableVector3f v2){
    	return null;
    }
    
}
