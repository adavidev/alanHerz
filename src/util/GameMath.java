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


/**
 * @author Shannon Smith
 */
public class GameMath {

    public static final float RADIANS_PER_DEGREE = (float)(Math.PI/180.0);
    public static final float DEGREES_PER_RADIAN = (float)(180.0/Math.PI);
    
    public enum MapDir {
        EAST (0,0),
        NORTH_EAST (1,45),
        NORTH (2,90),
        NORTH_WEST (3,135),
        WEST (4,180),
        SOUTH_WEST (5,-135),
        SOUTH (6,-90),
        SOUTH_EAST (7,-45);
        
        public final int octant;
        public final int degrees;
        
        private MapDir(int octant,int degrees){
            this.octant = octant;
            this.degrees = degrees;
        }
        
        public int getX(){
            if (octant == 0 || octant == 1 || octant == 7){
                return 1;
            } else if (octant == 3 || octant == 4 || octant == 5){
                return -1;
            } else {
                return 0;
            }
        }
        
        public int getY(){
            if (octant == 1 || octant == 2 || octant == 3){
                return 1;
            } else if (octant == 5 || octant == 6 || octant == 7){
                return -1;
            } else {
                return 0;
            }
        }
        
        public MapDir rotate(MapRotate rot){
            int newDir = (octant + rot.rot + 8)%8;
            for(MapDir test : MapDir.values()){
                if (test.octant == newDir){
                    return test;
                }
            }
            return null;
        }
        
        public MapDir add(MapDir dir){
        	int newDir = (dir.octant + octant)%8;
            for(MapDir test : MapDir.values()){
                if (test.octant == newDir){
                    return test;
                }
            }
            return null;
        }
        
        public static MapDir getDir(int dx, int dy){
            if (Math.abs(dx) < 2*Math.abs(dy)){
                if (dy < 0){
                    return SOUTH;
                } else {
                    return NORTH;
                }
            } else if (Math.abs(dy) < 2*Math.abs(dx)){
                if (dx < 0){
                    return WEST;
                } else {
                    return EAST;
                }
            } else {
                if (dx > 0 && dy > 0){
                    return NORTH_EAST;
                } else if (dx < 0 && dy > 0){
                    return NORTH_WEST;
                } else if (dx > 0 && dy < 0){
                    return SOUTH_EAST;
                } else if (dx < 0 && dy < 0){
                    return SOUTH_WEST;
                } else {
                    // dx = dy = 0!
                    return null;
                }
            }
        }
        
        public String toString(){
        	switch(this){
        	case EAST: return "East";
        	case NORTH_EAST: return "North-East";
        	case NORTH: return "North";
        	case NORTH_WEST: return "North-West";
        	case WEST: return "West";
        	case SOUTH_WEST: return "South-West";
        	case SOUTH: return "South";
        	case SOUTH_EAST: return "South-East";
        	default: return null;
        	}
        }
        
    }
    
    public enum MapRotate {
        LEFT_45 (1,45),
        LEFT_90 (2,90),
        LEFT_135 (3,135),
        LEFT_180 (4,180),
        RIGHT_45 (-1,-45),
        RIGHT_90 (-2,-90),
        RIGHT_135 (-3,-135),
        RIGHT_180 (-4,-180);
        
        public final int rot;
        public final int degrees;
        
        private MapRotate(int rot, int degrees){
            this.rot = rot;
            this.degrees = degrees;
        }
        
        public static MapRotate getRotation(MapDir from, MapDir to){
        	int octDif = to.octant - from.octant;
        	if (octDif < -4){
        		octDif += 8;
        	} else if (octDif > 4){
        		octDif -= 8;
        	}
        	for (MapRotate rot : MapRotate.values()){
        		if (rot.rot == octDif){
        			return rot;
        		}
        	}
        	// octDiff == 0!
        	return null;
        }
        
    }
        
    
    public static float clamp(float val, float min, float max){
        if (val < min){
            return min;
        } else if (val > max){
            return max;
        } else {
            return val;
        }
    }

    public static int clamp(int val, int min, int max){
        if (val < min){
            return min;
        } else if (val > max){
            return max;
        } else {
            return val;
        }
    }
    
    public static float len(float x, float y){
        return (float)Math.sqrt(x*x + y*y);
    }
    
    public static float dist(float x1, float y1, float x2, float y2){
    	return len(x2 - x1, y2 - y1);
    }

    public static float dist(Vector3f v1, Vector3f v2){
    	return (float)Math.sqrt((v1.x - v2.x)*(v1.x - v2.x) + 
    							(v1.y - v2.y)*(v1.y - v2.y) + 
    							(v1.z - v2.z)*(v1.z - v2.z));
    }
    

    
//    public static Vector3f checkCollision(Vector3f p1, Vector3f p2, Rect3D rect){
//
//    }    
    
    
    public static float getMinAngle(float cur, float target){
        cur = normaliseAngle(cur);
        target = normaliseAngle(target);
        float diff = target - cur;
        if (diff > Math.PI){
            diff -= Math.PI*2;
        } else if (diff < -Math.PI){
            diff += Math.PI*2;
        }
        return diff;
    }
    
    public static float normaliseAngle(float a){
        if ( a >= 0 && a <= Math.PI*2 ){
            return a;
        }
        
        while (a < 0){
            a += 2*Math.PI;
        }
        while (a > 2*Math.PI){
            a -= 2*Math.PI;
        }
        return a;
    }
    
    public final static float lineCircleIntersect(float x1, float y1, float x2, float y2, float r){
  
        if ((x1*x1 + y1*y1) < r*r){
        	// Already inside
        	return Float.NaN;
        }

        float dx = x2 - x1;
        float dy = y2 - y1;
        
        float a, b, c;
        a = dx*dx + dy*dy;
        b = 2.0f * (dx*x1 + dy*y1);
        c = (x1*x1 + y1*y1) - r*r;
        
        float d = b*b - 4*a*c;
        if(d < 0){
            return Float.NaN;
        }

        d = (float)Math.sqrt(d);
        float u;
        float u1 = (-b + d)/(2.0f*a);
        float u2 = (-b - d)/(2.0f*a);

        if (u1 > 0 && u1 < 1.0f && u2 > 0 && u2 < 1.0f){
            u = Math.min(u1,u2);
        } else if (u1 > 0 && u1 < 1.0f){
            u = u1;
        } else if (u2 > 0 && u2 < 1.0f){
            u = u2;
        } else {
            u = Float.NaN;
        }
        return u;
    }

    
    
}
