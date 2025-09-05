package org.hunseong.gachaSystem.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.hunseong.gachaSystem.GachaSystem;

import java.util.List;

public class ClickIconEvent implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String viewTitle = event.getView().getTitle();
        Player player = (Player) event.getWhoClicked();

        if (viewTitle.contains("뽑기판") || viewTitle.contains("뽑기판 보상 미리보기 - ")) {
            event.setCancelled(true);
            if (!viewTitle.equals("뽑기판")) return;

            List<Integer> lockedSlots = GachaSystem.getPlayerBoardData().getlockedSlots(player);
            if (lockedSlots.contains(event.getSlot() - 18)) {
                GachaSystem.getGiveReward().excuteGacha(player, event.getSlot());
                return;
            }
            // 미리보기 GUI
            switch (event.getSlot()) {
                case 8:
                    GachaSystem.getRewardPreviewGui().openRewardPreviewGui(player, "SSS");
                    break;
                case 7:
                    GachaSystem.getRewardPreviewGui().openRewardPreviewGui(player, "SSS");
                    break;
                case 6:
                    GachaSystem.getRewardPreviewGui().openRewardPreviewGui(player, "S");
                    break;
                case 2:
                    GachaSystem.getRewardPreviewGui().openRewardPreviewGui(player, "A");
                    break;
                case 1:
                    GachaSystem.getRewardPreviewGui().openRewardPreviewGui(player, "B");
                    break;
                case 0:
                    GachaSystem.getRewardPreviewGui().openRewardPreviewGui(player, "C");
                    break;
                default:
                    break;

            }
        }
    }
}