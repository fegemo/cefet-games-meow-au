/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.movement;

import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author 
 */
class Direcionamento {
    public Vector2 aceleracao;  // velocidade linear
    public double rotacao;      // velocidade angular
    
    public Direcionamento() {
        aceleracao = new Vector2();
        rotacao = 0;
    }
}
