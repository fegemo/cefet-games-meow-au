package br.cefetmg.games.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import br.cefetmg.games.sound.MyMusic;
import br.cefetmg.games.sound.MySound;

/**
 * Classe do jogo AstroCat
 * 
 * @author andrebrait
 *
 */
public class AstroCat extends MiniGame {

	private static final int NUM_ASTEROIDS = 6;

	private ParticleEffect rocket;
	private Texture[] asteroids;
	private Texture background, astrocat, planet;
	private MySound gasnoise, impact;
	private MyMusic backgroundMusic;
	
	private double asteroidsPerSecond;
	private double asteroidSpeed;

	public AstroCat(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
		super(screen, observer, difficulty, 15.0f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
	}

	@Override
	protected void onStart() {
		rocket = new ParticleEffect();
		rocket.loadEmitters(Gdx.files.local("astrocat/rocket.p"));
		asteroids = new Texture[NUM_ASTEROIDS];
		for (int i = 0; i < NUM_ASTEROIDS; i++) {
			asteroids[i] = assets.get("astrocat/asteroid" + (i + 1) + ".png", Texture.class);
		}
		astrocat = assets.get("astrocat/astrocat.png", Texture.class);
		background = assets.get("astrocat/background.png", Texture.class);
		planet = assets.get("astrocat/planet.png", Texture.class);
		gasnoise = new MySound(assets.get("astrocat/gasnoise.mp3", Sound.class));
		impact = new MySound(assets.get("astrocat/impact.ogg", Sound.class));
		backgroundMusic = new MyMusic(assets.get("astrocat/background.mp3", Music.class));
	}

	@Override
	protected void configureDifficultyParameters(float difficulty) {
		asteroidsPerSecond = DifficultyCurve.S.getCurveValueBetween(difficulty, 2.0f, 20.0f);
		asteroidSpeed = DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 1.0f, 3.0f);
	}

	@Override
	public void onHandlePlayingInput() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdate(float dt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDrawGame() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getInstructions() {
		return "Guie o gato no espaço até o planeta!";
	}

	@Override
	public boolean shouldHideMousePointer() {
		// TODO Auto-generated method stub
		return false;
	}

}
