package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class Meowsic extends MiniGame {

    private Texture background;
    private Sound song1, song2, song3, song4, song5, music, fail;
    private Texture cat;
    private Texture sheet;
    private Texture note1, note2, note3, note4, note5;
    private Texture pressed1, pressed2, pressed3, pressed4, pressed5;
    public float catX;
    public float catY;
    public static final int NUMBER_OF_NOTES = 25;
    public int[] noteX = new int[NUMBER_OF_NOTES];
    public float[] noteY = new float[NUMBER_OF_NOTES];
    public int[] noteLane = new int[NUMBER_OF_NOTES];
    public int[] clicked = new int[NUMBER_OF_NOTES];
    public int laneSize = 102;
    public int i = 0;
    public int k = 0;
    public double velocidade = 1;
    public double dificuldade = 1;
    public int errorCounter = 0;

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
        song1 = assets.get("meowsic/song1.wav", Sound.class);
        song2 = assets.get("meowsic/song2.wav", Sound.class);
        song3 = assets.get("meowsic/song3.wav", Sound.class);
        song4 = assets.get("meowsic/song4.wav", Sound.class);
        song5 = assets.get("meowsic/song5.wav", Sound.class);
        music = assets.get("meowsic/music.wav", Sound.class);
        fail = assets.get("meowsic/fail.wav", Sound.class);

        for (i = 0; i < NUMBER_OF_NOTES - 1; i++) {
            noteX[i] = 350 + laneSize * MathUtils.random(0, 4);
            noteY[i] = 500 + i * 125;
            clicked[i] = 0;
        }
        //Força que a última nota seja Dó, para soar mais "bonito"
        noteX[NUMBER_OF_NOTES - 1] = 350;
        noteY[NUMBER_OF_NOTES - 1] = 500 + i * 110 + MathUtils.random(0, 20);
        music.play(0.8f);
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
            music.stop();
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
        return "Aperte as notas na hora e ordem certa!";

    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

}
