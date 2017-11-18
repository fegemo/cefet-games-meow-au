package br.cefetmg.games.minigames;

import br.cefetmg.games.graphics.MultiAnimatedSprite;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.audio.Sound;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.sound.MySound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;

/**
 *
 * @author Rógenes
 */
public class BasCATball extends MiniGame {

    private MySound beat;
    private MySound fail;
    private MySound flyingdown;
    private MySound doh;

    private Sprite ball;
    private Sprite player;
    private Sprite bar;
    private Sprite target;
    private Sprite selector;
    private Sprite ball2;
    private Sprite court;
    private Sprite dorami;
    private Sprite pointer;
    private Sprite lightArrowL;
    private Sprite lightArrowR;
    private Sprite darkArrowL;
    private Sprite darkArrowR;
    private Sprite arrowL;
    private Sprite arrowR;

    private Texture ballTexture;
    private Texture playerTexture;
    private Texture barTexture;
    private Texture targetTexture;
    private Texture selectorTexture;
    private Texture courtTexture;
    private Texture doramiTexture;
    private Texture pointerTexture;
    private Texture lightArrowLTexture;
    private Texture lightArrowRTexture;
    private Texture darkArrowLTexture;
    private Texture darkArrowRTexture;
    private Texture doraemonTexture;

    private boolean withBall;
    private boolean shooting;
    private boolean failing;
    private boolean withoutBall;
    private boolean forward;
    private boolean ballGoingLeft;
    private boolean ballGoingRight;
    private boolean stopPlayingBeatStarPlayingFlyingDown;
    private boolean startPlayingFail;
    private boolean startPlayingDoh;
      

    private float barPositionX;
    private float targetPositionX;
    private float selectorPositionX;
    private float selectorPositionY;
    private float selectorSpeed;
    private float barLimit;
    private float targetScaleX;
    private float a, fall = 3;
    private final float gravity = -viewport.getWorldHeight() / 170;
    private final float basketPositionRX = viewport.getWorldWidth() * 0.795f;
    private final float basketPositionLX = viewport.getWorldWidth() * 0.190f;

    private int timerCount = 0;

    private String position;

    private Cat doraemon;

