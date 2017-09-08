package br.cefetmg.games;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
/**
 * Utilitário para verificação de colisão.
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */

public class Colision {
    /**
     * Verifica se dois círculos em 2D estão colidindo.
     * @param c1 círculo 1
     * @param c2 círculo 2
     * @return true se há colisão ou false, do contrário.
     */
    public static final boolean circlesOverlap(Circle c1, Circle c2) {
        Vector2 v1, v2;
        v1 = new Vector2(c1.x, c1.y);
        v2 = new Vector2(c2.x, c2.y);
        double distance2 = v1.dst2(v2);
        if(distance2>((c1.radius+c2.radius)*(c1.radius+c2.radius)))
            return false;
        else
            return true;
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
        float r1XMin = r1.x, r1XMax = r1.x + r1.width, r1YMin = r1.y, r1YMax = r1.y + r1.height;
        float r2XMin = r2.x, r2XMax = r2.x + r2.width, r2YMin = r2.y, r2YMax = r2.y + r2.height;
        
        if( (r1XMax>=r2XMin && r1XMin<=r2XMax) && (r1YMax>=r2YMin && r1YMin<=r2YMax))
            return true;
        else
            return false;
    }
    
    public static final Vector2 rectCircleOverlap(Rectangle r1, Circle c1) {
        float r1XCenter = r1.x + (r1.width/2), r1YCenter = r1.y + (r1.height/2);
        float horizontalDistance = abs(c1.x-r1XCenter);
        float verticalDistance = abs(c1.y-r1YCenter);
        float pXCoordinate;
        float pYCoordinate;
        
        //Clamp operation
        if(horizontalDistance>(r1.width/2))
            horizontalDistance = r1.width/2;
        if(verticalDistance>(r1.height/2))
            verticalDistance = r1.height/2;
        
        //Finding p Point Coordinates
        if(c1.x>r1XCenter)
            pXCoordinate = r1XCenter + horizontalDistance;
        else
            pXCoordinate = r1XCenter - horizontalDistance;
        if(c1.y>r1YCenter)
            pYCoordinate = r1YCenter + verticalDistance;
        else
            pYCoordinate = r1YCenter - verticalDistance;
        
        Vector2 c1Center = new Vector2(c1.x, c1.y);
        Vector2 pPoint = new Vector2(pXCoordinate, pYCoordinate);
        
        float distance2 = c1Center.dst2(pPoint);
        
        if(distance2<=(c1.radius*c1.radius))
            return pPoint;
        else
            return null;
    }
}
