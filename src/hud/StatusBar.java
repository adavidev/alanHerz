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

import org.lwjgl.opengl.GL11;

public class StatusBar {

	private int max;
    private int x,y;
    private int value;
    private static final int WIDTH = 256;
   
    public StatusBar(int x, int y, int max){
        this.x = x;
        this.y = y;
        this.max = max;
        value = 256;
    }
    
    public void setValue(int value){
        this.value = (value*WIDTH)/max;
    }
    
    public void draw(){
        GL11.glColor4f(0.0f,0.5f,1.0f,1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        
        
        // Draw energy
        GL11.glVertex2i(x, y);
        GL11.glVertex2i(x, y + 8);
        GL11.glVertex2i(x + value, y + 8) ;
        GL11.glVertex2i(x + value, y) ;

        GL11.glEnd();
        
    }
    
}
