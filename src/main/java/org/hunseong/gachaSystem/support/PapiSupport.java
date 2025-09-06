package org.hunseong.gachaSystem.support;


import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.PlaceholderAPI;
import org.hunseong.gachaSystem.GachaSystem;
import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;

public class PapiSupport extends PlaceholderExpansion {
    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return GachaSystem.getInstance().getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
        return "Gacha";
    }

    @Override
    public String getVersion() {
        return GachaSystem.getInstance().getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (player == null)
            return null;

        if (params.equalsIgnoreCase("Ticket")) {
            return String.valueOf(GachaSystem.getPlayerBoardData().getPlayerTicketData(player));
        }

        return null;
    }

    public String convertMessagePAPI(Player player, String message) {
        return (String) PlaceholderAPI.setPlaceholders(player, message);
    }

}

