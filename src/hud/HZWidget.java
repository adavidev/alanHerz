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

public abstract class HZWidget {

	protected boolean visible;
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected HZWidget parent;
	
	public HZWidget(HZWidget parent){
		this.parent = parent;
	}
	
	public void update(float timeStep){
		
	}
	
    public abstract void draw();
    
	public void setGeometry(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

    public int getX(){
    	if (parent == null){
    		return x;
    	} else {
    		return x + parent.getX();
    	}
    }
    
    public int getY(){
    	if (parent == null){
    		return y;
    	} else {
        	return y + parent.getY();
    	}
    }
	
    public void setVisible(boolean visible){
    	this.visible = visible;
    }
    
    public boolean isVisible(){
    	return visible;
    }
   
    public void onKeyPress(HZKey key){
    	
    }
   
    
}
