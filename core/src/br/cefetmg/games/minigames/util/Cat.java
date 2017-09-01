/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.util;

/**
 *
 * @author NataliaNatsumy
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;


public class Cat extends AnimatedSprite {


    
    Texture spriteSheet;

    TextureRegion[][] quadrosDaAnimacao;
    
    Animation animacaoAtual;
    Animation normal;
    Animation lancarPoder;
    Animation chutar;
    Animation socar;    
    Animation dancar;
    Animation morrer;
    
    float tempoDaAnimacao;
    
    private Sprite jogador;

    
    static final private int parado = 1;
    static final private int FRAME_WIDTH = 60;
    static final private int FRAME_HEIGHT = 20;

    public Cat(final Texture spriteSheet){

        super(new Animation(0.1f, new Array<TextureRegion>() {
            {
                TextureRegion[][] frames = TextureRegion.split(
                        spriteSheet, FRAME_WIDTH, FRAME_HEIGHT);
                super.addAll(new TextureRegion[]{
                        frames[0][0],
                        frames[0][1],
                        frames[0][2]
                });
            }
        }));
        super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
        super.setAutoUpdate(false);
        //this.jogador=jogador;

 //       quadrosDaAnimacao = TextureRegion.split(spriteSheet, 64, 64);

/*        normal = new Animation(0.1f,
=======
    public int x = 30;
    public int y = 10;
    
    private int parado = 1;
    
    public Cat(Texture spriteSheet){
        //this.jogador=jogador;
        this.spriteSheet = spriteSheet;
        
        spriteSheet = new Texture("sprite-cat.png");
        quadrosDaAnimacao = TextureRegion.split(spriteSheet, 64, 64);
        
        normal = new Animation(0.1f,
>>>>>>> dac944b518db7300ed9b8f12930a1286c2713248
          quadrosDaAnimacao[0][0], // 1ª linha, 1ª coluna
          quadrosDaAnimacao[0][1], // idem, 2ª coluna
          quadrosDaAnimacao[0][2],
          quadrosDaAnimacao[0][3]);
        
        lancarPoder = new Animation(0.1f,
          quadrosDaAnimacao[5][0], // 1ª linha, 1ª coluna
          quadrosDaAnimacao[5][1], // idem, 2ª coluna
          quadrosDaAnimacao[5][2],
          quadrosDaAnimacao[5][3],
          quadrosDaAnimacao[5][4],
          quadrosDaAnimacao[5][5],
          quadrosDaAnimacao[5][6]);
        
        
        chutar = new Animation(0.1f,
          quadrosDaAnimacao[7][0], // 1ª linha, 1ª coluna
          quadrosDaAnimacao[7][1], // idem, 2ª coluna
          quadrosDaAnimacao[7][2],
          quadrosDaAnimacao[7][3],
          quadrosDaAnimacao[7][4],
          quadrosDaAnimacao[7][5],
          quadrosDaAnimacao[7][6],
          quadrosDaAnimacao[7][7]);
        
        
        socar = new Animation(0.1f,
          quadrosDaAnimacao[6][0], // 1ª linha, 1ª coluna
          quadrosDaAnimacao[6][1], // idem, 2ª coluna
          quadrosDaAnimacao[6][2],
          quadrosDaAnimacao[6][3],
          quadrosDaAnimacao[6][4],
          quadrosDaAnimacao[6][5]);
        
        morrer = new Animation(0.1f,
          quadrosDaAnimacao[4][0], // 1ª linha, 1ª coluna
          quadrosDaAnimacao[4][1], // idem, 2ª coluna
          quadrosDaAnimacao[4][2],
          quadrosDaAnimacao[4][3],
          quadrosDaAnimacao[4][4],
          quadrosDaAnimacao[4][5],
          quadrosDaAnimacao[4][6],
          quadrosDaAnimacao[4][7],
          quadrosDaAnimacao[4][8]);
        
        animacaoAtual=normal;
        tempoDaAnimacao = 0;
<<<<<<< HEAD
        //jogador.setPosition(x, y);(*/

    }

    Vector2 getHeadPosition() {
        return new Vector2(
                this.getX() + this.getWidth() * 0.5f,
                this.getY() + this.getHeight() * 0.8f);
    }

    float getHeadDistanceTo(float enemyX, float enemyY) {
        return getHeadPosition().dst(enemyX, enemyY);
    }

  /*  public void update() {//int x, int y){

        tempoDaAnimacao += Gdx.graphics.getDeltaTime();

        if (Gdx.input.isButtonPressed(FRAME_WIDTH)) {
            animacaoAtual = chutar;

=======
        //jogador.setPosition(x, y);
    }
    
    public void update() {//int x, int y){
        
        tempoDaAnimacao += Gdx.graphics.getDeltaTime();
                
        if (Gdx.input.isButtonPressed(x)) {
            animacaoAtual = chutar;
                  
>>>>>>> dac944b518db7300ed9b8f12930a1286c2713248
        }
        else{
            animacaoAtual=normal;
        }
        //jogador.setPosition(x, y);
    }
<<<<<<< HEAD

=======
    
>>>>>>> dac944b518db7300ed9b8f12930a1286c2713248
    public void render(SpriteBatch batch) {
            //jogador.draw(batch);
            tempoDaAnimacao += Gdx.graphics.getDeltaTime();
            TextureRegion quadroCorrente = (TextureRegion)
            animacaoAtual.getKeyFrame(tempoDaAnimacao);
<<<<<<< HEAD
            batch.draw(quadroCorrente, FRAME_WIDTH, FRAME_HEIGHT);
            if(parado==0)
                animacaoAtual.setPlayMode(Animation.PlayMode.LOOP);

            else
                animacaoAtual.setPlayMode(Animation.PlayMode.NORMAL);


    }    */
          /*  batch.draw(quadroCorrente, x, y);
            if(parado==0)
                animacaoAtual.setPlayMode(PlayMode.LOOP_PINGPONG);
            else
                animacaoAtual.setPlayMode(PlayMode.NORMAL);

    }    */
}
