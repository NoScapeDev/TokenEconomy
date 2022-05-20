package net.noscape.project.tokenseco.utils.menu;

import org.bukkit.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

public abstract class Menu implements InventoryHolder {

    protected Inventory inventory;

    protected MenuUtil menuUtil;

    public Menu(MenuUtil menuUtil) {
        this.menuUtil = menuUtil;
    }

    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract void handleMenu(InventoryClickEvent e);

    public abstract void setMenuItems();

    public void open() {

        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());

        this.setMenuItems();

        menuUtil.getOwner().openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
