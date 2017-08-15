package br.cefetmg.games.minigames.util;

/**
 * Define o que acontece quando do término do minigame por causa de tempo 
 * esgotado.
 * 
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public enum TimeoutBehavior {
    /**
     * Jogador vence o desafio quando o tempo acaba e ele não foi derrotado.
     * 
     * Basicamente, ele deve sobreviver/impedir que algo aconteça até o final.
     */
    WINS_WHEN_MINIGAME_ENDS,
    
    /**
     * Jogador perde o desafio quando o tempo acaba e ele não conseguiu 
     * executar o que o minigame propôs.
     * 
     * Basicamente, o jogador precisa fazer uma atividade dentro do limite de
     * tempo do minigame.
     */
    FAILS_WHEN_MINIGAME_ENDS
}
