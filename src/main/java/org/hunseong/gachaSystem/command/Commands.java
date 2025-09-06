package org.hunseong.gachaSystem.command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.hunseong.gachaSystem.GachaSystem;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Commands implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("플레이어만 사용 가능한 명령어입니다.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            GachaSystem.getMainGui().openMainGUI(player);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        if (!player.hasPermission("gacha.admin")) {
            player.sendMessage("권한이 없습니다.");
            return true;
        }

        switch (subCommand) {
            case "설정":
                handleSetCommand(player, args);
                break;
            case "지급":
                handleAddCommand(player, args);
                break;
            case "차감":
                handleRemoveCommand(player, args);
                break;
            case "초기화":
                handleResetCommand(player, args);
                break;
            case "보상설정":
                handleRewardSettingCommand(player, args);
                break;
            case "reload":
                handleReloadCommand(player);
                break;
            default:
                player.sendMessage("§e/가차 설정 [플레이어] [값]");
                player.sendMessage("§e/가차 지급 [플레이어] [값]");
                player.sendMessage("§e/가차 차감 [플레이어] [값]");
                player.sendMessage("§e/가차 초기화 [플레이어]");
                player.sendMessage("§e/가차 보상설정 [C, B, A, S, SS, SSS]");
                break;
        }
        return true;
    }

    private void handleSetCommand(Player player, String[] args) {
        if (args.length != 3) {
            player.sendMessage("§e/가차 설정 [플레이어] [값]");
            return;
        }
        OfflinePlayer target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage("§c해당 플레이어를 찾을 수 없습니다.");
            return;
        }
        try {
            int amount = Integer.parseInt(args[2]);
            GachaSystem.getPlayerBoardData().setPlayerTicketData(target, amount);
            player.sendMessage("§f" + target.getName() + " 님의 뽑기 횟수를 " + amount + "으로 설정했습니다.");
        } catch (NumberFormatException e) {
            player.sendMessage("§c올바른 숫자 값을 입력해주세요.");
        }
    }

    private void handleAddCommand(Player player, String[] args) {
        if (args.length != 3) {
            player.sendMessage("§e/가차 지급 [플레이어] [값]");
            return;
        }
        OfflinePlayer target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage("§c해당 플레이어를 찾을 수 없습니다.");
            return;
        }
        try {
            int amount = Integer.parseInt(args[2]);
            int currentTickets = GachaSystem.getPlayerBoardData().getPlayerTicketData(target);
            GachaSystem.getPlayerBoardData().setPlayerTicketData(target, currentTickets + amount);
            player.sendMessage("§a" + target.getName() + " 님에게 뽑기 횟수 " + amount + "개를 지급했습니다.");
        } catch (NumberFormatException e) {
            player.sendMessage("§c올바른 숫자 값을 입력해주세요.");
        }
    }

    private void handleRemoveCommand(Player player, String[] args) {
        if (args.length != 3) {
            player.sendMessage("§e/가차 차감 [플레이어] [값]");
            return;
        }
        OfflinePlayer target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage("§c해당 플레이어를 찾을 수 없습니다.");
            return;
        }
        try {
            int amount = Integer.parseInt(args[2]);
            int currentTickets = GachaSystem.getPlayerBoardData().getPlayerTicketData(target);
            int newTickets = Math.max(0, currentTickets - amount); // 0 미만으로 내려가지 않도록 처리
            GachaSystem.getPlayerBoardData().setPlayerTicketData(target, newTickets);
            player.sendMessage("§a" + target.getName() + " 님의 뽑기 횟수 " + amount + "개를 차감했습니다.");
        } catch (NumberFormatException e) {
            player.sendMessage("§c올바른 숫자 값을 입력해주세요.");
        }
    }

    private void handleResetCommand(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage("§e/가차 초기화 [플레이어]");
            return;
        }
        OfflinePlayer target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage("§c해당 플레이어를 찾을 수 없습니다.");
            return;
        }
        GachaSystem.getResetRewardBoard().resetRewardBoard(target);
        player.sendMessage("§a" + target.getName() + " 님의 보상판을 초기화했습니다.");
    }

    private void handleRewardSettingCommand(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage("§e/가차 보상설정 [C, B, A, S, SS, SSS]");
            return;
        }
        String grade = args[1].toUpperCase();
        if (Arrays.asList("C", "B", "A", "S", "SS", "SSS").contains(grade)) {
            GachaSystem.getRewardSettingGui().openRewardSettingGui(player, grade);
        } else {
            player.sendMessage("§c| §f올바른 등급을 입력해주세요.");
            player.sendMessage("§6| §e/가차 보상설정 [C, B, A, S, SS, SSS]");
        }
    }

    private void handleReloadCommand(Player player) {
        GachaSystem.getInstance().reloadConfig();
        player.sendMessage("§aconfig 파일을 리로드했습니다.");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && sender.hasPermission("gacha.admin")) {
            return Arrays.asList("설정", "지급", "차감", "초기화", "보상설정", "reload").stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("설정") || args[0].equalsIgnoreCase("지급") || args[0].equalsIgnoreCase("차감") || args[0].equalsIgnoreCase("초기화")) {
                return null; // 모든 플레이어 이름 자동완성
            } else if (args[0].equalsIgnoreCase("보상설정")) {
                return Arrays.asList("C", "B", "A", "S", "SS", "SSS").stream()
                        .filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList(); // 빈 리스트 반환
    }
}