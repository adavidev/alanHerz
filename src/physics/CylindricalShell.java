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
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Disk;

import resource.Material;
import util.GameMath;
import util.ReadableVector3f;
import util.Vector3f;
import util.GameMath.MapDir;

public class CylindricalShell extends Shell {

	private float height;
	private float radius;
	private Cylinder cylinder;
	private Disk disk;
	
	public CylindricalShell(ReadableVector3f pos, float radius, float height) {
		super(pos);
		this.height = height;
		this.radius = radius;
		cylinder = new Cylinder();
		disk = new Disk();
	}

	public void setRadius(float radius){
		this.radius = radius;
	}
	
	public void setHeight(float height){
		this.height = height;
	}
	
	public Vector3f intersects(Shell shell) {
		if (shell == this){
			return null;
		}
		if (shell instanceof CylindricalShell){
			return intersects((CylindricalShell)shell);
		} else if (shell instanceof BoxShell){
			return intersects((BoxShell)shell);
		} else {
			throw new UnsupportedOperationException(("Cylinder/" + shell.getClass().getName() + " collision not yet implemented"));
		}
	}
	
	public Vector3f intersects(CylindricalShell shell){
		if (shell.pos.getZ() + height < pos.getZ() || 
			shell.pos.getZ() > pos.getZ() + height){
			return null;
		} else {
			Vector3f v = new Vector3f(pos);
			v.sub(shell.getPos());
			if (v.lengthSquared() < (radius + shell.radius)*(radius  + shell.radius)){
				float len = v.length();
				if (len == 0){
					v.x = 1;
				} else {
					v.normalise();
				}
				v.scale(radius + shell.radius - len);
				return v;
			} else {
				return null;
			}
		}
	}
	
	public Vector3f intersects(BoxShell rect){
		float dx = pos.getX() - rect.pos.getX();
		float dy = pos.getY() - rect.pos.getY();
		
		// Height test first.
		if (Math.max(rect.pos.getZ(), pos.getZ()) >  
			Math.min(rect.pos.getZ() + rect.getHeight(), pos.getZ() + height)){
			return null;
		} else {
			MapDir dir = rect.getDirection(pos);
			switch(dir){
				case NORTH:
					if (dy < radius + rect.getLength()/2.0){
						return new Vector3f(0,(radius + rect.getLength()/2) - dy,0);
					}
					break;
				case SOUTH:
					if (-dy < radius + rect.getLength()/2.0){
						return new Vector3f(0,-dy - (radius + rect.getLength()/2),0);
					}
					break;
				case EAST:
					if (dx < radius + rect.getLength()/2.0){
						return new Vector3f((radius + rect.getLength()/2) - dx, 0,0);
					}
					break;
				case WEST:
					if ((-dx) < radius + rect.getLength()/2.0){
						return new Vector3f(-dx - (radius + rect.getLength()/2),0,0);
					}
					break;
			}
		}
		return null;
	}

	public Vector3f testCollision(ReadableVector3f v1, ReadableVector3f v2) {
		// Height test first.
		if (v2.getZ() < pos.getZ() || v2.getZ() > pos.getZ() + height){
			return null;
		}
		
		float u = GameMath.lineCircleIntersect(v1.getX() - pos.getY(), v1.getY() - pos.getY(),
											   v2.getX() - pos.getX(), v2.getY() - pos.getY(), radius);
		if (!Float.isNaN(u)){
			return new Vector3f(u*(v2.getX() - v1.getX()) + v1.getX(),
								u*(v2.getY() - v1.getY()) + v1.getY(),
								u*(v2.getZ() - v1.getZ()) + v1.getZ());
		}
		return null;
	}

	public void draw(){
		Material.DEFAULT_RED.bind();
		GL11.glPushMatrix();
		cylinder.draw(radius,radius,height,10,1);
		GL11.glTranslatef(0,0,height);
		disk.draw(0,radius,10,1);
		GL11.glPopMatrix();
	}
	
}
