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
    static int SPACING = 60;
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
        snackerComicParams.fontFileName = "fonts/orangejuice.ttf";
        snackerComicParams.fontParameters.size = 55;
        snackerComicParams.fontParameters.minFilter = Texture.TextureFilter.Linear;
        snackerComicParams.fontParameters.magFilter = Texture.TextureFilter.Linear;
        snackerComicParams2= new FreeTypeFontLoaderParameter();
        snackerComicParams2.fontFileName = "fonts/orangejuice.ttf";
        snackerComicParams2.fontParameters.size = 65;
        snackerComicParams2.fontParameters.minFilter = Texture.TextureFilter.Linear;
        snackerComicParams2.fontParameters.magFilter = Texture.TextureFilter.Linear;
        assets.load("orangejuice.ttf", BitmapFont.class, snackerComicParams);
        assets.load("orangejuice.ttf", BitmapFont.class, snackerComicParams2);

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

        if(linePos.get(lines.size()-1).y>Gdx.graphics.getHeight()+lines.size()*6){
               game.setScreen(new MenuScreen(game, this));
        }
    }

    
    @Override
    public void draw() {

             font = assets.get("orangejuice.ttf");
             font.setColor(Color.DARK_GRAY);
             bigfont = assets.get("orangejuice.ttf");
             bigfont.setColor(Color.DARK_GRAY);


             batch.begin();
             batch.draw(background, 0, 0,
             viewport.getWorldWidth(),
             viewport.getWorldHeight());
             for ( int i=0; i<(lines.size()-1);i++){
                    if(!lines.get(i).equals("")){
                    if(lines.get(i).charAt(0)=='-'){
                        bigfont.draw(batch, lines.get(i).substring(1, lines.get(i).length()), linePos.get(i).x, linePos.get(i).y);
                        bigfont.setColor(Color.DARK_GRAY);
                    }else{
                        //font.draw(batch, lines.get(i), linePos.get(i).x, linePos.get(i).y);
                        font.draw(batch, lines.get(i), linePos.get(i).x, linePos.get(i).y, viewport.getWorldWidth()/4, 1, false);
                        font.setColor(Color.DARK_GRAY);
               
                       
                        
                    }
                 }
             }
             batch.end();
    }
    
}
