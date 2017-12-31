package br.cefetmg.games.sound;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

import br.cefetmg.games.Config;

/**
 * Created by fegemo on 30/12/17.
 */

public class SoundSamplingAwareInternalFileHandleResolver extends InternalFileHandleResolver {

    private static final String[] SOUND_EXTENSIONS = new String[] {"mp3", "wav", "ogg" };

    @Override
    public FileHandle resolve(String fileName) {

        for (String extension : SOUND_EXTENSIONS) {
            if (fileName.endsWith(extension)) {
                String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf(extension) - 1);
                fileName = String.format("%s-%s.%s", nameWithoutExtension, String.valueOf(Config.getPreferredSoundSamplingRate()), extension);
                break;
            }
        }
        return super.resolve(fileName);
    }
}
