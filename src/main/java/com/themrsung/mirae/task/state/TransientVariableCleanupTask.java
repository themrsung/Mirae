package com.themrsung.mirae.task.state;

import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.listener.Listeners;
import com.themrsung.mirae.state.State;

import java.time.LocalDateTime;

/**
 * Transient variable cleanup task.
 */
public class TransientVariableCleanupTask implements Runnable {
    @Override
    public void run() {
        State state = Mirae.getState();

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime teleportRequestCutoff = now.minusMinutes(1);
        state.clearTeleportRequests(teleportRequestCutoff);

        LocalDateTime directMessageCutoff = now.minusHours(1);
        state.clearDirectMessages(directMessageCutoff);

        int mailListCutoff = 15;
        state.getAccounts().forEach(a -> {
            a.clearMailList(mailListCutoff);
        });

        Listeners.ITEM_LISTENER.clearPropulsionMap();
    }
}
