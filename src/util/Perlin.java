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

import java.util.Random;

/**
 * @author Shannon Smith
 */
public class Perlin {
    
    private float[][][] grid;
    private int octaves;
    private float fade;
    private int size;
    
    public Perlin(int gridSize, long seed, int octaves, float fade){
        size = gridSize;
        grid = new float[gridSize][gridSize][2];
        this.fade = fade;
        this.octaves = octaves;
        Random rand = new Random(seed);
        for(int i = 0; i < gridSize;i++){
            for(int j = 0; j < gridSize; j++){
                double angle = rand.nextDouble()*Math.PI*2;
                grid[i][j][0] = (float)Math.sin(angle);
                grid[i][j][1] = (float)Math.cos(angle);
            }
        }
    }
    

    public float fractalSum(float x, float y){
        float sum = 0;
        float curWeight = 1.0f;
        for(int i = 0; i < octaves;i++){
            sum += curWeight*noise(x*(1<<i),y*(1<<i));
            curWeight *= fade;
        }
        return sum;
    }
    
    public float noise(float x, float y){
        int a,b;
        a =(int) Math.floor(x);
        b =(int) Math.floor(y);
        x -= a;
        y -= b;
        a %= (grid.length - 1);
        b %= (grid[0].length - 1);
        
        float s = grad(a,b,x,y); 
        float t = grad(a+1,b,x-1,y); 
        float u = grad(a,b+1,x,y-1); 
        float v = grad(a+1,b+1,x-1,y-1); 

        float st = lerp(fade(x),s,t);
        float uv = lerp(fade(x),u,v);
        return lerp(fade(y),st,uv);
    }
    
    private float grad(int gx, int gy,float x, float y){
        return grid[gx][gy][0]*x + grid[gx][gy][1]*y;
    }
    
    
	private static float lerp(float t, float a, float b) {
		return a + t * (b - a);
	}
    
    public static float fade(float n){
        return 3*n*n - 2*n*n*n;
    }
    
}
