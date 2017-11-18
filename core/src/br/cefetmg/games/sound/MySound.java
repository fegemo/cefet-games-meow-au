package br.cefetmg.games.sound;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

/**
 *
 * @author Luiza
 */
public class MySound implements AudioResource, Disposable {

    private final Sound sound;
    private long soundId;
    private float desiredVolume;

    public MySound(Sound sound) {
        this.sound = sound;
        this.desiredVolume = 1.0f;
    }

    public long play() {
        SoundManager.getInstance().addActiveSound(this);
        if (SoundManager.getInstance().isAudioEnabled()) {
            soundId = sound.play();
        } else {
            soundId = sound.play(0.0f);
        }
        return soundId;
    }

    public long play(float volume) {
        desiredVolume = volume;
        SoundManager.getInstance().addActiveSound(this);
        if (SoundManager.getInstance().isAudioEnabled()) {
            soundId = sound.play(volume);
        } else {
            soundId = sound.play(0.0f);
        }
        return soundId;
    }

    public void pause() {
        sound.pause();
    }

    public void stop() {
        sound.stop();
        SoundManager.getInstance().removeActiveSound(this);
    }

    public void setPan(long id, float pan, float vol) {
        sound.setPan(id, pan, vol);
    }

    public long play(float volume, float pitch, float pan) {
        desiredVolume = volume;
        SoundManager.getInstance().addActiveSound(this);
        if (SoundManager.getInstance().isAudioEnabled()) {
            soundId = sound.play(volume, pitch, pan);
        } else {
            soundId = sound.play(0.0f, pitch, pan);
        }
        return soundId;
    }

    public long loop() {
        if (SoundManager.getInstance().isAudioEnabled()) {
            return sound.loop();
        } else {
            return sound.loop(0.0f);
        }
    }

    public long loop(float volume) {
        desiredVolume = volume;
        if (SoundManager.getInstance().isAudioEnabled()) {
            return sound.loop(volume);
        } else {
            return sound.loop(0.0f);
        }
    }

    public long loop(float volume, float pitch, float pan) {
        if (SoundManager.getInstance().isAudioEnabled()) {
            return sound.loop(volume, pitch, pan);
        } else {
            return sound.loop(0.0f, pitch, pan);
        }
    }

    public void resume() {
        sound.resume();
    }

    @Override
    public void dispose() {
        sound.dispose();
    }
    
    @Override
    public void setVolume(float vol) {
        desiredVolume = vol;
        sound.setVolume(soundId, vol);
    }

    @Override
    public void suppressVolume() {
        sound.setVolume(soundId, 0);
    }
    
    @Override
    public void restoreVolume() {
        sound.setVolume(soundId, desiredVolume);
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
