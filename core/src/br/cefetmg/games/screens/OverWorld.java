package br.cefetmg.games.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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

public class OverWorld extends MenuScreen {

    private Vector2 click;
    private Stage stage;
    private boolean check = false;
    private boolean stop;
    private boolean s1, s2, s3, s4, s5;
    private Image map, arrow,
            icon1, stage1,
            icon2, stage2,
            icon3, stage3,
            icon4, stage4,
            icon5, stage5,
            exit, menu, play, water;

    public OverWorld(Game game, BaseScreen previous) {
        super(game, previous);
    }

    @Override
    public void appear() {
        Gdx.gl.glClearColor(0, 0, .5f, 1);
        stage = new Stage(new ScreenViewport());
        Group group = new Group();
        s1 = false;
        s2 = false;
        s3 = false;
        s4 = false;
        s5 = false;

        map = new Image(new Texture(Gdx.files.internal("world/desert.png")));
        arrow = new Image(new Texture(Gdx.files.internal("world/arrow.png")));
        icon1 = new Image(new Texture(Gdx.files.internal("world/icon1.png")));
        stage1 = new Image(new Texture(Gdx.files.internal("world/stage1.png")));
        stage2 = new Image(new Texture(Gdx.files.internal("world/stage2.png")));
        stage3 = new Image(new Texture(Gdx.files.internal("world/stage3.png")));
        stage4 = new Image(new Texture(Gdx.files.internal("world/stage4.png")));
        stage5 = new Image(new Texture(Gdx.files.internal("world/stage5.png")));
        exit = new Image(new Texture(Gdx.files.internal("world/exit.png")));
        icon2 = new Image(new Texture(Gdx.files.internal("world/icon2.png")));
        icon3 = new Image(new Texture(Gdx.files.internal("world/icon3.png")));
        icon4 = new Image(new Texture(Gdx.files.internal("world/icon4.png")));
        icon5 = new Image(new Texture(Gdx.files.internal("world/icon5.png")));
        menu = new Image(new Texture(Gdx.files.internal("world/menu.png")));
        play = new Image(new Texture(Gdx.files.internal("world/play.png")));
        water = new Image(new Texture(Gdx.files.internal("world/water.jpg")));

        map.setName("map");
        water.setName("water");
        play.setName("play");
        menu.setName("menu");
        arrow.setName("arrow");
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

        group.addActor(stage1);
        group.addActor(stage2);
        group.addActor(stage3);
        group.addActor(stage4);
        group.addActor(stage5);
        group.addActor(play);
        group.addActor(exit);
        group.addActor(water);
        group.addActor(map);
        group.addActor(icon1);
        group.addActor(icon2);
        group.addActor(icon3);
        group.addActor(icon4);
        group.addActor(icon5);
        group.addActor(menu);
        group.addActor(arrow);

        stage.addActor(group);

        map.setZIndex(2);
        water.setZIndex(1);
        stage1.setZIndex(0);
        stage2.setZIndex(0);
        stage3.setZIndex(0);
        stage4.setZIndex(0);
        stage5.setZIndex(0);
        play.setZIndex(0);
        exit.setZIndex(0);
        arrow.setZIndex(20);

        map.setOrigin(0, 0);
        map.setScale(viewport.getWorldWidth() / map.getWidth(), viewport.getWorldHeight() / map.getHeight());
        map.setPosition(0, 0);
        water.setOrigin(0, 0);
        water.setScale(viewport.getWorldWidth() / water.getWidth(), viewport.getWorldHeight() / water.getHeight());
        water.setPosition(0, 0);

        play.setScale(.65f);
        play.setOrigin(0, 0);
        play.setPosition(viewport.getWorldWidth() / 2 + 150, viewport.getWorldHeight() / 2 - 260);
        exit.setScale(1f);
        exit.setOrigin(0, 0);
        exit.setPosition(viewport.getWorldWidth() / 2 + 320, viewport.getWorldHeight() / 2 + 260);

        icon1.setScale(0.3f);
        icon1.setOrigin(0, 0);
        icon1.setPosition(775.29376f, 176.95001f);
        stage1.setScale(0.9f);
        stage1.setOrigin(0, 0);
        stage1.setPosition(viewport.getWorldWidth() / 2 - stage1.getWidth() * stage1.getScaleX() / 2, viewport.getWorldHeight() / 2 - stage1.getHeight() * stage1.getScaleY() / 2);

        icon2.setScale(0.9f);
        icon2.setOrigin(0, 0);
        icon2.setPosition(325.83545f, 453.82504f);
        stage2.setScale(0.9f);
        stage2.setOrigin(0, 0);
        stage2.setPosition(viewport.getWorldWidth() / 2 - stage2.getWidth() * stage2.getScaleX() / 2, viewport.getWorldHeight() / 2 - stage2.getHeight() * stage2.getScaleY() / 2);

        icon3.setScale(0.3f);
        icon3.setOrigin(0, 0);
        icon3.setPosition(570.648f, 545.2626f);
        stage3.setScale(0.9f);
        stage3.setOrigin(0, 0);
        stage3.setPosition(viewport.getWorldWidth() / 2 - stage3.getWidth() * stage3.getScaleX() / 2, viewport.getWorldHeight() / 2 - stage3.getHeight() * stage3.getScaleY() / 2);

        icon4.setScale(0.25f);
        icon4.setOrigin(0, 0);
        icon4.setPosition(630.8559f, 316.95004f);
        stage4.setScale(0.9f);
        stage4.setOrigin(0, 0);
        stage4.setPosition(viewport.getWorldWidth() / 2 - stage4.getWidth() * stage4.getScaleX() / 2, viewport.getWorldHeight() / 2 - stage4.getHeight() * stage4.getScaleY() / 2);

        icon5.setScale(0.4f);
        icon5.setOrigin(0, 0);
        icon5.setPosition(983.3172f, 320.38754f);
        stage5.setScale(0.9f);
        stage5.setOrigin(0, 0);
        stage5.setPosition(viewport.getWorldWidth() / 2 - stage5.getWidth() * stage5.getScaleX() / 2, viewport.getWorldHeight() / 2 - stage5.getHeight() * stage5.getScaleY() / 2);

        arrow.setScale(0.08f);
        arrow.setOrigin(0, 0);
        arrow.setPosition(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2);

        menu.setScale(0.1f);
        menu.setOrigin(0, 0);
        menu.setPosition(0, 0);

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

        click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(click);

        if (!stop) {
            arrow.setPosition(click.x - arrow.getWidth() / 2 * arrow.getScaleX(), click.y - arrow.getHeight() / 2 * arrow.getScaleY());
        }

        arrow.setZIndex(0);
        Actor hitActor = stage.hit(arrow.getX(), arrow.getY() + arrow.getHeight() * arrow.getScaleY(), false);

        growEffect();

        if (Gdx.input.justTouched() && hitActor != null) {
            if ("menu".equals(hitActor.getName())) {
                transitionScreen(new MenuScreen(super.game, this),
                        TransitionScreen.Effect.FADE_IN_OUT, 1f);
                stop = true;

            } else if (s1) {
                if ("play".equals(hitActor.getName())) {
                    firstStage(true);
                }
                if ("exit".equals(hitActor.getName())) {
                    stage1.setZIndex(0);
                    play.setZIndex(0);
                    exit.setZIndex(0);
                    s1 = false;

                }
            } else if (s2) {
                if ("play".equals(hitActor.getName())) {
                    secondStage(true);
                }
                if ("exit".equals(hitActor.getName())) {
                    stage2.setZIndex(0);
                    play.setZIndex(0);
                    exit.setZIndex(0);
                    s2 = false;

                }
            } else if (s3) {
                if ("play".equals(hitActor.getName())) {
                    thirdStage(true);
                }
                if ("exit".equals(hitActor.getName())) {
                    stage3.setZIndex(0);
                    play.setZIndex(0);
                    exit.setZIndex(0);
                    s3 = false;

                }
            } else if (s4) {
                if ("play".equals(hitActor.getName())) {
                    fourthStage(true);
                }
                if ("exit".equals(hitActor.getName())) {
                    stage4.setZIndex(0);
                    play.setZIndex(0);
                    exit.setZIndex(0);
                    s4 = false;

                }
            } else if (s5) {
                if ("play".equals(hitActor.getName())) {
                    lastStage(true);
                }
                if ("exit".equals(hitActor.getName())) {
                    stage5.setZIndex(0);
                    play.setZIndex(0);
                    exit.setZIndex(0);
                    s5 = false;

                }
            } else if ("icon1".equals(hitActor.getName())) {
                firstStage(false);
            } else if ("icon2".equals(hitActor.getName())) {
                secondStage(false);
            } else if ("icon3".equals(hitActor.getName())) {
                thirdStage(false);
            } else if ("icon4".equals(hitActor.getName())) {
                lastStage(false);
            } else if ("icon5".equals(hitActor.getName())) {
                fourthStage(false);
            } else {
                stop = false;
            }

        }
        arrow.setZIndex(20);
    }

