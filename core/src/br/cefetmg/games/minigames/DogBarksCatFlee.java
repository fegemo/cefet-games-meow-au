/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameState;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


/**
 *
 * @author Pedro e Arthur
 * ouçam https://www.youtube.com/watch?v=Gfw4yxn_kPQ
 */
public class DogBarksCatFlee extends MiniGame {
    private final int TILES_COUNT = 18;
    private Dog player;
    private Texture DogTextureStandBy;
    private Texture DogTexture;
    private Texture deadTexture;
    private Texture DogTextureWalking;
    private Animation<TextureRegion> DogWalking;
    private Animation<TextureRegion> DogBarking;
    private Sound BarkSound;
    private Sound MeawSound;
    private Sound WhiningSound;
    private Texture CatTexture;
    //private Array<Cat> enemies;
    private Cat enemy;
    private Array<Tiles> tilesVector; // Tem que criar 1 tipo tile que tem Vector2 e um int para representar qual é o 
    private Vector2 PosicaoInicial;
    private Texture tileTexture[] = new Texture[5];
    private float spawnInterval;
    private float minimumEnemySpeed;
    private float maximumEnemySpeed;
    private float TempoDeAnimacao;
    private int morreu =0 ;
    public float telaAnda= 3.5f;
    public int latindo_Counter =0;
    public boolean consegueOuver =false;
    public DogBarksCatFlee(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }
    
    private void TilesDraw(){
        for (Tiles tile : tilesVector) {
            batch.draw(tileTexture[tile.tile_Type ], tile.tileVector.x, tile.tileVector.y);
         
        }
    }
    
    private void UpdateDraw(){
        if( enemy.getPos().x - player.getPos().x >= 2*(player.getFrameWidth() + enemy.GetWidth() ) ){
            consegueOuver=true;
            enemy.moviment(new Vector2( enemy.getPos().x += -telaAnda , enemy.getPos().y ));
            for (Tiles tile : tilesVector) {
                tile.tileVector.x += -telaAnda; 
                if(tile.tileVector.x <= 0- tileTexture[0].getWidth()){
                    tile.tileChange();
                    tile.tileVector.x = 1400; // ainda não definido o num;
                } // ainda não definido o num;   
            }
        }
        else{
            consegueOuver=false;
        }
    }
    
    private void PlayerDraw(float dt) {
        if( enemy.getPos().x - player.getPos().x >= 2*(player.getFrameWidth() + enemy.GetWidth() )){
            batch.draw (player.Anima2(dt), player.getPos().x, player.getPos().y);
        }
        else if (player.isLatindo()) {
            batch.draw (player.Anima(dt), player.getPos().x, player.getPos().y);
        } else {
            batch.draw (player.getTexture(),player.getPos().x,player.getPos().y);
        }    
    }
    
    private void CatDraw(){
        if( ! enemy.vivoMorto() )
            batch.draw(enemy.getTexture(),enemy.getPos().x,enemy.getPos().y);
        if(enemy.vivoMorto() && morreu <10)
            batch.draw(deadTexture,enemy.oldPos.x,enemy.oldPos.y);
    }
    
