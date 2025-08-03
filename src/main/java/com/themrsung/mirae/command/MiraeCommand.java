package com.themrsung.mirae.command;

import com.themrsung.mirae.MX;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Abstract superclass for commands.
 */
public abstract class MiraeCommand extends Command {
    public static final @NotNull Component INSUFFICIENT_PERMISSIONS = Component.text("권한이 부족합니다.").style(MX.STYLE_ERROR);
    public static final @NotNull Component INTERNAL_ERROR = Component.text("오류가 발생했습니다. 관리자에게 문의하세요.").style(MX.STYLE_ERROR);
    public static final @NotNull Component INSUFFICIENT_FUNDS = Component.text("잔액이 부족합니다.").style(MX.STYLE_ERROR);

    public static final @NotNull Component CANNOT_BE_USED_BY_CONSOLE = Component.text("콘솔에서 사용할 수 없는 명령어입니다.").style(MX.STYLE_WARNING);
    public static final @NotNull Component CANNOT_FIND_ACCOUNT = Component.text("계정을 찾을 수 없습니다.").style(MX.STYLE_WARNING);
    public static final @NotNull Component COUNTERPARTY_IS_IGNORING_YOU = Component.text("상대방이 본인을 차단하고 있습니다.").style(MX.STYLE_WARNING);
    public static final @NotNull Component COUNTERPARTY_IS_OFFLINE = Component.text("상대방이 오프라인 상태입니다.").style(MX.STYLE_WARNING);
    public static final @NotNull Component YOU_ARE_IGNORING_COUNTERPARTY = Component.text("본인이 상대방을 차단하고 있습니다.").style(MX.STYLE_WARNING);
    public static final @NotNull Component INVALID_AMOUNT = Component.text("금액을 확인해주세요.").style(MX.STYLE_WARNING);
    public static final @NotNull Component INVALID_NUMBER = Component.text("숫자를 확인해주세요.").style(MX.STYLE_WARNING);
    public static final @NotNull Component NO_INBOUND_TELEPORT_REQUESTS = Component.text("수신한 텔레포트 요청이 없습니다.").style(MX.STYLE_WARNING);
    public static final @NotNull Component NO_INBOUND_DIRECT_MESSAGES = Component.text("수신한 메시지가 없습니다.").style(MX.STYLE_WARNING);
    public static final @NotNull Component COUNTERPARTY_HAS_DENIED_TELEPORT_REQUEST = Component.text("상대방이 텔레포트 요청을 거절했습니다.").style(MX.STYLE_WARNING);
    public static final @NotNull Component OUTBOUND_TELEPORT_REQUEST_ALREADY_EXISTS = Component.text("이미 텔레포트 요청을 보냈습니다.").style(MX.STYLE_WARNING);
    public static final @NotNull Component NO_OUTBOUND_TELEPORT_REQUESTS = Component.text("발신한 텔레포트 요청이 없습니다.").style(MX.STYLE_WARNING);
    public static final @NotNull Component INSUFFICIENT_SPACE_IN_INVENTORY = Component.text("인벤토리에 공간이 부족합니다.").style(MX.STYLE_WARNING);
    public static final @NotNull Component CANNOT_DO_THIS_TO_SELF = Component.text("본인에게 적용할 수 없습니다.").style(MX.STYLE_WARNING);
    public static final @NotNull Component CANNOT_FIND_WARP = Component.text("워프 지점을 찾을 수 없습니다.").style(MX.STYLE_WARNING);

    public static final @NotNull Component TELEPORTED_SUCCESSFULLY = Component.text("성공적으로 텔레포트하였습니다.").style(MX.STYLE_GOOD);
    public static final @NotNull Component TELEPORT_REQUEST_ACCEPTED = Component.text("텔레포트 요청을 수락했습니다.").style(MX.STYLE_GOOD);
    public static final @NotNull Component TELEPORT_REQUEST_DENIED = Component.text("텔레포트 요청을 거절했습니다.").style(MX.STYLE_GOOD);
    public static final @NotNull Component TELEPORT_REQUEST_CANCELLED_BY_SELF = Component.text("텔레포트 요청을 취소했습니다.").style(MX.STYLE_GOOD);
    public static final @NotNull Component TELEPORT_REQUEST_CANCELLED_BY_COUNTERPARTY = Component.text("상대방이 텔레포트 요청을 취소했습니다.").style(MX.STYLE_GOOD);
    public static final @NotNull Component HOME_SET_TO_HERE = Component.text("이곳으로 홈을 설정했습니다.").style(MX.STYLE_GOOD);
    public static final @NotNull Component HOME_DELETED = Component.text("홈이 삭제되었습니다.").style(MX.STYLE_GOOD);
    public static final @NotNull Component NICKNAME_RESET = Component.text("닉네임을 초기화했습니다.").style(MX.STYLE_GOOD);
    public static final @NotNull Component NICKNAME_SET = Component.text("닉네임이 설정되었습니다.").style(MX.STYLE_GOOD);

    /**
     * Creates a new command.
     *
     * @param name The command name
     */
    public MiraeCommand(@NotNull String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return List.of();
    }
}
