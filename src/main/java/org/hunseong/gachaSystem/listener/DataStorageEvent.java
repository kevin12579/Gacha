package org.hunseong.gachaSystem.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.hunseong.gachaSystem.database.DatabaseManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataStorageEvent implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        String title = inventory.getViewers().get(0).getOpenInventory().getTitle();

        if (!title.contains("가차 보상설정 - ")) return;

        String grade = title.replace("가차 보상설정 - ", "");
        List<ItemStack> gradeItems = new ArrayList<>();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && item.getType() != Material.AIR) {
                gradeItems.add(item);
            }
        }

        if (gradeItems.isEmpty()) {
            deleteRewardData(grade);
            player.sendMessage("§a설정된 아이템이 없어 기존 데이터를 모두 삭제했습니다.");
        } else {
            saveRewardData(grade, gradeItems);
            player.sendMessage("§a설정을 완료했습니다.");
        }

    }
    private void saveRewardData(String grade, List<ItemStack> items) {
        //공부용 주석
        //INSERT OR REPLACE : 매번 초기화 후 다시 저장 (새로 들어온건 INSERT, 기존에 있다면 삭제 후 추가하여 갱신)
        String sql = "INSERT OR REPLACE INTO rewards (grade, data) VALUES (?, ?);";
        try (PreparedStatement statement = DatabaseManager.getInstance().getConnection().prepareStatement(sql)) {
            //공부용 주석
            //ItemStack과 같은 마인크래프트 객체를 blob형식으로 저장하기 위하여 직렬화하여 사용하기위한 메모리 통로
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                 BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

                dataOutput.writeObject(items.toArray(new ItemStack[0]));

                statement.setString(1, grade);
                statement.setBytes(2, outputStream.toByteArray());
                statement.executeUpdate();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void deleteRewardData(String grade) {
        String sql = "DELETE FROM rewards WHERE grade = ?;";
        try (PreparedStatement statement = DatabaseManager.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, grade);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

