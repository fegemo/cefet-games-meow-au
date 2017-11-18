package br.cefetmg.games.sound;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Disposable;

/**
 *
 * @author Luiza
 */
public class MyMusic implements AudioResource, Disposable {

    private final Music music;
    private float desiredVolume;

    public MyMusic(Music current) {
        this.music = current;
        this.desiredVolume = 1.0f;
    }

    public void play() {
        SoundManager.getInstance().addActiveSound(this);
        if (SoundManager.getInstance().isAudioEnabled()) {
            music.setVolume(desiredVolume);
            music.play();
        } else {
            music.setVolume(0);
            music.play();
        }
    }

    public void stop() {
        music.stop();
        SoundManager.getInstance().removeActiveSound(this);
    }

    public void pause() {
        music.pause();
    }

    public boolean isPlaying() {
        return music.isPlaying();
    }

    public void setLooping(boolean isLooping) {
        music.setLooping(isLooping);
    }
    
    @Override
    public void setVolume(float vol) {
        desiredVolume = vol;
        music.setVolume(vol);
    }

    @Override
    public void suppressVolume() {
        music.setVolume(0);
    }
    
    @Override
    public void restoreVolume() {
        music.setVolume(desiredVolume);
    }

    @Override
    public void dispose() {
        music.dispose();
    }

}
