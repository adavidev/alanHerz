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
import hud.HZWidget;
import hud.ManagerOverlay;
import hud.StatusOverlay;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import map.Base;
import map.GameMap;

import org.jdom.JDOMException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import resource.Material;
import resource.ResourceLoader;
import resource.ResourceManager;
import resource.ResourceNotFoundException;
import unit.Mech;
import unit.Unit;
import unit.UnitFactory;
import util.GLUtils;
import util.GameMath;
import util.Vector3f;
import ai.RandomAI;
import ai.UnitAI.UnitMove;
import ai.UnitAI.UnitTurn;
import effects.Effect;

/*
 * Created on 26/05/2005
 */

/**
 * @author Shannon Smith
 */
public class Game extends HZState {

    private GameMap map;
    private final List<HZObject> objects;
    private final List<Unit> units;
    private final List<Effect> effects;
    private final List<HZWidget> overlays;
    private final List<Effect> newEffects;
    private final List<Player> players;
    private final List<Base> bases;
    private final Map<Player,UnitOrder> unitOrders;
    private final Map<Player,Mech> mechs;
    private final Player player;
    private final ManagerOverlay managerOverlay;
    private final ResourceManager resManager;
    private final UnitFactory unitFactory;
    private boolean finished;
    private Camera cam;
    
    private static Game instance = null;
    
    public static Game getInstance()
    {
    	if (instance == null)
    	{
    		instance = new Game();
    	}
    	return instance;
    }
    
