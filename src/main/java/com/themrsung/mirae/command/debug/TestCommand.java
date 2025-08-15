package com.themrsung.mirae.command.debug;

import com.themrsung.mirae.command.MiraeCommand;
import com.themrsung.mirae.item.CustomItem;
import com.themrsung.mirae.item.lootbox.LootBox;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Test command.
 */
public class TestCommand extends MiraeCommand {
    public TestCommand() {
        super("test");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CANNOT_BE_USED_BY_CONSOLE);
            return false;
        }

        if (!sender.isOp()) {
            sender.sendMessage(INSUFFICIENT_PERMISSIONS);
            return false;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
//        CustomEnchantment.Value.THREE_BY_THREE_MINING.setEnchantLevel(item, 1);
//        CustomEnchantment.Value.SUPER_SHOVEL.setEnchantLevel(item, 1);
//        player.getInventory().setItemInMainHand(item);

        player.sendMessage(CustomItem.STORMBREAKER.isItem(item) ? "true" : "false");

        player.getInventory().addItem(
                LootBox.TEST_BOX.getItem(),
                LootBox.PURPLE_BOX.getItem(),
                LootBox.RED_BOX.getItem(),
                LootBox.ORANGE_BOX.getItem(),
                LootBox.GREEN_BOX.getItem(),

                CustomItem.APPLE_PIE.getItem(),
                CustomItem.ARMY_STEW.getItem(),
                CustomItem.HAM.getItem(),
                CustomItem.SALAD.getItem(),
                CustomItem.SALT_BREAD.getItem(),
                CustomItem.SAUSAGE.getItem()
        );

//        if (args.length > 0 && args[0].equalsIgnoreCase("markets")) {
//            ActivePriceMarket stable = new ActivePriceMarket("stable_test", new ItemStack(Material.BLACK_CONCRETE), MarketCategory.NONE, 1500);
//            stable.setVolatilityLevel(VolatilityLevel.VERY_STABLE);
//
//            ActivePriceMarket vol = new ActivePriceMarket("volatile_test", new ItemStack(Material.WHITE_CONCRETE), MarketCategory.NONE, 1500);
//            vol.setVolatilityLevel(VolatilityLevel.RARE);
//
//            Mirae.getState().addMarket(stable);
//            Mirae.getState().addMarket(vol);
//
//            FixedPriceMarket tradable = new FixedPriceMarket("tradable", new ItemStack(Material.DIRT), MarketCategory.NONE);
//            FixedPriceMarket buyOnly = new FixedPriceMarket("buy_only", new ItemStack(Material.ENDER_PEARL), MarketCategory.NONE);
//            FixedPriceMarket sellOnly = new FixedPriceMarket("sell_only", new ItemStack(Material.COBBLESTONE), MarketCategory.NONE);
//
//            Mirae.getState().addMarket(tradable);
//            Mirae.getState().addMarket(buyOnly);
//            Mirae.getState().addMarket(sellOnly);
//
//            sender.sendMessage("markets created");
//            return true;
//        }
//
//        if (args.length > 0 && args[0].equalsIgnoreCase("reset")) {
//            Mirae.getState().clearMarkets();
//            sender.sendMessage("markets reset");
//            return true;
//        }
//
//        player.sendMessage(Mirae.getState().getMarkets().size() + "개의 시장이 존재합니다.");
//
//        MarketMenu menu = new MarketMenu(player, MarketCategory.NONE);
//        menu.openGUI();
        return true;
    }
}
