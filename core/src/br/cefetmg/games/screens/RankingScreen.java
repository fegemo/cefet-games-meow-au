package br.cefetmg.games.screens;

import br.cefetmg.games.transition.TransitionScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import java.util.Map;

/**
 *
 * @author Rogenes
 */
public class RankingScreen extends BaseScreen{

    private String testString;
    private String scoreKeeper;
    
    private String name,fileContent;
    
    private TextureRegion background;
    private TextureRegion pointer;
    private TextureRegion backspace;
    private TextureRegion enter;
    private TextureRegion space;
    
    
    
    private letter[][] letters = new letter [3][10];
    
    private boolean writingScore,showingScore;
    
    //dados do objeto da classe letter
    private float bottomX,bottomY,letterWidth,letterHeight;
    private float pointerX,pointerY,pointerSize;
    private float distBetweenLetters,distFromBoard;
    private int i,j,phaseNumber;
    private long fileLenght;
    
    private Vector2 click;
    private Vector2 backspaceBottom,backspaceSize;
    private Vector2 spaceBottom,spaceSize;
    private Vector2 enterBottom,enterSize;
    
    private FileHandle file;
    private boolean fileExists;

    
   
    public RankingScreen(Game game, BaseScreen previous, int phase) {
   
    /*
        constroi classe para salvar e mostrar o ranking atualizado
        (chamado quando ocorre gameover)
    
    */
        
        super(game, previous);
        this.phaseNumber = phase;
        this.init();
    }
    
    
    
    public RankingScreen(Game game, BaseScreen previous){
    
    /*
        constroi  classe para mostrar o ranking somente e nao gravar nada
        (chamado pelo menu principal)
    */
        
        super(game, previous);
        file = Gdx.files.local("localRanking.txt");
        fileExists = Gdx.files.local("localRanking.txt").exists();
        if(!fileExists){
            file.writeString("", false);
        }
        fileContent = file.readString();
        fileLenght = file.length();
        testString = "";
        scoreKeeper = "";
        name = "";
        writingScore = false;
        showingScore = true;
    }
    
    private void init(){
        name = "";
        distFromBoard = viewport.getWorldWidth()/7;
        bottomX = distFromBoard;
        bottomY = viewport.getWorldHeight()/3;
        letterWidth = viewport.getWorldWidth()/15;
        letterHeight = viewport.getWorldHeight()/7;
        distBetweenLetters = viewport.getWorldWidth()/14;
        pointerX = viewport.getWorldWidth()/2;
        pointerY = viewport.getWorldHeight()/2;
        pointerSize = letterWidth*0.5f;
        file = Gdx.files.local("localRanking.txt");
        fileExists = Gdx.files.local("localRanking.txt").exists();
        if(!fileExists){
            file.writeString("", false);
        }
        fileContent = file.readString();
        fileLenght = file.length();
        writingScore = true;
        showingScore = false;
    }
    
    public int rankingQuantity(){
        int quantity=0;
        if(file.length()>0){
            for(i=0;i<fileLenght;i++){
                if(fileContent.charAt(i)=='\n'){
                    quantity++;
                }
            }
        }
        return quantity;
    }
    
    public void insert(){
        boolean alreadyGreater = false;
        String converter;
        i=0;
        j=0;
        testString = "";
        scoreKeeper = "";
        if(rankingQuantity()>0){
            
            while(j < fileLenght){
                
                testString = ""+fileContent.charAt(j);
                j++;
                
                while(fileContent.charAt(j)!='\n'){
                        testString += fileContent.charAt(j);
                        j++;
                }
                testString = testString+'\n';
               
                converter = ""+testString.charAt(0);
                
                if(phaseNumber >= Integer.parseInt(converter) && !alreadyGreater){
                    if("".equals(scoreKeeper)){
                        scoreKeeper = name;
                        
                    }else{
                        scoreKeeper = scoreKeeper + name;
                    }
                    
                    scoreKeeper = scoreKeeper + testString;
                    alreadyGreater=true;
                }
                else{
                    if("".equals(scoreKeeper)){
                        scoreKeeper = testString;
                    }else{
                        scoreKeeper += testString;
                    }
                }
                
                j++;
                
            }
            if(!alreadyGreater){
                scoreKeeper += name;
                alreadyGreater=true;
            }
            
            file.writeString(scoreKeeper, false);
            fileContent = file.readString();
        }
        else{
            file.writeString(name, false);
            fileContent = file.readString();
        }
    }
    
