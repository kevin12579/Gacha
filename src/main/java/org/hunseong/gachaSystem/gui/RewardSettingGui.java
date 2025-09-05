package org.hunseong.gachaSystem.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hunseong.gachaSystem.GachaSystem;

import java.util.*;


public class RewardSettingGui {
    public Inventory setRewardSettingGui(Player player, String grade) {
        Inventory inventory = Bukkit.createInventory(player, 9*6, "가차 보상설정 - "+grade);

        List<ItemStack> rewards = GachaSystem.getGradeReward().getGradeRewards(grade);
        for (int i = 0; i < rewards.size(); i++) {
            inventory.setItem(i, rewards.get(i));
        }
        return inventory;
    }

    public void openRewardSettingGui(Player player, String grade) {
        player.openInventory(setRewardSettingGui(player, grade));
    }
}
