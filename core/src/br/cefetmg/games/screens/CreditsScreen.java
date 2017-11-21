/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.math.Vector2;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Isaac
 */
 

public class CreditsScreen extends BaseScreen{
    private BitmapFont font;
    private BitmapFont bigfont;
    private List<String> lines; 
    private List<Vector2> linePos;
    static int SPACING = 50;
    FreeTypeFontGenerator generator;
    private TextureRegion background;
    FreeTypeFontLoaderParameter snackerComicParams ;
   FreeTypeFontLoaderParameter snackerComicParams2 ;
    int bigword;
    public CreditsScreen(Game game, BaseScreen previous) {
        super(game, previous);
        
    }
 
    @Override
    public void appear() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        //init font
        background = new TextureRegion(new Texture("menu/menu-background.png"));
        snackerComicParams= new FreeTypeFontLoaderParameter();
        snackerComicParams.fontFileName = "fonts/DejaVuSans-Bold.ttf";
        snackerComicParams.fontParameters.size = 55;
        snackerComicParams.fontParameters.minFilter = Texture.TextureFilter.Linear;
        snackerComicParams.fontParameters.magFilter = Texture.TextureFilter.Linear;
        snackerComicParams2= new FreeTypeFontLoaderParameter();
        snackerComicParams2.fontFileName = "fonts/DejaVuSans-Bold.ttf";
        snackerComicParams2.fontParameters.size = 65;
        snackerComicParams2.fontParameters.minFilter = Texture.TextureFilter.Linear;
        snackerComicParams2.fontParameters.magFilter = Texture.TextureFilter.Linear;
        assets.load("DejaVuSans-Bold1.ttf", BitmapFont.class, snackerComicParams);
        assets.load("DejaVuSans-Bold2.ttf", BitmapFont.class, snackerComicParams2);
        //init text
        lines = new ArrayList<String>();
        linePos = new ArrayList<Vector2>();
        float aux=0;
           try{
                BufferedReader br = new BufferedReader(new InputStreamReader(Gdx.files.internal("creditsScreen/credits.txt").read()));
                String line = "";
                
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                    linePos.add(new Vector2((float) (Gdx.graphics.getWidth()*0.55),aux));
                 
                    aux-=SPACING;
                }
                br.close();
                
    } 
    catch(Exception e)
    {
        e.printStackTrace();
    }
        
    }

    @Override
    public void cleanUp() {

    }

    @Override
    public void handleInput() {
    }

    @Override
    public void update(float dt) {
       

        for ( int i=0; i<lines.size();i++){
            linePos.get(i).y+=1.5;
        }

        if(linePos.get(lines.size()-1).y>Gdx.graphics.getHeight()+lines.size()*(SPACING-1)){
               game.setScreen(new MenuScreen(game, this));
        }
    }

    
    @Override
    public void draw() {
             font = assets.get("DejaVuSans-Bold1.ttf");
             font.setColor(Color.BLACK);
             bigfont = assets.get("DejaVuSans-Bold2.ttf");
             bigfont.setColor(Color.BLACK);

             batch.begin();
             batch.draw(background, 0, 0,
             viewport.getWorldWidth(),
             viewport.getWorldHeight());
             for ( int i=0; i<(lines.size()-1);i++){
                    if(!lines.get(i).equals("")){
                    if(lines.get(i).charAt(0)=='-'){
                      /*  
                        assets.load("Roboto-Black.ttf", BitmapFont.class, snackerComicParams);
                        font = assets.get("Roboto-Black.ttf");*/
                        
                        bigfont.draw(batch, lines.get(i).substring(1, lines.get(i).length()), linePos.get(i).x, linePos.get(i).y);
                     /* snackerComicParams.fontParameters.size = 25;
                        assets.load("Roboto-Black.ttf", BitmapFont.class, snackerComicParams);
                        font = assets.get("Roboto-Black.ttf");*/

                    }else{
                        font.draw(batch, lines.get(i), linePos.get(i).x, linePos.get(i).y);
                        
                    }
                 }
             }
             batch.end();
    }
    
}