    /*public void removeExtra(){
        int cont=0;
        i=0;
        testString="";
        
        if(rankingQuantity()>2){
            while(true){
                
                testString+=fileContent.charAt(i);
                
                if(fileContent.charAt(i)=='\n'){
                    cont++;
                }
                if(cont==2){
                    file.writeString(testString, false);
                    fileContent = file.readString();
                    break;
                }              
                i++;
            }
        }
        
    }*/
    
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
        char auxChar = 'a';
        
        for(i=0;i<3;i++){
            for(j=0;j<10;j++){
                //so temos 26 letras
                if(i==2 && j==6){
                    break;
                }
                Vector2 v0 = new Vector2(bottomX,bottomY);
                Vector2 v1 = new Vector2(letterWidth,letterHeight);
                letter letterObj = new letter (v0,v1,auxChar);
                letters[i][j] = letterObj;
                bottomX+=distBetweenLetters;
                auxChar++;
            }
            bottomX = distFromBoard;
            bottomY -=  viewport.getWorldHeight()/6.5;
        }
            /*
            posicao do spaco, backspace e enter sao separadas das demais,
            baseadas na ultima letra
            ]*/
        i=2;
        j=5;
        backspaceBottom = new Vector2(letters[i][j].bottom.x+distBetweenLetters,letters[i][j].bottom.y);
        backspaceSize = new Vector2(letters[i][j].size.x,letters[i][j].size.y);
        
        spaceBottom = new Vector2(letters[i][j].bottom.x+2*distBetweenLetters, letters[i][j].bottom.y);
        spaceSize = new Vector2(letters[i][j].size.x*2,letters[i][j].size.y);
        
        enterBottom = new Vector2(letters[i][j].bottom.x+4*distBetweenLetters, letters[i][j].bottom.y);
        enterSize = new Vector2(letters[i][j].size.x,letters[i][j].size.y);
        
        backspace = new TextureRegion(new Texture("letters/backspace.png"));
        enter = new TextureRegion(new Texture("letters/enter.png"));
        space = new TextureRegion(new Texture("letters/space.png"));
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
        
        if(Gdx.input.justTouched()){
            if(writingScore){
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
                if(collider(backspaceBottom,backspaceSize,click)){
                    if(name.length()>0){
                        name=name.substring(0, name.length()-1);
                    }
                    
                }
                else if(collider(spaceBottom,spaceSize,click)){
                    name=name+" ";
                }
                else if(collider(enterBottom,enterSize,click)){
                    do{
                        name=phaseNumber+" -> "+name+'\n';
                        if(name.trim().isEmpty()){
                            drawCenterAlignedText("Nome não pode estar vazio!", viewport.getWorldHeight() * 0.5f);
                        }
                    }while(name.trim().isEmpty());
                    
                    writingScore=false;
                    showingScore=true;
                    /*
                    
                    ordena o arquivo e escreve o nome que foi inserido
                    na posicao correta
                    
                    */
                    
                    insert();
                    //removeExtra();
                    
                }

            }
            else if(showingScore){
                transitionScreen(new MenuScreen(super.game, this),
                TransitionScreen.Effect.FADE_IN_OUT, 1f);
            }
        }
    }
    

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
        if(writingScore){
            drawCenterAlignedText("YOUR SCORE:  "+phaseNumber+"", viewport.getWorldHeight() * 0.85f);
            
            drawCenterAlignedText("ENTER YOUR NAME:  "+name+"", viewport.getWorldHeight() * 0.75f);
            for(i=0;i<3;i++){
                for(j=0;j<10;j++){
                    if(i==2 && j==6){
                        break;
                    }
                    batch.draw(letters[i][j].tex, letters[i][j].bottom.x, letters[i][j].bottom.y, letters[i][j].size.x,letters[i][j].size.y);
                }
            }
            batch.draw(backspace,backspaceBottom.x,backspaceBottom.y,backspaceSize.x,backspaceSize.y);
            batch.draw(space,spaceBottom.x,spaceBottom.y,spaceSize.x,spaceSize.y);
            batch.draw(enter,enterBottom.x,enterBottom.y,enterSize.x,enterSize.y);
        
        }
        else if(showingScore){
            drawCenterAlignedText("RANKING! ", viewport.getWorldHeight()*0.90f);
            drawCenterAlignedText(fileContent, viewport.getWorldHeight()*0.80f);
        }
     
        
            batch.end();
    }

    @Override
    protected void assetsLoaded() {
        
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
