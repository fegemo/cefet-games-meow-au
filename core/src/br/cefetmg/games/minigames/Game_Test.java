package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.MiniGameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;

public class Game_Test extends MiniGame {

	public Game_Test(BaseScreen screen, MiniGameStateObserver observer, float difficulty, float maxDuration,
			TimeoutBehavior endOfGameSituation) {
		super(screen, observer, difficulty, maxDuration, endOfGameSituation);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void configureDifficultyParameters(float difficulty) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean shouldHideMousePointer() {
		// TODO Auto-generated method stub
		return false;
	}

}
