package org.hunseong.gachaSystem.core.player;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.hunseong.gachaSystem.GachaSystem;
import org.hunseong.gachaSystem.database.DatabaseManager;

import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class PlayerBoardData {
    public String getPlayerBoardData(OfflinePlayer player) {

        String sql = "SELECT data FROM player_reward_board WHERE player_uuid = ?";
        try (PreparedStatement statement = DatabaseManager.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, player.getUniqueId().toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("data");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 데이터가 없을 경우
    }

    public void savePlayerBoardData(OfflinePlayer player, String boardData) {
        int currentTickets = getPlayerTicketData(player);
        String sql = "INSERT OR REPLACE INTO player_reward_board (player_uuid, data, ticket) VALUES (?, ?, ?)";
        try (PreparedStatement statement = DatabaseManager.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, boardData);
            statement.setInt(3, currentTickets);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Integer getPlayerTicketData(OfflinePlayer player) {
        String sql = "SELECT ticket from player_reward_board where player_uuid = ?";
        try (PreparedStatement statement = DatabaseManager.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, player.getUniqueId().toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("ticket");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setPlayerTicketData(OfflinePlayer player, int ticket) {
        String sql = "UPDATE player_reward_board SET ticket = ? WHERE player_uuid = ?";
        try (PreparedStatement statement = DatabaseManager.getInstance().getConnection().prepareStatement(sql)) {
            statement.setInt(1, ticket);
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ItemStack getRewardItemFromSlotNum(Player player, int slot) {
        try {
            String boardDataJson = getPlayerBoardData(player);
            if (boardDataJson == null || boardDataJson.isEmpty()) {
                player.sendMessage("§c보상판 데이터가 존재하지 않습니다.");
                return null;
            }
            //JSON 문자열을 배열로 파싱
            JsonArray boardData = JsonParser.parseString(boardDataJson).getAsJsonArray();
            //GUI 슬롯을 JSON 배열 인덱스로 변환 (18~53 -> 0~35)
            int jsonIndex = slot - 18;
            //슬롯의 객체 가져오기
            JsonObject slotObject  = boardData.get(jsonIndex).getAsJsonObject();
            //String 변환
            if (!slotObject.has("item") || slotObject.get("item").isJsonNull()) {
                player.sendMessage("§c해당 슬롯에 아이템 데이터가 없습니다.");
                return null;
            }
            String encodedItem = slotObject.get("item").getAsString();

            return GachaSystem.getItemBase64().ItemDecodingBase64(encodedItem);
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage("§c아이템 데이터를 불러오는 중 오류가 발생했습니다.");
            return null;
        }
    }


    public String getGradeFromSlotNum(Player player, int slot) {
        String boardJson = getPlayerBoardData(player);
        try {
            JsonArray boardData = JsonParser.parseString(boardJson).getAsJsonArray();
            int jsonIndex = slot - 18;

            JsonObject slotObject = boardData.get(jsonIndex).getAsJsonObject();
            return slotObject.get("grade").getAsString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateSlotUnlockedStatus(Player player, int slot) {
        Gson gson = new Gson();
        String boardJson = getPlayerBoardData(player);
        try {
            JsonArray boardArray = JsonParser.parseString(boardJson).getAsJsonArray();
            int jsonIndex = slot - 18;

            if (jsonIndex < 0 || jsonIndex >= boardArray.size()) {
                return;
            }

            JsonObject slotObject = boardArray.get(jsonIndex).getAsJsonObject();
            slotObject.addProperty("unlocked", true);

            String updatedBoardJson = gson.toJson(boardArray);

            savePlayerBoardData(player, updatedBoardJson);

        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage("§c보상판 데이터 업데이트 중 오류가 발생했습니다.");
        }
    }

    public List<Integer> getlockedSlots(Player player) {
        String boardJson = getPlayerBoardData(player);
        if (boardJson == null) {
            return new ArrayList<>(); // 데이터가 없으면 빈 리스트 반환
        }
        List<Integer> unlockedlist = new ArrayList<>();
        try {
            JsonArray boardArray = JsonParser.parseString(boardJson).getAsJsonArray();
            for (int i = 0; i < boardArray.size(); i++) {
                JsonObject slotObject = boardArray.get(i).getAsJsonObject();
                boolean unlocked = slotObject.get("unlocked").getAsBoolean();
                if (!unlocked) {
                    unlockedlist.add(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return unlockedlist;
    }
}
