/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import java.util.concurrent.ThreadLocalRandom;


public class Meowsic extends MiniGame {

    private Texture background;
    private Sound song1,song2,song3,song4,song5,music,fail;
    private Texture cat;
    private Texture sheet;
    private Texture note;
    private Texture note1,note2,note3,note4,note5;
    private Texture Pressed1,Pressed2,Pressed3,Pressed4,Pressed5;
    public float x_cat;
    public float y_cat;
    public int N_NOTES = 25;
    public int[] note_x = new int[N_NOTES];
    public float[] note_y = new float[N_NOTES];
    public int[] raia_note = new int[N_NOTES];
    public int[] clicked = new int[N_NOTES];
    public int raia_tam = 102;
    public int i = 0; 
    public int k = 0;
    public double velocidade = 1;
    public double dificuldade = 1;
    public int contador_de_erros = 0;


    public Meowsic(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        background = assets.get("meowsic/background.png", Texture.class);
        cat = assets.get("meowsic/cat.png", Texture.class);
        sheet = assets.get("meowsic/sheet.png", Texture.class);
        note = assets.get("meowsic/note.png", Texture.class);
        Pressed1 = assets.get("meowsic/Pressed1.png", Texture.class);
        Pressed2 = assets.get("meowsic/Pressed2.png", Texture.class);
        Pressed3 = assets.get("meowsic/Pressed3.png", Texture.class);
        Pressed4 = assets.get("meowsic/Pressed4.png", Texture.class);
        Pressed5 = assets.get("meowsic/Pressed5.png", Texture.class);
        note1 = assets.get("meowsic/note1.png", Texture.class);
        note2 = assets.get("meowsic/note2.png", Texture.class);
        note3 = assets.get("meowsic/note3.png", Texture.class);
        note4 = assets.get("meowsic/note4.png", Texture.class);
        note5 = assets.get("meowsic/note5.png", Texture.class);
        song1 = assets.get("meowsic/song1.wav", Sound.class);
        song2 = assets.get("meowsic/song2.wav", Sound.class);
        song3 = assets.get("meowsic/song3.wav", Sound.class);
        song4 = assets.get("meowsic/song4.wav", Sound.class);
        song5 = assets.get("meowsic/song5.wav", Sound.class);
        music = assets.get("meowsic/music.wav", Sound.class);
        fail = assets.get("meowsic/fail.wav", Sound.class);
        
        for(i=0;i<N_NOTES-1;i++){
            note_x[i] = 350+raia_tam*ThreadLocalRandom.current().nextInt(0,5);
            note_y[i] = 500+i*125;
            clicked[i] = 0;
        }
        //Força que a última nota seja Dó, para soar mais "bonito"
            note_x[N_NOTES-1] = 350;
            note_y[N_NOTES-1] = 500+i*110+ThreadLocalRandom.current().nextInt(0,20);
                music.play(0.8f);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.velocidade = 0.5*(DifficultyCurve.LINEAR.getCurveValueBetween(difficulty,5, 10));
    }

    @Override
    public void onHandlePlayingInput() {
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(click);
        x_cat = click.x - cat.getWidth()/2;
        y_cat = click.y - cat.getHeight()/2;
    }

    @Override
    public void onUpdate(float dt) {
        for(k=0;k<N_NOTES;k++){
            if(clicked[k]==0)
                  note_y[k] -= velocidade;
              //Remove nota da tela ao passar do limite para ser clicada
            if(note_y[k]<=0){
                clicked[k] = 2;
            }
        }        
    }

