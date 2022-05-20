package net.noscape.project.tokenseco.utils.menu;

import org.bukkit.entity.*;

public class MenuUtil {

    private Player owner;

    public MenuUtil(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

}
