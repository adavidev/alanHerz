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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import util.GameMath;

/**
 * @author Shannon Smith
 */
public class KeyFrameAnimation {

    /** The length of the animation in seconds. */
    private float length;
    /** The name of the animation. */
    private String name;
    /** The animation keys. */
    private List<KeyFrame> keys = new ArrayList<KeyFrame>();
    
    /**
     * 
     * @param name
     * @param length
     */
    public KeyFrameAnimation(String name, float length){
        this.name = name;
        this.length = length;
    }
    
    public String getName(){
        return name;
    }
    
    public float getLength(){
        return length;
    }
    
    public void setState(float time, ModelState state){
        
        if (time > length){
            throw new IllegalStateException();
        }
        
        Iterator i = state.getJoints();
        float angleBefore;
        float angleAfter;
        float weight;
        float timeDif;
        ModelJoint joint;
        while (i.hasNext()){
            joint = (ModelJoint)i.next();
            KeyFrame before = getFrameBefore(time, joint);
            KeyFrame after = getFrameAfter(time, joint);
            if (after == before && before != null){
                if (before.getState().hasJoint(joint.getSection())){
                    joint.setAngle(before.getState().getAngle(joint.getSection()));
                }
            } else if (after != null && before != null){
                timeDif = after.getTime() - before.getTime();
                if (after.getTime() < before.getTime() ){
                    timeDif += length;
                }
                weight = Math.abs(time - before.getTime())/timeDif;
                angleBefore = before.getState().getAngle(joint.getSection());
                angleAfter = after.getState().getAngle(joint.getSection());
                
                float angleDif = (GameMath.normaliseAngle(angleAfter) - GameMath.normaliseAngle(angleBefore));

                if (angleDif > Math.PI){
                    angleDif -= Math.PI*2;
                } else if (angleDif < -Math.PI){
                    angleDif += Math.PI*2;
                }
                   
                if (before.getState().hasJoint(joint.getSection())){
    	            joint.setAngle(angleBefore + weight*angleDif);
                }
            }
        }
        
    }
    
    public KeyFrame getFrameBefore(float time, ModelJoint joint){
        KeyFrame before = null;
        for(int i = 0; i < keys.size();i++){
            KeyFrame frame = (KeyFrame)keys.get(i);
            if (frame.getState().hasJoint(joint.getSection())){
                if (frame.getTime() <= time){
                    before = frame;
                } else if (before == null || before.getTime() > time) {
                    before = frame;
                }
            }
        }
        return before;
    }
    
    public KeyFrame getFrameAfter(float time, ModelJoint joint){
        KeyFrame after = null;
        for(int i = (keys.size() - 1); i >= 0;i--){
            KeyFrame frame = (KeyFrame)keys.get(i);
            if (frame.getState().hasJoint(joint.getSection())){
                if (frame.getTime() >= time){
                    after = frame;
                } else if (after == null || after.getTime() < time) {
                    after = frame;
                }
            }
        }
        return after;
    }

    public KeyFrame getKeyFrame(float time, float dist){
        KeyFrame frame = null;
        for(int i = 0; i < keys.size();i++){
            KeyFrame keyFrame = (KeyFrame)keys.get(i);
            if (Math.abs(keyFrame.getTime() - time) < dist){
                if (frame != null && Math.abs(keyFrame.getTime() - time) < Math.abs(frame.getTime() - time)){
                    frame = keyFrame;
                } else {
                    frame = keyFrame;
                }
            }
        }
        return frame;
    }

    
    
    public Iterator getKeyFrames(){
        return keys.iterator();
    }
    
    public void addKeyFrame(KeyFrame keyFrame){
    	int index = 0;
    	while (index < keys.size() && keyFrame.getTime() > keys.get(index).getTime()){
    		index++;
    	}
        keys.add(index,keyFrame);
    }
    

}
