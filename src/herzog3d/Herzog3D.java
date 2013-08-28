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
package herzog3d;
import herzog3d.HZState.HZKey;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import core.iGameResource;

import resource.ResourceLoader;
import resource.ResourceManager;
import sound.ALMusicPlayer;
import sound.MidiMusicPlayer;
import sound.iMusicPlayer;
import unit.Unit;
import util.GLUtils;
import ai.RandomAI;

public final class Herzog3D {

    private HZState curState;
    
    private int screenShotNum = 1;
    private long lastTime;
    private ResourceManager res;
    private boolean exit;
    private iMusicPlayer musicPlayer;

	private Herzog3D(ResourceManager res) throws Exception {
	    this.res = res;
        exit = false;
        musicPlayer = new ALMusicPlayer();
	}
	
	public void runGame(Game game){
        musicPlayer.playRandomSong();
	    lastTime = System.nanoTime();
	    this.curState = game;
	    try {
		    while (!exit && !curState.isFinished()){
				if (!Display.isVisible()) {
				    Thread.sleep(20);
				} else if (Display.isCloseRequested()) {
					exit = true;
				} else {
					mainLoop();
				}
				Display.update();
		    }
	    } catch (InterruptedException ie){}
        cleanup();
	}
	
	
	private void mainLoop() {
        try {
            Thread.sleep(10);
	    } catch (InterruptedException ie){}
        
        curState.onKeyPress(0);
        
		for (int i = 0; i < Keyboard.getNumKeyboardEvents(); i++) {
			Keyboard.next();
			if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()){
				exit = true;
			} else if (Keyboard.getEventKey() == Keyboard.KEY_T && Keyboard.getEventKeyState()){
				System.out.println("Current time: " + Sys.getTime());
			} else if (Keyboard.getEventKey() == Keyboard.KEY_F5){
            	screenShot(screenShotNum++);
            }
		}
		float step = (System.nanoTime() - lastTime)/1000000000.0f;
		lastTime = System.nanoTime();
		curState.update(step);
        curState.draw();

	}

	/**
	 * Initialize
	 */
	private static void init() throws Exception {
	    GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glShadeModel(GL11.GL_SMOOTH);
//		GL11.glPolygonMode(GL11.GL_FRONT,GL11.GL_LINE); 
  		GLUtils.glLightColour(GL11.GL_SPECULAR, 0.1f,0.1f,0.1f, 1.0f);
  		GLUtils.glLightColour(GL11.GL_DIFFUSE, 1.0f,1.0f,1.0f, 1.0f);
  		GLUtils.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, 0.3f,0.3f,0.3f, 1.0f);
		
	 	GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);

        // Set up texture unit 0
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
	 	GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
	 	GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

        // Set up texture unit 1
	 	GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);
        
       
        
	}
	
	public static void displayInit(){
		DisplayMode mode = null;
		try {
			//find first display mode that allows us 640*480*16
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			for (int i = 0; i < modes.length; i++) {
				if (modes[i].getWidth() == 800 && modes[i].getHeight() == 600 && modes[i].getBitsPerPixel() >= 24 ) {
					mode = modes[i];
					break;
				}
			}
			if (mode != null) {
				//select above found displaymode
				System.out.println("Setting display mode to " + mode);
				Display.setDisplayMode(mode);
				//Display.setFullscreen(true);
				//Mouse.setGrabbed(true);
				System.out.println("Created display.");
			} else {
				mode = new DisplayMode(800,600);
				try {
					Display.setDisplayMode(mode);
					Display.create();
					System.out.println("Created OpenGL.");
				} catch (Exception e) {
					System.err.println("Failed to create OpenGL due to " + e);
					System.exit(1);
				}
			}
		} catch (Exception e) {
			System.err.println("Failed to create display due to " + e);
		}
		try {
			Display.create();
			System.out.println("Created OpenGL.");
		} catch (Exception e) {
			System.err.println("Failed to create OpenGL due to " + e);
			System.exit(1);
		}		
		
	}

	/**
	 * Cleanup
	 */
	private void cleanup() {
		Display.destroy();
        ((iGameResource)musicPlayer).cleanup();
	}
	public static void main(String[] arguments) throws Exception {
		displayInit();
		init();
	    ResourceLoader loader = new ResourceLoader();
	    System.out.print("Loading resources...");
	    ResourceManager res = loader.loadResources(new File("data/resources.xml"));
	    System.out.println("done.");
	    Herzog3D hz3d = new Herzog3D(res);
	    Player[] players = new Player[2];
	    players[0] = new Player("Player1",null);
	    players[0].setColour(new Color(255,96,96));
	    players[1] = new Player("Player2",null);
	    players[1].setColour(new Color(96,96,255));
		Game game = new Game(players,"Abgrund",res);
        for (int i = 0; i < 50; i++){
    		Unit unit = res.getUnitFactory().buildUnit((i%2==0)?players[0]:players[1],"Tank");
            float x = (float)Math.random()*48 + 1f;
            float y = (float)Math.random()*48 + 1f;
    		while (game.getUnitAt(x,y,0) != null){
                x = (float)Math.random()*16 + 1f;
                y = (float)Math.random()*16 + 1f;
    		}
    		unit.getPos().set(x,y,0);
    		unit.setAI(new RandomAI(unit));
            game.addUnit(unit);
            
        }
	    System.out.print("Binding textures...");
        res.bindTextures();
	    System.out.println("done.");

		hz3d.runGame(game);
	}

    public void screenShot(int number) {
    	String fileName = "hz_" + number + ".png";
    	int width = Display.getDisplayMode().getWidth();
    	int height = Display.getDisplayMode().getHeight();
        // allocate space for RBG pixels
        ByteBuffer framebytes = ByteBuffer.allocateDirect(width*height*3).order(ByteOrder.nativeOrder());
        int[] pixels = new int[width * height];
        int bindex;
        // grab a copy of the current frame contents as RGB (has to be UNSIGNED_BYTE or colors come out too dark)
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, framebytes);
        // copy RGB data from ByteBuffer to integer array
        for (int i = 0; i < pixels.length; i++) {
            bindex = i * 3;
            pixels[i] = 0xFF000000                                        // A
                      | ((framebytes.get(bindex) & 0x000000FF) << 16)     // R
                      | ((framebytes.get(bindex+1) & 0x000000FF) << 8)    // G
                      | ((framebytes.get(bindex+2) & 0x000000FF) << 0);   // B
        }
        // Create a BufferedImage with the RGB pixels then save as PNG
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, width, height, pixels, 0, width);
        AffineTransform tx = new AffineTransform();
        tx.translate(0,height);
        tx.scale(1,-1);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image,null);

        try {
			ImageIO.write(image, "png", new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
    } 

}