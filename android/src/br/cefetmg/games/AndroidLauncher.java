package br.cefetmg.games;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		Config.setPreferredSoundSamplingRate(Integer.parseInt(
                ((AudioManager)getSystemService(Context.AUDIO_SERVICE))
                        .getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE)));

		initialize(new MeowAuGame(new AndroidLeaderboard()), config);
	}
}
