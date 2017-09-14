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
     *
     * @param c1 círculo 1
     * @param c2 círculo 2
     * @return true se há colisão ou false, do contrário.
     */
    public static final boolean circlesOverlap(Circle c1, Circle c2) {

        vector_c1 = new Vector2(c1.x, c1.y);
        vector_c2 = new Vector2(c2.x, c2.y);

        //se a distância entre os centros de cada Circle for menor ou igual a soma dos raios dos
        //Circles, então ocorreu uma colisão(true)
        if (vector_c1.dst2(vector_c2) <= ((c1.radius + c2.radius)*(c1.radius + c2.radius))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifica se dois retângulos em 2D estão colidindo. Esta função pode
     * verificar se o eixo X dos dois objetos está colidindo e então se o mesmo
     * ocorre com o eixo Y.
     *
     * @param r1 retângulo 1
     * @param r2 retângulo 2
     * @return true se há colisão ou false, do contrário.
     */
     /**
     * Verifica se dois retângulos em 2D estão colidindo.
     * Esta função pode verificar se o eixo X dos dois objetos está colidindo
     * e então se o mesmo ocorre com o eixo Y.
     * @param a
     * @param b
     * @param r1 retângulo 1
     * @param r2 retângulo 2
     * @return true se há colisão ou false, do contrário.
     */
    public static final boolean lineOverlap(float aMin, float  aMax, float bMin, float  bMax){
        //vector2 x=max y=min
        if(aMin<bMin && bMin<=aMax)
            return true;
        return bMin<aMin && aMin<=bMax;
    }
    
    /*
    public static final boolean lineOverlap(float aMin, float aMax, float bMin, float bMax){
        return aMax >= bMin && aMin <= bMax;
    }
    public static final boolean rectsOverlap(Rectangle r1, Rectangle r2) {
        return lineOverlap(, 0, 0, 0)
    }*/
    public static final boolean rectsOverlap(Rectangle r1, Rectangle r2) {
        if(lineOverlap(r1.x,r1.width+r1.x,r2.x,r2.width+ r2.x))
            return lineOverlap(r1.y,r1.height+r1.y, r2.y,r2.height+r2.y);    
        return false;
    }
    
    public static final boolean circleRectCollision( Rectangle r2,Circle c1){
        Vector2 ponto = new Vector2();
        Vector2 centroRet = new Vector2(r2.x+r2.width/2,r2.y+r2.height/2);
        //distancia entre centro do circulo e o centro do quadrado
        Vector2 dist = centroRet.sub(c1.x, c1.y);
        Vector2 eixoX = new Vector2(dist.x,0);
        Vector2 eixoY = new Vector2(0,dist.y);
        //"clamped " acha um ponto na superficie do quadrado q é menor q a distancia
        eixoX.clamp(0, r2.width/2);
        eixoY.clamp(0, r2.height/2);
        ponto.x=eixoX.x;ponto.y=eixoY.y;
        
        
        //usa a funcao do circulo com o circulo e o ponto
        return circlesOverlap(c1, new Circle(ponto, 0) );
    }
}