package br.cefetmg.games.minigames;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.codeandweb.physicseditor.PhysicsShapeCache;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameState;
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

	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

	private PhysicsShapeCache physCache;

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

	private Body[] walls;
	private Set<Asteroid> asteroidSet;

	private ContactListener contactListener;

	public AstroCatGame(BaseScreen screen, MiniGameStateObserver observer, float difficulty) {
		super(screen, observer, difficulty, GAME_DURATION, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS);
	}

	private class AstroCatContactListener implements ContactListener {

		@Override
		public void beginContact(Contact contact) {
			if (((contact.getFixtureA().getFilterData().categoryBits & 1)
					| (contact.getFixtureB().getFilterData().categoryBits & 1)) != 0) {
				impact.play();
			}
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

	private static abstract class AstroCatBody {

		protected final Sprite sprite;
		protected final Body body;

		protected AstroCatBody(String bodyName, PhysicsShapeCache physCache, Texture texture, World world,
				Vector2 position) {
			body = physCache.createBody(bodyName, world, SCALE, SCALE);
			body.setTransform(position, 0.0f);
			sprite = new Sprite(texture);
			sprite.setSize(sprite.getWidth() * SCALE, sprite.getHeight() * SCALE);
			sprite.setOriginCenter();
			updatePosition();
		}

		public void updatePosition() {
			Vector2 bodyCenter = body.getPosition();
			sprite.setCenter(bodyCenter.x, bodyCenter.y);
			sprite.setRotation((float) Math.toDegrees(body.getAngle()));
		}

		public void draw(Batch batch) {
			sprite.draw(batch);
		}

		public void deactivate() {
			body.setActive(false);
			sprite.setAlpha(0.0f);
		}

		public void activate() {
			body.setActive(true);
			sprite.setAlpha(1.0f);
		}

	}

	private static class Asteroid extends AstroCatBody {

		private final int index;

		public Asteroid(int i, PhysicsShapeCache physCache, World world, Texture[] asteroidTextures, int asteroidNum) {
			super("asteroid" + asteroidNum, physCache, asteroidTextures[asteroidNum - 1], world, Vector2.Zero);
			index = i;
			deactivate();
		}

		public void setNewPositionAndSpeed(Vector2 position, Vector2 speed, float omega) {
			body.setTransform(position, speed.angleRad());
			body.setLinearVelocity(speed);
			body.setAngularVelocity(omega);
			updatePosition();
		}

		public void setNewRandomPositionAndSpeed(Viewport viewport, AstroCat player, float speed) {
			ThreadLocalRandom rand = ThreadLocalRandom.current();
			Vector2 newPosition;
			float angleMultiplier;
			if (rand.nextBoolean()) {
				// O asteróide sairá de uma das laterais da tela
				angleMultiplier = 0.1f;
				float x = (rand.nextBoolean() && rand.nextBoolean()) ? -DELTA_ASTEROID_START
						: viewport.getWorldWidth() + DELTA_ASTEROID_START;
				float y = rand.nextFloat() * (1.0f + rand.nextFloat()) * viewport.getWorldHeight();
				newPosition = new Vector2(x, y);
			} else {
				// O asteróide sairá de cima ou baixo da tela
				angleMultiplier = 0.2f;
				float x = rand.nextFloat() * (1.0f + rand.nextFloat()) * viewport.getWorldWidth();
				float y = rand.nextBoolean() ? -DELTA_ASTEROID_START : viewport.getWorldHeight() + DELTA_ASTEROID_START;
				newPosition = new Vector2(x, y);
			}
			Vector2 relativePlayerPosition = player.body.getPosition().cpy().sub(newPosition);
			float newSpeedNorm = getRandomWithinRange(rand, speed, 0.2f);
			float newAngleRad = getRandomWithinRange(rand, relativePlayerPosition.angleRad(), angleMultiplier);
			float newOmega = getRandomWithinRange(rand, 2.0f, 1.5f);
			setNewPositionAndSpeed(newPosition, new Vector2(newSpeedNorm, 0.0f).rotateRad(newAngleRad), newOmega);
		}

		@Override
		public boolean equals(Object o) {
			return o instanceof Asteroid && ((Asteroid) o).index == index;
		}

		@Override
		public int hashCode() {
			return 37 * index;
		}

	}

	private static class AstroCat extends AstroCatBody {

		private final ParticleEmitter rocket;

		public AstroCat(PhysicsShapeCache physCache, World world, Texture astroCatTexture, Vector2 position,
				ParticleEmitter rocketEmitter) {
			super("astrocat", physCache, astroCatTexture, world, position);
			rocket = rocketEmitter;
		}

		// @Override
		// public void updatePosition() {
		// float targetAngleRad = (float) body.getAngle();
		// float targetAngleDeg = (float) Math.toDegrees(targetAngleRad);
		// Vector2 emitterOffset = new Vector2(1.0f, 1.0f);
		// emitterOffset.setAngleRad(targetAngleRad);
		//
		// Vector2 position = body.getPosition();
		// sprite.setRotation(targetAngleDeg);
		// sprite.setCenter(position.x, position.y);
		//
		// rocket.setPosition(body.getPosition().x, body.getPosition().y);
		// setNewCenter(rocket.getAngle(), targetAngleDeg);
		// setNewCenter(rocket.getYOffsetValue(), emitterOffset.y);
		// setNewCenter(rocket.getXOffsetValue(), emitterOffset.x);
		// }
		//
		// private void setNewCenter(ScaledNumericValue value, float center) {
		// float spanHigh = (value.getHighMax() - value.getHighMin()) * 0.5f;
		// float spanLow = (value.getLowMax() - value.getLowMin()) * 0.5f;
		// value.setHigh(center - spanHigh, center + spanHigh);
		// value.setLow(center - spanLow, center + spanLow);
		// }
		//
		// private void setNewCenter(RangedNumericValue value, float center) {
		// float spanLow = (value.getLowMax() - value.getLowMin()) * 0.5f;
		// value.setLow(center - spanLow, center + spanLow);
		// }
		//
		// @Override
		// public void draw(Batch batch) {
		// sprite.draw(batch);
		// }

	}

	private static class Planet extends AstroCatBody {

		public Planet(PhysicsShapeCache physCache, World world, Texture planetTexture, Vector2 position) {
			super("planet", physCache, planetTexture, world, position);
		}

	}

	private static float getRandomWithinRange(ThreadLocalRandom rand, float value, float range) {
		return value + (rand.nextBoolean() ? -1.0f : 1.0f) * rand.nextFloat() * range * value;
	}

	@Override
	protected void onStart() {
		walls = new Body[4];
		asteroidSet = new HashSet<Asteroid>();
		ThreadLocalRandom rand = ThreadLocalRandom.current();

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
		physCache = new PhysicsShapeCache(Gdx.files.local("astrocat/physics.xml"));

		// Instanciando Sprites
		float verticalMiddle = viewport.getWorldHeight() / 2;
		astroCat = new AstroCat(physCache, world, astroCatTexture,
				new Vector2(viewport.getWorldWidth() * 0.1f, getRandomWithinRange(rand, verticalMiddle, 0.7f)),
				particleEffect.getEmitters().get(0));
		planet = new Planet(physCache, world, planetTexture,
				new Vector2(viewport.getWorldWidth() * 0.95f, getRandomWithinRange(rand, verticalMiddle, 0.8f)));

		// Instanciando asteróides
		int asteroidInstances = (int) (Math.ceil(maxAsteroids * 2.0f));

		asteroids = new Asteroid[asteroidInstances];
		for (int i = 0; i < asteroidInstances; i++) {
			Asteroid newAsteroid = new Asteroid(i, physCache, world, asteroidTextures, rand.nextInt(NUM_ASTEROIDS) + 1);
			newAsteroid.setNewPositionAndSpeed(Vector2.Zero, Vector2.Zero, 0.0f);
			asteroids[i] = newAsteroid;
		}

		// Inicializando o plano de fundo
		background = new Sprite(backgroundTexture);
		background.setOrigin(0.0f, 0.0f);
		background.setScale(viewport.getWorldWidth() / background.getWidth(),
				viewport.getWorldHeight() / background.getHeight());
		background.setPosition(0.0f, 0.0f);

		// Inserindo listener de detecção de colisão com som
		contactListener = new AstroCatContactListener();
		world.setContactListener(contactListener);

		createWalls();
	}

	private void createWalls() {
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDefHorizontal = new FixtureDef();
		FixtureDef fixtureDefVertical = new FixtureDef();
		PolygonShape shapeHorizontal = new PolygonShape();
		PolygonShape shapeVertical = new PolygonShape();

		bodyDef.type = BodyDef.BodyType.StaticBody;
		fixtureDefHorizontal.filter.maskBits = 2;
		fixtureDefHorizontal.filter.categoryBits = 8;
		fixtureDefVertical.filter.maskBits = 2;
		fixtureDefVertical.filter.categoryBits = 8;
		shapeHorizontal.setAsBox(viewport.getWorldWidth(), 1);
		shapeVertical.setAsBox(1, viewport.getWorldHeight());
		fixtureDefHorizontal.shape = shapeHorizontal;
		fixtureDefVertical.shape = shapeVertical;

		walls[0] = world.createBody(bodyDef);
		walls[0].createFixture(fixtureDefHorizontal);
		walls[0].setTransform(0.0f, 0.0f, 0.0f);

		walls[1] = world.createBody(bodyDef);
		walls[1].createFixture(fixtureDefVertical);
		walls[1].setTransform(0.0f, 0.0f, 0.0f);

		walls[2] = world.createBody(bodyDef);
		walls[2].createFixture(fixtureDefHorizontal);
		walls[2].setTransform(0.0f, viewport.getWorldHeight(), 0.0f);

		walls[3] = world.createBody(bodyDef);
		walls[3].createFixture(fixtureDefVertical);
		walls[3].setTransform(viewport.getWorldWidth(), 0.0f, 0.0f);

		shapeHorizontal.dispose();
		shapeVertical.dispose();
	}

	private void fillAsteroidSet() {
		while (asteroidSet.size() <= maxAsteroids) {
			ThreadLocalRandom rand = ThreadLocalRandom.current();
			Asteroid selected;
			do {
				selected = asteroids[rand.nextInt(asteroids.length)];
			} while (!asteroidSet.add(selected));
			selected.setNewRandomPositionAndSpeed(viewport, astroCat, (float) asteroidSpeed);
			selected.activate();
		}
	}

	private void removeOutOfBoundAsteroids() {
		for (Iterator<Asteroid> i = asteroidSet.iterator(); i.hasNext();) {
			Asteroid asteroid = i.next();
			Vector2 pos = asteroid.body.getPosition();
			if (pos.x <= -DELTA_ASTEROID_START || pos.y <= -DELTA_ASTEROID_START
					|| pos.x >= viewport.getWorldWidth() + DELTA_ASTEROID_START
					|| pos.y >= viewport.getWorldHeight() + DELTA_ASTEROID_START) {
				asteroid.deactivate();
				asteroid.setNewPositionAndSpeed(Vector2.Zero, Vector2.Zero, 0.0f);
				i.remove();
			}
		}
	}

	private void stepWorld(float dt) {
		accumulator += Math.min(dt, 0.25f);
		if (accumulator >= STEP_TIME) {
			accumulator -= STEP_TIME;
			world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
			updateBodies();
		}
	}

	private void updateBodies() {
		astroCat.updatePosition();
		removeOutOfBoundAsteroids();
		fillAsteroidSet();
		for (Asteroid asteroid : asteroidSet) {
			asteroid.updatePosition();
		}
		planet.updatePosition();
	}

	@Override
	protected void configureDifficultyParameters(float difficulty) {
		maxAsteroids = Math.ceil(DifficultyCurve.S.getCurveValueBetween(difficulty, 10.0f, 60.0f));
		asteroidSpeed = DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 400.0f, 1200.0f);
	}

	@Override
	public void onHandlePlayingInput() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdate(float dt) {
		stepWorld(dt);
	}

	@Override
	public void onDrawGame() {
		background.draw(batch);
		astroCat.draw(batch);
		planet.draw(batch);
		for (Asteroid asteroid : asteroids) {
			asteroid.draw(batch);
		}
		// particleEffect.start();
		backgroundMusic.setVolume(0.6f);
		if (isPaused()) {
			backgroundMusic.pause();
		} else if (MiniGameState.PLAYING.equals(getState())) {
			backgroundMusic.play();
		} else {
			backgroundMusic.stop();
		}
		debugRenderer.render(world, viewport.getCamera().combined);
	}

	@Override
	public String getInstructions() {
		return "Guie o gato no espaço até o planeta!";
	}

	@Override
	public boolean shouldHideMousePointer() {
		return false;
	}

	@Override
	protected void onEnd() {
		particleEffect.dispose();
		backgroundMusic.stop();
		backgroundMusic.dispose();
		world.destroyBody(astroCat.body);
		world.destroyBody(planet.body);
		for (Asteroid asteroid : asteroids) {
			world.destroyBody(asteroid.body);
		}
		for (Body wall : walls) {
			world.destroyBody(wall);
		}
	}

}
