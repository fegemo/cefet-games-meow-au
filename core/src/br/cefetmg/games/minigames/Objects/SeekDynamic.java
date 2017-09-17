/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.Objects;

import com.badlogic.gdx.math.Vector2;
import java.util.Vector;

/**
 *
 * @author Alberto
 * 
 * Input -> posição do clique do mouse na tela , posição do peixe
 * Output -> vetor velocidade do peixe que aponta para a posição do clique
 * 
 * A ideia é calcular  distância e 
 * 
 */
public class SeekDynamic {
    public final int maxAcceleration;
    public final int constantVelocidade;
    public Posicao pos;
    private Vector2 velocidade;
    private Vector2 aceleracao;

    public SeekDynamic(Vector2 posicao) {
        this.maxAcceleration = 500;
        this.constantVelocidade = 50;
        this.velocidade=new Vector2(0, 0);
        this.aceleracao=new Vector2(0, 0);
        pos = new Posicao(posicao);
    }
    
    public Posicao Calculate(Vector2 alvo, float dt){//mause clicado
        Vector2 auxAce = new Vector2(alvo.x,alvo.y);
        alvo.sub(pos.posicao);
        if(auxAce.len2()>maxAcceleration*maxAcceleration){
            auxAce.nor();
            auxAce.scl(maxAcceleration);
        }
        aceleracao = auxAce;
        Vector2 auxVel = new Vector2(velocidade.x,velocidade.y); 
        aceleracao.sub(auxVel.scl(constantVelocidade));//aceleracao = forca/m - kv
        Vector2 auxVel2 = new Vector2(velocidade.x,velocidade.y); 
        Vector2 auxAce2 = new Vector2(aceleracao.x, aceleracao.y);
        auxVel2.add(auxAce2.scl(dt));//velocidade+=aceleracao*dt;
        velocidade=auxVel2;
        Vector2 auxPos = new Vector2(pos.posicao.x, pos.posicao.x);
        auxPos.add(auxVel2.scl(dt));//pos+=velocidade*dt
        pos.setPosicao(auxPos);
        pos.rotacao=velocidade.angleRad();//rotacao=sentido da velocidade
        return pos;
    }
    public Posicao Calculate(float  dt){//nao ha forca externa (mause nao clicado)
        Vector2 auxVel = velocidade; 
        aceleracao = auxVel.scl(constantVelocidade);//aceleracao =  - kv
        
        
        Vector2 auxVel2 = new Vector2(velocidade.x,velocidade.y); 
        Vector2 auxAce2 = new Vector2(aceleracao.x, aceleracao.y);
        auxVel2.add(auxAce2.scl(dt));//velocidade+=aceleracao*dt;
        velocidade=auxVel2;
        Vector2 auxPos = new Vector2(pos.posicao.x, pos.posicao.x);
        auxPos.add(auxVel2.scl(dt));//pos+=velocidade*dt
        pos.setPosicao(auxPos);
        pos.rotacao=velocidade.angleRad();//rotacao=sentido da velocidade
        return pos;
    }
    
    public void setAceleracao(Vector2 aceleracao) {
        this.aceleracao = aceleracao;
    }

    public Posicao getPos() {
        return pos;
    }
    
    
    class Posicao{
        private Vector2 posicao;
        private float rotacao;
        public Posicao(Vector2 posicao) {
            this.posicao = posicao;
            this.rotacao = 0;
        }
        public Vector2 getPosicao() {
            return posicao;
        }

        public void setPosicao(Vector2 posicao) {
            this.posicao = posicao;
        }

        public void setRotacao(int rotacao) {
            this.rotacao = rotacao;
        }         
    }
    
    
}
