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
import herzog3d.Camera;
import herzog3d.Player;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import map.Base.BaseType;
import map.Tile.TileType;

import org.lwjgl.opengl.GL11;

import resource.Material;
import resource.ResourceManager;
import resource.ResourceNotFoundException;
import resource.Texture;
import util.GLUtils;
import util.Perlin;
import util.Vector3f;
import util.GameMath.MapDir;

/**
 * @author Shannon Smith
 */
public class GameMap {

    private static final int MINIMAP_SIZE = 128;
	private static final float COLLISION_THRESHOLD = 0.01f;
    private static final float WATER_HEIGHT = 0.7f;
    
    private final Tile[][] tiles;
    private final Perlin perlin;
    private final String name;
    private Material groundMaterial;
    private Material waterMaterial;
    private Material miniMap;
    private final int size;
    private Map<String, PlayerTemplate> players;
    private PlayerTemplate neutralPlayer;
    private int minPlayers;
    private int maxPlayers;
    
    public GameMap(String name, int size) {
        this.size = size;
        this.name = name;
        players = new HashMap<String, PlayerTemplate>();
        neutralPlayer = new PlayerTemplate("neutral");
        tiles = new Tile[size][size];
	    perlin = new Perlin(256,10,4,0.3f);
	    for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[0].length; j++){
                tiles[i][j] = new Tile(this,i,j,TileType.LOW_LAND);
            }
        }
    }
    
    public int getMinPlayers(){
    	return minPlayers;
    }
    
    public Material getMinimap(){
    	if (miniMap == null){
    		renderMinimap();
    	}
    	return miniMap;
    }
    
    
    
    private void renderMinimap(){
    	int size = MINIMAP_SIZE;
    	Texture tex = new Texture("ground");
    	miniMap = new Material(name + "_minimap");
        int[] viewport = GLUtils.getViewport();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0,size, size, 0, -10, 10) ;
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glViewport(0,0,size,size);

        GL11.glClearColor(0.0f,0,0,1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glScalef(size/tiles.length, size/tiles.length, size/tiles.length);

        GLUtils.glLightPos(1.0f,1.0f,1.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        draw(null);
        GL11.glEnable(GL11.GL_CULL_FACE);
        
        IntBuffer addr = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
    	GL11.glGenTextures(addr);
    	tex.setID(addr.get(0));
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getID());
    	
    	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
    	GL11.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, 0, 0, size, size, 0);

    	miniMap.setTexture(0,tex);
    	GL11.glViewport(viewport[0], viewport[1], viewport[2], viewport[3]);
    }
    
    public int getMaxPlayers(){
    	return maxPlayers;
    }
    
    public Collection<String> getPlayerTemplates(){
    	return players.keySet();
    }
    
    public List<Base> createPlayerBases(Player player, String id, ResourceManager res) throws ResourceNotFoundException{
    	PlayerTemplate template = players.get(id);
    	if (template.homeBase == null){
    		throw new IllegalStateException("Player: " + id + " does not have a home base!");
    	}
    	List<Base> bases = new ArrayList<Base>();
    	Base homeBase = new Base(template.homeBase,BaseType.HOME_BASE,res);
    	homeBase.setOwner(player);
    	bases.add(homeBase);
    	player.setHomeBase(homeBase);
    	for(Tile miniBase : template.miniBases){
    		bases.add(new Base(miniBase, BaseType.MINI_BASE, res));
    	}
    	return bases;
    }
    
    public void addPlayerTemplate(String name){
    	players.put(name, new PlayerTemplate(name));
    }
    
    public void setHomeBaseLocation(String player, int x, int y){
    	players.get(player).homeBase = tiles[x][y];
    }
    
    public void addBaseLocation(String player, int x, int y){
    	players.get(player).miniBases.add(tiles[x][y]);
    }
    
    public Material getGroundMaterial(){
        return groundMaterial;
    }
    
    public void setGroundMaterial(Material material){
        this.groundMaterial = material;
    }
    
    public void setWaterMaterial(Material material){
        this.waterMaterial = material;
    }
    
    public int getSize(){
        return tiles.length;
    }
   
    public String getName(){
        return name;
    }
    
    public void generateMesh(){
    	System.out.print("Generating map mesh...");
    	MapVertex[][] verticies = new MapVertex[size+1][size+1];
    	for(int i = 0; i <= size; i++){
            for(int j = 0; j <= size; j++){
            	verticies[i][j] = new MapVertex(new Vector3f(i,j,getHeightAt(i,j)),getNormalAt(i,j));
            }
    	}
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                tiles[i][j].generateMesh(verticies[i][j],verticies[i+1][j],verticies[i+1][j+1],verticies[i][j+1]);
            }
        }
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                tiles[i][j].linkMesh(this);
            }
        }
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                tiles[i][j].splitMesh(this);
            }
        }
    	System.out.println("done.");
    }

    private static final float STEP_DIST = 0.5f;
    private static final Vector3f v1 = new Vector3f();
    private static final Vector3f v2 = new Vector3f();
    private static final Vector3f v3 = new Vector3f();
    private static final Vector3f v4 = new Vector3f();
    private static final Vector3f temp = new Vector3f();
    public void getNormalAt(float x, float y, Vector3f norm){
    	v1.set(x+STEP_DIST,y,getHeightAt(x+STEP_DIST,y));
    	v1.x -= x;
    	v1.y -= y;
    	v1.z -= getHeightAt(x,y);
    	v2.set(x,y+STEP_DIST,getHeightAt(x,y+STEP_DIST));
    	v2.x -= x;
    	v2.y -= y;
    	v2.z -= getHeightAt(x,y);
    	v3.set(x-STEP_DIST,y,getHeightAt(x-STEP_DIST,y));
    	v3.x -= x;
    	v3.y -= y;
    	v3.z -= getHeightAt(x,y);
    	v4.set(x,y-STEP_DIST,getHeightAt(x,y-STEP_DIST));
    	v4.x -= x;
    	v4.y -= y;
    	v4.z -= getHeightAt(x,y);
    	v1.cross(v2,temp);
    	norm.add(temp);
    	v2.cross(v3,temp);
    	norm.add(temp);
    	v4.cross(v4,temp);
    	norm.add(temp);
    	v4.cross(v1);
    	norm.add(temp);
        norm.normalise();
    }
    
    public Vector3f getNormalAt(float x, float y){
    	Vector3f norm = new Vector3f();
    	getNormalAt(x,y,norm);
    	return norm;
    }
    
    public float getHeightAt(float x, float y){
        int tx = (int)Math.floor(x);
        int ty = (int)Math.floor(y);
        
        
        if (tx < 0){
            tx = 0;
            x = 0;
        }
        if (ty < 0){
            ty = 0;
            y = 0;
        }
        if (tx >= tiles.length){
            tx = tiles.length-1;
            x = tiles.length;
        }
        if (ty >= tiles[0].length){
            ty = tiles[0].length-1;
            y = tiles[0].length;
        }
        
//        if (getBaseAt(tx,ty) != null){
//            return tiles[tx][ty].getHeightAt(x - tx, y - ty);    
//        } else {
            return tiles[tx][ty].getHeightAt(x - tx, y - ty) + weight(perlin.fractalSum(x/2.0f,y/2.0f));
//
//        }
        
    }
    
    public boolean isInMap(float x, float y){
        if (x < 0 || x > size || y < 0 || y > size){
            return false;
        } else {
            return true;
        }
    }
    
    public Tile getTile(int x, int y){
        if (x < 0 || y < 0 || x >= tiles.length || y >= tiles[0].length){
            return null;
        } else {
            return tiles[x][y];
        }
    }
    
    public Tile getTileNeighbour(Tile tile, MapDir dir){
        switch (dir){
        case NORTH:
            return getTile(tile.getX(),tile.getY()+1);
        case NORTH_EAST:
            return getTile(tile.getX()+1,tile.getY()+1);
        case EAST:
            return getTile(tile.getX()+1,tile.getY());
        case SOUTH_EAST:
            return getTile(tile.getX()+1,tile.getY()-1);
        case SOUTH:
            return getTile(tile.getX(),tile.getY()-1);
        case SOUTH_WEST:
            return getTile(tile.getX()-1,tile.getY()-1);
        case WEST:
            return getTile(tile.getX()-1,tile.getY());
        case NORTH_WEST:
            return getTile(tile.getX()-1,tile.getY()+1);
        default:
            return null;
        }
    }
    
    public Tile getTileAt(float x, float y){
        return getTile((int)x,(int)y);
    }
    
    public float weight(float value){
        return value*value*1.0f;
    }
    
    
    
    public Vector3f testCollision(Vector3f p1, Vector3f p2){
        float h1,h2,hc;
        h1 = getHeightAt(p1.x,p1.y);
        h2 = getHeightAt(p2.x,p2.y);
        if (p1.z > h1 && p2.z < h2 ){ 
            Vector3f v = new Vector3f((p1.x + p2.x)/2,(p1.y + p2.y)/2,(p1.z + p2.z)/2);
            hc = getHeightAt(v.x,v.y);
            if ((hc - v.z) < COLLISION_THRESHOLD){
                return v;
            } else if (v.z > hc) {
                return testCollision(v,p2);
            } else {
                return testCollision(p1,v);
            }
        } else {
            return null;
        }
    }
    
    
    public void draw(Camera cam){
    	Vector3f camTarget = null;
    	if (cam != null){
    		camTarget = cam.getTarget().getPos();
    	}
        groundMaterial.bind();
        GL11.glBegin(GL11.GL_TRIANGLES);
    	for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[0].length; j++){
                if (cam == null || Math.abs(camTarget.x - i) < 16 && Math.abs(camTarget.y - j) < 16){
                    tiles[i][j].draw();
                }
            }
        }
        GL11.glEnd();
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[0].length; j++){
                if (cam == null || Math.abs(camTarget.x - i) < 16 && Math.abs(camTarget.y - j) < 16){
                    if (tiles[i][j].hasFeatures()){
                        tiles[i][j].drawFeatures();
                    }
                }
            }
        }
        drawWater();
    }
    
    public void drawWater(){
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthMask(false);
        waterMaterial.bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glNormal3f(0,0,1.0f);
        GL11.glTexCoord2f(0,0);
        GL11.glVertex3f(0,0,WATER_HEIGHT);
        GL11.glTexCoord2f(size,0);
        GL11.glVertex3f(size,0,WATER_HEIGHT);
        GL11.glTexCoord2f(size,size);
        GL11.glVertex3f(size,size,WATER_HEIGHT);
        GL11.glTexCoord2f(0,size);
        GL11.glVertex3f(0,size,WATER_HEIGHT);
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    public String toString(){
        return ("Map: " + name + " (" + size + ")");
        
    }
    
    public boolean canLand(float x, float y){
    	return tiles[(int)x][(int)y].unitCanEnter();
    }
    
    private class PlayerTemplate {
    	public Tile homeBase;
    	public List<Tile> miniBases;
    	public String name;
    	
    	public PlayerTemplate(String name){
    		this.name = name;
    		miniBases = new ArrayList<Tile>();
    	}
    	
    }

}
