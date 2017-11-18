package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.audio.Sound;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.sound.MySound;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

public class NinjaCat extends MiniGame {

    private int spawnedZombies, totalZombies;
    private int gcount;
    private int enemiesKilled;

    private float spawnInterval;
    private float speed;
    private float catSpeed;

    private Sprite backGround;
    private Sprite arrow;
    private Cat cat;
    private CatIntro ci;
    private Array<Zombie> zombies;
    private Array<DeadZombie> deadzomb;
    private KillingZombie god;

    private boolean rampage;
    private boolean right;
    private boolean flip;
    private boolean hit;
    private boolean gameover;
    private boolean gameclear;
    private boolean end;
    private boolean pose;
    private boolean victory;

    private Texture playerTexture;
    private Texture arrowTexture;
    private Texture bgTexture;
    private Texture zombieTex;
    private Texture zombieflipTex;
    private Texture rampageTex;
    private Texture deadZombie, deadZombieFlip;
    private Texture atk1, atk;
    private Texture d1, d2, d3, d4, d5, d6;
    private Texture texhit, texhit1;
    private Texture killZombie;
    private Texture catPose;

    private MySound intro;
    private MySound ken1, ken2;
    private MySound gosound;
    private MySound dzsound, dzsound1;

    public NinjaCat(BaseScreen screen,
            MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 50f,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {

        bgTexture = assets.get("ninja-cat/bg1.png", Texture.class);
        arrowTexture = assets.get("ninja-cat/arrow.png", Texture.class);
        zombieTex = assets.get("ninja-cat/zombie.png", Texture.class);
        zombieflipTex = assets.get("ninja-cat/zombieflip.png", Texture.class);
        rampageTex = assets.get("ninja-cat/cat1.png", Texture.class);
        atk1 = assets.get("ninja-cat/atk1.png", Texture.class);
        atk = assets.get("ninja-cat/atk.png", Texture.class);
        deadZombie = assets.get("ninja-cat/zombie1.png", Texture.class);
        deadZombieFlip = assets.get("ninja-cat/zombie1flip.png", Texture.class);
        killZombie = assets.get("ninja-cat/zombie2.png", Texture.class);
        d1 = assets.get("ninja-cat/d1.png", Texture.class);
        d2 = assets.get("ninja-cat/d2.png", Texture.class);
        d3 = assets.get("ninja-cat/d3.png", Texture.class);
        d4 = assets.get("ninja-cat/d4.png", Texture.class);
        d5 = assets.get("ninja-cat/d5.png", Texture.class);
        d6 = assets.get("ninja-cat/d6.png", Texture.class);
        texhit = assets.get("ninja-cat/hit.png", Texture.class);
        texhit1 = assets.get("ninja-cat/hit1.png", Texture.class);
        catPose = assets.get("ninja-cat/intro.png", Texture.class);

        intro = new MySound(assets.get("ninja-cat/Intro.mp3", Sound.class));
        ken1 = new MySound(assets.get("ninja-cat/ken1.mp3", Sound.class));
        ken2 = new MySound(assets.get("ninja-cat/ken2.mp3", Sound.class));
        gosound = new MySound(assets.get("ninja-cat/GameOver.mp3", Sound.class));
        dzsound = new MySound(assets.get("ninja-cat/zombie.mp3", Sound.class));
        dzsound1 = new MySound(assets.get("ninja-cat/zombie1.mp3", Sound.class));

        arrow = new Sprite(arrowTexture);
        arrow.setScale(0.08f);
        arrow.setOrigin(0, 0);
        arrow.setPosition(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2);

        backGround = new BackGround(bgTexture);
        backGround.setOrigin(0, 0);
        backGround.setScale(viewport.getWorldWidth() / backGround.getWidth(), viewport.getWorldHeight() / backGround.getHeight());
        backGround.setPosition(0, 0);

        zombies = new Array<Zombie>();
        deadzomb = new Array<DeadZombie>();

        ci = new CatIntro(catPose);
        ci.setOrigin(0, 0);
        ci.setScale(1.75f);
        ci.setPosition(viewport.getWorldWidth() * 0.45f, viewport.getWorldHeight() * .1f);

        hit = true;
        gcount = 0;
        pose = true;
        end = false;
        right = true;
        flip = false;
        intro.play(.3f);
        rampage = false;
        victory = false;
        gameover = false;
        gameclear = false;

    }

    private void scheduleZombiesSpawn() {
        Task t = new Task() {
            @Override
            public void run() {
                spawnZombies();
                if (++spawnedZombies < totalZombies) {
                    scheduleZombiesSpawn();
                }
            }
        };
        float nextSpawnMillis = this.spawnInterval
                * (rand.nextFloat() / 3 + 0.15f);
        timer.scheduleTask(t, nextSpawnMillis);

    }

    private void spawnZombies() {
        Zombie zomb;
        if (spawnedZombies % 2 == 0) {
            zomb = new Zombie(zombieTex);
            zomb.setCenter(0, 0);
            zomb.setScale(2.25f);
            zomb.setPosition(0, viewport.getWorldHeight() * .1f + 35);
        } else {
            zomb = new Zombie(zombieflipTex);
            zomb.setCenter(0, 0);
            zomb.setScale(2.25f);
            zomb.setPosition(viewport.getWorldWidth(), viewport.getWorldHeight() * .1f + 35);
        }
        zombies.add(zomb);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {

        this.speed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 2, 6);
        this.spawnInterval = DifficultyCurve.S_NEGATIVE
                .getCurveValueBetween(difficulty, .65f, 2.5f);
        this.totalZombies = (int) Math.ceil(maxDuration / spawnInterval) + 8;
        this.catSpeed = DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 24, 33);
    }

