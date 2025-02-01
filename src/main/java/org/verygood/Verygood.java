package org.verygood;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Verygood extends JavaPlugin {

    private int updateFrequency = 100; // 基础更新频率（100 tick = 5秒）
    private int maxPlayersThreshold = 20; // 玩家数量阈值，超过此值时降低更新频率
    private int minPlayersThreshold = 0; // 玩家数量阈值，低于此值时提高更新频率
    private int highLoadFrequency = 200; // 高负载时的更新频率（200 tick = 10秒）
    private int lowLoadFrequency = 50; // 低负载时的更新频率（50 tick = 2.5秒）

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("By TNTXZ");
        // 注册定时任务
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                // 动态调整更新频率
                adjustUpdateFrequency();
                // 更新玩家的生命值和饥饿值
                updatePlayerStats();
            }
        }, 0L, updateFrequency); // 初始更新频率
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void adjustUpdateFrequency() {
        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        if (onlinePlayers > maxPlayersThreshold) {
            // 高负载时降低更新频率
            updateFrequency = highLoadFrequency;
        } else if (onlinePlayers < minPlayersThreshold) {
            // 低负载时提高更新频率
            updateFrequency = lowLoadFrequency;
        } else {
            // 默认更新频率
            updateFrequency = 100;
        }
        // 重新设置定时任务的频率
        Bukkit.getScheduler().cancelTasks(this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                // 更新玩家的生命值和饥饿值
                updatePlayerStats();
            }
        }, 0L, updateFrequency);
    }

    private void updatePlayerStats() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getHealth() < 20) {
                player.setHealth(20); // 设置生命值为20
            }
            if (player.getFoodLevel() < 20) {
                player.setFoodLevel(20); // 设置饥饿值为20
            }
        }
    }
}