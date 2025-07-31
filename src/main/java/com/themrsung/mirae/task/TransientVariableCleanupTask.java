package com.themrsung.mirae.task;

import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.state.State;

import java.time.LocalDateTime;

public final class TransientVariableCleanupTask implements Runnable {
    @Override
    public void run() {
        State state = Mirae.getState();

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime teleportRequestCutoff = now.minusMinutes(1);
        state.clearTeleportRequests(teleportRequestCutoff);

        LocalDateTime directMessageCutoff = now.minusHours(1);
        state.clearDirectMessages(directMessageCutoff);
    }
}