    @Override
    protected void onStart() {
        TempoDeAnimacao = 0;
        WhiningSound = assets.get("DogBarksCatFlee/dog-whining-sound.mp3", Sound.class);
        deadTexture = assets.get("DogBarksCatFlee/kitten1-alt_3.png", Texture.class);
        BarkSound = assets.get("DogBarksCatFlee/BarkSound.wav", Sound.class);
        DogTextureStandBy = assets.get("DogBarksCatFlee/dog_separado_4.png", Texture.class);
        DogTexture = assets.get("DogBarksCatFlee/dog_spritesheet.png", Texture.class);
        DogTextureWalking = assets.get("DogBarksCatFlee/spritesheet2.png",Texture.class);
        TextureRegion[][] quadrosDeAnimacao = TextureRegion.split(DogTexture, 128,128);
        TextureRegion[][] quadrosDeAnimacao2 = TextureRegion.split(DogTextureWalking, 128,128);
        System.out.println(+ quadrosDeAnimacao.length);
        DogBarking = new Animation <TextureRegion>(0.3f,
                    quadrosDeAnimacao[0][3],
                    quadrosDeAnimacao[0][2],
                    quadrosDeAnimacao[0][1],
                    quadrosDeAnimacao[0][0]);
                DogWalking = new Animation <TextureRegion>(0.3f,
                    quadrosDeAnimacao2[0][0],
                    quadrosDeAnimacao2[0][1],
                    quadrosDeAnimacao2[0][2],
                    quadrosDeAnimacao2[0][3],
                    quadrosDeAnimacao2[0][4]);
        DogBarking.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        DogWalking.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        CatTexture = assets.get("DogBarksCatFlee/kitten1-alt.png", Texture.class);
        MeawSound = assets.get("DogBarksCatFlee/cat-meow.wav", Sound.class);

        tileTexture[0] = assets.get("DogBarksCatFlee/tile0.png", Texture.class);
        tileTexture[1] = assets.get("DogBarksCatFlee/tile1.png", Texture.class);
        tileTexture[2] = assets.get("DogBarksCatFlee/tile2.png", Texture.class);
        tileTexture[3] = assets.get("DogBarksCatFlee/tile3.png", Texture.class);
        tileTexture[4] = assets.get("DogBarksCatFlee/tile4.png", Texture.class);

        tilesVector = new Array<Tiles>();
        
        
        for (int i =0 ; i< TILES_COUNT ;i++ ) {
            tilesVector.add(new Tiles(new Vector2(1 + i * tileTexture[0].getWidth(), 41)));
        }
        inicializeDog();
        inicializeCat();
    }
    
    private void UpdateEnemy(){
        if( enemy.FleeAction( player.getBarkCounter())){
            MeawSound.play();
            enemy.morreu();
            player.BarkZero();
            super.challengeSolved();
        }    
        if( enemy.vivoMorto() && (enemy.get_quantidade_vidas() > 0))
            enemy.spawn();
        
    }
    
    private void inicializeDog(){
        TextureRegion[][] TextureDog = TextureRegion.split(DogTextureStandBy, DogTextureStandBy.getWidth(), DogTextureStandBy.getHeight());
        PosicaoInicial = new Vector2 (300,41);
        player = new Dog (3, PosicaoInicial, TextureDog[0][0], DogBarking,DogWalking);
    }
    
    private void inicializeCat(){
        TextureRegion[][] Cat = TextureRegion.split(CatTexture, CatTexture.getWidth(), CatTexture.getHeight());
        enemy = new Cat( (int) ( (float) ScareThereshold() * DifficultyCurve.S.getCurveValueBetween(spawnInterval,5f,1f)),PosicaoInicial,Cat[0][0] );
        enemy.settarQuantidade_vidas(spawnInterval);
        enemy.moviment(enemy.PosIniCat());
        enemy.spawn();
    }
    
    private int ScareThereshold (){
        return MathUtils.random(1, 5);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {

        spawnInterval = DifficultyCurve.S.getCurveValue(difficulty);
        System.out.println("spawnInterval" + spawnInterval);
  }

    @Override
    public void onHandlePlayingInput() {
        if (Gdx.input.justTouched()){
            player.Bark(consegueOuver);
            if (!consegueOuver) BarkSound.play();
            System.out.println( player.getBarkCounter() + " " + enemy.GetScareTheresold());
            
        }     
    }

    public void onUpdate(float dt) {
        if (super.getState() == MiniGameState.PLAYER_FAILED ) {
            WhiningSound.play();
        }
        if(player.isLatindo()){
            latindo_Counter++;
            if(latindo_Counter == 25 ){
                 latindo_Counter=0; 
                 player.InvertLatindo();
            }
        }
        if(enemy.mostrarGatoMorto && morreu <10)
            morreu++;
        else
            enemy.mostrarGatoMorto=!enemy.mostrarGatoMorto;
        
        TempoDeAnimacao += Gdx.graphics.getDeltaTime();
        UpdateDraw();
        UpdateEnemy();
    }

    @Override
    public void onDrawGame() {
        TilesDraw();
        PlayerDraw(TempoDeAnimacao);
        CatDraw();
    }

    @Override
    public String getInstructions() {
        return "Lata para espantar o gato";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }
        
}
class Cat extends Animal {
    private final int POSICAO_INICIAL_GATO_X = 1400;
    private final int POSICAO_INICIAL_GATO_Y = 51;
    // Precisa de armazenar a sprite para animacao
    
