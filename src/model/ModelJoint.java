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

/**
 * @author Shannon Smith
 */
public class ModelJoint {

    private float angle;
    private String section;

    public ModelJoint(String section, float angle){
        this.angle = angle;
        this.section = section;
    }

    public String getSection(){
        return section;
    }
    
    public float getAngle(){
        return angle;
    }
    
    public void setAngle(float angle){
        this.angle = angle;
    }
        
    
}
