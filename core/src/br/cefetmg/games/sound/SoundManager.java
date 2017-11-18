package br.cefetmg.games.sound;

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
}
