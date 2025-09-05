package org.hunseong.gachaSystem.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hunseong.gachaSystem.GachaSystem;

import java.util.List;

public class RewardPreviewGui {
    public Inventory setRewardPreviewGui(Player player, String grade) {
        Inventory inventory = Bukkit.createInventory(player, 9*6, "뽑기판 보상 미리보기 - "+grade);

        List<ItemStack> rewards = GachaSystem.getGradeReward().getGradeRewards(grade);
        for (int i = 0; i < rewards.size(); i++) {
            inventory.setItem(i, rewards.get(i));
        }
        return inventory;
    }

    public void openRewardPreviewGui(Player player, String grade) {
        player.openInventory(setRewardPreviewGui(player, grade));
    }
}
