package org.hunseong.gachaSystem.core.rewardboard;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.hunseong.gachaSystem.GachaSystem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class ResetRewardBoard {
    //final, Map.of -> 불변 데이터 리스트 맵핑
    private final Map<String, Integer> gradeDistribution = Map.of(
            "SSS", 1, "SS", 3, "S", 5, "A", 7, "B", 9, "C", 11
    );

    //보상판 리셋
    public void resetRewardBoard(OfflinePlayer player) {
        //데이터 섞기
        List<String> grades = new ArrayList<>(); //List 가 더 포괄적이라 ArrayList, LinkedList 사용가능, List 메서드 사용
        gradeDistribution.forEach((grade, count) -> {
            for (int i = 0; i < count; i++) {
                grades.add(grade); //["SSS", "SS", "SS", "SS", "S", "S", "S", "S", "S", "A", ... (36개)]
            }
        });
        Collections.shuffle(grades); //리스트 내부 섞기

        //data 저장을 위한 json 변환
        Gson gson = new Gson();
        JsonArray boardArray = new JsonArray();

        for (String grade : grades) {
            JsonObject slot = new JsonObject(); //ex) {"grade":"A", "unlocked":false, "item":"<아이템 데이터>"}
            slot.addProperty("grade", grade);
            slot.addProperty("unlocked", false);

            //아이템 랜덤 선택 TODO: 일회성 동시 다발적 데이터베이스 쿼리 접근
            List<ItemStack> items = GachaSystem.getGradeReward().getGradeRewards(grade);

            if (items == null || items.isEmpty()) {
                // 해당 등급 아이템 미설정 시, 슬롯을 비우고 다음으로 넘어감
                slot.addProperty("item", "");
                boardArray.add(slot);
                continue;
            }

            Random random = new Random();
            int randomIndex = random.nextInt(items.size());
            ItemStack item = items.get(randomIndex);

            slot.addProperty("item", GachaSystem.getItemBase64().ItemEncodingBase64(item));
            boardArray.add(slot);
        }
        GachaSystem.getPlayerBoardData().savePlayerBoardData(player, gson.toJson(boardArray));
    }


}
