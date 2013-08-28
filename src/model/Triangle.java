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
package model;

import org.lwjgl.opengl.GL11;

import util.Vector2f;
import util.Vector3f;

/**
 * @author Shannon Smith
 */
public class Triangle {

    public Vector3f[] pos = new Vector3f[3];
    public Vector3f[] norm = new Vector3f[3];
    public Vector2f[] tex = new Vector2f[3];
    
    public void draw(){
    	if (tex[0] != null)
        GL11.glTexCoord2f(tex[0].x,tex[0].y);
        GL11.glNormal3f(norm[0].x,norm[0].y,norm[0].z);
        GL11.glVertex3f(pos[0].x,pos[0].y,pos[0].z);
    	if (tex[1] != null)
        GL11.glTexCoord2f(tex[1].x,tex[1].y);
        GL11.glNormal3f(norm[1].x,norm[1].y,norm[1].z);
        GL11.glVertex3f(pos[1].x,pos[1].y,pos[1].z);
    	if (tex[2] != null)
        GL11.glTexCoord2f(tex[2].x,tex[2].y);
        GL11.glNormal3f(norm[2].x,norm[2].y,norm[2].z);
        GL11.glVertex3f(pos[2].x,pos[2].y,pos[2].z);
    }
    
}
