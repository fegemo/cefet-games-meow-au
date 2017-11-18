package br.cefetmg.games.sound;

/**
 *
 * @author Luiza
 */
public interface AudioResource {

    void setVolume(float vol);

    void suppressVolume();

    void restoreVolume();
}
