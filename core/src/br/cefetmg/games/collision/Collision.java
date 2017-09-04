/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.collision;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Thepe
 */
public class Collision {
    private static Vector2 vector_c1;
    private static Vector2 vector_c2;
    /**
     * Verifica se dois círculos em 2D estão colidindo.
     * @param c1 círculo 1
     * @param c2 círculo 2
     * @return true se há colisão ou false, do contrário.
     */
    public static final boolean circlesOverlap(Circle c1, Circle c2) {
        
        vector_c1 = new Vector2(c1.x , c1.y);
        vector_c2 = new Vector2(c2.x , c2.y);
        
        //se a distância entre os centros de cada Circle for menor ou igual a soma dos raios dos
        //Circles, então ocorreu uma colisão(true)
        if( vector_c1.dst(vector_c2) <= (c1.radius + c2.radius)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Verifica se dois retângulos em 2D estão colidindo.
     * Esta função pode verificar se o eixo X dos dois objetos está colidindo
     * e então se o mesmo ocorre com o eixo Y.
     * @param r1 retângulo 1
     * @param r2 retângulo 2
     * @return true se há colisão ou false, do contrário.
     */
    public static final boolean rectsOverlap(Rectangle r1, Rectangle r2) {
        Float a_X_min = r1.x;
        Float a_X_max = r1.x + r1.width;
        Float a_Y_min = r1.y;
        Float a_Y_max = r1.y + r1.height;
        
        Float b_X_min = r2.x;
        Float b_X_max = r2.x + r2.width;
        Float b_Y_min = r2.y;
        Float b_Y_max = r2.y + r2.height;
        
        return (  ( (b_X_min <= a_X_max) && (b_X_max >= a_X_min) ) 
                && ( (b_Y_min <= a_Y_max) && (b_Y_max >= a_Y_min) ) );
    }
}
