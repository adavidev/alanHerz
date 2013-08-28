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
package resource;

import java.awt.image.BufferedImage;

public class Texture {

	private String name;
	private BufferedImage image;
	private int id;
	
	public Texture(String name){
		this.name = name;
		id = -1;
	}
	
	public int getID(){
		return id;
	}
	
	public int getSize(){
		if (image == null){
			return -1;
		} else {
			return image.getWidth();
		}
	}
	
	public void setImage(BufferedImage image){
		this.image = image;
	}
	
	public void setID(int id){
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public BufferedImage getImage(){
		return image;
	}
	
	
}
