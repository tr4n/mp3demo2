package com.example.mp3demo.service;

import com.example.mp3demo.data.model.Audio;

interface AudioPlayerController {

    boolean create();

    boolean start();

    boolean pause();

    boolean stop();

    boolean play();

    Audio getCurrent();

    int getStatus();
}