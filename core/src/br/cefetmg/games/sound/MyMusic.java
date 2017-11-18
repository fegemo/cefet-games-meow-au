package br.cefetmg.games.sound;

import com.badlogic.gdx.audio.Music;

/**
 *
 * @author Luiza
 */
public class MyMusic implements AudioResource {

    private final Music current;

    public MyMusic(Music current) {
        this.current = current;
    }

    public void play() {
        SoundManager.getInstance().addActiveSound(this);
        if (SoundManager.getInstance().isAudioEnabled()) {
            current.setVolume(1.0f);
            current.play();
        } else {
            current.setVolume(0);
            current.play();
        }
    }

    public void stop() {
        current.stop();
        SoundManager.getInstance().removeActiveSound(this);
    }

    @Override
    public void setVolume(float vol) {
        current.setVolume(vol);
    }

    public void pause() {
        current.pause();
    }

    public boolean isPlaying() {
        return current.isPlaying();
    }

    public void setLooping(boolean isLooping) {
        current.setLooping(isLooping);
    }

//    public boolean isLooping() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public float getVolume() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    
//    public void setPan(float pan, float volume) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public void setPosition(float position) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public float getPosition() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    public void dispose() {
        current.dispose();
    }

}
