/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.movement;


/**
 *
 * @author Luiza-Pedro
 */
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 *
 * 
 */
public class Pose {

    public Vector2 posicao;
    public Vector2 velocidade;
    public float orientacao;

    public Pose() {
        this(new Vector2( 0, 0), 0);
    }

    public Pose(Vector2 position) {
        this.posicao=position;
        this.velocidade=new Vector2(0, 0);
    }

    public Pose(Vector2 posicao, float orientacao) {
        this.posicao = posicao;
        this.orientacao = orientacao;
    }

    /**
     * Retorna um vetor que representa a direção que o ângulo "orientação" 
     * desta pose representa.
     * @return um vetor na mesma direção que o ângulo.
     */
    public Vector2 getOrientacaoComoVetor() {
        return new Vector2(
                (float) Math.cos(orientacao),
                (float) Math.sin(orientacao));
    }

    /**
     * Define a orientação de forma que ela seja a mesma da direção do vetor
     * velocidade.
     * @param velocidade vetor velocidade, indicando a direção para onde esta
     * pose deve se orientar (rotacionar).
     */
    public void olharNaDirecaoDaVelocidade(Vector2 velocidade) {
        if (velocidade.len2() > 0) {
            orientacao = (float) Math.atan2(velocidade.y, velocidade.x);
        }
    }

    public void atualiza(Direcionamento guia, float delta) {
        // em vez de usar as componentes (ficar "sujando as mãos"), sempre
        // prefira programar de forma encapsulada ;)
        velocidade.add(guia.aceleracao.scl(delta));
        posicao.add(velocidade.scl(delta));
//        posicao.x += guia.velocidade.x * delta;
//        posicao.y += guia.velocidade.y * delta;
//        posicao.z += guia.velocidade.z * delta;
        orientacao += guia.rotacao * delta;
        orientacao = orientacao % ((float) Math.PI * 2);
    }
}
