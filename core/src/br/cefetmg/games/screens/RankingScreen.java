package br.cefetmg.games.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import java.util.Map;

/**
 *
 * @author Rogenes
 */
public class RankingScreen extends BaseScreen{

    private String name;
    
    private TextureRegion background;
    private TextureRegion pointer;
    
    private letter[][] letters = new letter [3][10];
    
    private boolean writing;
    private boolean saving;
    
    //dados do objeto da classe letter
    private float bottomX,bottomY,letterW,letterH;
    private float pointerX,pointerY;
    private float distBetweenLetters;
    private float distFromBoard;
    private float pointerSize;
    private int i,j;
    
    Preferences pref;
    Map<String,?> rankingMap;
    ;
    private Vector2 click;

    
    
    
    
    
    public RankingScreen(Game game, BaseScreen previous) {
        
        super(game, previous);
        writing = true;
        saving = false;
        pref = Gdx.app.getPreferences("myRanking");
        rankingMap=pref.get();
        
        this.init();
    }
    
    private void init(){
        name = "";
        distFromBoard = viewport.getWorldWidth()/7;
        bottomX = distFromBoard;
        bottomY = viewport.getWorldHeight()/3;
        letterW = viewport.getWorldWidth()/15;
        letterH = viewport.getWorldHeight()/7;
        distBetweenLetters = viewport.getWorldWidth()/14;
        pointerX = viewport.getWorldWidth()/2;
        pointerY = viewport.getWorldHeight()/2;
        pointerSize = letterW*0.5f;
    }
    
    
    public boolean collider(Vector2 bottom, Vector2 size, Vector2 click){
        return (click.x>bottom.x && click.x<(bottom.x+size.x)) &&
                (click.y>bottom.y && click.y<(bottom.y+size.y));
    }
    

    @Override
    public void appear() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        background = new TextureRegion(new Texture("ranking-background.jpg"));
        pointer = new TextureRegion(new Texture("pointer.png"));
        
        
        //auxChar define qual é  o asset carregado, qual a letra do teclado
        //comeca em a e vai selecionando as proximas letras automaticamente
        char auxChar = 'A';
        
        for(i=0;i<3;i++){
            for(j=0;j<10;j++){
                //so temos 26 letras
                if(i==2 && j==6){
                    break;
                }
                Vector2 v0 = new Vector2(bottomX,bottomY);
                Vector2 v1 = new Vector2(letterW,letterH);
                letter letterObj = new letter (v0,v1,auxChar);
                letters[i][j] = letterObj;
                bottomX+=distBetweenLetters;
                auxChar++;
            }
            bottomX = distFromBoard;
            bottomY -=  viewport.getWorldHeight()/6.5;
        }
        
        
    }

    @Override
    public void cleanUp() {
        assets.dispose();
    }

    @Override
    public void handleInput() {
        int exitFor=0;
        click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(click);
        
        if(writing && Gdx.input.justTouched()){
            
            for(i=0;i<3;i++){
                for(j=0;j<10;j++){
                    if(i==2 && j==6){
                        break;
                    }
                    if(collider(letters[i][j].bottom,letters[i][j].size,click)){
                        name=name+letters[i][j].symbol;
                        exitFor=1;
                        break;
                    }
                }
                if(exitFor==1){
                    break;
                }
            }
        }
        
        /*
        if (Gdx.input.justTouched()) {
            super.game.setScreen(new MenuScreen(super.game, this));
        }
        */
        
        if(writing){
            
        }
    }
    
    /*
    prefs.flush -> salva as alteracoes no prefs
    */

    @Override
    public void update(float dt) {
          pointerX = click.x-pointerSize/2;
          pointerY = click.y-pointerSize/2;
    }

    @Override
    public void draw() {
        batch.begin();
        batch.draw(background, 0, 0,
                viewport.getWorldWidth(),
                viewport.getWorldHeight());
        drawCenterAlignedText("***"+name+"***", viewport.getWorldHeight() * 0.85f);
        for(i=0;i<3;i++){
            for(j=0;j<10;j++){
                if(i==2 && j==6){
                    break;
                }
                batch.draw(letters[i][j].tex, letters[i][j].bottom.x, letters[i][j].bottom.y, letters[i][j].size.x,letters[i][j].size.y);
            }
        }
        batch.draw(pointer, pointerX, pointerY,pointerSize,pointerSize);
        
        batch.end();
    }
    
    private class letter{
    
        private TextureRegion tex;
        private Vector2 bottom;
        private Vector2 size;
        private char symbol;

        public letter(Vector2 bottom, Vector2 size, char symbol) {
            this.bottom = bottom;
            this.size = size;
            this.symbol = symbol;
            this.tex = new TextureRegion(new Texture("letters/"+symbol+".png"));
        }
        
    }
    
}
