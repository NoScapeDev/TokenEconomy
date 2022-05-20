package net.noscape.project.tokenseco.utils.menu;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

import java.util.*;

public class MenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getClickedInventory() != null) {
            InventoryHolder holder = Objects.requireNonNull(e.getClickedInventory()).getHolder();

            if (holder instanceof Menu) {
                e.setCancelled(true);

                if (e.getCurrentItem() == null) {
                    return;
                }

                Menu menu = (Menu) holder;
                menu.handleMenu(e);
            }
        }
    }

}
