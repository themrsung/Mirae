package com.themrsung.mirae.task.state;

import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.state.State;

import java.time.LocalDateTime;

/**
 * Mute expiration task.
 */
public class MuteExpirationTask implements Runnable {
    @Override
    public void run() {
        State state = Mirae.getState();

        state.getAccounts().forEach(account -> {
            if (!account.isMuted() || account.getMuteExpiration() == null) return;
            if (account.getMuteExpiration().isBefore(LocalDateTime.now())) {
                account.setMuted(false, null);
            }
        });
    }
}
