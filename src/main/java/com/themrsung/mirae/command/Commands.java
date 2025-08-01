package com.themrsung.mirae.command;

import com.themrsung.mirae.command.admin.*;
import com.themrsung.mirae.command.economy.BalanceCommand;
import com.themrsung.mirae.command.economy.CoinCommand;
import com.themrsung.mirae.command.economy.PayCommand;
import com.themrsung.mirae.command.home.*;
import com.themrsung.mirae.command.misc.FlexCommand;
import com.themrsung.mirae.command.social.DirectMessageCommand;
import com.themrsung.mirae.command.social.IgnoreCommand;
import com.themrsung.mirae.command.social.NicknameCommand;
import com.themrsung.mirae.command.social.ReplyCommand;
import com.themrsung.mirae.command.stats.MoneySupplyCommand;
import com.themrsung.mirae.command.teleport.*;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Commands.
 */
public final class Commands {
    private static final @NotNull SpawnCommand SPAWN_COMMAND = new SpawnCommand();
    private static final @NotNull BackCommand BACK_COMMAND = new BackCommand();
    private static final @NotNull SetSpawnCommand SET_SPAWN_COMMAND = new SetSpawnCommand();
    private static final @NotNull HomeCommand HOME_COMMAND = new HomeCommand();
    private static final @NotNull SetHomeCommand SET_HOME_COMMAND = new SetHomeCommand();
    private static final @NotNull DeleteHomeCommand DELETE_HOME_COMMAND = new DeleteHomeCommand();
    private static final @NotNull HomesCommand HOMES_COMMAND = new HomesCommand();

    private static final @NotNull BalanceCommand BALANCE_COMMAND = new BalanceCommand();
    private static final @NotNull CoinCommand COIN_COMMAND = new CoinCommand();
    private static final @NotNull PayCommand PAY_COMMAND = new PayCommand();

    private static final @NotNull ChangeMoneyCommand CHANGE_MONEY_COMMAND = new ChangeMoneyCommand();
    private static final @NotNull ChangeCoinCommand CHANGE_COIN_COMMAND = new ChangeCoinCommand();
    private static final @NotNull SetTierCommand SET_TIER_COMMAND = new SetTierCommand();
    private static final @NotNull SetTitleCommand SET_TITLE_COMMAND = new SetTitleCommand();
    private static final @NotNull GetTitleCommand GET_TITLE_COMMAND = new GetTitleCommand();
    private static final @NotNull SetDisplayNameCommand SET_DISPLAY_NAME_COMMAND = new SetDisplayNameCommand();
    private static final @NotNull MuteCommand MUTE_COMMAND = new MuteCommand();

    private static final @NotNull DirectMessageCommand DIRECT_MESSAGE_COMMAND = new DirectMessageCommand();
    private static final @NotNull ReplyCommand REPLY_COMMAND = new ReplyCommand();
    private static final @NotNull IgnoreCommand IGNORE_COMMAND = new IgnoreCommand();
    private static final @NotNull NicknameCommand NICKNAME_COMMAND = new NicknameCommand();

    private static final @NotNull TeleportAskCommand TELEPORT_ASK_COMMAND = new TeleportAskCommand();
    private static final @NotNull TeleportAskHereCommand TELEPORT_ASK_HERE_COMMAND = new TeleportAskHereCommand();
    private static final @NotNull TeleportAcceptCommand TELEPORT_ACCEPT_COMMAND = new TeleportAcceptCommand();
    private static final @NotNull TeleportDenyCommand TELEPORT_DENY_COMMAND = new TeleportDenyCommand();
    private static final @NotNull TeleportCancelCommand TELEPORT_CANCEL_COMMAND = new TeleportCancelCommand();

    private static final @NotNull FlexCommand FLEX_COMMAND = new FlexCommand();

    private static final @NotNull MoneySupplyCommand MONEY_SUPPLY_COMMAND = new MoneySupplyCommand();

    private static final @NotNull Set<MiraeCommand> ALL_COMMANDS = Set.of(
            SPAWN_COMMAND,
            BACK_COMMAND,
            SET_SPAWN_COMMAND,
            HOME_COMMAND,
            SET_HOME_COMMAND,
            DELETE_HOME_COMMAND,
            HOMES_COMMAND,

            BALANCE_COMMAND,
            COIN_COMMAND,
            PAY_COMMAND,

            CHANGE_MONEY_COMMAND,
            CHANGE_COIN_COMMAND,
            SET_TIER_COMMAND,
            SET_TITLE_COMMAND,
            GET_TITLE_COMMAND,
            SET_DISPLAY_NAME_COMMAND,
            MUTE_COMMAND,

            DIRECT_MESSAGE_COMMAND,
            REPLY_COMMAND,
            IGNORE_COMMAND,
            NICKNAME_COMMAND,

            TELEPORT_ASK_COMMAND,
            TELEPORT_ASK_HERE_COMMAND,
            TELEPORT_ACCEPT_COMMAND,
            TELEPORT_DENY_COMMAND,
            TELEPORT_CANCEL_COMMAND,

            FLEX_COMMAND,

            MONEY_SUPPLY_COMMAND
    );

