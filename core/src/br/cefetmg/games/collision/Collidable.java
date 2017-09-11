/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.collision;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author Thepe
 */
public interface Collidable {

    /**
     * Verifica se este objeto está colidindo com outro.
     *
     * @param other outro objeto para verificar se este está colidindo.
     * @return true/false se está colidindo ou não.
     */
    boolean collidesWith(Collidable other);

    /**
     * Verifica se este objeto está fora de uma região retangular.
     *
     * @param area área retangular.
     * @return true/false se está pelo menos parcialmente fora da região.
     */
    boolean isOutOfBounds(Rectangle area);

    /**
     * Retorna um retângulo mínimo que contenha a entidade.
     *
     * @return um retângulo.
     */
    Rectangle getMinimumBoundingRectangle();

    /**
     * Retorna um círculo mínimo que contenha a entidade.
     *
     * @return um círculo.
     */
    Circle getMinimumEnclosingBall();
}
