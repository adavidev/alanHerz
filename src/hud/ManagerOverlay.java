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
import herzog3d.HZState.HZKey;

import org.lwjgl.opengl.GL11;

public class ManagerOverlay extends HZWidget {

	private HZWidget[] selectableWidgets;
	private UnitDisplay unitDisplay;
    private HZWidget ordersDisplay;
    private HZWidget mapDisplay;
    private int curWidget;
    private Game game;
    
    public ManagerOverlay(Game game){
    	super(null);
    	this.game = game;
    	unitDisplay = new UnitDisplay(game.getPlayer(), game.getResources().getUnitFactory(),this);
    	unitDisplay.setGeometry(64,16,96,96);
    	ordersDisplay = new OrdersDisplay(game.getResources().getUnitAIFactory(),this);
    	ordersDisplay.setGeometry(64,128,96,96);
    	mapDisplay = new MapDisplay(this,game);
    	mapDisplay.setGeometry(64,256,128,128);
    	curWidget = 0;
    	selectableWidgets = new HZWidget[3];
    	selectableWidgets[0] = unitDisplay;
    	selectableWidgets[1] = ordersDisplay;
    	selectableWidgets[2] = mapDisplay;
    }

    public void onKeyPress(HZKey key){
    	if (key == HZKey.UP){
    		if (curWidget == 0){
    			curWidget = selectableWidgets.length - 1;
    		} else {
    			curWidget--;
    		}
    	} else if (key == HZKey.DOWN){
    		curWidget = (curWidget + 1)%selectableWidgets.length;
    	} else if (key == HZKey.LEFT){
    		selectableWidgets[curWidget].onKeyPress(key);
    	} else if (key == HZKey.RIGHT){
    		selectableWidgets[curWidget].onKeyPress(key);
    	} else if (key == HZKey.BUTTON_1){
    		game.orderUnit(game.getPlayer(),unitDisplay.getDisplayedUnitName());
    		visible = false;
    	} else if (key == HZKey.BUTTON_3){
    		visible = false;
    	}
    }
    
	public void update(float timeStep){
		unitDisplay.update(timeStep);
	}
    
    public void draw(){
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
    	GL11.glColor4f(0.0f,0.5f,1.0f,0.5f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2i(x, y);
        GL11.glVertex2i(x, y + height);
        GL11.glVertex2i(x + width, y + height) ;
        GL11.glVertex2i(x + width, y) ;
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);

        drawSelectionArrow(selectableWidgets[curWidget]);
        
        GL11.glPushMatrix();
        GL11.glTranslatef(x,y,0);
        unitDisplay.draw();
        ordersDisplay.draw();
        mapDisplay.draw();
        GL11.glPopMatrix();
        
    }
    
    private void drawSelectionArrow(HZWidget selected){
    	
    	int x = selected.getX() - 16;
    	int y = selected.getY() + selected.height/2;
    	
        GL11.glPolygonMode(GL11.GL_FRONT,GL11.GL_LINE);
        GL11.glColor4f(0.0f,0.0f,0.4f,1.0f);
        GL11.glLineWidth(2.0f);
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glVertex2i(x,y);
        GL11.glVertex2i(x-16,y-32);
        GL11.glVertex2i(x-16,y+32);
        GL11.glEnd();
        
        GL11.glPolygonMode(GL11.GL_FRONT,GL11.GL_FILL);
    	GL11.glColor4f(0.0f,0.5f,1.0f,0.5f);
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glVertex2i(x,y);
        GL11.glVertex2i(x-16,y-32);
        GL11.glVertex2i(x-16,y+32);
        GL11.glEnd();
    }
    
}
