package com.themrsung.mirae.command;

import com.themrsung.mirae.command.admin.*;
import com.themrsung.mirae.command.debug.TestCommand;
import com.themrsung.mirae.command.economy.BalanceCommand;
import com.themrsung.mirae.command.economy.CoinCommand;
import com.themrsung.mirae.command.economy.PayCommand;
import com.themrsung.mirae.command.home.*;
import com.themrsung.mirae.command.misc.FlexCommand;
import com.themrsung.mirae.command.misc.HeightCommand;
import com.themrsung.mirae.command.misc.HelmetCommand;
import com.themrsung.mirae.command.misc.ScoreboardToggleCommand;
import com.themrsung.mirae.command.social.*;
import com.themrsung.mirae.command.stats.MoneySupplyCommand;
import com.themrsung.mirae.command.teleport.*;
import com.themrsung.mirae.command.warp.WarpCommand;
import com.themrsung.mirae.command.warp.WarpsCommand;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Commands.
 */
public final class Commands {
    public static final @NotNull SpawnCommand SPAWN_COMMAND = new SpawnCommand();
    public static final @NotNull BackCommand BACK_COMMAND = new BackCommand();
    public static final @NotNull SetSpawnCommand SET_SPAWN_COMMAND = new SetSpawnCommand();
    public static final @NotNull HomeCommand HOME_COMMAND = new HomeCommand();
    public static final @NotNull SetHomeCommand SET_HOME_COMMAND = new SetHomeCommand();
    public static final @NotNull DeleteHomeCommand DELETE_HOME_COMMAND = new DeleteHomeCommand();
    public static final @NotNull HomesCommand HOMES_COMMAND = new HomesCommand();

    public static final @NotNull BalanceCommand BALANCE_COMMAND = new BalanceCommand();
    public static final @NotNull CoinCommand COIN_COMMAND = new CoinCommand();
    public static final @NotNull PayCommand PAY_COMMAND = new PayCommand();

    public static final @NotNull ChangeMoneyCommand CHANGE_MONEY_COMMAND = new ChangeMoneyCommand();
    public static final @NotNull ChangeCoinCommand CHANGE_COIN_COMMAND = new ChangeCoinCommand();
    public static final @NotNull SetTierCommand SET_TIER_COMMAND = new SetTierCommand();
    public static final @NotNull SetTitleCommand SET_TITLE_COMMAND = new SetTitleCommand();
    public static final @NotNull GetTitleCommand GET_TITLE_COMMAND = new GetTitleCommand();
    public static final @NotNull SetDisplayNameCommand SET_DISPLAY_NAME_COMMAND = new SetDisplayNameCommand();
    public static final @NotNull MuteCommand MUTE_COMMAND = new MuteCommand();
    public static final @NotNull SetWarpCommand SET_WARP_COMMAND = new SetWarpCommand();
    public static final @NotNull DeleteWarpCommand DELETE_WARP_COMMAND = new DeleteWarpCommand();

    public static final @NotNull DirectMessageCommand DIRECT_MESSAGE_COMMAND = new DirectMessageCommand();
    public static final @NotNull ReplyCommand REPLY_COMMAND = new ReplyCommand();
    public static final @NotNull IgnoreCommand IGNORE_COMMAND = new IgnoreCommand();
    public static final @NotNull NicknameCommand NICKNAME_COMMAND = new NicknameCommand();
    public static final @NotNull TitleCommand TITLE_COMMAND = new TitleCommand();
    public static final @NotNull LocalChatCommand LOCAL_CHAT_COMMAND = new LocalChatCommand();

    public static final @NotNull TeleportAskCommand TELEPORT_ASK_COMMAND = new TeleportAskCommand();
    public static final @NotNull TeleportAskHereCommand TELEPORT_ASK_HERE_COMMAND = new TeleportAskHereCommand();
    public static final @NotNull TeleportAcceptCommand TELEPORT_ACCEPT_COMMAND = new TeleportAcceptCommand();
    public static final @NotNull TeleportDenyCommand TELEPORT_DENY_COMMAND = new TeleportDenyCommand();
    public static final @NotNull TeleportCancelCommand TELEPORT_CANCEL_COMMAND = new TeleportCancelCommand();

    public static final @NotNull WarpCommand WARP_COMMAND = new WarpCommand();
    public static final @NotNull WarpsCommand WARPS_COMMAND = new WarpsCommand();

    public static final @NotNull FlexCommand FLEX_COMMAND = new FlexCommand();
    public static final @NotNull ScoreboardToggleCommand SCOREBOARD_TOGGLE_COMMAND = new ScoreboardToggleCommand();
    public static final @NotNull HeightCommand HEIGHT_COMMAND = new HeightCommand();
    public static final @NotNull HelmetCommand HELMET_COMMAND = new HelmetCommand();

    public static final @NotNull MoneySupplyCommand MONEY_SUPPLY_COMMAND = new MoneySupplyCommand();

    public static final @NotNull TestCommand TEST_COMMAND = new TestCommand();

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
            SET_WARP_COMMAND,
            DELETE_WARP_COMMAND,

            DIRECT_MESSAGE_COMMAND,
            REPLY_COMMAND,
            IGNORE_COMMAND,
            NICKNAME_COMMAND,
            TITLE_COMMAND,
            LOCAL_CHAT_COMMAND,

            TELEPORT_ASK_COMMAND,
            TELEPORT_ASK_HERE_COMMAND,
            TELEPORT_ACCEPT_COMMAND,
            TELEPORT_DENY_COMMAND,
            TELEPORT_CANCEL_COMMAND,

            WARP_COMMAND,
            WARPS_COMMAND,

            FLEX_COMMAND,
            SCOREBOARD_TOGGLE_COMMAND,
            HEIGHT_COMMAND,
            HELMET_COMMAND,

            MONEY_SUPPLY_COMMAND,

            TEST_COMMAND
    );

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
