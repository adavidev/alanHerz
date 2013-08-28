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
package physics;

import org.lwjgl.opengl.GL11;

import util.ReadableVector3f;
import util.Vector3f;
import util.GameMath.MapDir;


public class BoxShell extends Shell {

	private float length;
	private float height;
	
	public BoxShell(ReadableVector3f pos, float length, float height){
		super(pos);
		this.length = length;
		this.height = height;
	}
	
	public float getLength(){
		return length;
	}
	
	public float getHeight(){
		return height;
	}

	public Vector3f testCollision(ReadableVector3f v1, ReadableVector3f v2) {
		if (contains(v1) || contains(v2)){
			return new Vector3f(v1);
		} else {
			return null;
		}
	}

	public boolean contains(ReadableVector3f v){
		return v.getX() > (pos.getX() - length/2.0f) && v.getX() < (pos.getX() + length/2.0f) &&
			   v.getY() > (pos.getY() - length/2.0f)  && v.getY() < (pos.getY() + length/2.0f) && 
	 	       v.getZ() > pos.getZ() && v.getZ() < (pos.getZ() + height);
	}

	public Vector3f intersects(Shell shell) {
		throw new UnsupportedOperationException("Box col not implemented yet!");
	}
	
	public MapDir getDirection(ReadableVector3f p){
		float dx = p.getX() - pos.getX();
		float dy = p.getY() - pos.getY();
		if (Math.abs(dx) > Math.abs(dy)){
			if (Math.abs(dy) <= length/2.0f){
				return ((dx < 0)?MapDir.WEST:MapDir.EAST);
			} else {
				if (dy < 0){
					return ((dx < 0)?MapDir.SOUTH_WEST:MapDir.SOUTH_EAST);
				} else {
					return ((dx < 0)?MapDir.NORTH_WEST:MapDir.NORTH_EAST);
				}
			}
		} else {
			if (Math.abs(dx) <= length/2.0f){
				return ((dy < 0)?MapDir.SOUTH:MapDir.NORTH);
			} else {
				if (dx < 0){
					return ((dy < 0)?MapDir.SOUTH_WEST:MapDir.NORTH_WEST);
				} else {
					return ((dy < 0)?MapDir.SOUTH_EAST:MapDir.NORTH_EAST);
				}
			}
		}
	}
	
	public void draw(){
		float len = length/2;
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3f(-len,-len,height);
		GL11.glVertex3f(len,-len,height);
		GL11.glVertex3f(len,len,height);
		GL11.glVertex3f(-len,len,height);
		GL11.glVertex3f(-len,-len,0);
		GL11.glVertex3f(len,-len,0);
		GL11.glVertex3f(len,len,0);
		GL11.glVertex3f(-len,len,0);
		GL11.glEnd();
	}
	
}
