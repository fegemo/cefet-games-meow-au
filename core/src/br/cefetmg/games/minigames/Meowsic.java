package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import br.cefetmg.games.sound.MyMusic;
import br.cefetmg.games.sound.MySound;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class Meowsic extends MiniGame {

    private Texture background;
    private MySound song1, song2, song3, song4, song5, fail;
    private MyMusic music1, music2, music3, music4, music5;
    private MyMusic chosenMusic;
    private Texture cat;
    private Texture sheet;
    private Texture note1, note2, note3, note4, note5;
    private Texture pressed1, pressed2, pressed3, pressed4, pressed5;
    private float catX;
    private float catY;
    private static final int NUMBER_OF_NOTES = 25;
    private final int[] noteX = new int[NUMBER_OF_NOTES];
    private final float[] noteY = new float[NUMBER_OF_NOTES];
    private final int[] clicked = new int[NUMBER_OF_NOTES];
    private final int laneSize = 102;
    private int i = 0;
    private int k = 0;
    private double velocidade = 1;
    private int errorCounter = 0;

    public Meowsic(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
        super(screen, observer, difficulty, 10f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS);
    }

    @Override
    protected void onStart() {
        background = assets.get("meowsic/background.png", Texture.class);
        cat = assets.get("meowsic/cat.png", Texture.class);
        sheet = assets.get("meowsic/sheet.png", Texture.class);
        pressed1 = assets.get("meowsic/Pressed1.png", Texture.class);
        pressed2 = assets.get("meowsic/Pressed2.png", Texture.class);
        pressed3 = assets.get("meowsic/Pressed3.png", Texture.class);
        pressed4 = assets.get("meowsic/Pressed4.png", Texture.class);
        pressed5 = assets.get("meowsic/Pressed5.png", Texture.class);
        note1 = assets.get("meowsic/note1.png", Texture.class);
        note2 = assets.get("meowsic/note2.png", Texture.class);
        note3 = assets.get("meowsic/note3.png", Texture.class);
        note4 = assets.get("meowsic/note4.png", Texture.class);
        note5 = assets.get("meowsic/note5.png", Texture.class);
        song1 = new MySound(assets.get("meowsic/song1.wav", Sound.class));
        song2 = new MySound(assets.get("meowsic/song2.wav", Sound.class));
        song3 = new MySound(assets.get("meowsic/song3.wav", Sound.class));
        song4 = new MySound(assets.get("meowsic/song4.wav", Sound.class));
        song5 = new MySound(assets.get("meowsic/song5.wav", Sound.class));
        music1 =  new MyMusic(assets.get("meowsic/music1.mp3", Music.class));
        music2 =  new MyMusic(assets.get("meowsic/music2.mp3", Music.class));
        music3 =  new MyMusic(assets.get("meowsic/music3.mp3", Music.class));
        music4 =  new MyMusic(assets.get("meowsic/music4.mp3", Music.class));
        music5 =  new MyMusic(assets.get("meowsic/music5.mp3", Music.class));
        fail = new MySound(assets.get("meowsic/fail.wav", Sound.class));

        for (i = 0; i < NUMBER_OF_NOTES; i++) {
            noteX[i] = 350 + laneSize * MathUtils.random(0, 4);
            noteY[i] = 470 + i * 125;
            clicked[i] = 0;
        }
        
        //Seleciona música de acordo com a velocidade
        if(velocidade < 2.7) {
            chosenMusic = music1;
        } else if (velocidade < 3.0) {
            chosenMusic = music2;
        } else if (velocidade < 3.8) {
            chosenMusic = music3;
        } else if (velocidade < 4.6) {
            chosenMusic = music4;
        } else {
            chosenMusic = music5;
        }
        
        chosenMusic.play();
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.velocidade = 0.5 * (DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 5, 10));
    }

    @Override
    public void onHandlePlayingInput() {
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(click);
        catX = click.x - cat.getWidth() / 2;
        catY = click.y - cat.getHeight() / 2;
    }

    @Override
    public void onUpdate(float dt) {
        for (k = 0; k < NUMBER_OF_NOTES; k++) {
            if (clicked[k] == 0) {
                noteY[k] -= velocidade;
            }
            //Remove nota da tela ao passar do limite para ser clicada
            if (noteY[k] <= 0) {
                clicked[k] = 2;
            }
        }
    }
    
    @Override
    public void onGamePaused(boolean justPaused){
       if (justPaused){
           chosenMusic.pause();
       } else {
           chosenMusic.play();
       }
    }
    
    @Override
    public void onDrawGame() {
        // Desenha cenário
        batch.draw(background, 0, 0);
        batch.draw(sheet, 350, 0);
        BitmapFont font = new BitmapFont();
        font.setColor(Color.WHITE);
        int somaErros = 0, somaAcertos = 0;

        for (i = 0; i < NUMBER_OF_NOTES; i++) {
            if (clicked[i] == 1) {
                somaAcertos = somaAcertos + 1;
            } else if (clicked[i] == 2 && errorCounter == 0) {
                somaErros = somaErros + 1;
            }
        }
        if (somaErros >= 6) {
            errorCounter = 1;
            super.challengeFailed();
            chosenMusic.stop();
            fail.play();
        }

        font.draw(batch, "Acertos: " + somaAcertos, 1000, 700);
        font.draw(batch, "Erros: " + somaErros, 100, 700);

        // Desenha as notas correspondentes a cada coluna
        for (i = 0; i < NUMBER_OF_NOTES; i++) {
            if (noteX[i] == 350) {
                if (clicked[i] == 0) {
                    batch.draw(note1, noteX[i], noteY[i]);
                } else if (clicked[i] == 1) {
                    batch.draw(note1, 1000, 50 + (i * 20));
                } else if (clicked[i] == 2) {
                    batch.draw(note1, 100, 50 + (i * 20));
                }
            } else if (noteX[i] == 350 + laneSize * 1) {
                if (clicked[i] == 0) {
                    batch.draw(note2, noteX[i], noteY[i]);
                } else if (clicked[i] == 1) {
                    batch.draw(note2, 1000, 50 + (i * 20));
                } else if (clicked[i] == 2) {
                    batch.draw(note2, 100, 50 + (i * 20));
                }
            } else if (noteX[i] == 350 + laneSize * 2) {
                if (clicked[i] == 0) {
                    batch.draw(note3, noteX[i], noteY[i]);
                } else if (clicked[i] == 1) {
                    batch.draw(note3, 1000, 50 + (i * 20));
                } else if (clicked[i] == 2) {
                    batch.draw(note3, 100, 50 + (i * 20));
                }
            } else if (noteX[i] == 350 + laneSize * 3) {
                if (clicked[i] == 0) {
                    batch.draw(note4, noteX[i], noteY[i]);
                } else if (clicked[i] == 1) {
                    batch.draw(note4, 1000, 50 + (i * 20));
                } else if (clicked[i] == 2) {
                    batch.draw(note4, 100, 50 + (i * 20));
                }
            } else if (noteX[i] >= 350 + laneSize * 3) {
                if (clicked[i] == 0) {
                    batch.draw(note5, noteX[i], noteY[i]);
                } else if (clicked[i] == 1) {
                    batch.draw(note5, 1000, 50 + (i * 20));
                } else if (clicked[i] == 2) {
                    batch.draw(note5, 100, 50 + (i * 20));
                }
            }

            //Verifica colisão do click com a nota e ajusta realidade da hit box
            if (catY >= noteY[i] - 25 && catY <= noteY[i] + 60 && catX >= noteX[i] - 20 && catX <= noteX[i] + 65 && (Gdx.input.justTouched()) && clicked[i] == 0) {
                if (noteX[i] == 350 && noteY[i] <= 90 && noteY[i] >= 10) {
                    song1.play(0.5f);
                    batch.draw(pressed1, 350, 0);
                    clicked[i] = 1;
                } else if (noteX[i] == 350 + laneSize * 1 && noteY[i] <= 90 && noteY[i] >= 10) {
                    song2.play(0.5f);
                    batch.draw(pressed2, 350, 0);
                    clicked[i] = 1;
                } else if (noteX[i] == 350 + laneSize * 2 && noteY[i] <= 90 && noteY[i] >= 10) {
                    song3.play(0.5f);
                    batch.draw(pressed3, 350, 0);
                    clicked[i] = 1;
                } else if (noteX[i] == 350 + laneSize * 3 && noteY[i] <= 90 && noteY[i] >= 10) {
                    song4.play(0.5f);
                    batch.draw(pressed4, 350, 0);
                    clicked[i] = 1;
                } else if (noteX[i] >= 350 + laneSize * 3 && noteY[i] <= 90 && noteY[i] >= 10) {
                    song5.play(0.5f);
                    batch.draw(pressed5, 350, 0);
                    clicked[i] = 1;
                }
            }
        }
        batch.draw(cat, catX, catY);
    }

    @Override
    public String getInstructions() {
        return "Aperte as notas na hora certa!";

    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }
}
