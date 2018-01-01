package br.cefetmg.games.screens;

import br.cefetmg.games.Config;
import br.cefetmg.games.graphics.hud.SoundIcon;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

public class OverworldScreen extends BaseScreen {

    private static final int NUMBER_OF_LEVELS = 5;
    private static final int[] MINIMUM_SCORE_TO_UNLOCK_NEXT_LEVEL
            = new int[]{3, 9, 18, 36, 72};

    private Stage stage;

    private MySound click1, click2;

    private boolean stop;
    private boolean[] openLevels;
    private Image stage1;
    private Image stage2;
    private Image stage3;
    private Image stage4;
    private Image stage5;
    private Image exit;
    private Image play;

    private final InputMultiplexer inputMultiplexer;
    private BitmapFont fonteDeTexto;
    private MyMusic backgroundMusic;
    private int currentLevel;
    private int score;
    private final StringBuilder scoreText;
    private FileHandle progressFile;

    private static final float ICON_SIZE = 115f;

    OverworldScreen(Game game, BaseScreen previous) {
        this(game, previous, 0, 0);
    }

    OverworldScreen(Game game, BaseScreen previous,
                    int stageJustPlayed, int remainingLivesAtStageEnd) {
        super(game, previous);
        inputMultiplexer = new InputMultiplexer();

        // se um arquivo de progresso.txt existe, lemos para saber qual é o 
        // maior nível que está aberto e a pontuação. Se não existir, definimos
        // nível 0 e pontuação 0
        progressFile = Gdx.files.local(Config.PROGRESS_LOCAL_FILE);
        if (progressFile.exists()) {
            readProgressFile();
        } else {
            currentLevel = 0;
            score = 0;
        }

        // atualiza a pontuação considerando que o jogador chegou à 
        // OverworldScreen de volta de uma sequência... ele pontua baseado nas
        // vidas restantes da sequência, ponderado pelo nível (e.g., o primeiro
        // dá menos pontos que o segundo porque ele é mais fácil)
        score += (((stageJustPlayed + 1) * 3) * remainingLivesAtStageEnd) / 3;

        // agora verificamos qual é o nível máximo que deve estar desbloqueado,
        // baseado na nova pontuação calculada
        boolean hasReachedMaximumScore = false;
        while (score >= MINIMUM_SCORE_TO_UNLOCK_NEXT_LEVEL[currentLevel]) {
            // incrementa o nível atual, mas sem exceder o índice do nível 
            // máximo
            currentLevel++;
            if (currentLevel >= NUMBER_OF_LEVELS) {
                hasReachedMaximumScore = true;
                currentLevel--;
                break;
            }
        }
        saveProgressFile();

        // monta a string com a pontuação que aparece no canto superior direito
        // da tela
        scoreText = new StringBuilder("Pontos\n");
        scoreText.append(score);
        if (!hasReachedMaximumScore) {
            scoreText.append("/")
                    .append(MINIMUM_SCORE_TO_UNLOCK_NEXT_LEVEL[currentLevel]);
        }

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
            String stageFile = String.format(Locale.getDefault(), "world/stage%d.png", i + 1);
            String iconFile = String.format(Locale.getDefault(), "world/icon%d.png", i + 1);
            assets.load(stageFile, Texture.class, linearFilter);
            assets.load(iconFile, Texture.class, linearFilter);
        }
        assets.load("hud/no-sound-button.png", Texture.class, linearFilter);
        assets.load("hud/sound-button.png", Texture.class, linearFilter);