    private final int BeScaredThereshold;
    private int quantidade_vidas;
    private boolean morto;
    public boolean mostrarGatoMorto=false;
    public Vector2 oldPos;
    public boolean visivel = true;

    // Construtor do Jogo DogBarksCatFlee
    public Cat(int BeScaredThereshold,Vector2 Pos, TextureRegion CatTexture) {
        super(Pos, CatTexture);
        this.morto = true;
        this.BeScaredThereshold = BeScaredThereshold;
        this.quantidade_vidas = 1;
    }
    
    public void SetInvisivel () {
        visivel = false;
    }
    
    public Vector2 PosIniCat(){
        return new Vector2(POSICAO_INICIAL_GATO_X,POSICAO_INICIAL_GATO_Y);
    }
    
    public int GetWidth () {
        return FRAME_WIDTH;
    }
    public int GetScareTheresold(){
        return this.BeScaredThereshold;
    }
    public int GetHeight () {
        return FRAME_HEIGHT;
    }
    
    public int get_quantidade_vidas(){
        return this.quantidade_vidas;
    }
    
    public void morreu(){
        this.quantidade_vidas--;
        this.morto=true;
        System.out.println("MORREUU");
        oldPos = this.getPos();
        mostrarGatoMorto = !mostrarGatoMorto;
        moviment(new Vector2(POSICAO_INICIAL_GATO_X,POSICAO_INICIAL_GATO_Y));
    }
    
    public boolean vivoMorto(){
        return this.morto;
    }
    public void spawn(){
        System.out.println("spawn" + this.quantidade_vidas );
        this.morto=false;
    }
    
    public void settarQuantidade_vidas(float variavelControleDificuldade){
        this.quantidade_vidas = (int)( MathUtils.ceil(variavelControleDificuldade*8));
    }

    
    
    public boolean FleeAction (int BarkCounter) {
        // Caso positivo ativa a funcao de sair da dela
        // Presente na classe do jogo
        return BarkCounter >= BeScaredThereshold;
    }

}

class Dog extends Animal{
    private int barkCounter;
    private boolean latindo;
    private final Animation<TextureRegion> animacao;
    private final Animation<TextureRegion> animacao2;
    private int lives;
    

    public Dog(int lives, Vector2 Pos, TextureRegion DogTexture, Animation<TextureRegion> animacao , Animation<TextureRegion> animacao2 ) {
        
        super(Pos, DogTexture);
        this.animacao = animacao;
        this.animacao2 = animacao2;
        this.lives = lives;
        barkCounter = 0;
        this.lives = lives;
        latindo = false;
    }
    
    public TextureRegion Anima (float dt) {
        return ((TextureRegion)animacao.getKeyFrame(dt));
    }
    
    public TextureRegion Anima2 (float dt) {
        return ((TextureRegion)animacao2.getKeyFrame(dt));
    }
    
    
    public int getFrameWidth () {
        return FRAME_WIDTH;
    }
    
    public int getFrameHeight () {
        return FRAME_HEIGHT;
    }
    
    public int getBarkCounter() {
        return barkCounter;
    }

    public boolean isLatindo() {
        return latindo;
    }
    
    public void InvertLatindo () {
        latindo = !latindo;
    }
    
    
    
    public void Bark (boolean gatoOuve){
        if(!gatoOuve)
            this.barkCounter ++;
        else
            this.barkCounter = 0;
        latindo = true;
    }
    public void BarkZero(){
        barkCounter = 0;
    }
    public void wasHurt () {
        lives --;
    }
}


class Animal extends Sprite {
    private Vector2 Pos;
    static final int FRAME_WIDTH = 131;
    static final int FRAME_HEIGHT = 32;
   
    public Animal(Vector2 Pos, TextureRegion AnimalSpriteSheet) {
        super(AnimalSpriteSheet);
        this.Pos = Pos;
    }
    
    public Animal (Sprite AnimalSprite) {
        super (AnimalSprite);
    }

    public Vector2 getPos() {
        return Pos;
    }
    
    public void moviment (Vector2 NovaPosicao) {
        this.Pos = NovaPosicao;
    }
}

class Tiles {
    public Vector2 tileVector;
    public int tile_Type;

    public Tiles(Vector2 tileVector) {
        this.tileVector = tileVector;
        this.tile_Type = MathUtils.random(4);
    }
    public void tileChange(){
        this.tile_Type = MathUtils.random(4);
    }
}

