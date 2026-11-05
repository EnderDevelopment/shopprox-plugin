# Shopprox Minecraft Shop Plugin

Shopprox is a Minecraft shop plugin for Paper servers that provides an in-game GUI shop with buy and sell actions, emerald currency, fixed item prices, and the `/shop` command with the `/loja` alias.

## Install

1. Build the plugin with Java 21 and Maven:
   ```bash
   mvn package
   ```
2. Copy the generated `target/plugin.jar` file into your server's `plugins` folder.
3. Start or restart the Paper server.
4. Confirm that the plugin loads successfully in the server console.

## Use

1. Join the server as a player.
2. Run `/shop` or `/loja`.
3. Left-click an item in the shop menu to buy one item.
4. Right-click an item to sell one item.
5. Use emeralds as currency:
   - Diamond: buy for 12 emeralds, sell for 6 emeralds
   - Gold ingot: buy for 5 emeralds, sell for 2 emeralds
   - Iron ingot: buy for 3 emeralds, sell for 1 emerald
   - Emerald: buy for 20 emeralds, sell for 10 emeralds
   - Obsidian: buy for 16 emeralds, sell for 8 emeralds
   - Golden apple: buy for 40 emeralds, sell for 20 emeralds
6. Close the inventory to exit the shop.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/442bd7de-6cf7-43df-ae68-ad1771760c5d?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=442bd7de-6cf7-43df-ae68-ad1771760c5d
