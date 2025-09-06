package org.hunseong.gachaSystem;

import org.bukkit.entity.Item;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.hunseong.gachaSystem.command.Commands;
import org.hunseong.gachaSystem.core.player.PlayerBoardData;
import org.hunseong.gachaSystem.core.rewardboard.GiveReward;
import org.hunseong.gachaSystem.core.rewardboard.GradeReward;
import org.hunseong.gachaSystem.core.rewardboard.ResetRewardBoard;
import org.hunseong.gachaSystem.database.DatabaseManager;
import org.hunseong.gachaSystem.gui.MainGui;
import org.hunseong.gachaSystem.gui.RewardPreviewGui;
import org.hunseong.gachaSystem.gui.RewardSettingGui;
import org.hunseong.gachaSystem.listener.DataStorageEvent;
import org.hunseong.gachaSystem.listener.ClickIconEvent;
import org.hunseong.gachaSystem.util.ItemBase64;

public final class GachaSystem extends JavaPlugin implements Listener{

    private static GachaSystem instance;

    private static MainGui mainGui;
    private static RewardSettingGui rewardSettingGui;
    private static RewardPreviewGui rewardPreviewGui;

    private static GiveReward giveReward;
    private static GradeReward gradeReward;
    private static ResetRewardBoard resetRewardBoard;

    private static PlayerBoardData playerBoardData;

    private static ItemBase64 itemBase64;

    @Override
    public void onEnable() {
        instance = this;
        mainGui = new MainGui();
        rewardSettingGui = new RewardSettingGui();
        rewardPreviewGui = new RewardPreviewGui();
        giveReward = new GiveReward();
        gradeReward = new GradeReward();
        resetRewardBoard = new ResetRewardBoard();
        playerBoardData = new PlayerBoardData();
        itemBase64 = new ItemBase64();
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new DataStorageEvent(), this);
        getServer().getPluginManager().registerEvents(new ClickIconEvent(), this);

        getCommand("가차").setExecutor(new Commands());
        getCommand("가차").setTabCompleter(new Commands());

        DatabaseManager.getInstance();
        getLogger().info("가차 플러그인 활성화");
    }

    @Override
    public void onDisable() {
        DatabaseManager.getInstance().closeConnection();
        getLogger().info("건차 플러그인 비활성화.");
    }

    public void executeAsync(Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(this, task);
    }

    public static GachaSystem getInstance(){
        return instance;
    }

    public static MainGui getMainGui() {
        return mainGui;
    }

    public static RewardPreviewGui getRewardPreviewGui() {
        return rewardPreviewGui;
    }

    public static RewardSettingGui getRewardSettingGui() {
        return rewardSettingGui;
    }

    public static GiveReward getGiveReward() {
        return giveReward;
    }
    public static GradeReward getGradeReward() {
        return gradeReward;
    }
    public static ResetRewardBoard getResetRewardBoard() {
        return resetRewardBoard;
    }

    public static PlayerBoardData getPlayerBoardData() {
        return playerBoardData;
    }

    public static ItemBase64 getItemBase64() {
        return itemBase64;
    }
}
