package br.cefetmg.games.screens;

import br.cefetmg.games.Config;
import br.cefetmg.games.graphics.hud.SoundIcon;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import br.cefetmg.games.transition.TransitionScreen;
import java.util.Arrays;
import br.cefetmg.games.minigames.factories.*;
import br.cefetmg.games.sound.MyMusic;
import br.cefetmg.games.sound.MySound;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;
import java.util.HashSet;

public class OverworldScreen extends BaseScreen {

    private static final int NUMBER_OF_LEVELS = 5;
    private Vector2 click;
    private Stage stage;

    protected MySound click1,click2;

    private boolean check = false;
    private boolean stop,bool1=false;
    private Vector2[] posicaoIcone;
    private boolean[] openLevels;
    private Image map,
            icon1, stage1,
            icon2, stage2,
            icon3, stage3,
            icon4, stage4,
            icon5, stage5,
            exit, menu, play, water;
    
    private final InputMultiplexer inputMultiplexer;
    
    private ArrayList<Image> locks;
    private boolean desenhaMeio=true;
    private MyMusic backgroundMusic;
    private int currentStage;
    private int score;
    FileHandle file;

    public OverworldScreen(Game game, BaseScreen previous) {
        super(game, previous);
        inputMultiplexer = new InputMultiplexer();
    }

    @Override
    public void appear() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        
        TextureParameter linearFilter = new TextureParameter();
        linearFilter.minFilter = Texture.TextureFilter.Linear;
        linearFilter.magFilter = Texture.TextureFilter.Linear;
        
        assets.load("world/desert.png", Texture.class, linearFilter);
        assets.load("world/menu.png", Texture.class, linearFilter);
        assets.load("world/play.png", Texture.class, linearFilter);
        assets.load("world/water.jpg", Texture.class, linearFilter);
        assets.load("world/cadeado.png", Texture.class, linearFilter);
        for (int i = 0; i < NUMBER_OF_LEVELS; i++) {
            String stageFile = String.format("world/stage%d.png", i + 1);
            String iconFile = String.format("world/icon%d.png", i + 1);
            assets.load(stageFile, Texture.class, linearFilter);
            assets.load(iconFile, Texture.class, linearFilter);
        }
        assets.load("hud/no-sound-button.png", Texture.class, linearFilter);
        assets.load("hud/sound-button.png", Texture.class, linearFilter);

        assets.load("menu/click2.mp3", Sound.class);
        assets.load("menu/click3.mp3", Sound.class);
        assets.load("world/overworldtheme.mp3", Music.class); 

