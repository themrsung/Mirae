package com.themrsung.mirae.account;

import com.themrsung.mirae.MX;
import com.themrsung.mirae.item.economy.TitleItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An account title.
 */
public enum AccountTitle {
    /**
     * Empty title.
     */
    EMPTY("empty", Component.empty()),

    /**
     * Special title for ex-Capitalism users.
     */
    CAPITALIST("capitalist", Component.text("자본주의자").style(Style.style()
            .color(TextColor.fromHexString("#4870e0"))
            .decorate(TextDecoration.BOLD)
            .hoverEvent(HoverEvent.showText(Component.text("자본주의서버 출신 플레이어입니다.").style(MX.STYLE_WARNING)))
            .build())),

    /**
     * Special title for ex-Julie users.
     */
    DOOMSDAY("doomsday", Component.text("둠스데이클럽").style(Style.style()
            .color(TextColor.fromHexString("#ffe925"))
            .decorate(TextDecoration.BOLD)
            .hoverEvent(HoverEvent.showText(Component.text("J").style(MX.STYLE_WARNING)))
            .build())),

    BETA_TESTER("beta_tester", Component.text("베타 테스터").style(Style.style()
            .color(TextColor.fromHexString("#3dff21"))
            .decorate(TextDecoration.BOLD)
            .hoverEvent(HoverEvent.showText(Component.text("베타 테스트에 하루 1시간 이상 참여하셨습니다.").style(MX.STYLE_GOOD)))
            .build())),

    /**
     * End crystal emoji. Reserved for Early Donors.
     */
    END_CRYSTAL("end_crystal", Component.text(":mc_end_crystal:")
            .hoverEvent(HoverEvent.showText(Component.text("사전예약자").style(MX.STYLE_SPECIAL)))),

    /**
     * Nether star emoji. Reserved for donors.
     */
    NETHER_STAR("nether_star", Component.text(":mc_nether_star:")
            .hoverEvent(HoverEvent.showText(Component.text("후원자").style(MX.STYLE_SPECIAL)))),

    /// Acquirable

