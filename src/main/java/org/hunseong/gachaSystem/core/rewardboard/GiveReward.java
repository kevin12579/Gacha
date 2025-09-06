package org.hunseong.gachaSystem.core.rewardboard;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.hunseong.gachaSystem.GachaSystem;


public class GiveReward {

    public void excuteGacha(Player player, int slot) {
        int tickets = GachaSystem.getPlayerBoardData().getPlayerTicketData(player);
        if (tickets < 1) {
            player.sendMessage("§c뽑기 횟수가 부족합니다.");
            return;
        }

        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage("§c인벤토리 공간이 부족합니다. 1칸 이상 비워주세요.");
            return;
        }

        ItemStack reward = GachaSystem.getPlayerBoardData().getRewardItemFromSlotNum(player, slot);
        if (reward == null) {
            player.sendMessage("§c해당 슬롯의 아이템을 불러올 수 없습니다. 먼저 보상을 설정해주세요.");
            player.sendMessage("§c아이템을 전부 설정 후 /가차 초기화 [플레이어]를 통해 초기화를 진행해주세요.");
            player.sendMessage("§c유저 보상판 데이터에 아이템이 NULL로 설정되어 일어나는 문제입니다.");
            return;
        }
        GachaSystem.getPlayerBoardData().setPlayerTicketData(player, tickets - 1);
        player.getInventory().addItem(reward);
        GachaSystem.getPlayerBoardData().updateSlotUnlockedStatus(player, slot);
        String grade = GachaSystem.getPlayerBoardData().getGradeFromSlotNum(player, slot);

        String message = null;
        switch (grade) {
            case "SSS":
                //보상판 초기화
                GachaSystem.getResetRewardBoard().resetRewardBoard(player);
                player.sendMessage( GachaSystem.getMainGui().getGradeColor("SSS")+"SSS보상§f을 획득하셨습니다. 보상판이 §a초기화§f되었습니다.");
                player.closeInventory();
                return;
            case "SS":
                message = GachaSystem.getMainGui().getGradeColor("SS")+"SS보상§f을 획득하셨습니다.";
                break;
            case "S":
                message = GachaSystem.getMainGui().getGradeColor("S")+"§5S보상§f을 획득하셨습니다.";
                break;
            case "A":
                message = GachaSystem.getMainGui().getGradeColor("A")+"§bA보상§f을 획득하셨습니다.";
                break;
            case "B":
                message = GachaSystem.getMainGui().getGradeColor("B")+"§aB보상§f을 획득하셨습니다.";
                break;
            case "C":
                message = GachaSystem.getMainGui().getGradeColor("C")+"§7C보상§f을 획득하셨습니다.";
                break;
        }
        player.sendMessage(message);
        GachaSystem.getMainGui().openMainGUI(player);
    }



}