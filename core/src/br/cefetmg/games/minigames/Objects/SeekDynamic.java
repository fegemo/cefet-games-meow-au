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
 */
public class SeekDynamic {
    public final int maxAcceleration;
    public final int constantVelocidade;
    public Posicao pos;
    private Vector2 velocidade;
    private Vector2 aceleracao;

    public SeekDynamic(Vector2 posicao) {
        this.maxAcceleration = 10;
        this.constantVelocidade = 10;
        this.velocidade=new Vector2(0, 0);
        this.aceleracao=new Vector2(0, 0);
        pos = new Posicao(posicao);
    }
    
    public Posicao Calculate(Vector2 alvo){//mause clicado
        Vector2 auxAce = alvo;
        alvo.sub(pos.posicao);
        if(auxAce.len2()>maxAcceleration*maxAcceleration){
            auxAce.nor();
            auxAce.scl(maxAcceleration);
        }
        aceleracao = auxAce;
        Vector2 auxVel = velocidade; 
        aceleracao.sub(auxVel.scl(constantVelocidade));//aceleracao = forca/m - kv
        //velocidade+=aceleracao*dt
        //pos+=velocidade*dt
        //rotacao=sentido da velocidade
        return pos;
    }
    public Posicao Calculate(){//nao ha forca externa (mause nao clicado)
        Vector2 auxVel = velocidade; 
        aceleracao = auxVel.scl(constantVelocidade);//aceleracao =  - kv
        //velocidade+=aceleracao*dt
        //pos+=velocidade*dt
        //rotacao=sentido da velocidade
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
        private int rotacao;
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
