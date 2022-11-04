package com.example.engine_android;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.SoundPool;

import com.example.engine_common.interfaces.IAudio;
import com.example.engine_common.interfaces.ISound;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class AudioAndroid implements IAudio {
    private HashMap<String, SoundAndroid> sounds;
    private SoundPool soundPool;
    private AssetManager assetManager;

    //UTILIZAR MEDIAPLAYER PARA MUSICA DE FONDO, EN ESE CASO COMO CARGAR LOS SONIDOS

    public void init () {
        soundPool = new SoundPool.Builder().setMaxStreams(10).build();
    }

    //COMO SETEAR EL VOLUMEN EN LA CARGA, COMO PARAMETRO DE LOAD SOUND??
    @Override
    public String loadSound(String filePath) throws IOException {
        File soundFile = new File(filePath);
        AssetFileDescriptor assetDescriptor = assetManager.openFd(filePath);
        int soundId = soundPool.load(assetDescriptor, 0);
        sounds.put(soundFile.getName(), new SoundAndroid(soundId, 0.5f, 0, 1, 1));
        return soundFile.getName();
    }

    public void playSound(String soundName) {
        SoundAndroid s = sounds.get(soundName);
        soundPool.play(s.getSoundId(), s.getVolume(),  s.getVolume(), s.getPriority(), s.getLoop(), s.getRate());
    }
}
