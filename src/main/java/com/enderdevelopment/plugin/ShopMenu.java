package com.enderdevelopment.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ShopMenu implements Listener {
    private static final int[] ITEM_SLOTS = {10, 11, 12, 13, 14, 15};
    private final List<ShopItem> items;

    public ShopMenu(List<ShopItem> items) {
        this.items = List.copyOf(items);
    }

    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(new ShopHolder(), 27,
                Component.text(Main.SHOP_TITLE).color(NamedTextColor.DARK_GREEN));
        for (int index = 0; index < items.size() && index < ITEM_SLOTS.length; index++) {
            inventory.setItem(ITEM_SLOTS[index], createShopItem(items.get(index)));
        }
        inventory.setItem(22, createItem(Material.BOOK, "How to use", List.of(
                "Left click: buy 1 item", "Right click: sell 1 item", "Currency: emeralds"
        ), NamedTextColor.YELLOW));
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof ShopHolder)) return;
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) return;
        int itemIndex = indexForSlot(event.getRawSlot());
        if (itemIndex < 0 || itemIndex >= items.size()) return;
        ShopItem item = items.get(itemIndex);
        if (event.isLeftClick()) buy(player, item);
        if (event.isRightClick()) sell(player, item);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof ShopHolder) {
            event.getPlayer().sendMessage(Component.text("Shop closed.").color(NamedTextColor.GRAY));
        }
    }

    private void buy(Player player, ShopItem item) {
        if (!removeItems(player, Material.EMERALD, item.buyPrice())) {
            player.sendMessage(Component.text("You need " + item.buyPrice() + " emeralds to buy this.").color(NamedTextColor.RED));
            return;
        }
        giveItems(player, new ItemStack(item.material()));
        player.sendMessage(Component.text("Bought 1 " + readable(item.material()) + " for " + item.buyPrice() + " emeralds.").color(NamedTextColor.GREEN));
    }

    private void sell(Player player, ShopItem item) {
        if (!removeItems(player, item.material(), 1)) {
            player.sendMessage(Component.text("You do not have a " + readable(item.material()) + " to sell.").color(NamedTextColor.RED));
            return;
        }
        giveItems(player, new ItemStack(Material.EMERALD, item.sellPrice()));
        player.sendMessage(Component.text("Sold 1 " + readable(item.material()) + " for " + item.sellPrice() + " emeralds.").color(NamedTextColor.GREEN));
    }

    private boolean removeItems(Player player, Material material, int amount) {
        int available = 0;
        for (ItemStack stack : player.getInventory().getStorageContents()) {
            if (stack != null && stack.getType() == material) available += stack.getAmount();
        }
        if (available < amount) return false;
        int remaining = amount;
        for (int slot = 0; slot < player.getInventory().getStorageContents().length && remaining > 0; slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack == null || stack.getType() != material) continue;
            int removed = Math.min(remaining, stack.getAmount());
            stack.setAmount(stack.getAmount() - removed);
            remaining -= removed;
            player.getInventory().setItem(slot, stack.getAmount() == 0 ? null : stack);
        }
        return true;
    }

    private void giveItems(Player player, ItemStack item) {
        Map<Integer, ItemStack> leftovers = player.getInventory().addItem(item);
        for (ItemStack leftover : leftovers.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), leftover);
            player.sendMessage(Component.text("Your inventory was full; the item was dropped nearby.").color(NamedTextColor.YELLOW));
        }
    }

    private ItemStack createShopItem(ShopItem item) {
        return createItem(item.material(), readable(item.material()), List.of(
                "Buy: " + item.buyPrice() + " emeralds (left click)",
                "Sell: " + item.sellPrice() + " emeralds (right click)"
        ), NamedTextColor.WHITE);
    }

    private ItemStack createItem(Material material, String name, List<String> lines, NamedTextColor color) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name).color(color));
        List<Component> lore = new ArrayList<>();
        for (String line : lines) lore.add(Component.text(line).color(NamedTextColor.GRAY));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private int indexForSlot(int slot) {
        for (int index = 0; index < ITEM_SLOTS.length; index++) if (ITEM_SLOTS[index] == slot) return index;
        return -1;
    }

    private String readable(Material material) {
        return material.name().toLowerCase().replace('_', ' ');
    }

    private static final class ShopHolder implements InventoryHolder {
        @Override
        public Inventory getInventory() {
            return null;
        }
    }
}
