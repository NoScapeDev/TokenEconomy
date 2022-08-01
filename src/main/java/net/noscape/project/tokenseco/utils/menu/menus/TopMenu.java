package net.noscape.project.tokenseco.utils.menu.menus;

import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.data.*;
import net.noscape.project.tokenseco.utils.*;
import net.noscape.project.tokenseco.utils.menu.*;
import org.bukkit.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.util.*;
import java.util.stream.*;

public class TopMenu extends Menu {

    private final TokensEconomy te = TokensEconomy.getPlugin(TokensEconomy.class);

    public TopMenu(MenuUtil menuUtil) {
        super(menuUtil);
    }

    @Override
    public String getMenuName() {
        return Utils.applyFormat(TokensEconomy.getConfigManager().getTitleTop());
    }

    @Override
    public int getSlots() {
        return TokensEconomy.getConfigManager().getSlotsTop();
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void setMenuItems() {

        // add all entries.
        if (te.isMySQL()) {
            for (String top : MySQLUserData.getTop10().keySet()) {
                inventory.addItem(getSkull(top, top.indexOf(top)));
            }
        } else if (te.isH2()) {
            for (String top : H2UserData.getTop10().keySet()) {
                inventory.addItem(getSkull(top, top.indexOf(top)));
            }
        }

        // fill glass
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta glass_meta = glass.getItemMeta();
        assert glass_meta != null;
        glass_meta.setDisplayName(Utils.applyFormat("&8*"));
        glass.setItemMeta(glass_meta);

        // loop through empty slots and set glass
        for(int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, glass);
            }
        }
    }

    public ItemStack getSkull(String name, int slot) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        assert meta != null;
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(name));

        for (String lore : TokensEconomy.getConfigManager().getTokenTop().getStringList("gui.items.top_players.lore")) {
            nameItem(name, skull, Utils.applyFormat(Utils.applyFormat(TokensEconomy.getConfigManager().getTokenTop().getString("gui.items.top_players.displayname"))), lore);
        }

        String displayname = TokensEconomy.getConfigManager().getTokenTop().getString("gui.items.top_players.displayname");

        assert displayname != null;
        if (te.isMySQL()) {
            displayname = displayname.replaceAll("%tokens%", String.valueOf(MySQLUserData.getTokensDoubleByName(name)));
        } else if (te.isH2()) {
            displayname = displayname.replaceAll("%tokens%", String.valueOf(H2UserData.getTokensDoubleByName(name)));
        }

        displayname = displayname.replaceAll("%top_player%", name);
        displayname = displayname.replaceAll("%number%", String.valueOf(slot + 1).replace("-", ""));

        meta.setDisplayName(Utils.applyFormat(displayname));

        skull.setItemMeta(meta);

        return skull;
    }

    private ItemStack nameItem(final String player, final ItemStack item, final String name, final String... lore) {
        final ItemMeta item_meta = item.getItemMeta();
        assert item_meta != null;

        if (te.isMySQL()) {
            item_meta.setDisplayName(name.replaceAll("%tokens%", String.valueOf(MySQLUserData.getTokensDoubleByName(player))));
        } else if (te.isH2()) {
            item_meta.setDisplayName(name.replaceAll("%tokens%", String.valueOf(H2UserData.getTokensDoubleByName(player))));
        }
            /* Setting the lore but translating the color codes as well. */
        item_meta.setLore(Arrays.stream(lore).map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList()));

        item_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item_meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item_meta.addItemFlags(ItemFlag.HIDE_DESTROYS);

        item.setItemMeta(item_meta);
        return item;
    }

}
