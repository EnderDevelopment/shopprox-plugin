package com.enderdevelopment.plugin;

import org.bukkit.Material;

public record ShopItem(Material material, int buyPrice, int sellPrice) {
}
