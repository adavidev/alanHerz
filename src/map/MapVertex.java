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
package map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import util.Vector3f;

/**
 * @author Shannon Smith
 */
public class MapVertex {

	public Vector3f pos;
	public Vector3f norm;
	
    public MapVertex(Vector3f pos, Vector3f norm){
    	this.pos = pos;
    	this.norm = norm;
    }
    
    public void draw(float texScale){
        GL11.glNormal3f(norm.x, norm.y, norm.z);
        GL13.glMultiTexCoord2f(GL13.GL_TEXTURE0,pos.x*texScale,pos.y*texScale);
        GL13.glMultiTexCoord2f(GL13.GL_TEXTURE1,pos.x,pos.y);
        GL11.glVertex3f(pos.x,pos.y,pos.z);
    }
}
