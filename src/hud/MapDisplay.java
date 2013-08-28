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

import herzog3d.Game;

import org.lwjgl.opengl.GL11;

import resource.Material;

public class MapDisplay extends HZWidget {

	private Game game;
	
	public MapDisplay(HZWidget parent, Game game) {
		super(parent);
		this.game = game;
		
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
        game.getMap().getMinimap().bind();
        GL11.glColor4f(1.0f,1.0f,1.0f,1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0,0);
        GL11.glVertex2i(x, y);
        GL11.glTexCoord2f(0,1);
        GL11.glVertex2i(x, y + height);
        GL11.glTexCoord2f(1,1);
        GL11.glVertex2i(x + width, y + height) ;
        GL11.glTexCoord2f(1,0);
        GL11.glVertex2i(x + width, y) ;
        GL11.glEnd();
	}

}