    public BasCATball(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 12f,/*Tempo maximo da fase*/
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {

        withoutBall = true;
        withBall = false;
        shooting = false;
        ballGoingLeft = false;
        ballGoingRight = false;
        stopPlayingBeatStarPlayingFlyingDown = true;
        startPlayingFail = true;
        startPlayingDoh = true;
        position = "standingFront";

        courtTexture = assets.get("bascatball/court.png", Texture.class);
        ballTexture = assets.get("bascatball/ball.png", Texture.class);
        playerTexture = assets.get("bascatball/player.png", Texture.class);
        barTexture = assets.get("bascatball/bar.png", Texture.class);
        targetTexture = assets.get("bascatball/target.png", Texture.class);
        selectorTexture = assets.get("bascatball/selector.png", Texture.class);
        doramiTexture = assets.get("bascatball/dorami.png", Texture.class);
        pointerTexture = assets.get("bascatball/pointer.png", Texture.class);
        lightArrowLTexture = assets.get("bascatball/lightarrowL.png", Texture.class);
        lightArrowRTexture = assets.get("bascatball/lightarrowR.png", Texture.class);
        darkArrowLTexture = assets.get("bascatball/darkarrowL.png", Texture.class);
        darkArrowRTexture = assets.get("bascatball/darkarrowR.png", Texture.class);
        doraemonTexture = assets.get("bascatball/doraemon.png", Texture.class);
        beat = new MySound(assets.get("bascatball/beats.mp3", Sound.class));
        fail = new MySound(assets.get("bascatball/fail.mp3", Sound.class));
        flyingdown = new MySound(assets.get("bascatball/flyingdown.mp3", Sound.class));
        doh = new MySound(assets.get("bascatball/doh.mp3", Sound.class));

        lightArrowL = new Sprite(lightArrowLTexture);
        lightArrowL.setScale(0.3f, 0.6f);
        lightArrowL.setOrigin(0, 0);
        lightArrowL.setPosition(viewport.getWorldWidth() * 0.08f - lightArrowL.getWidth() * lightArrowL.getScaleX(),
                viewport.getWorldHeight() * 0.4f);

        lightArrowR = new Sprite(lightArrowRTexture);
        lightArrowR.setScale(0.3f, 0.6f);
        lightArrowR.setOrigin(0, 0);
        lightArrowR.setPosition(viewport.getWorldWidth() * 0.92f, viewport.getWorldHeight() * 0.4f);

        darkArrowL = new Sprite(darkArrowLTexture);
        darkArrowL.setScale(0.2f, 0.5f);
        darkArrowL.setOrigin(0, 0);
        darkArrowL.setPosition(viewport.getWorldWidth() * 0.08f - darkArrowL.getWidth() * darkArrowL.getScaleX(),
                viewport.getWorldHeight() * 0.4f);

        darkArrowR = new Sprite(darkArrowRTexture);
        darkArrowR.setScale(0.2f, 0.5f);
        darkArrowR.setOrigin(0, 0);
        darkArrowR.setPosition(viewport.getWorldWidth() * 0.92f, viewport.getWorldHeight() * 0.4f);

        arrowL = darkArrowL;
        arrowL.setScale(0.2f, 0.5f);
        arrowL.setOrigin(0, 0);
        arrowL.setPosition(viewport.getWorldWidth() * 0.08f - arrowL.getWidth() * arrowL.getScaleX(),
                viewport.getWorldHeight() * 0.4f);

        arrowR = darkArrowR;
        arrowR.setScale(0.2f, 0.5f);
        arrowR.setOrigin(0, 0);
        arrowR.setPosition(viewport.getWorldWidth() * 0.92f, viewport.getWorldHeight() * 0.4f);

        pointer = new Sprite(pointerTexture);
        pointer.setScale(0.08f);
        pointer.setOrigin(0, 0);
        pointer.setPosition(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2);

        court = new Sprite(courtTexture);
        court.setOrigin(0, 0);
        court.setScale(viewport.getWorldWidth() / court.getWidth(), viewport.getWorldHeight() / court.getHeight());
        court.setPosition(0, 0);

        dorami = new Sprite(doramiTexture);
        dorami.setOrigin(0, 0);
        dorami.setScale(0.3f);
        dorami.setPosition(viewport.getWorldWidth() / 2, viewport.getWorldHeight() * 1.3f);

        player = new Sprite(playerTexture);
        player.setScale(0.2f);
        player.setOrigin(0, 0); //origem se torna canto inferior esquerdo
        player.setPosition(viewport.getWorldWidth() / 2, viewport.getWorldHeight() * 0.25f);

        ball = new Sprite(ballTexture);
        ball.setScale(0.08f);
        ball.setOrigin(0, 0);
        ball.setPosition((basketPositionLX*0.75f + rand.nextFloat() * basketPositionRX*0.85f ),
                viewport.getWorldHeight() + viewport.getWorldHeight() * 0.1f);

        bar = new Sprite(barTexture);
        bar.setScale(0.8f, 0.3f);
        bar.setOrigin(0, 0);
        barPositionX = viewport.getWorldWidth() / 2 - (bar.getWidth() / 2) * bar.getScaleX();
        bar.setPosition(barPositionX, viewport.getWorldHeight() * 0.75f);

        target = new Sprite(targetTexture);
        target.setScale(targetScaleX, 0.35f);
        target.setOrigin(0, 0);
        targetPositionX = viewport.getWorldWidth() / 2 - (target.getWidth() / 2) * target.getScaleX();
        target.setPosition(targetPositionX, viewport.getWorldHeight() * 0.75f);

        selector = new Sprite(selectorTexture);
        selector.setScale(0.8f, 0.8f);
        selector.setOrigin(0, 0);
        selectorPositionX = barPositionX - viewport.getWorldWidth() / 150;
        selectorPositionY = bar.getY() + bar.getHeight() / 2 * bar.getScaleY() - selector.getHeight() / 2 * selector.getScaleY();
        selector.setPosition(selectorPositionX, selectorPositionY);

        ball2 = new Sprite(ballTexture);
        ball2.setScale(0.08f);
        ball2.setOrigin(0, 0);

        barLimit = bar.getX() + bar.getWidth() * bar.getScaleX();

        doraemon = new Cat(doraemonTexture);
        doraemon.setScale(0.3f);
        doraemon.setOrigin(0, 0);
        doraemon.setPosition(player.getX(), player.getY());

        beat.stop();
        beat.loop();

    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        selectorSpeed = DifficultyCurve.LINEAR.getCurveValueBetween(
                difficulty,
                viewport.getWorldWidth() / 95f,
                viewport.getWorldWidth() / 55f
        );
        targetScaleX = DifficultyCurve.LINEAR_NEGATIVE.getCurveValueBetween(difficulty, 0.05f, 0.4f);
    }

    @Override
    public void onHandlePlayingInput() {
        Vector2 click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(click);
        pointer.setPosition(click.x - pointer.getWidth() / 2 * pointer.getScaleX(), click.y - pointer.getHeight() / 2 * pointer.getScaleY());

        if (Gdx.input.isTouched()) {
            if (withoutBall) {
                
                if (Gdx.input.getX() >= viewport.getScreenWidth() / 2) {
                    player.setX(player.getX() + 6);
                    arrowR = lightArrowR;
                    if(player.getX()>=viewport.getWorldWidth()*0.942){
                        player.setX(player.getX() - 6);
                    }
                } else {
                    player.setX(player.getX() - 6);
                    arrowL = lightArrowL;
                    if(player.getX()<=viewport.getWorldWidth()*0.008){
                        player.setX(player.getX() + 6);
                    }
                }
                
            }     
        } else {
            arrowL = darkArrowL;
            arrowR = darkArrowR;
            doraemon.startAnimation(position);
        }

        if (Gdx.input.justTouched()) {
            if (withBall) {
                if (selector.getBoundingRectangle().overlaps(target.getBoundingRectangle())) {
                    shooting = true;
                    withBall = false;
                    withoutBall = false;
                } else {
                    withBall = false;
                    withoutBall = false;
                    failing = true;
                }
            } else if (withoutBall) {
                if (Gdx.input.getX() >= viewport.getScreenWidth() / 2) {
                    doraemon.startAnimation("movingFront");
                    position = "standingFront";

                } else {
                    doraemon.startAnimation("movingBack");
                    position = "standingBack";
                }
            }
        }
    }

    float eqBallRightTrajetory(float posicaox) {
        return viewport.getWorldHeight() * 0.15f + a * posicaox;
    }

    float eqBallLeftTrajetory(float posicaox) {
        return viewport.getWorldHeight() * 0.90f + a * posicaox;
    }

    @Override
    public void onUpdate(float dt) {

        doraemon.update(dt);

        if (withoutBall) {
            ball.setPosition(ball.getX(), ball.getY() + gravity);
            if ((ball.getY() + ball.getHeight() * ball.getScaleY()) < viewport.getScreenHeight() * 0.2) {
                timerCount++;
                beat.stop();
                if(timerCount>5){
                    doh.play();
                    super.challengeFailed();
                }
                
            }//Se não conseguiu nem pegar a bola perde o desafio

            if (ball.getBoundingRectangle().overlaps(player.getBoundingRectangle())) {
                withBall = true;
                withoutBall = false;
                if (player.getX() + player.getWidth() / 2 * player.getScaleX() <= viewport.getScreenWidth() / 2) {
                    a = ((viewport.getWorldHeight() * 8 / 10) - viewport.getWorldHeight() * 0.25f) / (viewport.getScreenWidth() - player.getX() + player.getWidth() / 2 * player.getScaleX());
                    ballGoingRight = true;
                    dorami.setX(basketPositionRX);
                    position = "standingFront";
                } else {
                    a = -(((viewport.getWorldHeight() * 8 / 10) - viewport.getWorldHeight() * 0.25f) / (player.getX() + player.getWidth() / 2 * player.getScaleX()));
                    ballGoingLeft = true;
                    dorami.setX(basketPositionLX);
                    position = "standingBack";
                }
            }
        } else if (withBall) {
            if (ballGoingRight) {
                ball2.setPosition(player.getX() + player.getWidth() / 2 * player.getScaleX(), player.getY() + player.getHeight() * player.getScaleY() / 2);
            } else if (ballGoingLeft) {
                ball2.setPosition(player.getX(), player.getY() + player.getHeight() * player.getScaleY() / 2);
            }

            if (selector.getX() <= bar.getX()) {
                forward = true;
            } else if (selector.getX() >= barLimit) {
                forward = false;
            }
            if (forward) {
                selector.setX(selector.getX() + selectorSpeed);
            } else {
                selector.setX(selector.getX() - selectorSpeed);
            }
        } else if (shooting) {

            if ((ball2.getX() + ball2.getWidth() / 2 * ball2.getScaleX() <= basketPositionRX) && ballGoingRight) {
                ball2.setX(ball2.getX() + viewport.getScreenWidth() / 150);
                ball2.setY(eqBallRightTrajetory(ball2.getX()));
            } else if (ball2.getX() + ball2.getWidth() / 2 * ball2.getScaleX() >= basketPositionLX && ballGoingLeft) {
                ball2.setX(ball2.getX() - viewport.getScreenWidth() / 150);
                ball2.setY(eqBallLeftTrajetory(ball2.getX()));
            } else {

                timerCount++;

                if (timerCount > 10) {
                    dorami.setY(dorami.getY() + gravity - fall);
                    if (stopPlayingBeatStarPlayingFlyingDown) {
                        beat.stop();
                        flyingdown.play(1, 0.8f, 1);
                        stopPlayingBeatStarPlayingFlyingDown=false;
                    }
                }

                if (dorami.getBoundingRectangle().y <= (ball2.getBoundingRectangle().y - 3)) {
                    ball2.setY(ball2.getY() - fall + gravity);
                }
                if (timerCount > 70) {
                    super.challengeSolved();
                }
                fall += 0.15;
            }
        } else if (failing) {
            beat.stop();
            if (startPlayingFail) {
                fail.play(1, 0.8f, 1);
                startPlayingFail = false;
            }
            timerCount++;
            if (ballGoingRight) {
                ball2.setX(ball2.getX() + viewport.getScreenWidth() / 200);
            } else if (ballGoingLeft) {
                ball2.setX(ball2.getX() - viewport.getScreenWidth() / 200);
            }
            if (timerCount > 120 && startPlayingDoh) {
                doh.play();
                startPlayingDoh=false;
            }
            if (timerCount > 150) {
                super.challengeFailed();
            }
        }
    }

    @Override
    public String getInstructions() {
        return "Pegue a bola e faça a cesta";
    }

    @Override
    public void onDrawGame() {

        court.draw(batch);
        //player.draw(batch);
        doraemon.draw(batch);
        pointer.draw(batch);
        if (withoutBall) {
            ball.draw(batch);
            arrowL.draw(batch);
            arrowR.draw(batch);
        } else if (withBall) {
            bar.draw(batch);
            target.draw(batch);
            selector.draw(batch);
            ball2.draw(batch);
        } else if (shooting) {
            ball2.draw(batch);
            dorami.draw(batch);
        } else if (failing) {
            ball2.draw(batch);
        }

    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    class Cat extends MultiAnimatedSprite {

        public Cat(final Texture move) {
            super(new HashMap<String, Animation>() {
                {
                    TextureRegion[][] frames = TextureRegion
                            .split(move, move.getWidth() / 6, move.getHeight() / 3);
                    Animation movingFront = new Animation(0.1f,
                            frames[0][0],
                            frames[0][1],
                            frames[0][2],
                            frames[0][3],
                            frames[0][4],
                            frames[0][5]
                    );
                    movingFront.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
                    put("movingFront", movingFront);

                    TextureRegion[][] frames2 = TextureRegion
                            .split(move, move.getWidth() / 6, move.getHeight() / 3);
                    Animation movingBack = new Animation(0.1f,
                            frames2[1][3],
                            frames2[1][2],
                            frames2[1][1],
                            frames2[1][0],
                            frames2[1][5],
                            frames2[1][4]
                    );
                    movingBack.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
                    put("movingBack", movingBack);

                    TextureRegion[][] frames3 = TextureRegion.split(move, move.getWidth() / 6, move.getHeight() / 3);
                    Animation standingFront = new Animation(0.1f,
                            frames3[2][0]
                    );
                    standingFront.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
                    put("standingFront", standingFront);

                    TextureRegion[][] frames4 = TextureRegion.split(move, move.getWidth() / 6, move.getHeight() / 3);
                    Animation standingBack = new Animation(0.1f,
                            frames4[2][1]
                    );
                    standingBack.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
                    put("standingBack", standingBack);

                }
            }, "standingFront");
        }

        @Override
        public void update(float dt) {
            super.update(dt);
            super.setPosition(player.getX(), player.getY());
        }
    }
}
