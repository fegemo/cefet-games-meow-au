/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.sound;
import com.badlogic.gdx.audio.Sound;
/**
 *
 * @author Alberto
 */
public class MySound implements AudioResource{

    private final Sound current;
    private long soundId;

    public MySound(Sound current) {
        this.current = current;
    }
       
    public long play(){
        SoundManeger sm = new SoundManeger();
        SoundManeger.add(this);
        if(SoundManeger.getSound()){
            soundId=current.play();
        }else{
            soundId=current.play(0.0f);
        }
        return soundId;
    }
    
    public long play(float volume){
        SoundManeger.add(this);
        if(SoundManeger.getSound()){
            soundId=current.play(volume);
        }else{
            soundId=current.play(0.0f);
        }
        return soundId;
    }
    
    public void pause(){
        current.pause();
    }
    
    public void stop(){
        current.stop();
        SoundManeger.remove(this);
        
    }

    public void setPan(long id, float pan,float vol){
        current.setPan(id,pan, vol);
    }
    
    @Override
    public void setVolume(float vol) {
        current.setVolume(soundId, vol);
    }
    
    public long play(float volume, float pitch, float pan) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public long loop() {
        return current.loop();
    }

    public long loop(float volume) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public long loop(float volume, float pitch, float pan) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void resume() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void dispose() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void stop(long soundId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void pause(long soundId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void resume(long soundId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setLooping(long soundId, boolean looping) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setPitch(long soundId, float pitch) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setVolume(long soundId, float volume) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