        file = Gdx.files.local(Config.PROGRESS_LOCAL_FILE);

        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    protected void assetsLoaded() {
        locks = new ArrayList<Image>();
        openLevels = new boolean[NUMBER_OF_LEVELS];
        for (int i = 0; i < NUMBER_OF_LEVELS; i++) {
            openLevels[i] = false;
            locks.add(new Image(assets.get("world/cadeado.png", Texture.class)));
        }
        
        map = new Image(assets.get("world/desert.png", Texture.class));
        stage1 = new Image(assets.get("world/stage1.png", Texture.class));
        stage2 = new Image(assets.get("world/stage2.png", Texture.class));
        stage3 = new Image(assets.get("world/stage3.png", Texture.class));
        stage4 = new Image(assets.get("world/stage4.png", Texture.class));
        stage5 = new Image(assets.get("world/stage5.png", Texture.class));
        icon1 = new Image(assets.get("world/icon1.png", Texture.class));
        icon2 = new Image(assets.get("world/icon2.png", Texture.class));
        icon3 = new Image(assets.get("world/icon3.png", Texture.class));
        icon4 = new Image(assets.get("world/icon4.png", Texture.class));
        icon5 = new Image(assets.get("world/icon5.png", Texture.class));
        exit = new Image(assets.get("world/menu.png", Texture.class));
        menu = new Image(assets.get("world/menu.png", Texture.class));
        play = new Image(assets.get("world/play.png", Texture.class));
        water = new Image(assets.get("world/water.jpg", Texture.class));

        
        bool1 = true;
        desenhaMeio = true;
        click1 = new MySound(assets.get("menu/click2.mp3", Sound.class));
        click2 = new MySound(assets.get("menu/click3.mp3", Sound.class));
        backgroundMusic = new MyMusic(assets.get("world/overworldtheme.mp3", Music.class));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        
        stage = new Stage(viewport, batch);
        map.setName("map");
        water.setName("water");
        play.setName("play");
        menu.setName("menu");
        icon1.setName("icon1");
        icon2.setName("icon2");
        icon3.setName("icon3");
        icon4.setName("icon4");
        icon5.setName("icon5");
        stage1.setName("stage1");
        stage2.setName("stage2");
        stage3.setName("stage3");
        stage4.setName("stage4");
        stage5.setName("stage5");
        exit.setName("exit");
        
        stage.addActor(stage1);
        stage.addActor(stage2);
        stage.addActor(stage3);
        stage.addActor(stage4);
        stage.addActor(stage5);
        stage.addActor(play);
        stage.addActor(exit);
        stage.addActor(water);
        stage.addActor(map);
        stage.addActor(icon1);
        stage.addActor(icon2);
        stage.addActor(icon3);
        stage.addActor(icon4);
        stage.addActor(icon5);
        stage.addActor(menu);

        SoundIcon soundIcon = new SoundIcon(stage);
        soundIcon.create(
                    assets.get("hud/no-sound-button.png", Texture.class),
                    assets.get("hud/sound-button.png", Texture.class));

        map.setZIndex(2);
        water.setZIndex(1);
        stage1.setZIndex(0);
        stage2.setZIndex(0);
        stage3.setZIndex(0);
        stage4.setZIndex(0);
        stage5.setZIndex(0);
        play.setZIndex(0);
        exit.setZIndex(0);

        map.setOrigin(0, 0);
        map.setScale(viewport.getWorldWidth() / map.getWidth(), viewport.getWorldHeight() / map.getHeight());
        map.setPosition(0, 0);
        water.setOrigin(0, 0);
        water.setScale(viewport.getWorldWidth() / water.getWidth(), viewport.getWorldHeight() / water.getHeight());
        water.setPosition(0, 0);

        play.setScale(.9f);
        play.setOrigin(0, 0);
        play.setPosition(viewport.getWorldWidth() / 2 + 50, viewport.getWorldHeight() / 2 - 100);
        exit.setScale(.9f);
        exit.setOrigin(0, 0);
        exit.setPosition(viewport.getWorldWidth() / 2 - 225, viewport.getWorldHeight() / 2 - 100);
        
        posicaoIcone = new Vector2[NUMBER_OF_LEVELS];
        
        posicaoIcone[0]= new Vector2(775.29376f,176.95001f);
        posicaoIcone[1]= new Vector2(325.83545f, 453.82504f);
        posicaoIcone[2]= new Vector2(570.648f-20, 545.2626f);
        posicaoIcone[3]= new Vector2(630.8559f, 316.95004f);
        posicaoIcone[4]= new Vector2(983.3172f, 320.38754f);
      
        icon1.setScale(0.2f);
        icon1.setOrigin(0, 0);
        icon1.setPosition(posicaoIcone[0].x,posicaoIcone[0].y);
        stage1.setScale(0.8f);
        stage1.setOrigin(0, 0);
        
        icon2.setScale(0.9f);
        icon2.setOrigin(0, 0);
        icon2.setPosition(posicaoIcone[1].x,posicaoIcone[1].y);
        stage2.setScale(0.8f);
        stage2.setOrigin(0, 0);
        
        icon3.setScale(0.3f);
        icon3.setOrigin(0, 0);
        icon3.setPosition(posicaoIcone[2].x+20,posicaoIcone[2].y);
        stage3.setScale(0.8f);
        stage3.setOrigin(0, 0);
        
        icon4.setScale(0.25f);
        icon4.setOrigin(0, 0);
        icon4.setPosition(posicaoIcone[3].x,posicaoIcone[3].y);
        stage4.setScale(0.8f);
        stage4.setOrigin(0, 0);
        
        icon5.setScale(0.4f);
        icon5.setOrigin(0, 0);
        icon5.setPosition(posicaoIcone[4].x,posicaoIcone[4].y);
        stage5.setScale(0.8f);
        stage5.setOrigin(0, 0);
        
        menu.setScale(.8f);
        menu.setOrigin(0, 0);
        menu.setPosition(0, 0);

        stage.setViewport(viewport);
        stage.act(Gdx.graphics.getDeltaTime());

        
        inputMultiplexer.addProcessor(soundIcon.getInputProcessor());

        int i =0;
        for (Image cadeado : locks) {
            cadeado.setPosition(posicaoIcone[i].x + cadeado.getImageHeight()/2,posicaoIcone[i].y + + cadeado.getImageWidth()/2);
            cadeado.setScale(0.5f);
            i++;
        }
        
        // File Handle
        // Read and Create Progress File
        if (!file.exists()) {
            file.writeString("0:", false);
            file.writeString("0", true);
            currentStage = 0;
            score = 0;
        }else {
            String arquivo = file.readString();
            String[] split = arquivo.split(":");
            currentStage = Integer.parseInt(split[0]);
            score = Integer.parseInt(split[1]);
        }

    }

