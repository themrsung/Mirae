package com.themrsung.mirae.gui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Graphical User Interface.
 */
public interface GUI {
    /**
     * Returns the player who is using this GUI.
     *
     * @return The player
     */
    @NotNull Player getPlayer();

    /**
     * Opens the GUI to the player.
     */
    void openGUI();
}