    ACACIA_BOAT("acacia_boat", Component.text(":mc_acacia_boat:")),
    ACACIA_DOOR("acacia_door", Component.text(":mc_acacia_door:")),
    ACACIA_SIGN("acacia_sign", Component.text(":mc_acacia_sign")),
    APPLE("apple", Component.text(":mc_apple:")),
    ARMOR_STAND("armor_stand", Component.text(":mc_armor_stand:")),
    ARROW("arrow", Component.text(":mc_arrow:")),
    BAKED_POTATO("baked_potato", Component.text(":mc_baked_potato:")),
    BAMBOO("bamboo", Component.text(":mc_bamboo:")),
    BARRIER("barrier", Component.text(":mc_barrier:")),
    BEEF("beef", Component.text(":mc_beef:")),
    BEETROOT("beetroot", Component.text(":mc_beetroot:")),
    BEETROOT_SEEDS("beetroot_seeds", Component.text(":mc_beetroot_seeds:")),
    BEETROOT_SOUP("beetroot_soup", Component.text(":mc_beetroot_soup:")),
    BELL("bell", Component.text(":mc_bell:")),
    BIRCH_BOAT("birch_boat", Component.text(":mc_birch_boat:")),
    BIRCH_DOOR("birch_door", Component.text(":mc_birch_door:")),
    BIRCH_SIGN("birch_sign", Component.text(":mc_birch_sign:")),
    BLACK_DYE("black_dye", Component.text(":mc_black_dye:")),
    BLAZE_POWDER("blaze_powder", Component.text(":mc_blaze_powder:")),
    BLAZE_ROD("blaze_rod", Component.text(":mc_blaze_rod:")),
    BLUE_DYE("blue_dye", Component.text("mc_blue_dye")),
    BONE("bone", Component.text(":mc_bone:")),
    BONE_MEAL("bone_meal", Component.text("mc_bone_meal")),
    BOOK("book", Component.text("mc_book")),
    BOW("bow", Component.text(":mc_bow:")),
    BOWL("bowl", Component.text(":mc_bowl:")),
    BOW_PULLING_0("bow_pulling_0", Component.text(":mc_bow_pulling_0:")),
    BOW_PULLING_1("bow_pulling_1", Component.text(":mc_bow_pulling_1:")),
    BOW_PULLING_2("bow_pulling_2", Component.text(":mc_bow_pulling_2:")),
    BREAD("bread", Component.text(":mc_bread:")),
    BREWING_STAND("brewing_stand", Component.text(":mc_brewing_stand:")),
    BRICK("brick", Component.text(":mc_brick:")),
    BROWN_DYE("brown_dye", Component.text(":mc_brown_dye:")),
    BUCKET("bucket", Component.text(":mc_bucket:")),
    CAKE("cake", Component.text(":mc_cake:")),
    CAMPFIRE("campfire", Component.text(":mc_campfire:")),
    CARROT("carrot", Component.text(":mc_carrot:")),
    CARROT_ON_A_STICK("carrot_on_a_stick", Component.text(":mc_carrot_on_a_stick:")),
    CAULDRON("cauldron", Component.text(":mc_cauldron:")),
    CHAINMAIL_BOOTS("chainmail_boots", Component.text(":mc_chainmail_boots:")),
    CHAINMAIL_CHESTPLATE("chainmail_chestplate", Component.text(":mc_chainmail_chestplate:")),
    CHAINMAIL_HELMET("chainmail_helmet", Component.text(":mc_chainmail_helmet:")),
    CHAINMAIL_LEGGINGS("chainmail_leggings", Component.text(":mc_chainmail_leggings:")),
    CHARCOAL("charcoal", Component.text(":mc_charcoal:")),
    CHEST_MINECART("chest_minecart", Component.text(":mc_chest_minecart:")),
    CHICKEN("chicken", Component.text(":mc_chicken:")),
    CHORUS_FRUIT("chorus_fruit", Component.text(":mc_chorus_fruit:")),
    CLAY_BALL("clay_ball", Component.text(":mc_clay_ball:")),
    CLOCK("clock", Component.text(":mc_clock:")),
    COAL("coal", Component.text(":mc_coal:")),
    COCOA_BEANS("cocoa_beans", Component.text(":mc_cocoa_beans:")),
    COD("cod", Component.text(":mc_cod:")),
    COD_BUCKET("cod_bucket", Component.text(":mc_cod_bucket:")),
    COMMAND_BLOCK_MINECART("command_block_minecart", Component.text(":mc_command_block_minecart:")),
    COMPARATOR("comparator", Component.text(":mc_comparator:")),
    COMPASS("compass", Component.text(":mc_compass:")),
    COOKED_BEEF("cooked_beef", Component.text(":mc_cooked_beef:")),
    COOKED_CHICKEN("cooked_chicken", Component.text(":mc_cooked_chicken:")),
    COOKED_COD("cooked_cod", Component.text(":mc_cooked_cod:")),
    COOKED_MUTTON("cooked_mutton", Component.text(":mc_cooked_mutton:")),
    COOKED_PORKCHOP("cooked_porkchop", Component.text(":mc_cooked_porkchop:")),
    COOKED_RABBIT("cooked_rabbit", Component.text(":mc_cooked_rabbit:")),
    COOKED_SALMON("cooked_salmon", Component.text(":mc_cooked_salmon:")),
    COOKIE("cookie", Component.text(":mc_cookie:")),
    CREEPER_BANNER_PATTERN("creeper_banner_pattern", Component.text(":mc_creeper_banner_pattern:")),
    CROSSBOW_ARROW("crossbow_arrow", Component.text(":mc_crossbow_arrow:")),
    CROSSBOW_FIREWORK("crossbow_firework", Component.text(":mc_crossbow_firework:")),
    CROSSBOW_PULLING_0("crossbow_pulling_0", Component.text(":mc_crossbow_pulling_0:")),
    CROSSBOW_PULLING_1("crossbow_pulling_1", Component.text(":mc_crossbow_pulling_1:")),
    CROSSBOW_PULLING_2("crossbow_pulling_2", Component.text(":mc_crossbow_pulling_2:")),
    CROSSBOW_STANDBY("crossbow_standby", Component.text(":mc_crossbow_standby:")),
    CYAN_DYE("cyan_dye", Component.text(":mc_cyan_dye:")),
    DARK_OAK_BOAT("dark_oak_boat", Component.text(":mc_dark_oak_boat:")),
    DARK_OAK_DOOR("dark_oak_door", Component.text(":mc_dark_oak_door:")),
    DARK_OAK_SIGN("dark_oak_sign", Component.text(":mc_dark_oak_sign:")),
    DIAMOND("diamond", Component.text(":mc_diamond:")),
    DIAMOND_AXE("diamond_axe", Component.text(":mc_diamond_axe:")),
    DIAMOND_BOOTS("diamond_boots", Component.text(":mc_diamond_boots:")),
    DIAMOND_CHESTPLATE("diamond_chestplate", Component.text(":mc_diamond_chestplate:")),
    DIAMOND_HELMET("diamond_helmet", Component.text(":mc_diamond_helmet:")),
    DIAMOND_HOE("diamond_hoe", Component.text(":mc_diamond_hoe:")),
    DIAMOND_HORSE_ARMOR("diamond_horse_armor", Component.text(":mc_diamond_horse_armor:")),
    DIAMOND_LEGGINGS("diamond_leggings", Component.text(":mc_diamond_leggings:")),
    DIAMOND_PICKAXE("diamond_pickaxe", Component.text(":mc_diamond_pickaxe:")),
    DIAMOND_SHOVEL("diamond_shovel", Component.text(":mc_diamond_shovel:")),
    DIAMOND_SWORD("diamond_sword", Component.text(":mc_diamond_sword:")),
    DRAGON_BREATH("dragon_breath", Component.text(":mc_dragon_breath:")),
    DRIED_KELP("dried_kelp", Component.text(":mc_dried_kelp:")),
    EGG("egg", Component.text(":mc_egg:")),
    ELYTRA("elytra", Component.text(":mc_elytra:")),
    EMERALD("emerald", Component.text(":mc_emerald:")),
    ENCHANTED_BOOK("enchanted_book", Component.text(":mc_enchanted_book:")),
    ENDER_EYE("ender_eye", Component.text(":mc_ender_eye:")),
    ENDER_PEARL("ender_pearl", Component.text(":mc_ender_pearl:")),
    EXPERIENCE_BOTTLE("experience_bottle", Component.text(":mc_experience_bottle:")),
    FEATHER("feather", Component.text(":mc_feather:")),
    FERMENTED_SPIDER_EYE("fermented_spider_eye", Component.text(":mc_fermented_spider_eye:")),
    FILLED_MAP("filled_map", Component.text(":mc_filled_map:")),
    FILLED_MAP_MARKINGS("filled_map_markings", Component.text(":mc_filled_map_markings:")),
    FIREWORK_ROCKET("firework_rocket", Component.text(":mc_firework_rocket:")),
    FIREWORK_STAR("firework_star", Component.text(":mc_firework_star:")),
    FIREWORK_STAR_OVERLAY("firework_star_overlay", Component.text(":mc_firework_star_overlay:")),
    FIRE_CHARGE("fire_charge", Component.text(":mc_fire_charge:")),
    FISHING_ROD("fishing_rod", Component.text(":mc_fishing_rod:")),
    FISHING_ROD_CAST("fishing_rod_cast", Component.text(":mc_fishing_rod_cast:")),
    FLINT("flint", Component.text(":mc_flint:")),
    FLINT_AND_STEEL("flint_and_steel", Component.text(":mc_flint_and_steel:")),
    FLOWER_BANNER_PATTERN("flower_banner_pattern", Component.text(":mc_flower_banner_pattern:")),
    FLOWER_POT("flower_pot", Component.text(":mc_flower_pot:")),
    FURNACE_MINECART("furnace_minecart", Component.text(":mc_furnace_minecart:")),
    GHAST_TEAR("ghast_tear", Component.text(":mc_ghast_tear:")),
    GLASS_BOTTLE("glass_bottle", Component.text(":mc_glass_bottle:")),
    GLISTERING_MELON_SLICE("glistering_melon_slice", Component.text(":mc_glistering_melon_slice:")),
    GLOBE_BANNER_PATTERN("globe_banner_pattern", Component.text(":mc_globe_banner_pattern:")),
    GLOWSTONE_DUST("glowstone_dust", Component.text(":mc_glowstone_dust:")),
    GOLDEN_APPLE("golden_apple", Component.text(":mc_golden_apple:")),
    GOLDEN_AXE("golden_axe", Component.text(":mc_golden_axe:")),
    GOLDEN_BOOTS("golden_boots", Component.text(":mc_golden_boots:")),
    GOLDEN_CARROT("golden_carrot", Component.text(":mc_golden_carrot:")),
    GOLDEN_CHESTPLATE("golden_chestplate", Component.text(":mc_golden_chestplate:")),
    GOLDEN_HELMET("golden_helmet", Component.text(":mc_golden_helmet:")),
    GOLDEN_HOE("golden_hoe", Component.text(":mc_golden_hoe:")),
    GOLDEN_HORSE_ARMOR("golden_horse_armor", Component.text(":mc_golden_horse_armor:")),
    GOLDEN_LEGGINGS("golden_leggings", Component.text(":mc_golden_leggings:")),
    GOLDEN_PICKAXE("golden_pickaxe", Component.text(":mc_golden_pickaxe:")),
    GOLDEN_SHOVEL("golden_shovel", Component.text(":mc_golden_shovel:")),
    GOLDEN_SWORD("golden_sword", Component.text(":mc_golden_sword:")),
    GOLD_INGOT("gold_ingot", Component.text(":mc_gold_ingot:")),
    GOLD_NUGGET("gold_nugget", Component.text(":mc_gold_nugget:")),
    GRAY_DYE("gray_dye", Component.text(":mc_gray_dye:")),
    GREEN_DYE("green_dye", Component.text(":mc_green_dye:")),
    GUNPOWDER("gunpowder", Component.text(":mc_gunpowder:")),
    HEART_OF_THE_SEA("heart_of_the_sea", Component.text(":mc_heart_of_the_sea:")),
    HOPPER("hopper", Component.text(":mc_hopper:")),
    HOPPER_MINECART("hopper_minecart", Component.text(":mc_hopper_minecart:")),
    INK_SAC("ink_sac", Component.text(":mc_ink_sac:")),
    IRON_AXE("iron_axe", Component.text(":mc_iron_axe:")),
    IRON_BOOTS("iron_boots", Component.text(":mc_iron_boots:")),
    IRON_CHESTPLATE("iron_chestplate", Component.text(":mc_iron_chestplate:")),
    IRON_DOOR("iron_door", Component.text(":mc_iron_door:")),
    IRON_HELMET("iron_helmet", Component.text(":mc_iron_helmet:")),
    IRON_HOE("iron_hoe", Component.text(":mc_iron_hoe:")),
    IRON_HORSE_ARMOR("iron_horse_armor", Component.text(":mc_iron_horse_armor:")),
    IRON_INGOT("iron_ingot", Component.text(":mc_iron_ingot:")),
    IRON_LEGGINGS("iron_leggings", Component.text(":mc_iron_leggings:")),
    IRON_NUGGET("iron_nugget", Component.text(":mc_iron_nugget:")),
    IRON_PICKAXE("iron_pickaxe", Component.text(":mc_iron_pickaxe:")),
    IRON_SHOVEL("iron_shovel", Component.text(":mc_iron_shovel:")),
    IRON_SWORD("iron_sword", Component.text(":mc_iron_sword:")),
    ITEM_FRAME("item_frame", Component.text(":mc_item_frame:")),
    JUNGLE_BOAT("jungle_boat", Component.text(":mc_jungle_boat:")),
    JUNGLE_DOOR("jungle_door", Component.text(":mc_jungle_door:")),
    JUNGLE_SIGN("jungle_sign", Component.text(":mc_jungle_sign:")),
    KELP("kelp", Component.text(":mc_kelp:")),
    KNOWLEDGE_BOOK("knowledge_book", Component.text(":mc_knowledge_book:")),
    LANTERN("lantern", Component.text(":mc_lantern:")),
    LAPIS_LAZULI("lapis_lazuli", Component.text(":mc_lapis_lazuli:")),
    LAVA_BUCKET("lava_bucket", Component.text(":mc_lava_bucket:")),
    LEAD("lead", Component.text(":mc_lead:")),
    LEATHER("leather", Component.text(":mc_leather:")),
    LEATHER_HORSE_ARMOR("leather_horse_armor", Component.text(":mc_leather_horse_armor:")),
    LIGHT_BLUE_DYE("light_blue_dye", Component.text(":mc_light_blue_dye:")),
    LIGHT_GRAY_DYE("light_gray_dye", Component.text(":mc_light_gray_dye:")),
    LIME_DYE("lime_dye", Component.text(":mc_lime_dye:")),
    LINGERING_POTION("lingering_potion", Component.text(":mc_lingering_potion:")),
    MAGENTA_DYE("magenta_dye", Component.text(":mc_magenta_dye:")),
    MAGMA_CREAM("magma_cream", Component.text(":mc_magma_cream:")),
    MAP("map", Component.text(":mc_map:")),
    MELON_SEEDS("melon_seeds", Component.text(":mc_melon_seeds:")),
    MELON_SLICE("melon_slice", Component.text(":mc_melon_slice:")),
    MILK_BUCKET("milk_bucket", Component.text(":mc_milk_bucket:")),
    MINECART("minecart", Component.text(":mc_minecart:")),
    MOJANG_BANNER_PATTERN("mojang_banner_pattern", Component.text(":mc_mojang_banner_pattern:")),
    MUSHROOM_STEW("mushroom_stew", Component.text(":mc_mushroom_stew:")),
    MUSIC_DISC_11("music_disc_11", Component.text(":mc_music_disc_11:")),
    MUSIC_DISC_13("music_disc_13", Component.text(":mc_music_disc_13:")),
    MUSIC_DISC_BLOCKS("music_disc_blocks", Component.text(":mc_music_disc_blocks:")),
    MUSIC_DISC_CAT("music_disc_cat", Component.text(":mc_music_disc_cat:")),
    MUSIC_DISC_CHIRP("music_disc_chirp", Component.text(":mc_music_disc_chirp:")),
    MUSIC_DISC_FAR("music_disc_far", Component.text(":mc_music_disc_far:")),
    MUSIC_DISC_MALL("music_disc_mall", Component.text(":mc_music_disc_mall:")),
    MUSIC_DISC_MELLOHI("music_disc_mellohi", Component.text(":mc_music_disc_mellohi:")),
    MUSIC_DISC_STAL("music_disc_stal", Component.text(":mc_music_disc_stal:")),
    MUSIC_DISC_STRAD("music_disc_strad", Component.text(":mc_music_disc_strad:")),
    MUSIC_DISC_WAIT("music_disc_wait", Component.text(":mc_music_disc_wait:")),
    MUSIC_DISC_WARD("music_disc_ward", Component.text(":mc_music_disc_ward:")),
    MUTTON("mutton", Component.text(":mc_mutton:")),
    NAME_TAG("name_tag", Component.text(":mc_name_tag:")),
    NAUTILUS_SHELL("nautilus_shell", Component.text(":mc_nautilus_shell:")),
    NETHER_BRICK("nether_brick", Component.text(":mc_nether_brick:")),
    NETHER_WART("nether_wart", Component.text(":mc_nether_wart:")),
    OAK_BOAT("oak_boat", Component.text(":mc_oak_boat:")),
    OAK_DOOR("oak_door", Component.text(":mc_oak_door:")),
    OAK_SIGN("oak_sign", Component.text(":mc_oak_sign:")),
    ORANGE_DYE("orange_dye", Component.text(":mc_orange_dye:")),
    PAINTING("painting", Component.text(":mc_painting:")),
    PAPER("paper", Component.text(":mc_paper:")),
    PHANTOM_MEMBRANE("phantom_membrane", Component.text(":mc_phantom_membrane:")),
    PINK_DYE("pink_dye", Component.text(":mc_pink_dye:")),
    POISONOUS_POTATO("poisonous_potato", Component.text(":mc_poisonous_potato:")),
    POPPED_CHORUS_FRUIT("popped_chorus_fruit", Component.text(":mc_popped_chorus_fruit:")),
    PORKCHOP("porkchop", Component.text(":mc_porkchop:")),
    POTATO("potato", Component.text(":mc_potato:")),
    POTION("potion", Component.text(":mc_potion:")),
    POTION_OVERLAY("potion_overlay", Component.text(":mc_potion_overlay:")),
    PRISMARINE_CRYSTALS("prismarine_crystals", Component.text(":mc_prismarine_crystals:")),
    PRISMARINE_SHARD("prismarine_shard", Component.text(":mc_prismarine_shard:")),
    PUFFERFISH("pufferfish", Component.text(":mc_pufferfish:")),
    PUFFERFISH_BUCKET("pufferfish_bucket", Component.text(":mc_pufferfish_bucket:")),
    PUMPKIN_PIE("pumpkin_pie", Component.text(":mc_pumpkin_pie:")),
    PUMPKIN_SEEDS("pumpkin_seeds", Component.text(":mc_pumpkin_seeds:")),
    PURPLE_DYE("purple_dye", Component.text(":mc_purple_dye:")),
    QUARTZ("quartz", Component.text(":mc_quartz:")),
    RABBIT("rabbit", Component.text(":mc_rabbit:")),
    RABBIT_FOOT("rabbit_foot", Component.text(":mc_rabbit_foot:")),
    RABBIT_HIDE("rabbit_hide", Component.text(":mc_rabbit_hide:")),
    RABBIT_STEW("rabbit_stew", Component.text(":mc_rabbit_stew:")),
    REDSTONE("redstone", Component.text(":mc_redstone:")),
    RED_DYE("red_dye", Component.text(":mc_red_dye:")),
    REPEATER("repeater", Component.text(":mc_repeater:")),
    ROTTEN_FLESH("rotten_flesh", Component.text(":mc_rotten_flesh:")),
    RUBY("ruby", Component.text(":mc_ruby:")),
    SADDLE("saddle", Component.text(":mc_saddle:")),
    SALMON("salmon", Component.text(":mc_salmon:")),
    SALMON_BUCKET("salmon_bucket", Component.text(":mc_salmon_bucket:")),
    ARMADILLO_SCUTE("armadillo_scute", Component.text(":mc_armadillo_scute:")),
    TURTLE_SCUTE("turtle_scute", Component.text(":mc_turtle_scute:")),
    SEAGRASS("seagrass", Component.text(":mc_seagrass:")),
    SEA_PICKLE("sea_pickle", Component.text(":mc_sea_pickle:")),
    SHEARS("shears", Component.text(":mc_shears:")),
    SHULKER_SHELL("shulker_shell", Component.text(":mc_shulker_shell:")),
    SKULL_BANNER_PATTERN("skull_banner_pattern", Component.text(":mc_skull_banner_pattern:")),
    SLIME_BALL("slime_ball", Component.text(":mc_slime_ball:")),
    SNOWBALL("snowball", Component.text(":mc_snowball:")),
    SPAWN_EGG("spawn_egg", Component.text(":mc_spawn_egg:")),
    SPAWN_EGG_OVERLAY("spawn_egg_overlay", Component.text(":mc_spawn_egg_overlay:")),
    SPECTRAL_ARROW("spectral_arrow", Component.text(":mc_spectral_arrow:")),
    SPIDER_EYE("spider_eye", Component.text(":mc_spider_eye:")),
    SPLASH_POTION("splash_potion", Component.text(":mc_splash_potion:")),
    SPRUCE_BOAT("spruce_boat", Component.text(":mc_spruce_boat:")),
    SPRUCE_DOOR("spruce_door", Component.text(":mc_spruce_door:")),
    SPRUCE_SIGN("spruce_sign", Component.text(":mc_spruce_sign:")),
    STICK("stick", Component.text(":mc_stick:")),
    STONE_AXE("stone_axe", Component.text(":mc_stone_axe:")),
    STONE_HOE("stone_hoe", Component.text(":mc_stone_hoe:")),
    STONE_PICKAXE("stone_pickaxe", Component.text(":mc_stone_pickaxe:")),
    STONE_SHOVEL("stone_shovel", Component.text(":mc_stone_shovel:")),
    STONE_SWORD("stone_sword", Component.text(":mc_stone_sword:")),
    STRING("string", Component.text(":mc_string:")),
    STRUCTURE_VOID("structure_void", Component.text(":mc_structure_void:")),
    SUGAR("sugar", Component.text(":mc_sugar:")),
    SUGAR_CANE("sugar_cane", Component.text(":mc_sugar_cane:")),
    SUSPICIOUS_STEW("suspicious_stew", Component.text(":mc_suspicious_stew:")),
    SWEET_BERRIES("sweet_berries", Component.text(":mc_sweet_berries:")),
    TIPPED_ARROW_BASE("tipped_arrow_base", Component.text(":mc_tipped_arrow_base:")),
    TIPPED_ARROW_HELMET("tipped_arrow_helmet", Component.text(":mc_tipped_arrow_helmet:")),
    TNT_MINECART("tnt_minecart", Component.text(":mc_tnt_minecart:")),
    TOTEM_OF_UNDYING("totem_of_undying", Component.text(":mc_totem_of_undying:")),
    TRIDENT("trident", Component.text(":mc_trident:")),
    TROPICAL_FISH("tropical_fish", Component.text(":mc_tropical_fish:")),
    TROPICAL_FISH_BUCKET("tropical_fish_bucket", Component.text(":mc_tropical_fish_bucket:")),
    TURTLE_EGG("turtle_egg", Component.text(":mc_turtle_egg:")),
    TURTLE_HELMET("turtle_helmet", Component.text(":mc_turtle_helmet:")),
    WATER_BUCKET("water_bucket", Component.text(":mc_water_bucket:")),
    WHEAT("wheat", Component.text(":mc_wheat:")),
    WHEAT_SEEDS("wheat_seeds", Component.text(":mc_wheat_seeds:")),
    WHITE_DYE("white_dye", Component.text(":mc_white_dye:")),
    WOODEN_AXE("wooden_axe", Component.text(":mc_wooden_axe:")),
    WOODEN_HOE("wooden_hoe", Component.text(":mc_wooden_hoe:")),
    WOODEN_PICKAXE("wooden_pickaxe", Component.text(":mc_wooden_pickaxe:")),
    WOODEN_SHOVEL("wooden_shovel", Component.text(":mc_wooden_shovel:")),
    WOODEN_SWORD("wooden_sword", Component.text(":mc_wooden_sword:")),
    WRITABLE_BOOK("writable_book", Component.text(":mc_writable_book:")),
    WRITTEN_BOOK("written_book", Component.text(":mc_written_book:")),
    YELLOW_DYE("yellow_dye", Component.text(":mc_yellow_dye:"));

