package com.themrsung.mirae.task.state;

import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;

/**
 * Updates names
 */
public final class UpdateNameTask implements Runnable {
    @Override
    public void run() {
        Mirae.getState().getAccounts().forEach(Account::updateName);
    }
}
