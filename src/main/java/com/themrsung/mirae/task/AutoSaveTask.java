package com.themrsung.mirae.task;

import com.themrsung.mirae.Mirae;

import java.io.IOException;

public class AutoSaveTask implements Runnable {
    @Override
    public void run() {
        try {
            Mirae.getState().save();
        } catch (IOException e) {
            Mirae.getInstance().getLogger().warning("Autosave failed!");
        }
    }
}
