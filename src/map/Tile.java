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
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import util.Vector3f;
import util.GameMath.MapDir;
import util.GameMath.MapRotate;

/**
 * @author Shannon Smith
 */
public class Tile {

    private static final float SPLIT_THRESHOLD = 0.05f;
    private static final int MAX_DEPTH = 6;
    
    public enum TileType{
        LOW_LAND(1.0f,0),
        HIGH_LAND(3.0f,1),
        WATER(0.0f,2);
        
        public final float height;
        public final int index;
        
        private TileType(float height, int index){
            this.height = height;
            this.index = index;
        }
    }

    private MapTri leftTri;
    private MapTri rightTri;
    
    private List<Feature> features;
    
    private int x;
    private int y;
    private GameMap map;
    private boolean locked;
    private TileType type;
    
    public Tile(GameMap map, int x, int y, TileType type){
        this.type = type;
        this.map = map;
        this.x = x;
        this.y = y;
        locked = false;
        features = new ArrayList<Feature>();
    }
    
    public TileType getType(){
        return type;
    }
    
    public Vector3f getCenter(){
    	return new Vector3f(x + 0.5f, y + 0.5f, getHeightAt(0.5f,0.5f));
    }
    
    public void setType(TileType type){
        this.type = type;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public void addFeature(Feature feature){
        features.add(feature);
    }

    public void lock(){
        locked = true;
    }
    
    public void unlock(){
        locked = false;
    }
    
    public boolean isLocked(){
        return locked;
    }
    
    public float getCornerHeight(MapDir dir){
        float height = type.height;
        Tile t1 = map.getTileNeighbour(this,dir);
        Tile t2 = map.getTileNeighbour(this,dir.rotate(MapRotate.LEFT_45));
        Tile t3 = map.getTileNeighbour(this,dir.rotate(MapRotate.RIGHT_45));
        float count = 1;
        if (t1 != null){
            height += t1.type.height;
            count++;
        }
        if (t2 != null){
            height += t2.type.height;
            count++;
        }
        if (t3 != null){
            height += t3.type.height;
            count++;
        }
        return height/count;
    }
    
    /**
     * 
     * 
     * @param x float between 0 and 1
     * @param y float between 0 and 1
     * @return
     */
    public float getHeightAt(float x, float y){
        float a,b,c,d;
        a = getCornerHeight(MapDir.NORTH_WEST);
        b = getCornerHeight(MapDir.NORTH_EAST);
        c = getCornerHeight(MapDir.SOUTH_WEST);
        d = getCornerHeight(MapDir.SOUTH_EAST);
        
        
        float fx = fade(x);
        float fy = fade(y);

        float ab = lerp(fx,a,b);
        float cd = lerp(fx,c,d);

        return (lerp(fy,cd,ab));
    }
    
    public Tile getNeighbour(GameMap map, MapDir dir){
        
        switch (dir){
        case EAST:
            return map.getTile(x+1,y);
        case NORTH_EAST:
            return map.getTile(x+1,y+1);
        case NORTH:
            return map.getTile(x,y+1);
        case NORTH_WEST:
            return map.getTile(x-1,y+1);
        case WEST:
            return map.getTile(x-1,y);
        case SOUTH_WEST:
            return map.getTile(x-1,y-1);
        case SOUTH:
            return map.getTile(x,y-1);
        case SOUTH_EAST:
            return map.getTile(x+1,y-1);
        default:
            return null;
        }
    }
    
	private static float lerp(float t, float a, float b) {
		return a + t * (b - a);
	}
    
    public float fade(float n){
        return 3*n*n - 2*n*n*n;
        
    }
    
    public boolean unitCanEnter(){
        return (type != TileType.WATER);
    }
    
    public MapTri getRoamTriLeft(){
        return leftTri;
    }

    public MapTri getRoamTriRight(){
        return rightTri;
    }
    
    public void generateMesh(MapVertex v1, MapVertex v2, MapVertex v3, MapVertex v4){
    	leftTri = new MapTri(v1,v2,v4); 
    	rightTri = new MapTri(v3,v4,v2);
    	leftTri.setBaseNeighbour(rightTri);
    	rightTri.setBaseNeighbour(leftTri);
    }

    public void splitMesh(GameMap map){
	    recursiveSplit(leftTri,map,0);
	    recursiveSplit(rightTri,map,0);
    }

    public void linkMesh(GameMap map){
        Tile neigh;
        if ((neigh = getNeighbour(map,MapDir.NORTH)) != null){
            rightTri.setLeftNeighbour(neigh.getRoamTriLeft());
        }
        if ((neigh = getNeighbour(map,MapDir.SOUTH)) != null){
            leftTri.setLeftNeighbour(neigh.getRoamTriRight());
        }
        if ((neigh = getNeighbour(map,MapDir.EAST)) != null){
            rightTri.setRightNeighbour(neigh.getRoamTriLeft());
        }
        if ((neigh = getNeighbour(map,MapDir.WEST)) != null){
            leftTri.setRightNeighbour(neigh.getRoamTriRight());
        }
    }
    
    private void recursiveSplit(MapTri curNode, GameMap map, int depth){
        if(depth > MAX_DEPTH){
            return;
        }
        if (curNode.isSplit()){
            recursiveSplit(curNode.getLeftChild(),map,depth+1);
            recursiveSplit(curNode.getRightChild(),map,depth+1);
            return;
        } else {
            float var = curNode.calcVariance(map); 
            if ( var > SPLIT_THRESHOLD){
                curNode.split(map);
                if (curNode.getLeftChild().calcVariance(map) > var){
                    return;
//                    throw new IllegalStateException();
                }
                if (curNode.getRightChild().calcVariance(map) > var){
//                    throw new IllegalStateException();
                    return;
                }
                recursiveSplit(curNode.getLeftChild(),map,depth+1);
                recursiveSplit(curNode.getRightChild(),map,depth+1);
            }
        }
    }
    
    public void draw(){
        recursiveDraw(leftTri);
        recursiveDraw(rightTri);
    }

    public boolean hasFeatures(){
        return features.size() != 0;
    }
    
    public void drawFeatures(){
	    for(int i = 0; i < features.size();i++){
	        ((Feature)features.get(i)).draw(this);
	    }
    }
    
    private void recursiveDraw(MapTri tri){
        if (tri.isSplit()){
            recursiveDraw(tri.getLeftChild());
            recursiveDraw(tri.getRightChild());
        } else {
            tri.draw(1.0f/map.getSize());
        }
    }
    
    /**
     * Manhattan distance.
     * 
     * @param tile
     * @return
     */
    public int distanceTo(Tile tile){
        return Math.abs(tile.getX() - x) + Math.abs(tile.getY() - y);
    }
    
    public String toString(){
        return ("tile " + x + "," + y);
    }
    
}
