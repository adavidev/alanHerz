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

import herzog3d.HZState.HZKey;

import org.lwjgl.opengl.GL11;

import resource.Material;
import ai.UnitAI;
import ai.UnitAIFactory;

public class OrdersDisplay extends HZWidget {

	private int curAI;
	private UnitAI[] ais;
	private UnitAIFactory factory;
	
	public OrdersDisplay(UnitAIFactory factory, HZWidget parent){
		super(parent);
		ais = new UnitAI[factory.getUnitAICount()];
		curAI = 0;
		for(String name : factory.getAINames()){
			ais[curAI++] = factory.buildAI(name,null);
		}
		curAI = 0;
	}
	
	
	public void onKeyPress(HZKey key){
		if (key == HZKey.LEFT){
			previous();
		} else if (key == HZKey.RIGHT) {
			next();
		}
	}
	
	
	public void next(){
		curAI = (curAI + 1)%ais.length;
	}

	public void previous(){
		if (curAI == 0){
			curAI = ais.length - 1;
		} else {
			curAI--;
		}
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

        GL11.glColor4f(1.0f,1.0f,1.0f,1.0f);
        GL11.glPushMatrix();
        GL11.glTranslatef(x+16,y+16,0);
        ais[curAI].draw();
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_BLEND);
        Material.DEFAULT_WHITE.bind();

	}
	
}