    /**
     * Returns the spawn command.
     *
     * @return The spawn command
     */
    public static @NotNull SpawnCommand getSpawnCommand() {
        return SPAWN_COMMAND;
    }

    /**
     * Returns the back command.
     *
     * @return The back command
     */
    public static @NotNull BackCommand getBackCommand() {
        return BACK_COMMAND;
    }

    /**
     * Returns the set spawn command.
     *
     * @return The set spawn command
     */
    public static @NotNull SetSpawnCommand getSetSpawnCommand() {
        return SET_SPAWN_COMMAND;
    }

    /**
     * Returns the home command.
     *
     * @return The home command
     */
    public static @NotNull HomeCommand getHomeCommand() {
        return HOME_COMMAND;
    }

    public static @NotNull SetHomeCommand getSetHomeCommand() {
        return SET_HOME_COMMAND;
    }

    public static @NotNull DeleteHomeCommand getDeleteHomeCommand() {
        return DELETE_HOME_COMMAND;
    }

    public static @NotNull HomesCommand getHomesCommand() {
        return HOMES_COMMAND;
    }

    public static @NotNull BalanceCommand getBalanceCommand() {
        return BALANCE_COMMAND;
    }

    public static @NotNull CoinCommand getCoinCommand() {
        return COIN_COMMAND;
    }

    public static @NotNull PayCommand getPayCommand() {
        return PAY_COMMAND;
    }

    public static @NotNull ChangeMoneyCommand getChangeMoneyCommand() {
        return CHANGE_MONEY_COMMAND;
    }

    public static @NotNull ChangeCoinCommand getChangeCoinCommand() {
        return CHANGE_COIN_COMMAND;
    }

    public static @NotNull SetTierCommand getSetTierCommand() {
        return SET_TIER_COMMAND;
    }

    public static @NotNull SetTitleCommand getSetTitleCommand() {
        return SET_TITLE_COMMAND;
    }

    public static @NotNull GetTitleCommand getGetTitleCommand() {
        return GET_TITLE_COMMAND;
    }

    public static @NotNull SetDisplayNameCommand getSetDisplayNameCommand() {
        return SET_DISPLAY_NAME_COMMAND;
    }

    public static @NotNull MuteCommand getMuteCommand() {
        return MUTE_COMMAND;
    }

    public static @NotNull DirectMessageCommand getDirectMessageCommand() {
        return DIRECT_MESSAGE_COMMAND;
    }

    public static @NotNull ReplyCommand getReplyCommand() {
        return REPLY_COMMAND;
    }

    public static @NotNull IgnoreCommand getIgnoreCommand() {
        return IGNORE_COMMAND;
    }

    public static @NotNull NicknameCommand getNicknameCommand() {
        return NICKNAME_COMMAND;
    }

    public static @NotNull TeleportAskCommand getTeleportAskCommand() {
        return TELEPORT_ASK_COMMAND;
    }

    public static @NotNull TeleportAskHereCommand getTeleportAskHereCommand() {
        return TELEPORT_ASK_HERE_COMMAND;
    }

    public static @NotNull TeleportAcceptCommand getTeleportAcceptCommand() {
        return TELEPORT_ACCEPT_COMMAND;
    }

    public static @NotNull TeleportDenyCommand getTeleportDenyCommand() {
        return TELEPORT_DENY_COMMAND;
    }

    public static @NotNull TeleportCancelCommand getTeleportCancelCommand() {
        return TELEPORT_CANCEL_COMMAND;
    }

    public static @NotNull FlexCommand getFlexCommand() {
        return FLEX_COMMAND;
    }

    public static @NotNull MoneySupplyCommand getMoneySupplyCommand() {
        return MONEY_SUPPLY_COMMAND;
    }

    /**
     * Returns the set of all commands.
     *
     * @return The set of all commands
     */
    public static @NotNull Set<MiraeCommand> getCommands() {
        return ALL_COMMANDS;
    }

    /**
     * Prevents instantiation.
     *
     * @throws Exception Always
     */
    private Commands() throws Exception {
        throw new Exception("Cannot instantiate utility class.");
    }
}
