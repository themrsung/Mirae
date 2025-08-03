package com.themrsung.mirae.gui.convenience;

import com.themrsung.mirae.gui.GUI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Virtual workbench.
 */
public class VirtualWorkbench implements GUI {
    /**
     * Creates a new workbench.
     *
     * @param player The player
     */
    public VirtualWorkbench(@NotNull Player player) {
        this.player = player;
    }

    private final @NotNull Player player;

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    @Override
    public void openGUI() {
        player.openWorkbench(null, true);
    }
}
