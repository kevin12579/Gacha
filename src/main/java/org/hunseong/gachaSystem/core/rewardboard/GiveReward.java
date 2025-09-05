package org.hunseong.gachaSystem.core.rewardboard;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.hunseong.gachaSystem.GachaSystem;


public class GiveReward {

    /**
     * 플레이어의 요청에 따라 뽑기를 수행합니다.
     * @param player 플레이어 객체
     * @param slot 클릭한 슬롯 번호 (1~36)
     */
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
                player.sendMessage("§cSSS보상§f을 획득하셨습니다. 보상판이 §a초기화§f되었습니다.");
                player.closeInventory();//TODO: 몇초간 보상판 오픈 불가 (성능상 이슈가 있다면)
                return;
            case "SS":
                message = "§6SS보상§f을 획득하셨습니다.";
                break;
            case "S":
                message = "§5S보상§f을 획득하셨습니다.";
                break;
            case "A":
                message = "§bA보상§f을 획득하셨습니다.";
                break;
            case "B":
                message = "§aB보상§f을 획득하셨습니다.";
                break;
            case "C":
                message = "§7C보상§f을 획득하셨습니다.";
                break;
        }
        player.sendMessage(message);
        GachaSystem.getMainGui().openMainGUI(player);
    }



}