    @Override
    public void onDrawGame() {
       // Desenha cenário
        batch.draw(background, 0, 0);
        batch.draw(sheet, 350, 0);
        BitmapFont font = new BitmapFont();
        font.setColor(Color.WHITE);
        int soma_erros = 0, soma_acertos = 0;
    
        for(i=0;i<N_NOTES;i++){
            if(clicked[i]==1)
                soma_acertos = soma_acertos+1;
            else if(clicked[i]==2 && contador_de_erros == 0)
                soma_erros = soma_erros+1;
        }
         if(soma_erros >=6){
            contador_de_erros = 1;
            super.challengeFailed();
            music.stop();
            fail.play();
         }
        
        font.draw(batch, "Acertos: " + soma_acertos, 1000, 700);
        font.draw(batch, "Erros: " + soma_erros, 100, 700);
        
        // Desenha as notas correspondentes a cada coluna
        for (i=0;i<N_NOTES;i++){
                if(note_x[i]==350){
                    if(clicked[i] == 0)
                        batch.draw(note1, note_x[i], note_y[i]);
                    else if (clicked[i] == 1)
                        batch.draw(note1,1000 , 50+(i*20));
                    else if(clicked[i] == 2)
                        batch.draw(note1,100 , 50+(i*20));
                }
                else if(note_x[i]==350+raia_tam*1){
                    if(clicked[i] == 0)
                        batch.draw(note2, note_x[i], note_y[i]);
                    else if (clicked[i] == 1)
                        batch.draw(note2,1000 , 50+(i*20));
                    else if(clicked[i] == 2)
                        batch.draw(note2,100 , 50+(i*20));
                }
                else if(note_x[i]==350+raia_tam*2){
                    if(clicked[i] == 0)
                        batch.draw(note3, note_x[i], note_y[i]);
                    else if (clicked[i] == 1)
                        batch.draw(note3,1000 , 50+(i*20));
                    else if(clicked[i] == 2)
                        batch.draw(note3,100 , 50+(i*20));
                }
                else if(note_x[i]==350+raia_tam*3){
                    if(clicked[i] == 0)
                        batch.draw(note4, note_x[i], note_y[i]);
                    else if (clicked[i] == 1)
                        batch.draw(note4,1000 , 50+(i*20));
                                else if(clicked[i] == 2)
                        batch.draw(note4,100 , 50+(i*20));
                }
                else if(note_x[i]>=350+raia_tam*3){
                    if(clicked[i] == 0)
                        batch.draw(note5, note_x[i], note_y[i]);
                    else if (clicked[i] == 1)
                        batch.draw(note5,1000 , 50+(i*20));
                                else if(clicked[i] == 2)
                        batch.draw(note5,100 , 50+(i*20));
                }

            //Verifica colisão do click com a nota e ajusta realidade da hit box
           if(y_cat >= note_y[i]-25 && y_cat <= note_y[i]+60 && x_cat >= note_x[i]-20 && x_cat <= note_x[i]+65 && (Gdx.input.justTouched() )&& clicked[i]==0){
                if(note_x[i]==350 && note_y[i] <= 90 &&note_y[i] >= 10){
                    song1.play(0.5f);  
                    batch.draw(Pressed1,350 , 0);
                    clicked[i] = 1;
                }
                else if(note_x[i]==350+raia_tam*1 && note_y[i] <= 90 &&note_y[i] >= 10){
                    song2.play(0.5f);  
                    batch.draw(Pressed2,350 , 0); 
                    clicked[i] = 1;
                }
                else if(note_x[i]==350+raia_tam*2 && note_y[i] <= 90 &&note_y[i] >= 10){
                    song3.play(0.5f);  
                    batch.draw(Pressed3,350 , 0); 
                    clicked[i] = 1;
                }
                else if(note_x[i]==350+raia_tam*3 && note_y[i] <= 90 &&note_y[i] >= 10){
                    song4.play(0.5f);  
                    batch.draw(Pressed4,350 , 0); 
                    clicked[i] = 1;
                }
                else if(note_x[i]>=350+raia_tam*3 && note_y[i] <= 90 &&note_y[i] >= 10){
                    song5.play(0.5f);  
                    batch.draw(Pressed5,350 , 0); 
                    clicked[i] = 1;
                }
            }
        }
                batch.draw(cat, x_cat, y_cat);     
    }
    @Override
    public String getInstructions() {
        return null;

    }
    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

}
