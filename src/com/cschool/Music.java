package com.cschool;


import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;

public class Music {
    private Clip clip;
    public Music(String s){
        try{
            AudioInputStream ais =
                    AudioSystem.getAudioInputStream(new File("src/resources/pacman_beginning.wav"));
            AudioFormat baseFormat = ais.getFormat();
            AudioFormat decodeFormat;
            decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels()*2, baseFormat.getSampleRate(), false);
            AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais );
        clip = AudioSystem.getClip();
        clip.open(dais);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void play(){
        if(clip== null) return;
        stop();
        clip.setFramePosition(0);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
     clip.stop();
    }
    public void close(){
        stop();
        clip.close();
    }
}

class GameOverMusic {
    private Clip clip;
    public GameOverMusic(String s){
        try{
            AudioInputStream ais =
                    AudioSystem.getAudioInputStream(new File("src/resources/pacman_death.wav"));
            AudioFormat baseFormat = ais.getFormat();
            AudioFormat decodeFormat;
            decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels()*2, baseFormat.getSampleRate(), false);
            AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais );
            clip = AudioSystem.getClip();
            clip.open(dais);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void play(){
        if(clip== null) return;
        stop();
        clip.setFramePosition(0);
        clip.start();
    }
    public void stop(){
        clip.stop();
    }
    public void close(){
        stop();
        clip.close();
    }
}