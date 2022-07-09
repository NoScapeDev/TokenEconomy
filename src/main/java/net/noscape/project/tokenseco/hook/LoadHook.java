package net.noscape.project.tokenseco.hook;

import net.milkbowl.vault.economy.*;
import net.noscape.project.tokenseco.*;
import org.bukkit.*;
import org.bukkit.permissions.*;
import org.bukkit.plugin.*;

import java.util.*;

public class LoadHook {

    public static Economy econ = null;
    public static Permission vaultPerm = null;

    @SuppressWarnings("ConstantConditions")
    public static void load() {
        econ = new EconomyVault();
        RegisteredServiceProvider<Permission> rsp = TokensEconomy.getInstance().getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp != null) {
            vaultPerm = rsp.getProvider();
        }

        TokensEconomy.getInstance().getServer().getServicesManager().register(Economy.class, econ, TokensEconomy.getInstance(), ServicePriority.Highest);

        if (TokensEconomy.getInstance().getConfig().getBoolean("t.support.disable-essentials-eco")) {
            Collection<RegisteredServiceProvider<Economy>> econs = Bukkit.getPluginManager().getPlugin("Vault").getServer().getServicesManager().getRegistrations(Economy.class);
            for (RegisteredServiceProvider<Economy> econ : econs) {
                if (econ.getProvider().getName().equalsIgnoreCase("Essentials Economy")||
                        econ.getProvider().getName().equalsIgnoreCase("EssentialsX Economy")) {
                    TokensEconomy.getInstance().getServer().getServicesManager().unregister(econ.getProvider());
                }
            }
        }

        Bukkit.getConsoleSender().sendMessage("[Vault] TokenEconomy: Hooked into vault.");
    }

    public static boolean loadcm() {
        RegisteredServiceProvider<Economy> rsp = TokensEconomy.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }

    public static void unload() {
        TokensEconomy.getInstance().getServer().getServicesManager().unregister(econ);
    }
}
