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
    private List<String> lines; 
    private List<Vector2> linePos;
    FreeTypeFontGenerator generator;
    public CreditsScreen(Game game, BaseScreen previous) {
        super(game, previous);
        
    }
 
    @Override
    public void appear() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        //init font
        FreeTypeFontLoaderParameter snackerComicParams = new FreeTypeFontLoaderParameter();
        snackerComicParams.fontFileName = "fonts/snacker-comic.ttf";
        snackerComicParams.fontParameters.size = 50;
        snackerComicParams.fontParameters.minFilter = Texture.TextureFilter.Linear;
        snackerComicParams.fontParameters.magFilter = Texture.TextureFilter.Linear;
        assets.load("snacker-comic-50.ttf", BitmapFont.class, snackerComicParams);
        
        //init text
        lines = new ArrayList<String>();
        linePos = new ArrayList<Vector2>();
        float aux=0;
           try{
                BufferedReader br = new BufferedReader(new InputStreamReader(Gdx.files.internal("creditsScreen/credits.txt").read()));
                String line = "";
                
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                    linePos.add(new Vector2((float) (Gdx.graphics.getWidth()*0.5),aux));
                    aux-=50;
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
            linePos.get(i).y++;
        }
    }

    @Override
    public void draw() {
             font = assets.get("snacker-comic-50.ttf");
             font.setColor(Color.RED);
             batch.begin();
             for ( int i=0; i<lines.size();i++){
             //    if(lines.get(i).getChars(0, i, dst, i))
              //     font.draw(batch, lines.get(i), linePos.get(i).x, linePos.get(i).y);
             }
             batch.end();
    }
    
}
