package br.cefetmg.games.screens;

import br.cefetmg.games.graphics.hud.SoundIcon;
import br.cefetmg.games.transition.TransitionScreen;
import br.cefetmg.games.sound.MyMusic;
import br.cefetmg.games.sound.MySound;
import br.cefetmg.games.sound.SoundManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Uma tela de Menu Principal do jogo.
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class MenuScreen extends BaseScreen {

	public static final int BUTTONS_WIDTH = 204;
	public static final int BUTTONS_HEIGHT = 54;
	public static final int PLAY_Y = 360;
	public static final int RANKING_Y = 276;
	public static final int CREDITS_Y = 192;
	public static final int EXIT_Y = 108;

	public static final int LOGO_X = 160;
	public static final int LOGO_Y = 360;
	public static final int LOGO_WIDTH = 960;
	public static final int LOGO_HEIGHT = 386;

	private SoundIcon soundIcon;

	private TextureRegion background;
	private Texture btnPlay;
	private Texture btnExit;
	private Texture btnRanking;
	private Texture btnCredits;
	private Texture btnNormal;
	private Texture btnSurvival;
	private Texture btnBack;
	private Texture logo;
	private MySound click1;
	private MySound click2;
	private int selecionaModo = 0;

	private boolean shouldContinueBackgroundMusic;

	private static final int getButtonsX(Viewport viewport) {
		return (int) (viewport.getWorldWidth() * 0.6f);
	}

	/**
	 * Cria uma nova tela de menu.
	 *
	 * @param game
	 *            o jogo dono desta tela.
	 * @param previous
	 *            a tela de onde o usuário veio.
	 */
	public MenuScreen(Game game, BaseScreen previous) {
		super(game, previous);
	}

	/**
	 * Configura parâmetros da tela e instancia objetos.
	 */
	@Override
	public void appear() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.input.setCursorCatched(false);

		// instancia música tema
		assets.load("menu/meowautheme.mp3", Music.class);

		// instancia a textura e a região de textura (usada para repetir)
		TextureLoader.TextureParameter linearFilter = new TextureLoader.TextureParameter();
		linearFilter.minFilter = Texture.TextureFilter.Linear;
		linearFilter.magFilter = Texture.TextureFilter.Linear;
		assets.load("menu/menu-background.png", Texture.class, linearFilter);
		assets.load("menu/button_jogar.png", Texture.class, linearFilter);
		assets.load("menu/button_sair.png", Texture.class, linearFilter);
		assets.load("menu/button_ranking.png", Texture.class, linearFilter);
		assets.load("menu/button_creditos.png", Texture.class, linearFilter);
		assets.load("menu/button_normal.png", Texture.class, linearFilter);
		assets.load("menu/button_survival.png", Texture.class, linearFilter);
		assets.load("menu/button_voltar.png", Texture.class, linearFilter);
		assets.load("menu/logo.png", Texture.class, linearFilter);

		assets.load("menu/click1.mp3", Sound.class);
		assets.load("menu/click2.mp3", Sound.class);

		assets.load("hud/no-sound-button.png", Texture.class, linearFilter);
		assets.load("hud/sound-button.png", Texture.class, linearFilter);
		soundIcon = new SoundIcon(new Stage(viewport, batch));
	}

	@Override
	protected void assetsLoaded() {
		background = new TextureRegion(assets.get("menu/menu-background.png", Texture.class));
		btnPlay = assets.get("menu/button_jogar.png", Texture.class);
		btnExit = assets.get("menu/button_sair.png", Texture.class);
		btnRanking = assets.get("menu/button_ranking.png", Texture.class);
		btnCredits = assets.get("menu/button_creditos.png", Texture.class);
		btnNormal = assets.get("menu/button_normal.png", Texture.class);
		btnSurvival = assets.get("menu/button_survival.png", Texture.class);
		btnBack = assets.get("menu/button_voltar.png", Texture.class);
		logo = assets.get("menu/logo.png", Texture.class);

		MyMusic musicaTema = SoundManager.getInstance().playBackgroundMusic("menu/meowautheme.mp3");
		musicaTema.setLooping(true);
		musicaTema.setVolume(0.4f);

		click1 = new MySound(assets.get("menu/click1.mp3", Sound.class));
		click2 = new MySound(assets.get("menu/click2.mp3", Sound.class));
		soundIcon.create(assets.get("hud/no-sound-button.png", Texture.class),
				assets.get("hud/sound-button.png", Texture.class));
		Gdx.input.setInputProcessor(soundIcon.getInputProcessor());
	}

	/**
	 * Recebe <em>input</em> do jogador.
	 */
	@Override
	public void handleInput() {

		// verifica se clique foi em algum botão
		if (Gdx.input.justTouched()) {
			Vector3 clickPosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(clickPosition);
			
			float buttonsX = getButtonsX(viewport);
			Rectangle playBounds = new Rectangle(buttonsX, PLAY_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT);
			Rectangle rankingBounds = new Rectangle(buttonsX, RANKING_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT);
			Rectangle creditsBounds = new Rectangle(buttonsX, CREDITS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT);
			Rectangle exitBounds = new Rectangle(buttonsX, EXIT_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT);

			if (selecionaModo == 0) {
				if (playBounds.contains(clickPosition.x, clickPosition.y)) {
					selecionaModo = 1;
					click2.play();
				}
				if (rankingBounds.contains(clickPosition.x, clickPosition.y)) {
					shouldContinueBackgroundMusic = true;
					transitionScreen(new RankingScreen(super.game, this), TransitionScreen.Effect.FADE_IN_OUT, 0.7f);
					click2.play();
				}
				if (creditsBounds.contains(clickPosition.x, clickPosition.y)) {
					navigateToCredits();
					click2.play();
				}
				if (exitBounds.contains(clickPosition.x, clickPosition.y)) {
					click1.play();
					Gdx.app.exit();
				}
			} else {
				if (playBounds.contains(clickPosition.x, clickPosition.y)) {
					click2.play();
					navigateToMicroGameScreen(false);
				}
				if (rankingBounds.contains(clickPosition.x, clickPosition.y)) {
					click2.play();
					navigateToMicroGameScreen(true);
				}
				if (creditsBounds.contains(clickPosition.x, clickPosition.y)) {
					// Volta para os botões
					selecionaModo = 0;
					click1.play();
				}
			}

		}
	}

	/**
	 * Atualiza a lógica da tela.
	 *
	 * @param dt
	 *            Tempo desde a última atualização.
	 */
	@Override
	public void update(float dt) {
		soundIcon.update(dt);
	}

	/**
	 * Desenha o conteúdo da tela de Menu.
	 */
	@Override
	public void draw() {
		batch.begin();
		batch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
		batch.draw(logo, LOGO_X, LOGO_Y, LOGO_WIDTH, LOGO_HEIGHT);

		float buttonsX = getButtonsX(viewport);
		if (selecionaModo == 0) {
			batch.draw(btnPlay, buttonsX, PLAY_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT);
			batch.draw(btnRanking, buttonsX, RANKING_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT);
			batch.draw(btnCredits, buttonsX, CREDITS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT);
			batch.draw(btnExit, buttonsX, EXIT_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT);
		} else if (selecionaModo == 1) {
			batch.draw(btnNormal, buttonsX, PLAY_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT);
			batch.draw(btnSurvival, buttonsX, RANKING_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT);
			batch.draw(btnBack, buttonsX, CREDITS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT);
		}

		batch.end();

		soundIcon.draw();
	}

	/**
	 * Navega para a tela de jogo.
	 */
	private void navigateToMicroGameScreen(boolean isSurvival) {
		if (isSurvival) {
			transitionScreen(new PlayingGamesScreen(super.game, this), TransitionScreen.Effect.FADE_IN_OUT, 0.7f);
		} else {
			transitionScreen(new OverworldScreen(super.game, this), TransitionScreen.Effect.FADE_IN_OUT, 0.7f);
		}
	}

	private void navigateToCredits() {
		shouldContinueBackgroundMusic = true;
		game.setScreen(new CreditsScreen(game, this));
	}

	/**
	 * Libera os recursos necessários para esta tela.
	 */
	@Override
	public void cleanUp() {
		background.getTexture().dispose();
		if (!shouldContinueBackgroundMusic) {
			SoundManager.getInstance().stopBackgroundMusic("menu/meowautheme.mp3");
		}
	}

}
