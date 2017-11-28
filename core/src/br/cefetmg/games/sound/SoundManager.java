package br.cefetmg.games.sound;

import com.badlogic.gdx.Gdx;
import java.util.HashSet;

/**
 * Gerencia a habilitação/desabilitação dos sons e músicas do jogo.
 *
 * @author Luiza
 */
public class SoundManager {

    private boolean audioEnabled;
    private final HashSet<AudioResource> audios;
    private static SoundManager INSTANCE;
    private MyMusic backgroundMusic;

    
    /**
     * Construtor privado pra evitar que esta classe seja instanciada fora dela
     * mesma. Feito para forçarmos o uso do padrão de projeto singleton.
     */
    private SoundManager() {
        audioEnabled = true;
        audios = new HashSet<AudioResource>();
    }

    /**
     * Retorna a (única) instância da classe.
     *
     * @return a instância da classe.
     */
    public static SoundManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SoundManager();
        }
        return INSTANCE;
    }

    public void disableSounds() {
        audioEnabled = false;
        for (AudioResource audio : audios) {
            audio.suppressVolume();
        }
    }

    public void enableSounds() {
        audioEnabled = true;
        for (AudioResource audio : audios) {
            audio.restoreVolume();
        }
    }

    public boolean isAudioEnabled() {
        return audioEnabled;
    }

    /**
     * Coloca um áudio na lista de sons ativos. Visibilidade de pacote.
     * @param audio 
     */
    void addActiveSound(AudioResource audio) {
        audios.add(audio);
    }

    /**
     * Remove um áudio da lista de sons ativos. Visibilidade de pacote.
     * @param audio 
     */
    void removeActiveSound(AudioResource audio) {
        audios.remove(audio);
    }
    
    /**
     * Toca uma música de fundo que pode continuar nas telas seguintes. Se
     * o método for chamado mais de uma vez para a mesma música, ela 
     * simplesmente continua tocando. Se for uma música nova, a anterior é
     * interrompida e a nova é iniciada.
     * @param musicPath o nome (caminho) para o arquivo da música.
     * @return retorna o objeto MyMusic com a música que passou a tocar, ou a
     * música que já estava tocando.
     */
    public MyMusic playBackgroundMusic(String musicPath) {
        boolean isItANewSound = true;
        
        if (backgroundMusic != null) {
            String currentMusicPath = backgroundMusic.getAssetPath();
            
            // a nova música solicitada é a mesma que já está tocando?
            // se for diferente, precisamos trocá-la
            if (!musicPath.equals(currentMusicPath)) {
                removeActiveSound(backgroundMusic);
            } else {
                // não faz nada, porque pediu-se para tocar a mesma música que
                // já estava tocando
                isItANewSound = false;
            }
        }
        
        if (isItANewSound) {
            backgroundMusic = new MyMusic(
                    Gdx.audio.newMusic(Gdx.files.internal(musicPath)), musicPath);
            backgroundMusic.play();
            addActiveSound(backgroundMusic);            
        }
        
        return backgroundMusic;
    }
    
    /**
     * Pára a música de fundo, se tiver uma tocando.
     * @param musicPath o nome (caminho) para o arquivo da música.
     */
    public void stopBackgroundMusic(String musicPath) {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            removeActiveSound(backgroundMusic);
            backgroundMusic = null;
        }
    }
}
