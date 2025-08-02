package com.themrsung.mirae.task.state;

import com.themrsung.mirae.Mirae;

import java.io.IOException;

/**
 * Auto save task.
 */
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
