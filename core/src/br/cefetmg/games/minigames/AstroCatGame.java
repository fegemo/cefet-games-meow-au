package br.cefetmg.games.minigames;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.RangedNumericValue;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.codeandweb.physicseditor.PhysicsShapeCache;

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
public class AstroCatGame extends MiniGame {

	private static final int NUM_ASTEROIDS = 6;
	private static final float GAME_DURATION = 15.0f;

	private static final float STEP_TIME = 1f / 60f;
	private static final int VELOCITY_ITERATIONS = 6;
	private static final int POSITION_ITERATIONS = 2;
	private static final float DELTA_ASTEROID_START = 50.0f;

	private static final float SCALE = 0.4f;

	private float accumulator = 0.0f;

	private PhysicsShapeCache physicsBodies;

	private ParticleEffect particleEffect;
	private Texture[] asteroidTextures;
	private Texture astroCatTexture, planetTexture, backgroundTexture;
	private Sprite background;
	private MySound gasnoise, impact;
	private MyMusic backgroundMusic;

	private World world;

	private double maxAsteroids;
	private double asteroidSpeed;

	private AstroCat astroCat;
	private Planet planet;
	private Asteroid[] asteroids;

	private Set<Asteroid> asteroidSet;

	private ContactListener asteroidListener;

