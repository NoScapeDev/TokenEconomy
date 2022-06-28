package net.noscape.project.tokenseco.utils.menu.menus;

import net.noscape.project.tokenseco.*;
import net.noscape.project.tokenseco.data.*;
import net.noscape.project.tokenseco.managers.*;
import net.noscape.project.tokenseco.utils.*;
import net.noscape.project.tokenseco.utils.menu.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.util.*;
import java.util.stream.*;

public class TokenShop extends Menu {

    private final TokensEconomy te = TokensEconomy.getPlugin(TokensEconomy.class);

    public TokenShop(MenuUtil menuUtil) {
        super(menuUtil);
    }

    @Override
    public String getMenuName() {
        return Utils.applyFormat(TokensEconomy.getConfigManager().getTitleShop());
    }

    @Override
    public int getSlots() {
        return TokensEconomy.getConfigManager().getSlotsShop();
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        for (String items : Objects.requireNonNull(TokensEconomy.getConfigManager().getTokenshop().getConfigurationSection("gui.items")).getKeys(false)) {
            String displayname = TokensEconomy.getConfigManager().getTokenshop().getString("gui.items." + items + ".displayname");
            int tokens = TokensEconomy.getConfigManager().getTokenshop().getInt("gui.items." + items + ".tokens");
            int amount_material = TokensEconomy.getConfigManager().getTokenshop().getInt("gui.items." + items + ".amount");


            if (Objects.requireNonNull(Objects.requireNonNull(e.getCurrentItem()).getItemMeta()).getDisplayName().equals(Utils.applyFormat(displayname))) {
                if (hasMaterial(player.getInventory(), amount_material)) {
                    e.setCancelled(true);
                    TokenManager ptokens = TokensEconomy.getTokenManager(player);
                    if (!(ptokens.getTokens() >= TokensEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                        // remove material from inventory
                        removeMaterial(player.getInventory(), amount_material);

                        // get and add tokens
                        if (!TokensEconomy.getConfigManager().isBankBalanceShop()) {
                            TokenManager token = TokensEconomy.getTokenManager(player);
                            token.addTokens(tokens);
                        } else {
                            BankManager bank = TokensEconomy.getBankManager(player);
                            bank.addBank(tokens);
                        }

                        // confirmation
                        player.sendMessage(Utils.applyFormat(Objects.requireNonNull(TokensEconomy.getConfigManager().getMessages().getString("m.RECEIVED")).replaceAll("%tokens%", String.valueOf(tokens))));
                        if (TokensEconomy.getConfigManager().getTokenshop().getBoolean("gui.sound.enable")) {
                            player.playSound(player.getLocation(),
                                    Sound.valueOf(Objects.requireNonNull(TokensEconomy.getConfigManager().getTokenshop().getString("gui.sound.success")).toUpperCase()), 1, 1);
                        }
                    } else {
                        player.sendMessage(Utils.applyFormat("&cYou have reached the max token balance!"));
                    }
                } else {
                    player.sendMessage(Utils.applyFormat(TokensEconomy.getConfigManager().getMessages().getString("m.NOT_ENOUGH_MATERIALS")));
                    if (TokensEconomy.getConfigManager().getTokenshop().getBoolean("gui.sound.enable")) {
                        player.playSound(player.getLocation(),
                                Sound.valueOf(Objects.requireNonNull(TokensEconomy.getConfigManager().getTokenshop().getString("gui.sound.failed")).toUpperCase()), 1, 1);
                    }
                }
            }
        }
    }

    @Override
    public void setMenuItems() {


        // main items

        for (String items : Objects.requireNonNull(TokensEconomy.getConfigManager().getTokenshop().getConfigurationSection("gui.items")).getKeys(false)) {

            String displayname = TokensEconomy.getConfigManager().getTokenshop().getString("gui.items." + items + ".displayname");
            int slot = TokensEconomy.getConfigManager().getTokenshop().getInt("gui.items." + items + ".slot");
            boolean glow = TokensEconomy.getConfigManager().getTokenshop().getBoolean("gui.items." + items + ".glow");
            String material = Objects.requireNonNull(TokensEconomy.getConfigManager().getTokenshop().getString("gui.items." + items + ".material")).toUpperCase();
            int amount = TokensEconomy.getConfigManager().getTokenshop().getInt("gui.items." + items + ".amount");

            //List<String> lore = TokensEconomy.getConfigManager().getTokenshop().getStringList("gui.items." + items + ".lore");

            ItemStack item = new ItemStack(Material.valueOf(material), amount);

            for (String lore : TokensEconomy.getConfigManager().getTokenshop().getStringList("gui.items." + items + ".lore")) {
                nameItem(item, Utils.applyFormat(displayname), lore);
            }

            if (glow) {
                item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            }

            inventory.setItem(slot, item);
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

    private void removeMaterial(Inventory inventory, int amount) {
        if (amount <= 0) return;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inventory.getItem(slot);
            if (is == null) continue;
            if (Material.valueOf(Objects.requireNonNull(TokensEconomy.getConfigManager().getTokenshop().getString("gui.item-exchange")).toUpperCase()) == is.getType()) {
                int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    amount = -newAmount;
                    if (amount == 0) break;
                }
            }
        }
    }

    private boolean hasMaterial(Inventory inventory, int amount) {
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inventory.getItem(slot);
            if (is == null) continue;
            if (Material.valueOf(Objects.requireNonNull(TokensEconomy.getConfigManager().getTokenshop().getString("gui.item-exchange")).toUpperCase()) == is.getType()
                    && is.getAmount() >= amount)
                return true;
        }

        return false;
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
}