    /**
     * The set of special titles.
     */
    private static final @NotNull EnumSet<AccountTitle> SPECIAL_TITLES = EnumSet.of(
            EMPTY,
            CAPITALIST,
            DOOMSDAY,
            END_CRYSTAL,
            NETHER_STAR
    );

    /**
     * The set of acquirable titles.
     */
    private static final @NotNull Set<AccountTitle> ACQUIRABLE_TITLES = Arrays.stream(values())
            .filter(t -> !SPECIAL_TITLES.contains(t))
            .collect(Collectors.toUnmodifiableSet());

    /**
     * Returns the set of acquirable titles.
     *
     * @return The set of acquirable titles
     */
    public static @NotNull Set<AccountTitle> getAcquirableTitles() {
        return ACQUIRABLE_TITLES;
    }

    /**
     * Returns whether the given item is an account title.
     *
     * @param item The item
     * @return The result
     */
    public static @NotNull AccountTitleQueryResult isTitle(@Nullable ItemStack item) {
        if (item == null) return new AccountTitleQueryResult(false, null);
        boolean isEnchantedBook = item.getType() == Material.ENCHANTED_BOOK;

        ItemMeta meta = item.getItemMeta();
        List<Component> lore = meta.lore();

        boolean hasLore = lore != null && lore.contains(TitleItem.LORE);

        Component name = meta.displayName();
        if (name == null) return new AccountTitleQueryResult(false, null);

        if (!isEnchantedBook || !hasLore) return new AccountTitleQueryResult(false, null);

        AccountTitle title = Arrays.stream(values())
                .filter(t -> Objects.equals(t.value, name))
                .findAny()
                .orElse(null);

        if (title == null) return new AccountTitleQueryResult(false, null);

        return new AccountTitleQueryResult(true, title);
    }

