package br.cefetmg.games.sound;

import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author Luiza
 */
public class MySound implements AudioResource {

    private final Sound current;
    private long soundId;

    public MySound(Sound current) {
        this.current = current;
    }

    public long play() {
        SoundManager.getInstance().addActiveSound(this);
        if (SoundManager.getInstance().isAudioEnabled()) {
            soundId = current.play();
        } else {
            soundId = current.play(0.0f);
        }
        return soundId;
    }

    public long play(float volume) {
        SoundManager.getInstance().addActiveSound(this);
        if (SoundManager.getInstance().isAudioEnabled()) {
            soundId = current.play(volume);
        } else {
            soundId = current.play(0.0f);
        }
        return soundId;
    }

    public void pause() {
        current.pause();
    }

    public void stop() {
        current.stop();
        SoundManager.getInstance().removeActiveSound(this);
    }

    public void setPan(long id, float pan, float vol) {
        current.setPan(id, pan, vol);
    }

    @Override
    public void setVolume(float vol) {
        current.setVolume(soundId, vol);
    }

    public long play(float volume, float pitch, float pan) {
        SoundManager.getInstance().addActiveSound(this);
        if (SoundManager.getInstance().isAudioEnabled()) {
            soundId = current.play(volume, pitch, pan);
        } else {
            soundId = current.play(0.0f, pitch, pan);
        }
        return soundId;
    }

    public long loop() {
        if (SoundManager.getInstance().isAudioEnabled()) {
            return current.loop();
        } else {
            return current.loop(0.0f);
        }
    }

    public long loop(float volume) {
        if (SoundManager.getInstance().isAudioEnabled()) {
            return current.loop(volume);
        } else {
            return current.loop(0.0f);
        }
    }

    public long loop(float volume, float pitch, float pan) {
        if (SoundManager.getInstance().isAudioEnabled()) {
            return current.loop(volume, pitch, pan);
        } else {
            return current.loop(0.0f, pitch, pan);
        }
    }

    public void resume() {
        current.resume();
    }

    public void dispose() {
        current.dispose();
    }

//    public void stop(long soundId) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public void pause(long soundId) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//    
//    public void resume(long soundId) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//}
//
//    public void setLooping(long soundId, boolean looping) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public void setPitch(long soundId, float pitch) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public void setVolume(long soundId, float volume) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
}