    @Override
    public void cleanUp() {
        assets.dispose();
        backgroundMusic.stop();
    }

    @Override
    public void handleInput() {
        click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(click);

        Actor hitActor = stage.hit(click.x, click.y, false);
        
        growEffect(click);
        if (Gdx.input.justTouched() && hitActor != null && !stop) {
            if ("menu".equals(hitActor.getName())) {
                click1.play();
                transitionScreen(new MenuScreen(super.game, this),
                        TransitionScreen.Effect.FADE_IN_OUT, 0.5f);
                stop = true;
            } else if (openLevels[0]){
                 if ("play".equals(hitActor.getName())) {
                    stop = true;
                    click1.play();
                    firstStage(true);
                    desenhaMeio=true;
                }
                if ("exit".equals(hitActor.getName())) {
                    click1.play();
                    play.setZIndex(0);
                    exit.setZIndex(0);
                    openLevels[0] = false;
                    desenhaMeio=true;
                }
            }else if (openLevels[1]) {
                if ("play".equals(hitActor.getName())) {
                    stop = true;
                    click1.play();
                    secondStage(true);
                    desenhaMeio=true;
                }
                if ("exit".equals(hitActor.getName())) {
                    click1.play();
                    play.setZIndex(0);
                    exit.setZIndex(0);
                    openLevels[1] = false;
                    desenhaMeio=true;
                }
            } else if (openLevels[2]) {
                if ("play".equals(hitActor.getName())) {
                    stop = true;
                    click1.play();
                    thirdStage(true);
                    desenhaMeio=true;
                }
                if ("exit".equals(hitActor.getName())) {
                    click1.play();
                    play.setZIndex(0);
                    exit.setZIndex(0);
                    openLevels[2] = false;
                    desenhaMeio=true;
                }
            } else if (openLevels[3]) {
                if ("play".equals(hitActor.getName())) {
                    stop = true;
                    click1.play();
                    lastStage(true);
                    desenhaMeio=true;
                }
                if ("exit".equals(hitActor.getName())) {
                    click1.play();
                    play.setZIndex(0);
                    exit.setZIndex(0);
                    openLevels[3] = false;
                    desenhaMeio=true;
                }
            } else if (openLevels[4]) {
                if ("play".equals(hitActor.getName())) {
                    
                    stop = true;
                    click1.play();
                    fourthStage(true);
                    desenhaMeio=true;
                }
                if ("exit".equals(hitActor.getName())) {
                    
                    click1.play();
                    play.setZIndex(0);
                    exit.setZIndex(0);
                    openLevels[4] = false;
                    desenhaMeio=true;
                }
            } else if ("icon1".equals(hitActor.getName())) {
                click1.play();desenhaMeio=false;
                firstStage(false);
            } else if ("icon2".equals(hitActor.getName()) && currentStage >= 1) {
                click1.play();desenhaMeio=false;
                secondStage(false);
            } else if ("icon3".equals(hitActor.getName()) && currentStage >= 2) {
                click1.play();desenhaMeio=false;
                thirdStage(false);
            } else if ("icon4".equals(hitActor.getName()) && currentStage >= 3) {
                click1.play();desenhaMeio=false;
                lastStage(false);
            } else if ("icon5".equals(hitActor.getName()) && currentStage >= 4) {
                click1.play();desenhaMeio=false;
                fourthStage(false);
            } else if((("icon5".equals(hitActor.getName()) && currentStage<4 )|| ("icon4".equals(hitActor.getName())&& currentStage<3)||("icon3".equals(hitActor.getName())&& currentStage<2) || ("icon2".equals(hitActor.getName())&& currentStage<1))){
                click2.play();
                stop = false;
            }else{
            }
        }
    }

