package net.noscape.project.tokenseco.utils.menu.menus;

import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.managers.*;
import net.noscape.project.tokenseco.utils.*;
import net.noscape.project.tokenseco.utils.menu.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.util.*;
import java.util.stream.*;

import static net.noscape.project.tokenseco.utils.Utils.msgPlayer;

public class TokenMenu extends Menu {

    private final TokensEconomy te = TokensEconomy.getPlugin(TokensEconomy.class);

    public TokenMenu(MenuUtil menuUtil) {
        super(menuUtil);
    }

    @Override
    public String getMenuName() {
        return Utils.applyFormat(TokensEconomy.getConfigManager().getTitleMenu(menuUtil.getOwner()));
    }

    @Override
    public int getSlots() {
        return TokensEconomy.getConfigManager().getSlotsMenu();
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

        String exchange = TokensEconomy.getConfigManager().getTokenMenu().getString("gui.items.exchange.displayname");
        String top = TokensEconomy.getConfigManager().getTokenMenu().getString("gui.items.top.displayname");

        if (Objects.requireNonNull(Objects.requireNonNull(e.getCurrentItem()).getItemMeta()).getDisplayName().equals(Utils.applyFormat(exchange))) {
            e.setCancelled(true);
            new ExchangeMenu(TokensEconomy.getMenuUtil(menuUtil.getOwner())).open();
        } else if (Objects.requireNonNull(Objects.requireNonNull(e.getCurrentItem()).getItemMeta()).getDisplayName().equals(Utils.applyFormat(top))) {
            e.setCancelled(true);
            new TopMenu(TokensEconomy.getMenuUtil(menuUtil.getOwner())).open();
        } else if (Objects.requireNonNull(Objects.requireNonNull(e.getCurrentItem()).getItemMeta()).getDisplayName().equals(Utils.applyFormat("&6Token Guide &7(Commands)"))) {
            e.setCancelled(true);
            menuUtil.getOwner().closeInventory();
            msgPlayer(menuUtil.getOwner(), "&e&nToken Commands&r",
                    "",
                    "&6/tokens pay <user> <amount> &7- pay a user amount of tokens.",
                    "&6/tokens balance &7- get amount of tokens you have.",
                    "&6/tokens bank &7- get amount of tokens in your bank.",
                    "&6/tokens exchange &7- opens the exchange gui",
                    "&6/tokens top &7- opens the top players gui",
                    "&6/tokens stats &7- gets the server tokens stats",
                    "&6/tokens toggle &7- toggles token request for yourself.",
                    "&6/tokens help &7- shows this help guide.",
                    "");
        } else {
            e.setCancelled(true);
        }
    }

    @Override
    public void setMenuItems() {

        // add config items
        for (String items : Objects.requireNonNull(TokensEconomy.getConfigManager().getTokenMenu().getConfigurationSection("gui.items")).getKeys(false)) {
            TokenManager tokens = TokensEconomy.getTokenManager(menuUtil.getOwner());

            String displayname = Objects.requireNonNull(TokensEconomy.getConfigManager().getTokenMenu().getString("gui.items." + items + ".displayname")).replaceAll("%tokens%", String.valueOf(tokens.getTokens()));
            int slot = TokensEconomy.getConfigManager().getTokenMenu().getInt("gui.items." + items + ".slot");
            boolean glow = TokensEconomy.getConfigManager().getTokenMenu().getBoolean("gui.items." + items + ".glow");
            String material = Objects.requireNonNull(TokensEconomy.getConfigManager().getTokenMenu().getString("gui.items." + items + ".material")).toUpperCase();

            ItemStack item = new ItemStack(Material.valueOf(material), 1);

            for (String lore1 : TokensEconomy.getConfigManager().getTokenMenu().getStringList("gui.items." + items + ".lore")) {
                nameItem(item, Utils.applyFormat(displayname), lore1.replaceAll("%tokens%", String.valueOf(tokens.getTokens())));
            }

            if (glow) {
                item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            }

            inventory.setItem(slot, item);
        }


        // book item

        if (inventory.getSize() == 27) {
            ItemStack guide = new ItemStack(Material.BOOK, 1);
            ItemMeta guide_meta = guide.getItemMeta();
            assert guide_meta != null;
            guide_meta.setDisplayName(Utils.applyFormat("&6Token Guide &7(Commands)"));

            List<String> lore = new ArrayList<>();
            lore.add("&7Click to see the token commands.");
            // ============================================================================

            for (String l : lore) {
                guide_meta.setLore(Collections.singletonList(Utils.applyFormat(l)));
            }

            guide.setItemMeta(guide_meta);

            inventory.setItem(26, guide);
        } else if (inventory.getSize() == 54) {
            ItemStack guide = new ItemStack(Material.BOOK, 1);
            ItemMeta guide_meta = guide.getItemMeta();
            assert guide_meta != null;
            guide_meta.setDisplayName(Utils.applyFormat("&6Token Guide &7(Commands)"));

            List<String> lore = new ArrayList<>();
            lore.add("&7Click to see the token commands.");
            // ============================================================================

            for (String l : lore) {
                guide_meta.setLore(Collections.singletonList(Utils.applyFormat(l)));
            }

            guide.setItemMeta(guide_meta);

            inventory.setItem(53, guide);
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

    private ItemStack nameItem(final ItemStack item, final String name, final String... lore) { //  Changes String lore to String... Lore
        final ItemMeta item_meta = item.getItemMeta();
        assert item_meta != null;
        item_meta.setDisplayName(name); // You should most likely translate here too unless you do it beforehand.
        /* Setting the lore but translating the color codes as well. */
        item_meta.setLore(Arrays.stream(lore).map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList()));

        item_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item_meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item_meta.addItemFlags(ItemFlag.HIDE_DESTROYS);

        item.setItemMeta(item_meta);
        return item;
    }

    public List<String> wordWrapLore(String string) {
        StringBuilder sb = new StringBuilder(string);

        int i = 0;
        while (i + 35 < sb.length() && (i = sb.lastIndexOf(" ", i + 35)) != -1) {
            sb.replace(i, i + 1, "\n");
        }
        return Arrays.asList(sb.toString().split("\n"));

    }
}