	public AstroCatGame(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
		super(screen, observer, difficulty, GAME_DURATION, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
	}

	private class AsteroidContactListener implements ContactListener {

		@Override
		public void beginContact(Contact contact) {
			impact.play();
		}

		@Override
		public void endContact(Contact contact) {

		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {

		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {

		}

	}

	private static interface AstroCatBody {

		public void updatePosition();

		public void draw(Batch batch);

		public Sprite getSprite();

		public Body getBody();

	}

	private static class Asteroid implements AstroCatBody {

		private final Sprite sprite;
		private final Body body;
		private final int index;

		public Asteroid(int i, PhysicsShapeCache physCache, World world, Texture[] asteroidTextures, int asteroidNum) {
			index = i;
			sprite = new Sprite(asteroidTextures[asteroidNum - 1]);
			sprite.setSize(sprite.getWidth() * SCALE, sprite.getHeight() * SCALE);
			body = physCache.createBody("asteroid" + asteroidNum, world, SCALE, SCALE);
			deactivatePhysics();
		}

		@Override
		public void updatePosition() {
			sprite.setPosition(body.getPosition().x, body.getPosition().y);
			// sprite.setRotation((float) Math.toDegrees(body.getAngle()));
			sprite.setOrigin(0.0f, 0.0f);
		}

		@Override
		public void draw(Batch batch) {
			sprite.draw(batch);
		}

		public void setNewPositionAndSpeed(Vector2 position, Vector2 speed, float omega) {
			body.setTransform(position, speed.angleRad());
			body.setLinearVelocity(speed.scl(20.0f));
			body.setAngularVelocity(omega);
			updatePosition();
		}

		public void getNewRandomPositionAndSpeed(Viewport viewport, AstroCat player, float speed) {
			ThreadLocalRandom rand = ThreadLocalRandom.current();
			Vector2 newPosition;
			if (rand.nextBoolean()) {
				// O asteróide sairá de uma das laterais da tela
				float x = rand.nextBoolean() ? -DELTA_ASTEROID_START : viewport.getWorldWidth() + DELTA_ASTEROID_START;
				float y = rand.nextFloat() * viewport.getWorldHeight();
				newPosition = new Vector2(x, y);
			} else {
				// O asteróide sairá de cima ou baixo da tela
				float x = rand.nextFloat() * viewport.getWorldWidth();
				float y = rand.nextBoolean() ? -DELTA_ASTEROID_START : viewport.getWorldHeight() + DELTA_ASTEROID_START;
				newPosition = new Vector2(x, y);
			}
			Vector2 newSpeed = newPosition.cpy().sub(player.getBody().getPosition());
			float newSpeedNorm = speed + (rand.nextBoolean() ? -1.0f : 1.0f) * rand.nextFloat() * 0.2f * speed;
			float oldAngleRad = body.getAngle();
			float newAngleRad = oldAngleRad + (rand.nextBoolean() ? -1.0f : 1.0f) * 0.2f * oldAngleRad;
			setNewPositionAndSpeed(newPosition, newSpeed.setAngleRad(newAngleRad).nor().scl(newSpeedNorm),
					(rand.nextBoolean() ? -1.0f : 1.0f) * rand.nextFloat() * 5.0f);
		}

		@Override
		public Sprite getSprite() {
			return sprite;
		}

		@Override
		public Body getBody() {
			return body;
		}

		@Override
		public boolean equals(Object o) {
			return o instanceof Asteroid && ((Asteroid) o).index == index;
		}

		@Override
		public int hashCode() {
			return 37 * index;
		}

		public void deactivatePhysics() {
			body.setActive(false);
		}

		public void activatePhysics() {
			body.setActive(true);
		}
	}

	private static class AstroCat implements AstroCatBody {

		private final Sprite sprite;
		private final Body body;
		private final ParticleEmitter rocket;

		public AstroCat(PhysicsShapeCache physCache, World world, Texture astroCatTexture, Vector2 position,
				ParticleEmitter rocketEmitter) {
			rocket = rocketEmitter;
			sprite = new Sprite(astroCatTexture);
			sprite.setSize(sprite.getWidth() * SCALE, sprite.getHeight() * SCALE);
			body = physCache.createBody("astrocat", world, SCALE, SCALE);
			body.setTransform(position, 0.0f);
			updatePosition();
		}

		@Override
		public void updatePosition() {
			float targetAngleRad = (float) body.getAngle();
			float targetAngleDeg = (float) Math.toDegrees(targetAngleRad);
			Vector2 emitterOffset = new Vector2(1.0f, 1.0f);
			emitterOffset.setAngleRad(targetAngleRad);
			emitterOffset.scl(sprite.getHeight() * -0.525f);

			sprite.setPosition(body.getPosition().x, body.getPosition().y);
			// sprite.setRotation(targetAngleDeg);
			sprite.setOrigin(0.0f, 0.0f);
			rocket.setPosition(body.getPosition().x, body.getPosition().y);
			setNewCenter(rocket.getAngle(), targetAngleDeg);
			setNewCenter(rocket.getYOffsetValue(), emitterOffset.y);
			setNewCenter(rocket.getXOffsetValue(), emitterOffset.x);
		}

		private void setNewCenter(ScaledNumericValue value, float center) {
			float spanHigh = (value.getHighMax() - value.getHighMin()) * 0.5f;
			float spanLow = (value.getLowMax() - value.getLowMin()) * 0.5f;
			value.setHigh(center - spanHigh, center + spanHigh);
			value.setLow(center - spanLow, center + spanLow);
		}

		private void setNewCenter(RangedNumericValue value, float center) {
			float spanLow = (value.getLowMax() - value.getLowMin()) * 0.5f;
			value.setLow(center - spanLow, center + spanLow);
		}

		@Override
		public void draw(Batch batch) {
			sprite.draw(batch);
		}

		@Override
		public Sprite getSprite() {
			return sprite;
		}

		@Override
		public Body getBody() {
			return body;
		}
	}

	private static class Planet implements AstroCatBody {

		private final Sprite sprite;
		private final Body body;

		public Planet(PhysicsShapeCache physCache, World world, Texture planetTexture, Vector2 position) {
			sprite = new Sprite(planetTexture);
			sprite.setSize(sprite.getWidth() * SCALE, sprite.getHeight() * SCALE);
			body = physCache.createBody("planet", world, SCALE, SCALE);
			body.setTransform(position, 0.0f);
			updatePosition();
		}

		@Override
		public void updatePosition() {
			sprite.setPosition(body.getPosition().x, body.getPosition().y);
			// sprite.setRotation((float) Math.toDegrees(body.getAngle()));
			sprite.setOrigin(0.0f, 0.0f);
		}

		@Override
		public void draw(Batch batch) {
			sprite.draw(batch);
		}

		@Override
		public Sprite getSprite() {
			return sprite;
		}

		@Override
		public Body getBody() {
			return body;
		}
	}

	@Override
	protected void onStart() {
		asteroidSet = new HashSet<Asteroid>();

		// Carregando efeito de partículas
		particleEffect = new ParticleEffect();
		particleEffect.load(Gdx.files.local("astrocat/rocket.p"), Gdx.files.local("astrocat"));
		particleEffect.scaleEffect(SCALE);

		// Carregando texturas
		asteroidTextures = new Texture[NUM_ASTEROIDS];
		for (int i = 0; i < NUM_ASTEROIDS; i++) {
			asteroidTextures[i] = assets.get("astrocat/asteroid" + (i + 1) + ".png", Texture.class);
		}
		astroCatTexture = assets.get("astrocat/astrocat.png", Texture.class);
		backgroundTexture = assets.get("astrocat/background.png", Texture.class);
		planetTexture = assets.get("astrocat/planet.png", Texture.class);
		gasnoise = new MySound(assets.get("astrocat/gasnoise.mp3", Sound.class));
		impact = new MySound(assets.get("astrocat/impact.ogg", Sound.class));
		backgroundMusic = new MyMusic(assets.get("astrocat/background.mp3", Music.class));
		world = new World(new Vector2(0.0f, 0.0f), true);
		physicsBodies = new PhysicsShapeCache(Gdx.files.local("astrocat/physics.xml"));

		// Instanciando Sprites
		float verticalMiddle = viewport.getWorldHeight() * 0.5f;
		astroCat = new AstroCat(physicsBodies, world, astroCatTexture, new Vector2(10.0f, verticalMiddle),
				particleEffect.getEmitters().get(0));
		planet = new Planet(physicsBodies, world, planetTexture, new Vector2(viewport.getWorldWidth(), verticalMiddle));

		// Instanciando asteróides
		int asteroidInstances = (int) (Math.ceil(maxAsteroids * 2.0f));

		ThreadLocalRandom rand = ThreadLocalRandom.current();
		asteroids = new Asteroid[asteroidInstances];
		for (int i = 0; i < asteroidInstances; i++) {
			Asteroid newAsteroid = new Asteroid(i, physicsBodies, world, asteroidTextures,
					rand.nextInt(NUM_ASTEROIDS) + 1);
			newAsteroid.setNewPositionAndSpeed(new Vector2(-30.0f, -30.0f), new Vector2(0.0f, 0.0f), 0.0f);
			newAsteroid.deactivatePhysics();
			asteroids[i] = newAsteroid;
		}

		// Inicializando o plano de fundo
		background = new Sprite(backgroundTexture);
		background.setOrigin(0.0f, 0.0f);
		background.setScale(viewport.getWorldWidth() / background.getWidth(),
				viewport.getWorldHeight() / background.getHeight());
		background.setPosition(0.0f, 0.0f);

		// Inserindo listener de detecção de colisão com som
		asteroidListener = new AsteroidContactListener();
		world.setContactListener(asteroidListener);
	}

	private void fillAsteroidSet() {
		while (asteroidSet.size() <= maxAsteroids) {
			ThreadLocalRandom rand = ThreadLocalRandom.current();
			Asteroid selected;
			do {
				selected = asteroids[rand.nextInt(asteroids.length)];
			} while (!asteroidSet.add(selected));
			selected.getNewRandomPositionAndSpeed(viewport, astroCat, (float) asteroidSpeed);
			selected.activatePhysics();
		}
	}

	private void removeOutOfBoundAsteroids() {
		for (Iterator<Asteroid> i = asteroidSet.iterator(); i.hasNext();) {
			Asteroid asteroid = i.next();
			Vector2 pos = asteroid.getBody().getPosition();
			if (pos.x <= -DELTA_ASTEROID_START || pos.y <= -DELTA_ASTEROID_START
					|| pos.x >= viewport.getWorldWidth() + DELTA_ASTEROID_START
					|| pos.y >= viewport.getWorldHeight() + DELTA_ASTEROID_START) {
				asteroid.deactivatePhysics();
				asteroid.setNewPositionAndSpeed(
						new Vector2(-viewport.getWorldWidth() * 2.0f, -viewport.getWorldHeight() * 2.0f),
						new Vector2(0.0f, 0.0f), 0.0f);
				i.remove();
			}
		}
	}

	private void stepWorld(float dt) {
		accumulator += Math.min(dt, 0.25f);
		if (accumulator >= STEP_TIME) {
			accumulator -= STEP_TIME;
			world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
		}
	}

	@Override
	protected void configureDifficultyParameters(float difficulty) {
		maxAsteroids = Math.ceil(DifficultyCurve.S.getCurveValueBetween(difficulty, 10.0f, 60.0f));
		asteroidSpeed = DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 50.0f, 120.0f);
	}

	@Override
	public void onHandlePlayingInput() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdate(float dt) {
		removeOutOfBoundAsteroids();
		fillAsteroidSet();
		stepWorld(dt);
		astroCat.updatePosition();
		for (Asteroid asteroid : asteroidSet) {
			asteroid.updatePosition();
		}
		planet.updatePosition();
	}

	@Override
	public void onDrawGame() {
		background.draw(batch);
		astroCat.draw(batch);
		for (Asteroid asteroid : asteroids) {
			asteroid.draw(batch);
		}
		planet.draw(batch);
		particleEffect.start();
	}

	@Override
	public String getInstructions() {
		return "Guie o gato no espaço até o planeta!";
	}

	@Override
	public boolean shouldHideMousePointer() {
		return false;
	}

}
