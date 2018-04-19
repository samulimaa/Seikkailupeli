package com.seikkailupeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;


public class SoundManager {

    public static Music music;
    public static Music walkSound;

    public static void create(){

        music = Gdx.audio.newMusic(Gdx.files.internal("ArtOfSilence.mp3"));
        walkSound = Gdx.audio.newMusic(Gdx.files.internal("walkSound.mp3"));

    }

    public static void dispose(){
        music.dispose();
    }
}
