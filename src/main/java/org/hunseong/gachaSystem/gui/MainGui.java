package org.hunseong.gachaSystem.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hunseong.gachaSystem.GachaSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainGui {
    public Inventory setMainGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 9*6, "뽑기판");
        //미리보기
        inventory.setItem(0, setPreviewIcon("C"));
        inventory.setItem(1, setPreviewIcon("B"));
        inventory.setItem(2, setPreviewIcon("A"));
        inventory.setItem(6, setPreviewIcon("S"));
        inventory.setItem(7, setPreviewIcon("SS"));
        inventory.setItem(8, setPreviewIcon("SSS"));

        //가림막
        inventory.setItem(3, setBorderIcon());
        inventory.setItem(4, setBorderIcon());
        inventory.setItem(5, setBorderIcon());
        for (int i = 9; i <= 17; i++) {
            inventory.setItem(i, setBorderIcon());
        }

        //남은 등급 개수 확인 해시맵 생성
        String board = GachaSystem.getPlayerBoardData().getPlayerBoardData(player);
        Map<String, Integer> remainingGrades = new HashMap<>();
        remainingGrades.put("SSS", 0);
        remainingGrades.put("SS", 0);
        remainingGrades.put("S", 0);
        remainingGrades.put("A", 0);
        remainingGrades.put("B", 0);
        remainingGrades.put("C", 0);

        //보상판에서 언락된 보상 슬롯 리스트
        List<Integer> unlocklist = new ArrayList<>();

        //유저 보상판 데이터 조회
        JsonArray boardArray = JsonParser.parseString(board).getAsJsonArray();
        for (int i = 0; i < boardArray.size(); i++) {
            JsonObject slotObject = boardArray.get(i).getAsJsonObject();
            boolean unlocked = slotObject.get("unlocked").getAsBoolean();

            if (!unlocked) {
                String grade = slotObject.get("grade").getAsString();
                remainingGrades.put(grade, remainingGrades.get(grade) + 1);
            } else {
                unlocklist.add(i);
            }
        }

        //티켓 수 조회
        Integer ticketamount = GachaSystem.getPlayerBoardData().getPlayerTicketData(player);
        //보상 아이템
        for (int i = 18; i <= 53; i++) {
            //이미 오픈한 보상은 아이템으로 표시
            if (unlocklist.contains((i - 18))) {
                inventory.setItem(i, GachaSystem.getPlayerBoardData().getRewardItemFromSlotNum(player, i));
            } else {
                inventory.setItem(i, setRewardIcon("§e#"+(i - 17)+"번 뽑기", remainingGrades, ticketamount));
            }
        }
        return inventory;
    }

    public void openMainGUI(Player player) {
        String existingBoard = GachaSystem.getPlayerBoardData().getPlayerBoardData(player);
        if (existingBoard == null) {
            GachaSystem.getResetRewardBoard().resetRewardBoard(player);
        }
        player.openInventory(setMainGUI(player));
    }

    public ItemStack setRewardIcon(String name, Map<String, Integer> remainingGrades, Integer ticketamount) {
        Material material = Material.valueOf(GachaSystem.getInstance().getConfig().getString("main-gui.reward-icon.material", "BLACK_STAINED_GLASS_PANE"));
        int model_data = GachaSystem.getInstance().getConfig().getInt("main-gui.reward-icon.custom-model-data", 0);

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add("§f");
        lore.add("§f남은 등급 개수:");
        lore.add(String.format(getGradeColor("SSS")+"SSS등급§f: §e%d§f개", remainingGrades.get("SSS")));
        lore.add(String.format(getGradeColor("SS")+"SS등급§f: §e%d§f개", remainingGrades.get("SS")));
        lore.add(String.format(getGradeColor("S")+"S등급§f: §e%d§f개", remainingGrades.get("S")));
        lore.add(String.format(getGradeColor("A")+"A등급§f: §e%d§f개", remainingGrades.get("A")));
        lore.add(String.format(getGradeColor("B")+"B등급§f: §e%d§f개", remainingGrades.get("B")));
        lore.add(String.format(getGradeColor("C")+"C등급§f: §e%d§f개", remainingGrades.get("C")));
        lore.add("§f");
        lore.add("§e현재 보유중인 뽑기권: §a" + ticketamount + "§f개");
        lore.add("§f");
        lore.add("§b→ §f클릭으로 §6뽑기권§f을 §c소모§f하여 §a오픈§f합니다.");
        lore.add("§f");
        lore.add("§6[!] §e해당 등급 보상중에서 §b랜덤§e으로 §a1§e개가 지급됩니다.");
        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.setCustomModelData(model_data);
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack setBorderIcon() {
        Material material = Material.valueOf(GachaSystem.getInstance().getConfig().getString("main-gui.border-icon.material", "BLACK_STAINED_GLASS_PANE"));
        int model_data = GachaSystem.getInstance().getConfig().getInt("main-gui.border-icon.custom-model-data", 0);
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        List<String> lore = new ArrayList<>();
        meta.setDisplayName("");
        meta.setLore(lore);
        meta.setCustomModelData(model_data);
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack setPreviewIcon(String grade) {
        String path = "main-gui.preview-icon." + grade;
        Material material = Material.valueOf(GachaSystem.getInstance().getConfig().getString(path + ".material", "BOOK"));
        int model_data = GachaSystem.getInstance().getConfig().getInt(path + ".custom-model-data", 0);
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add("§f");
        lore.add("§f클릭 시 보상 목록을 확인합니다.");

        meta.setDisplayName(getGradeColor(grade)+grade+"등급 보상");
        meta.setLore(lore);
        meta.setCustomModelData(model_data);

        item.setItemMeta(meta);

        return item;
    }

    public String getGradeColor(String grade) {
        String path = "main-gui.preview-icon." + grade;
        return GachaSystem.getHexColor().hexFormat(GachaSystem.getInstance().getConfig().getString(path + ".grade-color", "&#ffffff"));
    }
}