    /**
     * Creates a new title.
     *
     * @param key   The key
     * @param value The value
     */
    AccountTitle(@NotNull String key, @NotNull Component value) {
        this.key = key;
        this.value = value.applyFallbackStyle(Style.empty());
    }

    /// Titles

    /**
     * Returns the title of matching key, or {@link #EMPTY the empty title}.
     *
     * @param key The key
     * @return The title if present, {@link #EMPTY} otherwise
     */
    public static @NotNull AccountTitle getOrEmpty(@Nullable String key) {
        if (key == null) return EMPTY;

        try {
            return valueOf(key.toUpperCase());
        } catch (IllegalArgumentException e) {
            return EMPTY;
        }
    }

    /**
     * Returns the set of all keys.
     *
     * @return The set of all keys
     */
    public static @NotNull Set<String> getKeys() {
        return Arrays.stream(values())
                .map(AccountTitle::toString)
                .map(String::toLowerCase)
                .collect(Collectors.toUnmodifiableSet());
    }

    /// Body

    private final @NotNull String key;
    private final @NotNull Component value;

    /**
     * Returns the key.
     *
     * @return The key
     */
    public @NotNull String getKey() {
        return key;
    }

    /**
     * Returns the value.
     *
     * @return The value
     */
    public @NotNull Component getValue() {
        return value;
    }

    /**
     * Generates the title item.
     *
     * @return The title item
     */
    public @NotNull ItemStack generateItem() {
        return new TitleItem(this).getItem();
    }
}
