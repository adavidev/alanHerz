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
package util;

import util.GameMath.MapDir;
import util.GameMath.MapRotate;

public class Test {

    public static void main(String[] args){
//        Vector3f p1 = new Vector3f(2,3,0);
//        Vector3f p2 = new Vector3f(3,1,0);
//        Vector3f cs = new Vector3f(3,2,0);
//        Vector3f c = GameMath.checkCollision(p1,p2,cs,1.2f);
//
//        System.out.println("Checking: " + p1 + ", " + p2 + ", " + cs + " - " + 1.0f);
//        System.out.println("  Colision found: " + c);
    	MapDir test = MapDir.EAST;
    	for(int i = 0; i < 8; i++){
    		System.out.println(test);
    		test = test.rotate(MapRotate.LEFT_45);
    	}
		System.out.println(test);
    	System.out.println("====");
    	for(int i = 0; i < 8; i++){
    		System.out.println(test);
    		test = test.rotate(MapRotate.RIGHT_45);
    	}
		System.out.println(test);
    	
        
    }
    
    
    
}