    public Game() {
    	ResourceLoader loader = new ResourceLoader();
	    System.out.print("Loading resources...");
	    ResourceManager res = null;
			try {
				res = loader.loadResources(new File("data/resources.xml"));
			} catch (ResourceNotFoundException | IOException | JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	    System.out.println("done.");
    	
    	Player[] players = new Player[2];
	    players[0] = new Player("Player1",null);
	    players[0].setColour(new Color(255,96,96));
	    players[1] = new Player("Player2",null);
	    players[1].setColour(new Color(96,96,255));
    	
    	map = res.getMap("Abgrund");
        this.cam = new Camera(this);
        this.resManager = res;
        this.player = players[0];
        
        objects = new LinkedList<HZObject>();
        bases = new ArrayList<Base>();
        units = new ArrayList<Unit>();
        mechs = new HashMap<Player,Mech>();
        effects = new LinkedList<Effect>();
        newEffects = new ArrayList<Effect>();
        overlays = new ArrayList<HZWidget>();
        unitOrders = new HashMap<Player,UnitOrder>();
        this.players = new ArrayList<Player>();
        unitFactory = resManager.getUnitFactory();
        Collections.addAll(this.players,players);

        Iterator<String> playerLocations = map.getPlayerTemplates().iterator();
        
        for(int i = 0; i < players.length;i++){
        	if (!playerLocations.hasNext()){
        		throw new IllegalStateException("Too many players for map!");
        	}
            Mech mech = new Mech(players[i],resManager);
            mech.setAI(players[i]);
            players[i].setMech(mech);
            players[i].setUnit(mech);
            mechs.put(players[i],mech);
            units.add(mech);
            String locID = playerLocations.next();
            System.out.println("Adding player at: " + locID);
            bases.addAll(map.createPlayerBases(players[i], locID, resManager));
            objects.add(mech);
            mech.setPos(players[i].getHomeBase().getCenter());
            mech.move(0,0,4);
        }
        cam.setMech(players[0].getMech());
        managerOverlay = new ManagerOverlay(this);
        managerOverlay.setGeometry(208,64,384,416);
        overlays.add(new StatusOverlay(resManager,players[0]));
        overlays.add(managerOverlay);
        
        for (int i = 0; i < 50; i++){
    		Unit unit = res.getUnitFactory().buildUnit((i%2==0)?players[0]:players[1],"Tank");
            float x = (float)Math.random()*48 + 1f;
            float y = (float)Math.random()*48 + 1f;
    		while (this.getUnitAt(x,y,0) != null){
                x = (float)Math.random()*16 + 1f;
                y = (float)Math.random()*16 + 1f;
    		}
    		unit.getPos().set(x,y,0);
    		unit.setAI(new RandomAI(unit));
            this.addUnit(unit);
        }
        System.out.print("Binding textures...");
        res.bindTextures();
	    System.out.println("done.");
    }

	public void orderUnit(Player player, String unitName){
    	unitOrders.put(player, new UnitOrder(unitName,unitFactory.getTimeToBuild(unitName)));
    	player.setUnitOrder(unitOrders.get(player));
    }
    
    public Unit pickupOrder(Player player){
    	UnitOrder order = unitOrders.remove(player);
    	return resManager.getUnitFactory().buildUnit(player, order.getUnitName());
    }
    
    public Player getPlayer(){
    	return player;
    }
    
    public Player getWinner(){
    	if (!finished){
    		return null;
    	} else {
    		for(Player player : players){
    			if (player.getHomeBase().isAlive()){
    				return player;
    			}
    		}
    	}
    	return null;
    }

    public ResourceManager getResources(){
    	return resManager;
    }
    
    public GameMap getMap(){
        return map;
    }
    
    public Collection<Unit> getUnits(){
        return Collections.unmodifiableCollection(units);
    }
    
    public Collection<Mech> getMechs(){
        return Collections.unmodifiableCollection(mechs.values());
    }
    
    public Collection<Effect> getEffects(){
        return Collections.unmodifiableList(effects);
    }
    
    public Collection<Base> getBases(){
    	return bases;
    }
    
    public void removeUnit(Unit unit){
        units.remove(unit);
    }
    
    public void addUnit(Unit unit){
        units.add(unit);
        objects.add(unit);
    }
    
    public boolean hasFinished(){
        return finished;
    }
    
    public String getNextState(){
        return "GameFinished";
    }

    public void addEffect(Effect e){
        newEffects.add(e);
    }
    
    public void update(float step){
        cam.update(step);
        
        for(Iterator<Unit> i = units.iterator(); i.hasNext();){
        	Unit unit = i.next();
        	if (unit.getArmour() < 0){
        		i.remove();
        	} else if (unit.isActive()) {
            	unit.update(this,step);
        	}
        }
        
        satisfyConstraints();
        
//        for(Mech mech : mechs.values()){
//            mech.update(this,step);
//        }
        for(Iterator<Effect> i = effects.iterator(); i.hasNext();){
        	Effect effect = i.next();
            effect.update(this,step);
            if (!effect.isActive()){
                i.remove();
            }
        }
        for(UnitOrder order : unitOrders.values()){
            order.update(step);
        }
        effects.addAll(newEffects);
        newEffects.clear();
        
        if (managerOverlay.isVisible()){
        	managerOverlay.update(step);
        }
        
        int survivingPlayers = 0;
        for(Player player : players){
        	if (player.getHomeBase() != null && player.getHomeBase().isAlive()){
        		survivingPlayers++;
        	}
        }
        if (survivingPlayers < 2){
        	finished = true;
        }
        
    }
    
    public void satisfyConstraints(){
    	for(int i = 0; i < 10; i++){
	    	for(HZObject obj1 : objects){
	    		for(HZObject obj2 : objects){
	    			Vector3f v = obj1.intersects(obj2);
	    			if (v != null){
	    				v.scale(0.2f);
	    				obj1.move(v);
	    			}
	    		}
	    		for(HZObject base : bases){
	    			Vector3f v = obj1.intersects(base);
	    			if (v != null){
	    				//v.scale(0.9f);
	    				obj1.move(v);
	    			}
	    		}
	    	}
    	}
    }
    
    public Unit getUnitAt(float x, float y, float tolerance){
    	for(Unit unit : units){
    		if (!(unit instanceof Mech) && GameMath.dist(unit.getPos().x,unit.getPos().y,x,y) < (unit.getRadius() + tolerance)){
    			return unit;
    		}
    	}
    	return null;
    }
    
    /**
     * All rendering is done in here
     */
    public void draw() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
        cam.transform();
        GLUtils.glLightPos(1.0f,1.0f,1.0f);
        GL11.glColor3f(1.0f,1.0f,1.0f);
        getMap().draw(cam);

        for (Unit unit : units){
        	if (unit.isActive()){
        		unit.draw();
        	}
        }
        
//        for (Mech mech : mechs.values()){
//        	if (mech.isActive()){
//        		mech.draw(this);
//        	}
//        }
        for (Base base : bases){
       		base.draw(map);
        }
        for(Effect effect : effects){
            effect.draw();
        }
        
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0,800, 600, 0, 0, 1) ;
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        Material.DEFAULT_WHITE.bind();
        for(HZWidget overlay : overlays){
        	if (overlay.isVisible()){
        		overlay.draw();
        	}
        }
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        
    }

    public void onKeyPress(int key){
    	while (Keyboard.next())
		{
			if (Keyboard.getEventKeyState())
			{
				if (Keyboard.getEventKey() == Keyboard.KEY_UP)
				{
					player.setMove(UnitMove.MOVE_FORWARD);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_DOWN)
				{
					player.setMove(UnitMove.MOVE_BACKWARD);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_LEFT)
				{
					player.setTurn(UnitTurn.TURN_LEFT);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT)
				{
					player.setTurn(UnitTurn.TURN_RIGHT);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_A)
				{
					player.setTransform(true);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_S)
				{
					player.setFire(true);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_D)
				{
					managerOverlay.setVisible(!managerOverlay.isVisible());
		    		player.setMove(UnitMove.MOVE_STOP);
		    		player.setTurn(UnitTurn.TURN_STOP);
				}
			} else
			{
				if (Keyboard.getEventKey() == Keyboard.KEY_UP)
				{
					player.setMove(UnitMove.MOVE_STOP);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_DOWN)
				{
					player.setMove(UnitMove.MOVE_STOP);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_LEFT)
				{
					player.setTurn(UnitTurn.TURN_STOP);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT)
				{
					player.setTurn(UnitTurn.TURN_STOP);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_A)
				{
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_S)
				{
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_D)
				{
				}
			}
		}
    }

    public void onKeyRelease(HZKey key) {

	}

	public boolean isFinished() {
		return finished;
	}
	
	public Base getBase(float x, float y){
		for(Base base : bases){
			if(base.isInTile((int)x,(int)y)){
				return base;
			}
		}
		return null;
	}

    
}
