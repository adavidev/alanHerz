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
package map;

import util.GameMath;
import util.Vector3f;

/**
 * @author Shannon Smith
 */
public class MapTri {

    private MapTri baseNeighbour;
    private MapTri leftNeighbour;
    private MapTri rightNeighbour;
    
    private MapTri leftChild;
    private MapTri rightChild;
    
    private final MapVertex top;
    private final MapVertex left;
    private final MapVertex right;
    
    public MapTri(MapVertex top, MapVertex left, MapVertex right){
        this.top = top;
        this.left = left;
        this.right = right;
        if (left == null || right == null || top == null){
        	throw new NullPointerException();
        }
    }
    
    public float calcVariance(GameMap map){
    	float x,y,z;
    	x = (left.pos.x + right.pos.x)/2;
    	y = (left.pos.y + right.pos.y)/2;
    	z = (left.pos.z + right.pos.z)/2;
    	return GameMath.dist(left.pos,right.pos)*Math.abs(z - map.getHeightAt(x,y));
    }
    
    public MapTri getBaseNeighbour(){
        return baseNeighbour;
    }
    
    public MapTri getLeftNeighbour(){
        return leftNeighbour;
    }
    
    public MapTri getRightNeighbour(){
        return rightNeighbour;
    }
    
    public void setNeighbours(MapTri base, MapTri left, MapTri right){
        this.baseNeighbour = base;
        this.leftNeighbour = left;
        this.rightNeighbour = right;
    }
    
    public void setLeftNeighbour(MapTri tri){
        this.leftNeighbour = tri;
    }
    
    public MapTri getLeftChild(){
        return leftChild;
    }
    
    public MapTri getRightChild(){
        return rightChild;
    }
    
    public void setRightNeighbour(MapTri tri){
        this.rightNeighbour = tri;
    }
    
    public void setBaseNeighbour(MapTri tri){
        this.baseNeighbour = tri;
    }
    
    public boolean isSplit(){
        return leftChild != null;
    }
    
    public void split(GameMap h){
        
//        System.out.println("Splitting!");
        if (leftChild != null){
            throw new IllegalStateException();
        }
        
        if (baseNeighbour != null){
	        if (baseNeighbour.getBaseNeighbour() != this){
	            baseNeighbour.split(h);
	        }
        }

        MapVertex mid;
        if (baseNeighbour != null && baseNeighbour.isSplit()){
        	mid = baseNeighbour.leftChild.top;
        } else {
            Vector3f v = new Vector3f((left.pos.x + right.pos.x)/2,(left.pos.y + right.pos.y)/2,0);
            v.z = h.getHeightAt(v.x,v.y);
            mid = new MapVertex(v,h.getNormalAt(v.x,v.y));
        }
        
        leftChild = new MapTri(mid,top,left);
        rightChild = new MapTri(mid,right,top);
        leftChild.setNeighbours(leftNeighbour,rightChild,null);
        rightChild.setNeighbours(rightNeighbour,null,leftChild);
        if (leftNeighbour != null){
            if (leftNeighbour.getBaseNeighbour() == this){
                leftNeighbour.setBaseNeighbour(leftChild);
            } else if (leftNeighbour.getLeftNeighbour() == this){
                leftNeighbour.setLeftNeighbour(leftChild);
            } else if (leftNeighbour.getRightNeighbour() == this){
                leftNeighbour.setRightNeighbour(leftChild);
            } else {
                throw new IllegalStateException();
            }
        }
        if (rightNeighbour != null){
            if (rightNeighbour.getBaseNeighbour() == this){
                rightNeighbour.setBaseNeighbour(rightChild);
            } else if (rightNeighbour.getLeftNeighbour() == this){
                rightNeighbour.setLeftNeighbour(rightChild);
            } else if (rightNeighbour.getRightNeighbour() == this){
                rightNeighbour.setRightNeighbour(rightChild);
            } else {
                throw new IllegalStateException();
            }
        }
        
        if (baseNeighbour != null){
	        if (!baseNeighbour.isSplit()){
	            baseNeighbour.split(h);
	            leftChild.setRightNeighbour(baseNeighbour.getRightChild());
	            rightChild.setLeftNeighbour(baseNeighbour.getLeftChild());
	        } else {
	            leftChild.setRightNeighbour(baseNeighbour.getRightChild());
	            rightChild.setLeftNeighbour(baseNeighbour.getLeftChild());
	        }
        }
    
    }
    
    public String toString(){
        return "Roam Tri: " + top + "  " + left + "  " + right;
    }
    
    public void draw(float texScale){

    	top.draw(texScale);
    	left.draw(texScale);
    	right.draw(texScale);

        /*
        GL11.glEnd();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glColor3f(1.0f,0,0);
        GL11.glVertex3f(top.x,top.y,top.z);
        GL11.glVertex3f(top.x + top.nx/2,top.y + top.ny/2,top.z + top.nz/2);
        GL11.glVertex3f(left.x,left.y,left.z);
        GL11.glVertex3f(left.x + left.nx/2,left.y + left.ny/2,left.z + left.nz/2);
        GL11.glVertex3f(left.x,left.y,left.z);
        GL11.glVertex3f(left.x + left.nx/2,left.y + left.ny/2,left.z + left.nz/2);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glColor3f(0,1.0f,0);
        */
    }
}