        assets.load("menu/click2.mp3", Sound.class);
        assets.load("menu/click3.mp3", Sound.class);
        assets.load("world/overworldtheme.mp3", Music.class);

        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    protected void assetsLoaded() {
        ArrayList<Image> locks = new ArrayList<Image>();
        openLevels = new boolean[NUMBER_OF_LEVELS];
        for (int i = 0; i < NUMBER_OF_LEVELS; i++) {
            openLevels[i] = false;
            locks.add(new Image(assets.get("world/cadeado.png", Texture.class)));
        }

        Image map = new Image(assets.get("world/desert.png", Texture.class));
        stage1 = new Image(assets.get("world/stage1.png", Texture.class));
        stage2 = new Image(assets.get("world/stage2.png", Texture.class));
        stage3 = new Image(assets.get("world/stage3.png", Texture.class));
        stage4 = new Image(assets.get("world/stage4.png", Texture.class));
        stage5 = new Image(assets.get("world/stage5.png", Texture.class));
        Image icon1 = new Image(assets.get("world/icon1.png", Texture.class));
        Image icon2 = new Image(assets.get("world/icon2.png", Texture.class));
        Image icon3 = new Image(assets.get("world/icon3.png", Texture.class));
        Image icon4 = new Image(assets.get("world/icon4.png", Texture.class));
        Image icon5 = new Image(assets.get("world/icon5.png", Texture.class));
        Array<Image> levelIcons = new Array<Image>(new Image[] {icon1, icon2, icon3, icon4, icon5});
        exit = new Image(assets.get("world/menu.png", Texture.class));
        Image menu = new Image(assets.get("world/menu.png", Texture.class));
        play = new Image(assets.get("world/play.png", Texture.class));
        Image water = new Image(assets.get("world/water.jpg", Texture.class));
        fonteDeTexto = super.messagesFont;

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
        soundIcon.getButton().setY(viewport.getWorldHeight() * 0.12f);
        soundIcon.getButton().setX(viewport.getWorldHeight() * 0.08f);

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

        Vector2[] posicaoIcone = new Vector2[NUMBER_OF_LEVELS];

        posicaoIcone[0] = new Vector2(775.29376f, 176.95001f);
        posicaoIcone[1] = new Vector2(320.83545f, 453.82504f);
        posicaoIcone[2] = new Vector2(520.648f, 545.2626f);
        posicaoIcone[3] = new Vector2(983.3172f, 320.38754f);
        posicaoIcone[4] = new Vector2(638.8559f, 316.95004f);

        icon1.setSize(ICON_SIZE, ICON_SIZE);
        icon1.setOrigin(Align.center);
        icon1.setPosition(posicaoIcone[0].x, posicaoIcone[0].y);
        stage1.setScale(0.8f);
        stage1.setOrigin(0, 0);

        icon2.setSize(ICON_SIZE, ICON_SIZE);
        icon2.setOrigin(Align.center);
        icon2.setPosition(posicaoIcone[1].x, posicaoIcone[1].y);
        stage2.setScale(0.8f);
        stage2.setOrigin(0, 0);

        icon3.setSize(ICON_SIZE, ICON_SIZE);
        icon3.setOrigin(Align.center);
        icon3.setPosition(posicaoIcone[2].x + 20, posicaoIcone[2].y);
        stage3.setScale(0.8f);
        stage3.setOrigin(0, 0);

        icon4.setSize(ICON_SIZE, ICON_SIZE);
        icon4.setOrigin(Align.center);
        icon4.setPosition(posicaoIcone[3].x, posicaoIcone[3].y);
        stage4.setScale(0.8f);
        stage4.setOrigin(0, 0);

        icon5.setSize(ICON_SIZE, ICON_SIZE);
        icon5.setOrigin(Align.center);
        icon5.setPosition(posicaoIcone[4].x, posicaoIcone[4].y);
        stage5.setScale(0.8f);
        stage5.setOrigin(0, 0);

        menu.setScale(.8f);
        menu.setOrigin(0, 0);
        menu.setY(viewport.getWorldHeight() * 0.02f);
        menu.setX(viewport.getWorldHeight() * 0.02f);

        stage.setViewport(viewport);
        stage.act(Gdx.graphics.getDeltaTime());

        inputMultiplexer.addProcessor(soundIcon.getInputProcessor());

        Vector2[] lockDisplacements = new Vector2[] {
            new Vector2(),
            new Vector2(),
            new Vector2(30, 0),
            new Vector2(),
            new Vector2()
        };
        for (int i = 0; i < locks.size(); i++) {
            Image lock = locks.get(i);
            // configura os cadeados
            Vector2 lockPosition = lockDisplacements[i].add(posicaoIcone[i]);
            lock.setZIndex(10);
            lock.setAlign(Align.center);
            lock.setPosition(lockPosition.x, lockPosition.y);
            lock.setScale(0.5f);

            // coloca apenas aqueles das fases que ainda estão travadas
            if (i > currentLevel) {
                stage.addActor(locks.get(i));
            }
        }

        // faz os ícones aumentarem/dimunuírem quando o mouse entra/sai
        for (final Image icon : levelIcons) {
            icon.addListener(new ClickListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    event.getTarget().setScale(1.1f);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    event.getTarget().setScale(0.9f);
                }
            });
        }

        // faz o ícone do nível atual ficar pulsando
        Image currentLevelIcon = levelIcons.get(currentLevel);
        RepeatAction repeatingSize = new RepeatAction();
        repeatingSize.setCount(RepeatAction.FOREVER);
        repeatingSize.setAction(Actions.sequence(
                Actions.scaleBy(0.05f, 0.05f, 1, Interpolation.smooth),
                Actions.scaleBy(-0.05f, -0.05f, 1, Interpolation.smooth)
        ));
        currentLevelIcon.addAction(repeatingSize);
    }

    @Override
    public void cleanUp() {
        assets.dispose();
        backgroundMusic.stop();
    }

    @Override
    public void handleInput() {
        Vector2 click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(click);

        Actor hitActor = stage.hit(click.x, click.y, false);

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            click1.play();
            transitionScreen(new MenuScreen(super.game, this),
                    TransitionScreen.Effect.FADE_IN_OUT, 0.5f);
            stop = true;
        }
        if (Gdx.input.justTouched() && hitActor != null && !stop) {
            if ("menu".equals(hitActor.getName())) {
                click1.play();
                transitionScreen(new MenuScreen(super.game, this),
                        TransitionScreen.Effect.FADE_IN_OUT, 0.5f);
                stop = true;
            } else if (openLevels[0]) {
                if ("play".equals(hitActor.getName())) {
                    stop = true;
                    click1.play();
                    firstStage(true);
                }
                if ("exit".equals(hitActor.getName())) {
                    click1.play();
                    play.setZIndex(0);
                    exit.setZIndex(0);
                    openLevels[0] = false;
                }
            } else if (openLevels[1]) {
                if ("play".equals(hitActor.getName())) {
                    stop = true;
                    click1.play();
                    secondStage(true);
                }
                if ("exit".equals(hitActor.getName())) {
                    click1.play();
                    play.setZIndex(0);
                    exit.setZIndex(0);
                    openLevels[1] = false;
                }
            } else if (openLevels[2]) {
                if ("play".equals(hitActor.getName())) {
                    stop = true;
                    click1.play();
                    thirdStage(true);
                }
                if ("exit".equals(hitActor.getName())) {
                    click1.play();
                    play.setZIndex(0);
                    exit.setZIndex(0);
                    openLevels[2] = false;
                }
            } else if (openLevels[3]) {
                if ("play".equals(hitActor.getName())) {
                    stop = true;
                    click1.play();
                    fourthStage(true);
                }
                if ("exit".equals(hitActor.getName())) {
                    click1.play();
                    play.setZIndex(0);
                    exit.setZIndex(0);
                    openLevels[3] = false;
                }
            } else if (openLevels[4]) {
                if ("play".equals(hitActor.getName())) {

                    stop = true;
                    click1.play();
                    lastStage(true);
                }
                if ("exit".equals(hitActor.getName())) {

                    click1.play();
                    play.setZIndex(0);
                    exit.setZIndex(0);
                    openLevels[4] = false;
                }
            } else if ("icon1".equals(hitActor.getName())) {
                click1.play();
                firstStage(false);
            } else if ("icon2".equals(hitActor.getName()) && currentLevel >= 1) {
                click1.play();
                secondStage(false);
            } else if ("icon3".equals(hitActor.getName()) && currentLevel >= 2) {
                click1.play();
                thirdStage(false);
            } else if ("icon4".equals(hitActor.getName()) && currentLevel >= 3) {
                click1.play();
                fourthStage(false);
            } else if ("icon5".equals(hitActor.getName()) && currentLevel >= 4) {
                click1.play();
                lastStage(false);
            } else if ((("icon5".equals(hitActor.getName()) && currentLevel < 4) || ("icon4".equals(hitActor.getName()) && currentLevel < 3) || ("icon3".equals(hitActor.getName()) && currentLevel < 2) || ("icon2".equals(hitActor.getName()) && currentLevel < 1))) {
                click2.play();
                stop = false;
            }
        }
    }

    private void saveProgressFile() {
        String content = String.format(Locale.getDefault(), "%d:%d", this.currentLevel, this.score);
        progressFile.writeString(content, false);
    }

    private void readProgressFile() {
        String[] content = progressFile.readString().split(":");
        currentLevel = Integer.parseInt(content[0]);
        score = Integer.parseInt(content[1]);
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
                            new RunningFactory(),
                            // arthur e pedro
                            new DogBarksCatFleeFactory()
                    )
            ), .1f, .2f, currentLevel), TransitionScreen.Effect.FADE_IN_OUT, 0.7f);
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
                            new ClickFindCatFactory(),
                            // andré brait
                            new AstroCatFactory()
                    )
            ), .3f, .4f, currentLevel), TransitionScreen.Effect.FADE_IN_OUT, 0.7f);
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
            ), .5f, .6f, currentLevel), TransitionScreen.Effect.FADE_IN_OUT, 0.7f);
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
            ), .7f, .8f, currentLevel), TransitionScreen.Effect.FADE_IN_OUT, 0.7f);
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
            ), 0.9f, 1, currentLevel), TransitionScreen.Effect.FADE_IN_OUT, 0.7f);
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
        drawScoreText();
        batch.end();
    }

    private void drawScoreText() {
        final float horizontalPosition = viewport.getWorldWidth() * 0.45f;
        final float verticalPosition = viewport.getWorldHeight();
        fonteDeTexto.setColor(Color.WHITE);
                fonteDeTexto.draw(batch,
                scoreText,
                horizontalPosition,
                verticalPosition * 0.95f,
                0.9f * viewport.getWorldWidth(),
                Align.center,
                true);
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
