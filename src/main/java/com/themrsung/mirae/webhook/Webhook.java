package com.themrsung.mirae.webhook;

import com.sun.net.httpserver.HttpServer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import com.themrsung.mirae.MX;
import com.themrsung.mirae.Mirae;
import com.themrsung.mirae.account.Account;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * Webhook.
 */
public class Webhook {
    private static final int PORT = 1234;
    private static final @NotNull String AUTH_KEY = "hi mom hello!";

    /**
     * Initialize.
     */
    public Webhook() {
        this.server = initializeHttpServer();

        server.createContext("/donation", exchange -> {
            if (!"POST".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            try {
                JsonObject json = JsonParser.parseString(body).getAsJsonObject();
                String authKey = json.get("key").getAsString();
                String username = json.get("username").getAsString();
                long amount = json.get("amount").getAsLong();

                if (!Objects.equals(authKey, AUTH_KEY)) {
                    exchange.sendResponseHeaders(403, -1);
                }

                giveCoins(username, amount);
                exchange.sendResponseHeaders(200, -1);
            } catch (Exception e) {
                exchange.sendResponseHeaders(400, -1);
            }
        });

        server.start();
    }

    private final @NotNull HttpServer server;

    protected static @NotNull HttpServer initializeHttpServer() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            return server;
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize HTTP server", e);
        }
    }

    /**
     * Gives coins to user.
     * @param username The username
     * @param amount The amount
     */
    protected void giveCoins(@Nullable String username, long amount) {
        Optional.ofNullable(username).ifPresentOrElse(name -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(name);
            Account account = Mirae.getState().getAccount(player);

            if (account == null) {
                return;
            }

            Bukkit.getLogger().info("Webhook triggered.");
//            account.modifyCoinBalance(amount, EconomyCause.NATIVE_DEPOSIT, "Automated coin delivery via Imweb.");

        }, this::notifyFailureToAdmins);
    }

    /**
     * Notifies failure to online admins.
     */
    protected void notifyFailureToAdmins() {
        Bukkit.getOnlinePlayers().stream()
                .filter(Player::isOp)
                .forEach(p -> {
                    p.sendMessage(Component.text("후원 코인을 지급하는데 문제가 발생했습니다.").style(MX.STYLE_ERROR));
                });
    }
}
