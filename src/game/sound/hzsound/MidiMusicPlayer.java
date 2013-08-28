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
package game.sound.hzsound;

import game.sound.core.iMusicPlayer;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

import resource.ResourceManager;

public class MidiMusicPlayer implements iMusicPlayer {

    private Synthesizer synth;
    private Sequencer sequencer;
    private ResourceManager resManager;
    
    public MidiMusicPlayer(ResourceManager resManager){
    	this.resManager = resManager;
    }
    
    public void init(){
      sequencer = null;
      try {
          sequencer = MidiSystem.getSequencer();
          sequencer.open();
      } catch (MidiUnavailableException e) {
          e.printStackTrace();
      }
    }

    public void playSong(String name){
        if (sequencer.isRunning()){
            sequencer.stop();
        }
        Sequence seq = resManager.getSong(name);
        if (seq != null){
            try {
                sequencer.setSequence(seq);
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }
            sequencer.start();
        }
    }
    
    public void playRandomSong(){
        int songNum = (int)(Math.random()*resManager.getSongCount());
        for(String str : resManager.getAllSongNames()){
            if (songNum == 0){
                playSong(str);
            }
            songNum--;
        }
    }
    
    public void stopPlaying(){
        sequencer.stop();
    }
    
    public void cleanUp(){
        stopPlaying();
        sequencer.close();
    }
}
