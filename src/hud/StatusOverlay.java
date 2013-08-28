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
import org.lwjgl.opengl.GL13;

import resource.Material;
import resource.ResourceManager;

public class StatusOverlay extends HZWidget {

    private Player player;
    private StatusBar energyStatus;
    private StatusBar armourStatus;
    private StatusBar baseStatus;
    private StatusBar ammoStatus;
    private Material orderProcessingIcon;
    private Material orderReadyIcon;
    
    public StatusOverlay(ResourceManager resManager, Player player){
    	super(null);
        this.player = player;
        energyStatus = new StatusBar(120,16,100);
        armourStatus = new StatusBar(120,40,100);
        baseStatus = new StatusBar(424,16,1000);
        ammoStatus = new StatusBar(424,40,100);
        orderProcessingIcon = resManager.getMaterial("order_processing");
        orderReadyIcon = resManager.getMaterial("order_ready");
        visible = true;
    }

    public void draw() {
        int energy = Math.round(player.getMech().getEnergy()*2.56f);
        int armour = Math.round(player.getMech().getArmour()*2.56f);
        int baseArmour = player.getHomeBase().getLife();
        int ammo = Math.round(player.getMech().getAmmo()*2.56f);
        baseStatus.setValue(baseArmour);
        
        if (player.getUnitOrder() != null){
        	if (player.getUnitOrder().ready()){
            	orderReadyIcon.bind();
        	} else {
            	orderProcessingIcon.bind();
        	}
        	GL11.glEnable(GL11.GL_BLEND);
        	GL11.glColor4f(1.0f,1.0f,1.0f,1.0f);
        	GL11.glBegin(GL11.GL_QUADS);
        	GL11.glTexCoord2f(0,0);
        	GL11.glVertex2i(384,16);
        	GL11.glTexCoord2f(0,1);
        	GL11.glVertex2i(384,48);
        	GL11.glTexCoord2f(1,1);
        	GL11.glVertex2i(384 + 32,48);
        	GL11.glTexCoord2f(1,0);
        	GL11.glVertex2i(384 + 32,16);
        	GL11.glEnd();
        	GL11.glDisable(GL11.GL_BLEND);
        }
        
        GL11.glDisable(GL11.GL_LIGHTING);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        energyStatus.draw();
        armourStatus.draw();
        baseStatus.draw();
        ammoStatus.draw();
        
    }
    
    
    
}