    @Override
    public void onHandlePlayingInput() {
        Vector2 click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(click);
        arrow.setPosition(click.x - arrow.getWidth() / 2 * arrow.getScaleX(), click.y - arrow.getHeight() / 2 * arrow.getScaleY());

        if (!pose) {
            if (arrow.getX() > cat.getX() && !right && !rampage && !gameover && !gameclear) {
                if (hit) {
                    setCat(texhit1);
                } else {
                    setCat(texhit);
                }
                cat.flipFrames(true, false);
                right = true;
            } else if (arrow.getX() < cat.getX() && right && !rampage && !gameover && !gameclear) {
                if (hit) {
                    setCat(texhit1);
                } else {
                    setCat(texhit);
                }
                cat.flipFrames(true, false);
                right = false;
            }
            if (!rampage && !gameover) {
                for (Zombie zomb : zombies) {
                    if (zomb.getBoundingRectangle().overlaps(arrow.getBoundingRectangle())) {
                        rampage = true;
                        if (arrow.getX() > cat.getX() && !right) {
                            flip = false;
                            right = true;
                        } else if (arrow.getX() < cat.getX() && right) {
                            flip = true;
                            right = false;
                        }
                        break;
                    }
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && !rampage) {
                    for (Zombie zomb : zombies) {
                        if (zomb.getX() > cat.getX()) {

                            rampage = true;
                            if (!right) {
                                right = true;
                            }
                            break;

                        }
                    }
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && !rampage) {
                    for (Zombie zomb : zombies) {
                        if (zomb.getX() < cat.getX()) {

                            rampage = true;
                            if (right) {
                                right = false;
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    class Cat extends AnimatedSprite {

        static final int FRAME_WIDTH = 95;
        static final int FRAME_HEIGHT = 76;

        Cat(final Texture catTexture) {
            super(new Animation(.14f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            catTexture, FRAME_WIDTH, FRAME_HEIGHT);
                    super.addAll(new TextureRegion[]{
                        frames[0][0]
                    });
                }
            }));
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(false);
        }
    }

    class Zombie extends AnimatedSprite {

        static final int FRAME_WIDTH = 50;
        static final int FRAME_HEIGHT = 79;

        Zombie(final Texture zombieTexture) {
            super(new Animation(.18f / (speed / 3), new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            zombieTexture, FRAME_WIDTH, FRAME_HEIGHT);
                    super.addAll(new TextureRegion[]{
                        frames[0][0],
                        frames[0][1],
                        frames[0][2],
                        frames[0][3],
                        frames[0][4],
                        frames[0][5],
                        frames[0][6],
                        frames[0][7],
                        frames[0][8],
                        frames[0][9],
                        frames[0][10],
                        frames[0][11]

                    });
                }
            }));
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(true);

        }
    }

    class BackGround extends AnimatedSprite {

        static final int FRAME_WIDTH = 768;
        static final int FRAME_HEIGHT = 368;

        BackGround(final Texture bgTexture) {
            super(new Animation(.25f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            bgTexture, FRAME_WIDTH, FRAME_HEIGHT);
                    super.addAll(new TextureRegion[]{
                        frames[0][0],
                        frames[0][1],
                        frames[0][2],
                        frames[0][3],
                        frames[0][4],
                        frames[0][5],
                        frames[0][6],
                        frames[0][7],});
                }
            }));
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(true);

        }
    }

    class DeadZombie extends AnimatedSprite {

        static final int FRAME_WIDTH = 52;
        static final int FRAME_HEIGHT = 70;

        DeadZombie(final Texture deadzombieTexture) {
            super(new Animation(.14f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            deadzombieTexture, FRAME_WIDTH, FRAME_HEIGHT);
                    super.addAll(new TextureRegion[]{
                        frames[0][0],
                        frames[0][1],
                        frames[0][2],
                        frames[0][3],
                        frames[0][4],
                        frames[0][5],
                        frames[0][6],
                        frames[0][7]

                    });
                }
            }));
            super.getAnimation().setPlayMode(Animation.PlayMode.NORMAL);
            super.setAutoUpdate(true);

        }
    }

    class KillingZombie extends AnimatedSprite {

        static final int FRAME_WIDTH = 60;
        static final int FRAME_HEIGHT = 79;

        KillingZombie(final Texture deadzombieTexture) {
            super(new Animation(.18f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            deadzombieTexture, FRAME_WIDTH, FRAME_HEIGHT);
                    super.addAll(new TextureRegion[]{
                        frames[0][0],
                        frames[0][1],
                        frames[0][2],
                        frames[0][3],
                        frames[0][4]

                    });
                }
            }));
            super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.setAutoUpdate(true);

        }
    }

    class CatIntro extends AnimatedSprite {

        static final int FRAME_WIDTH = 70;
        static final int FRAME_HEIGHT = 85;

        CatIntro(final Texture catPose) {
            super(new Animation(.18f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            catPose, FRAME_WIDTH, FRAME_HEIGHT);
                    super.addAll(new TextureRegion[]{
                        frames[0][0],
                        frames[0][1],
                        frames[0][2],
                        frames[0][3],
                        frames[0][4]

                    });
                }
            }));
            if (pose && !end) {
                super.getAnimation().setPlayMode(Animation.PlayMode.NORMAL);
            }
            if (gameclear) {
                super.getAnimation().setPlayMode(Animation.PlayMode.REVERSED);
            }
            super.setAutoUpdate(true);

        }
    }

    void setCat(Texture tex) {
        float x, y;
        if (pose) {
            x = ci.getX();
            y = ci.getY();
        } else {
            x = cat.getX();
            y = cat.getY();
        }
        pose = false;

        cat = new Cat(tex);
        cat.setOrigin(0, 0);
        cat.setScale(1.5f);
        cat.setPosition(x, y);

        if (gameover) {
            if (god.getX() < cat.getX()) {
                cat.flipFrames(true, false);
            }
        } else if (!right) {
            cat.flipFrames(true, false);
        }
    }

    @Override
    public void onUpdate(float dt) {
        if (gameclear && !end) {
            if (victory) {
                ci = new CatIntro(catPose);
                ci.setOrigin(0, 0);
                ci.setScale(1.75f);
                ci.setPosition(cat.getX(), cat.getY());
                if (!right) {
                    ci.flipFrames(true, false);
                }
                end = true;
            } else {
                gcount++;
                if (gcount >= 15) {
                    victory = true;
                }
            }
        } else if (end) {
            if (ci.isAnimationFinished()) {
                end = false;
                super.challengeSolved();
            }
        } else if (pose) {
            if (ci.isAnimationFinished()) {
                scheduleZombiesSpawn();
                setCat(texhit1);
                ci.setX(viewport.getWorldWidth());
            }
        } else if (gameover) {
            if (gcount == 0) {
                setCat(d1);
            }
            if (gcount == 10) {
                setCat(d2);
            }
            if (gcount == 20) {
                setCat(d3);
            }
            if (gcount == 30) {
                setCat(d4);
            }
            if (gcount == 40) {
                setCat(d5);
            }
            if (gcount == 50) {
                setCat(d6);
            }
            if (gcount == 55) {
                super.challengeFailed();
            }
            gcount++;
            cat.update();
        } else {
            for (DeadZombie dz : deadzomb) {
                if (dz.isAnimationFinished()) {
                    deadzomb.removeValue(dz, true);
                }
            }
            if (rampage) {
                setCat(rampageTex);
                if (right) {
                    cat.setX(cat.getX() + catSpeed);
                } else {
                    cat.setX(cat.getX() - catSpeed);
                }

                for (Zombie zomb : zombies) {
                    if (zomb.getBoundingRectangle().overlaps(cat.getBoundingRectangle())) {
                        if (rand.nextInt() % 2 == 0) {
                            dzsound.play(.017f);
                        } else {
                            dzsound1.play(.017f);
                        }
                        rampage = false;
                        if (rand.nextInt() % 2 == 0) {
                            ken1.play(.05f);
                        } else {
                            ken2.play(.05f);
                        }

                        if (hit) {
                            setCat(atk1);

                        } else {
                            setCat(atk);
                        }
                        hit = !hit;
                        DeadZombie dz;
                        if (!right) {
                            dz = new DeadZombie(deadZombie);
                        } else {
                            dz = new DeadZombie(deadZombieFlip);
                        }
                        dz.setCenter(0, 0);
                        dz.setScale(2.25f);
                        dz.setPosition(zomb.getX(), zomb.getY());
                        deadzomb.add(dz);
                        this.zombies.removeValue(zomb, true);
                        this.enemiesKilled++;
                        if (this.enemiesKilled >= this.totalZombies) {
                            gameclear = true;
                        }
                    }
                }
            }
            cat.update();

            for (Zombie zomb : zombies) {
                if (zomb.getX() < cat.getX()) {
                    zomb.setPosition(zomb.getX() + speed, zomb.getY());
                } else {
                    zomb.setPosition(zomb.getX() - speed, zomb.getY());
                }
                zomb.setScale(.75f);
                cat.setScale(1f);
                if (zomb.getBoundingRectangle().overlaps(cat.getBoundingRectangle()) && !rampage) {
                    gosound.play();
                    gameover = true;
                    this.god = new KillingZombie(killZombie);
                    god.setCenter(0, 0);
                    god.setScale(2.25f);
                    god.setPosition(zomb.getX(), zomb.getY());
                    if (zomb.getX() > cat.getX()) {
                        god.flipFrames(true, false);
                    }
                    zombies.removeValue(zomb, true);
                }
                cat.setScale(1.5f);
                zomb.setScale(2.25f);

            }
        }
    }

    @Override
    public String getInstructions() {
        return "Mate os Zumbis";
    }

    @Override
    public void onDrawGame() {
        backGround.draw(batch);

        for (DeadZombie zomb : deadzomb) {
            zomb.draw(batch);
        }
        if (victory || pose) {
            ci.draw(batch);
        } else {
            cat.draw(batch);
        }

        if (gameover) {
            god.draw(batch);
        }
        for (Zombie zomb : zombies) {
            zomb.draw(batch);
        }
        arrow.draw(batch);
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }
}
