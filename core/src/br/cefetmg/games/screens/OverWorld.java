package br.cefetmg.games.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import br.cefetmg.games.transition.TransitionScreen;
import java.util.Arrays;
import br.cefetmg.games.minigames.factories.*;
import java.util.HashSet;

/**
 *
 * @author Rogenes
 */
public class OverWorld extends MenuScreen {

    private Vector2 click;
    private Stage stage;
    private int timer = 0;
    private boolean check = false;
    private boolean stop;

    public OverWorld(Game game, BaseScreen previous) {

        super(game, previous);

    }

    @Override
    public void appear() {
        Gdx.gl.glClearColor(0, 0, .5f, 1);

        stage = new Stage(new ScreenViewport());
        Group group = new Group();

        Image map = new Image(new Texture(Gdx.files.internal("world/desert.png")));
        Image arrow = new Image(new Texture(Gdx.files.internal("world/arrow.png")));
        Image icon1 = new Image(new Texture(Gdx.files.internal("world/icon1.png")));
        Image icon2 = new Image(new Texture(Gdx.files.internal("world/icon2.png")));
        Image icon3 = new Image(new Texture(Gdx.files.internal("world/icon3.png")));
        Image icon4 = new Image(new Texture(Gdx.files.internal("world/icon4.png")));
        Image icon5 = new Image(new Texture(Gdx.files.internal("world/icon5.png")));

        map.setName("map");
        arrow.setName("arrow");
        icon1.setName("icon1");
        icon2.setName("icon2");
        icon3.setName("icon3");
        icon4.setName("icon4");
        icon5.setName("icon5");

        group.addActor(map);
        group.addActor(icon1);
        group.addActor(icon2);
        group.addActor(icon3);
        group.addActor(icon4);
        group.addActor(icon5);
        group.addActor(arrow);

        stage.addActor(group);

        map.setOrigin(0, 0);
        map.setScale(viewport.getWorldWidth() / map.getWidth(), viewport.getWorldHeight() / map.getHeight());
        map.setPosition(0, 0);
        
        icon1.setScale(0.3f);
        icon1.setOrigin(0, 0);
        icon1.setPosition(775.29376f, 176.95001f);

        icon2.setScale(0.9f);
        icon2.setOrigin(0, 0);
        icon2.setPosition(325.83545f, 453.82504f);

        icon3.setScale(0.3f);
        icon3.setOrigin(0, 0);
        icon3.setPosition(570.648f, 545.2626f);

        icon4.setScale(0.25f);
        icon4.setOrigin(0, 0);
        icon4.setPosition(630.8559f, 316.95004f);

        icon5.setScale(0.3f);
        icon5.setOrigin(0, 0);
        icon5.setPosition(993.3172f, 330.38754f);

        arrow.setScale(0.08f);
        arrow.setOrigin(0, 0);
        arrow.setPosition(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2);
        
        stage.setViewport(viewport);
        stage.act(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void cleanUp() {
        assets.dispose();
    }

    @Override
    public void handleInput() {
        Gdx.input.setCursorCatched(true);
        Group group = (Group) stage.getActors().first();
        Image map = (Image) group.findActor("map");
        Image arrow = (Image) group.findActor("arrow");
        Image icon1 = (Image) group.findActor("icon1");
        Image icon2 = (Image) group.findActor("icon2");
        Image icon3 = (Image) group.findActor("icon3");
        Image icon4 = (Image) group.findActor("icon4");
        Image icon5 = (Image) group.findActor("icon5");
        
        click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(click);

        if (!stop) {
            arrow.setPosition(click.x - arrow.getWidth() / 2 * arrow.getScaleX(), click.y - arrow.getHeight() / 2 * arrow.getScaleY());
        }

        arrow.setZIndex(arrow.getZIndex() - 6);
        Actor hitActor = stage.hit(arrow.getX(), arrow.getY(), false);
        growEffect();

        if (Gdx.input.justTouched()) {
            System.out.println(arrow.getX() + " < x   y > " + arrow.getY());
            if (!"map".equals(hitActor.getName())) {
                if ("icon1".equals(hitActor.getName())) {
                    firstStage();
                    stop = true;
                } else if ("icon2".equals(hitActor.getName())) {
                    secondStage();
                    stop = true;
                } else if ("icon3".equals(hitActor.getName())) {
                    thirdStage();
                    stop = true;
                } else if ("icon4".equals(hitActor.getName())) {
                    lastStage();
                    stop = true;
                } else if ("icon5".equals(hitActor.getName())) {
                    fourthStage();
                    stop = true;
                } else {
                    stop = false;
                }
            }
        }
        arrow.setZIndex(arrow.getZIndex() + 6);
    }

    private void firstStage() {
        transitionScreen(new PlayingGamesScreen(super.game, this, 5, new HashSet<MiniGameFactory>(
                Arrays.asList(
                        new RainingCatsFactory(),
                        // flávio
                        new ShootTheCariesFactory(),
                        new ShooTheTartarusFactory(),
                        // gustavo henrique e rogenes
                        new BasCATballFactory(),
                        new RunningFactory()
                )
        ), .2f, .2f), TransitionScreen.Effect.FADE_IN_OUT, 1f);
    }

    private void secondStage() {
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
        ), .4f, .4f), TransitionScreen.Effect.FADE_IN_OUT, 1f);
    }

    private void thirdStage() {
        transitionScreen(new PlayingGamesScreen(super.game, this, 5, new HashSet<MiniGameFactory>(
                Arrays.asList(
                        // cassiano e gustavo jordão
                        new TicCatDogFactory(),
                        new JumpTheObstaclesFactory(),
                        // luiza e pedro cordeiro
                        new SpyFishFactory(),
                        new PhantomCatFactory()
                )
        ), .6f, .6f), TransitionScreen.Effect.FADE_IN_OUT, 1f);
    }

    private void fourthStage() {
        transitionScreen(new PlayingGamesScreen(super.game, this, 5, new HashSet<MiniGameFactory>(
                Arrays.asList(
                        // gabriel e natália
                        new MouseAttackFactory(),
                        new JetRatFactory(),
                        // emanoel e vinícius
                        new HeadSoccerFactory(),
                        new CatAvoiderFactory()
                )
        ), .8f, .8f), TransitionScreen.Effect.FADE_IN_OUT, 1f);
    }

    private void lastStage() {
        transitionScreen(new PlayingGamesScreen(super.game, this, 5, new HashSet<MiniGameFactory>(
                Arrays.asList(
                        // joão e miguel
                        new CannonCatFactory(),
                        new MeowsicFactory(),
                        // túlio
                        new NinjaCatFactory(),
                        //estevao e sarah//
                        new TheFridgeGameFactory(),
                        new KillTheRatsFactory()
                )
        ), 1, 1), TransitionScreen.Effect.FADE_IN_OUT, 1f);
    }

    private void growEffect() {
        Group group = (Group) stage.getActors().first();
        Image arrow = (Image) group.findActor("arrow");
        Image icon1 = (Image) group.findActor("icon1");
        Image icon2 = (Image) group.findActor("icon2");
        Image icon3 = (Image) group.findActor("icon3");
        Image icon4 = (Image) group.findActor("icon4");
        Image icon5 = (Image) group.findActor("icon5");
        Actor hitActor = stage.hit(arrow.getX(), arrow.getY(), false);
        float h;
        if (!stop) {
            if ("icon1".equals(hitActor.getName())) {
                if (check) {
                    timer++;
                    h = .00075f;
                } else {
                    timer--;
                    h = -.00075f;
                }
                if (timer < 0) {
                    check = !check;
                }
                if (timer > 45) {
                    check = !check;
                }
                icon1.setScale(icon1.getScaleX() + h, icon1.getScaleY() + h);
            } else if ("icon2".equals(hitActor.getName())) {
                if (check) {
                    timer++;
                    h = .00075f;
                } else {
                    timer--;
                    h = -.00075f;
                }
                if (timer < 0) {
                    check = !check;
                }
                if (timer > 45) {
                    check = !check;
                }
                icon2.setScale(icon2.getScaleX() + h, icon2.getScaleY() + h);
            } else if ("icon3".equals(hitActor.getName())) {
                if (check) {
                    timer++;
                    h = .00075f;
                } else {
                    timer--;
                    h = -.00075f;
                }
                if (timer < 0) {
                    check = !check;
                }
                if (timer > 45) {
                    check = !check;
                }
                icon3.setScale(icon3.getScaleX() + h, icon3.getScaleY() + h);
            } else if ("icon4".equals(hitActor.getName())) {
                if (check) {
                    timer++;
                    h = .00075f;
                } else {
                    timer--;
                    h = -.00075f;
                }
                if (timer < 0) {
                    check = !check;
                }
                if (timer > 45) {
                    check = !check;
                }
                icon4.setScale(icon4.getScaleX() + h, icon4.getScaleY() + h);
            } else if ("icon5".equals(hitActor.getName())) {
                if (check) {
                    timer++;
                    h = .00075f;
                } else {
                    timer--;
                    h = -.00075f;
                }
                if (timer < 0) {
                    check = !check;
                }
                if (timer > 45) {
                    check = !check;
                }
                icon5.setScale(icon5.getScaleX() + h, icon5.getScaleY() + h);
            } else {
                icon1.setScale(0.3f);
                icon2.setScale(0.9f);
                icon3.setScale(0.3f);
                icon4.setScale(0.25f);
                icon5.setScale(0.3f);
                timer = 0;
            }
        }
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void draw() {

        stage.draw();

    }
}
