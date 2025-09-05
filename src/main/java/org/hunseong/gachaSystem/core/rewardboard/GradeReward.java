package org.hunseong.gachaSystem.core.rewardboard;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.hunseong.gachaSystem.database.DatabaseManager;

import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GradeReward {

    public List<ItemStack> getGradeRewards(String grade) {
        List<ItemStack> items = new ArrayList<>();
        String sql = "SELECT data FROM rewards WHERE grade = ?;";
        try (PreparedStatement statement = DatabaseManager.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, grade);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    byte[] rawData = resultSet.getBytes("data");

                    ByteArrayInputStream inputStream = new ByteArrayInputStream(rawData);
                    BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

                    ItemStack[] itemArray = (ItemStack[]) dataInput.readObject();
                    dataInput.close();

                    items = Arrays.asList(itemArray);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

}