   private void firstStage(boolean go) {
        openLevels[0] = true;
        stage1.setZIndex(18);
        if (go) {
            transitionScreen(new PlayingGamesScreen(super.game, this, 5, new HashSet<MiniGameFactory>(
                    Arrays.asList(
                            
                            new TheFridgeGameFactory(),
                            new MouseAttackFactory(),
                            // gustavo henrique e rogenes
                            new BasCATballFactory(),
                            new RunningFactory()
                    )
            ), .1f, .2f), TransitionScreen.Effect.FADE_IN_OUT, 0.7f);
            score += 2;
            if (currentStage == 0) currentStage = 1;
            String stage = (String.valueOf(currentStage)+":"+String.valueOf(score));
            file.writeString(stage, false);
        }
        
    }

    private void secondStage(boolean go) {
        openLevels[1] = true;
        stage2.setZIndex(18);
        if (go) {
            transitionScreen(new PlayingGamesScreen(super.game, this, 5, new HashSet<MiniGameFactory>(
                    Arrays.asList(
                            // rafael e luis carlos
                            new DodgeTheVeggiesFactory(),
                            new CatchThatHomeworkFactory(),
                            // adriel
                            new UnderwaterCatFactory(),
                            // arthur e pedro
                            new DogBarksCatFleeFactory(),
                            new ClickFindCatFactory()
                    )
            ), .3f, .4f), TransitionScreen.Effect.FADE_IN_OUT, 0.7f);
            score += 4;
            if (currentStage == 1) currentStage = 2;
            String stage = (String.valueOf(currentStage)+":"+String.valueOf(score));
            file.writeString(stage, false);
        }
    }

    private void thirdStage(boolean go) {
        openLevels[2] = true;
        stage3.setZIndex(18);
        if (go) {
            transitionScreen(new PlayingGamesScreen(super.game, this, 5, new HashSet<MiniGameFactory>(
                    Arrays.asList(
                            // cassiano e gustavo jordão
                            new RainingCatsFactory(),
                            new JumpTheObstaclesFactory(),
                            // luiza e pedro cordeiro
                            new SpyFishFactory(),
                            new PhantomCatFactory()
                    )
            ), .5f, .6f), TransitionScreen.Effect.FADE_IN_OUT, 0.7f);
            score += 6;
            if (currentStage == 2) currentStage = 3;
            String stage = (String.valueOf(currentStage)+":"+String.valueOf(score));
            file.writeString(stage, false);
        }
    }

    private void fourthStage(boolean go) {
        openLevels[3] = true;
        stage4.setZIndex(18);
        if (go) {
            stage4.setZIndex(stage4.getZIndex() + 7);
            transitionScreen(new PlayingGamesScreen(super.game, this, 5, new HashSet<MiniGameFactory>(
                    Arrays.asList(
                            // gabriel e natália
                            new TicCatDogFactory(),
                            new JetRatFactory(),
                            // emanoel e vinícius
                            new HeadSoccerFactory(),
                            new CatAvoiderFactory()
                    )
            ), .7f, .8f), TransitionScreen.Effect.FADE_IN_OUT, 0.7f);
            score += 8;
            if (currentStage == 3) currentStage = 4;
            String stage = (String.valueOf(currentStage)+":"+String.valueOf(score));
            file.writeString(stage, false);
        }
    }