    private void firstStage(boolean go) {
        s1 = true;
        stage1.setZIndex(18);
        exit.setZIndex(19);
        play.setZIndex(19);
        if (go) {
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
            ), .2f, .2f), TransitionScreen.Effect.FADE_IN_OUT,
                    1f);
        }
    }

    private void secondStage(boolean go) {
        s2 = true;
        stage2.setZIndex(18);
        exit.setZIndex(19);
        play.setZIndex(19);
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
            ), .4f, .4f), TransitionScreen.Effect.FADE_IN_OUT, 1f);
        }
    }

    private void thirdStage(boolean go) {
        s3 = true;
        stage3.setZIndex(18);
        exit.setZIndex(19);
        play.setZIndex(19);
        if (go) {
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
    }

    private void fourthStage(boolean go) {
        s4 = true;
        stage4.setZIndex(18);
        exit.setZIndex(19);
        play.setZIndex(19);
        if (go) {
            stage4.setZIndex(stage4.getZIndex() + 7);
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
    }

    private void lastStage(boolean go) {
        s5 = true;
        stage5.setZIndex(18);
        exit.setZIndex(19);
        play.setZIndex(19);
        if (go) {
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
    }

    private void growEffect() {
        Actor hitActor = stage.hit(arrow.getX(), arrow.getY() + arrow.getHeight() * arrow.getScaleY(), false);
        if (!stop && hitActor != null) {
            if ("icon1".equals(hitActor.getName())) {
                if (check) {
                    icon1.setScale(.4f);
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
            } else if ("menu".equals(hitActor.getName())) {
                if (check) {
                    menu.setScale(.15f);
                    check = !check;
                }
            } else {
                menu.setScale(.1f);

                icon1.setScale(0.3f);
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
    
    @Override
    public void draw() {
        stage.draw();
    }
}
