package br.cefetmg.games.sound;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Disposable;

/**
 *
 * @author Luiza
 */
public class MyMusic implements AudioResource, Disposable {

    private final Music music;
    private final String assetPath;
    private float desiredVolume;

    public MyMusic(Music music) {
        this(music, "");
    }
    
    public MyMusic(Music music, String assetPath) {
        this.music = music;
        this.assetPath = assetPath;
        this.desiredVolume = 1.0f;
    }

    public void play() {
        SoundManager.getInstance().addActiveSound(this);
        if (SoundManager.getInstance().isAudioEnabled()) {
            music.setVolume(desiredVolume);
            music.play();
        } else {
            music.setVolume(0.0f);
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
        if (SoundManager.getInstance().isAudioEnabled()) {
            music.setVolume(vol);
        }
    }

    @Override
    public void suppressVolume() {
        music.setVolume(0.0f);
    }
    
    @Override
    public void restoreVolume() {
        music.setVolume(desiredVolume);
    }
    
    public String getAssetPath() {
        return assetPath;
    }

    @Override
    public void dispose() {
        music.dispose();
    }

    public void setOnCompletionListener(Music.OnCompletionListener listener){
        music.setOnCompletionListener(listener);
    }

}
