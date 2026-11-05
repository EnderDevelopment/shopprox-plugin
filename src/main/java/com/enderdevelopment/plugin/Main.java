package com.enderdevelopment.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Main extends JavaPlugin {
    public static final String SHOP_TITLE = "Server Shop";

    @Override
    public void onEnable() {
        List<ShopItem> items = List.of(
                new ShopItem(Material.DIAMOND, 12, 6),
                new ShopItem(Material.GOLD_INGOT, 5, 2),
                new ShopItem(Material.IRON_INGOT, 3, 1),
                new ShopItem(Material.EMERALD, 20, 10),
                new ShopItem(Material.OBSIDIAN, 16, 8),
                new ShopItem(Material.GOLDEN_APPLE, 40, 20)
        );

        ShopMenu shopMenu = new ShopMenu(items);
        getCommand("shop").setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof org.bukkit.entity.Player player)) {
                sender.sendMessage("Only players can open the shop.");
                return true;
            }
            shopMenu.open(player);
            return true;
        });
        Bukkit.getPluginManager().registerEvents(shopMenu, this);
        getLogger().info("Shop enabled with emerald currency.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Shop disabled.");
    }

    public static Component moneyMessage(String text) {
        return Component.text(text).color(NamedTextColor.GREEN);
    }
}
