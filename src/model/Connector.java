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

import util.ReadableVector3f;
import util.Vector3f;

public class Connector {

    private Vector3f pos;
    private Vector3f axis;
    
    public Connector(ReadableVector3f pos, ReadableVector3f axis){
        this.pos = new Vector3f(pos);
        this.axis = new Vector3f(axis);
        this.axis.normalise();
    }
    
    public void set(float x, float y, float z, float ax, float ay, float az){
        pos.x = x;
        pos.y = y;
        pos.z = z;
        axis.x = ax;
        axis.y = ay;
        axis.z = az;
    }
 
    public Vector3f getPosition(){
        return pos;
    }
    
    public Vector3f getAxis(){
        return axis;
    }
    
    public void draw(){
        GL11.glBegin(GL11.GL_LINES);
        GL11.glColor3f(1.0f,0,0);
        GL11.glVertex3f(pos.x,pos.y,pos.z);
        GL11.glVertex3f(pos.x + axis.x/5.0f,pos.y + axis.y/5.0f,pos.z + axis.z/5.0f);
        GL11.glEnd();
    }
    
    public void transformGL(float angle){
        GL11.glTranslatef(pos.x,pos.y,pos.z);
        GL11.glRotatef(angle,axis.x,axis.y,axis.z);
//        GL11.glTranslatef(-pos.x,-pos.y,-pos.z);
    }
    
}