    private void lastStage(boolean go) {
        openLevels[4] = true;
        stage5.setZIndex(18);
        if (go) {
            transitionScreen(new PlayingGamesScreen(super.game, this, 5, new HashSet<MiniGameFactory>(
                    Arrays.asList(
                            // joão e miguel
                            new CannonCatFactory(),
                            new MeowsicFactory(),
                            // túlio
                            new NinjaCatFactory(),
                            //estevao e sarah//
                            new KillTheRatsFactory()
                    )
            ), 0.9f, 1), TransitionScreen.Effect.FADE_IN_OUT, 0.7f);
            score += 10;
            if (currentStage == 4) currentStage = 5;
            String stage = (String.valueOf(currentStage)+":"+String.valueOf(score));
            file.writeString(stage, false);
        }
    }
    
    private void drawLocks(){
        int i=0;
        for (Image lock : locks ) {
            if(i>currentStage){
               if(i==3){
                    if(desenhaMeio) {
                        lock.draw(batch,1);
                    }
               }
               else
                lock.draw(batch,1);  
            }
            i++;
        }
    }
    
    private void growEffect(Vector2 click) {
        Actor hitActor = stage.hit(click.x, click.y, false);
        if (!stop && hitActor != null && !openLevels[1] && !openLevels[2] && !openLevels[3] && !openLevels[0] && !openLevels[4]) {
            if ("icon1".equals(hitActor.getName())) {
                if (check) {
                    icon1.setScale(.28f);
                    icon1.setPosition(755.29376f, 166.95001f);
                    check = !check;
                }
            } else if ("icon2".equals(hitActor.getName())) {
                if (check) {
                    icon2.setScale(1.1f);
                    icon2.setPosition(325.83545f, 443.82504f);
                    check = !check;
                }
            } else if ("icon3".equals(hitActor.getName())) {
                if (check) {
                    icon3.setScale(.4f);
                    icon3.setPosition(560.648f, 535.2626f);
                    check = !check;
                }
            } else if ("icon4".equals(hitActor.getName())) {
                if (check) {
                    icon4.setScale(.31f);
                    icon4.setPosition(620.8559f, 306.95004f);
                    check = !check;
                }
            } else if ("icon5".equals(hitActor.getName())) {
                if (check) {
                    icon5.setScale(.5f);
                    icon5.setPosition(973.3172f, 310.38754f);
                    check = !check;
                }
            } else {
                icon1.setScale(0.2f);
                icon1.setPosition(775.29376f, 176.95001f);

                icon2.setScale(0.9f);
                icon2.setPosition(325.83545f, 453.82504f);

                icon3.setScale(0.3f);
                icon3.setPosition(570.648f, 545.2626f);

                icon4.setScale(0.25f);
                icon4.setPosition(630.8559f, 316.95004f);

                icon5.setScale(0.4f);
                icon5.setPosition(983.3172f, 320.38754f);
                check = true;
            }
        }
    }

    private void showStage(Image stage) {
        if (stage.getScaleX() < .8f) {
            stage.setScale(stage.getScaleX() + .1f);
            stage.setPosition(viewport.getWorldWidth() / 2 - stage.getWidth() * stage.getScaleX() / 2, viewport.getWorldHeight() / 2 - stage.getHeight() * stage.getScaleY() / 2);

        } else {
            exit.setZIndex(19);
            play.setZIndex(19);
        }
    }

    private void hideStage(Image stage) {
        if (stage.getScaleX() > 0) {
            stage.setScale(stage.getScaleX() - .1f);
            stage.setPosition(viewport.getWorldWidth() / 2 - stage.getWidth() * stage.getScaleX() / 2, viewport.getWorldHeight() / 2 - stage.getHeight() * stage.getScaleY() / 2);
        }
    }

    @Override
    public void draw() {
        stage.draw();
	batch.setProjectionMatrix(camera.combined);
        batch.begin();
            drawLocks();
        batch.end();
    }

    @Override
    public void update(float dt) {
        if (openLevels[0]) {
            showStage(stage1);
        } else {
            hideStage(stage1);
        }
        if (openLevels[1]) {
            showStage(stage2);
        } else {
            hideStage(stage2);
        }
        if (openLevels[2]) {
            showStage(stage3);
        } else {
            hideStage(stage3);
        }
        if (openLevels[3]) {
            showStage(stage4);
        } else {
            hideStage(stage4);
        }
        if (openLevels[4]) {
            showStage(stage5);
        } else {
            hideStage(stage5);
        }
        stage.act(dt);
    }
}
