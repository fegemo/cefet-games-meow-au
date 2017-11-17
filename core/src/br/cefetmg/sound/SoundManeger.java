/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.sound;

import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author Alberto
 */
public class SoundManeger {
    private static boolean sound=true;
    private static final HashSet<AudioResource> audios=new HashSet<AudioResource>();
  
    public static void disableSounds(){
        sound=false;
        for (AudioResource audio : audios) {
            audio.setVolume(0.0f);
        }
    }

    public static void enableSounds(){
        sound=true;
        for (AudioResource audio : audios) {
            audio.setVolume(1.0f);
        }
    }

    public static void add(AudioResource audio){
        audios.add(audio);
    }

    public static void remove(AudioResource audio){
        audios.remove(audio);
    }

    public static boolean getSound(){
        return SoundManeger.sound;
    }
}
