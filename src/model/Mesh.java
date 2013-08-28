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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import util.Vector2f;
import util.Vector3f;

public class Mesh {

	private File source;
    private final List<Triangle> meshTriangles;
    private final List<Vector3f> meshPoints;
    private final List<Vector3f> meshNormals;
    private final List<Vector2f> meshTextureCoords;

    public Mesh(File source){
    	this.source = source;
	    meshTriangles = new ArrayList<Triangle>();
	    meshPoints = new ArrayList<Vector3f>();
	    meshTextureCoords = new ArrayList<Vector2f>();
	    meshNormals = new ArrayList<Vector3f>();
    }
    
    public File getSource(){
    	return source;
    }
    
    public Vector3f getVertex(int index){
        return (Vector3f)meshPoints.get(index);
    }
    
    public Vector3f getNormal(int index){
        return (Vector3f)meshNormals.get(index);
    }

    public Vector2f getTextureCoord(int index){
        return meshTextureCoords.get(index);
    }

    public Triangle getTriangle(int index){
        return (Triangle)meshTriangles.get(index);
    }
    
    public int getVertexCount(){
        return meshPoints.size();
    }
    
    public int getTriangleCount(){
        return meshTriangles.size();
    }
    
    public int getNormalCount(){
        return meshNormals.size();
    }
    
    public int getTextureCoordCount(){
        return meshTextureCoords.size();
    }
    
    public void addPoint(Vector3f v){
        meshPoints.add(v);
    }
    
    public void addNormal(Vector3f n){
        meshNormals.add(n);
    }
    
    public void addTextureCoord(Vector2f t){
        meshTextureCoords.add(t);
    }
    
    public void addTriangle(Triangle triangle){
        meshTriangles.add(triangle);
    }    
    
    
    public int indexOfPoint(Vector3f v){
        return meshPoints.indexOf(v);
    }

    public int indexOfNorm(Vector3f v){
        return meshNormals.indexOf(v);
    }

    public int indexOfTex(Vector2f v){
        return meshTextureCoords.indexOf(v);
    }
 
    public void draw(){
	    GL11.glBegin(GL11.GL_TRIANGLES);
	    for(int i = 0; i < meshTriangles.size();i++){
	        ((Triangle)meshTriangles.get(i)).draw();
	    }
	    GL11.glEnd();
    }
    
}
