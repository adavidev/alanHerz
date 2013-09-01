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
import game.core.DefaultOpenGLResource;
import game.core.GloballyManagedGame;
import game.core.LocalGameTime;
import game.core.hzsound.ALMusicPlayer;
import game.core.sound.iMusicPlayer;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import resource.ResourceLoader;
import resource.ResourceManager;
import unit.Unit;
import ai.RandomAI;

public class Herzog3D extends GloballyManagedGame {

    private HZState curState;
    
    private int screenShotNum = 1;
    private ResourceManager res;
    private iMusicPlayer musicPlayer;

	private Herzog3D(ResourceManager res) throws Exception {
	    this.res = res;
	    DefaultOpenGLResource gl = new DefaultOpenGLResource();
	}

	/**
	 * Initialize
	 */
	@Override
	protected void init() {
        gameSpecificCrap();
        super.init();
	}

	private void gameSpecificCrap() {
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
	    curState = game;
	    
	    musicPlayer = new ALMusicPlayer();
	}

	public static void main(String[] arguments) throws Exception {
		ResourceLoader loader = new ResourceLoader();
	    System.out.print("Loading resources...");
	    ResourceManager res = loader.loadResources(new File("data/resources.xml"));
	    System.out.println("done.");
		
		Herzog3D hz3d = new Herzog3D(res);

		hz3d.start();
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

	@Override
	protected void update() {
        
        
        curState.onKeyPress(0);

		float step = (float) LocalGameTime.getDelta();
		curState.update(step);
        curState.draw();
        super.update();
	}

	@Override
	protected void destroy() {
		super.destroy();
	}

}