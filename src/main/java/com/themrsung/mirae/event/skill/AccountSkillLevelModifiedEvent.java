package com.themrsung.mirae.event.skill;

import com.themrsung.mirae.account.Account;
import com.themrsung.mirae.event.MiraeEvent;
import com.themrsung.mirae.skill.SkillType;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an account skill level is modified.
 */
public class AccountSkillLevelModifiedEvent extends MiraeEvent {
    /**
     * Creates a new skill level modified event.
     *
     * @param account The account
     * @param before  The level before
     * @param after   The level after
     */
    public AccountSkillLevelModifiedEvent(@NotNull Account account, @NotNull SkillType type, long before, long after) {
        this.account = account;
        this.skillType = type;
        this.levelBefore = before;
        this.levelAfter = after;

        this.levelChange = after - before;
    }

    protected final @NotNull Account account;
    protected final @NotNull SkillType skillType;
    protected final long levelChange;
    protected final long levelBefore;
    protected final long levelAfter;

    /**
     * Returns the account whose level was changed.
     *
     * @return The account
     */
    public @NotNull Account getAccount() {
        return account;
    }

    /**
     * Returns the skill type which was changed.
     *
     * @return The skill type which was changed
     */
    public @NotNull SkillType getSkillType() {
        return skillType;
    }

    /**
     * Returns the net change of level.
     *
     * @return The net change of level
     */
    public long getLevelChange() {
        return levelChange;
    }

    /**
     * Returns the level before.
     *
     * @return The level before
     */
    public long getLevelBefore() {
        return levelBefore;
    }

    /**
     * Returns the level after.
     *
     * @return The level after
     */
    public long getLevelAfter() {
        return levelAfter;
    }
}
