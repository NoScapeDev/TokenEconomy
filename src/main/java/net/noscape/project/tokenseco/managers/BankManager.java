package net.noscape.project.tokenseco.managers;

import org.bukkit.*;

public class BankManager {

    private OfflinePlayer player;
    private int bank;

    public BankManager(OfflinePlayer player, int bank) {
        this.player = player;
        this.bank = bank;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public void setBank(int i) {
        this.bank = i;
    }

    public void addBank(int tokens) {
        int i = getBank() + tokens;

        setBank(i);
    }


    public void removeBank(int tokens) {
        int i = getBank() - tokens;

        setBank(i);
    }

    public int getBank() {
        return bank;
    }
}