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
package hud;

import herzog3d.Player;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import unit.Unit;
import unit.UnitFactory;
import util.GLUtils;

public class UnitDisplay extends HZWidget {

	private UnitFactory factory;
	private Unit currentUnit;
	private float angle;
	private Player player;
	
	public UnitDisplay(Player player, UnitFactory factory, HZWidget parent){
		super(parent);
		this.factory = factory;
		this.player = player;
		currentUnit = factory.buildUnit(null,"Tank");
		currentUnit.setOwner(player);
	}
	
	public String getDisplayedUnitName(){
		return currentUnit.getName();
	}
	
	public void update(float step){
		angle += 1.0f*step;
	}
	
	public void draw() {
        GL11.glPolygonMode(GL11.GL_FRONT,GL11.GL_LINE);
        GL11.glColor4f(0.0f,0.0f,0.4f,1.0f);
        GL11.glLineWidth(2.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(x, y);
        GL11.glVertex2i(x, y + height);
        GL11.glVertex2i(x + width, y + height) ;
        GL11.glVertex2i(x + width, y) ;
        GL11.glEnd();

        GL11.glPolygonMode(GL11.GL_FRONT,GL11.GL_FILL);
    	GL11.glColor4f(0.0f,0.5f,1.0f,0.8f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(x, y);
        GL11.glVertex2i(x, y + height);
        GL11.glVertex2i(x + width, y + height) ;
        GL11.glVertex2i(x + width, y) ;
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        
        int[] viewport = GLUtils.getViewport();
        
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GLU.gluPerspective(45,1.0f,0.001f,5.0f);
        GL11.glViewport(getX(),viewport[3] - (getY() + height),width,height);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GLU.gluLookAt(2.0f*(float)Math.cos(angle),2.0f*(float)Math.sin(angle),1.0f,0,0,0,0,0,1);
//        GL11.glTranslatef(x + width/2, y + height/2,0);
        currentUnit.draw();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glViewport(viewport[0],viewport[1],viewport[2],viewport[3]);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
	}
	
}
