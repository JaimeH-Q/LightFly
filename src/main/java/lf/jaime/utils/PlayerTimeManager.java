package lf.jaime.utils;

import lf.jaime.LightFly;
import lf.jaime.files.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static lf.jaime.utils.MessageUtils.getMessage;

public class PlayerTimeManager {
    private final ConcurrentHashMap<Player, Integer> playerTimers = new ConcurrentHashMap<>();
    private final LightFly plugin;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public PlayerTimeManager(LightFly plugin) {
        this.plugin = plugin;
        startTimerTask();
    }

    private void startTimerTask() {
        scheduler.scheduleAtFixedRate(() -> {
            for (Player player : playerTimers.keySet()) {
                int timeLeft = playerTimers.get(player);
                try{
                    checkForTimeWarning(timeLeft, player);

                    playerTimers.put(player, timeLeft - 1);
                    player.setAllowFlight(true);
                    if (timeLeft < 1) {
                        sendTempFlyFinishedMessage(player);
                        playerTimers.remove(player);
                        player.setAllowFlight(false);
                    }
                } catch (NullPointerException e){
                    playerTimers.remove(player);
                };
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void sendTempFlyFinishedMessage(Player player) {
        if(!plugin.getConfigManager().isSendTempFlyFinished()){
            return;
        }
        String message = plugin.getMessagesManager().getPrefix()+  plugin.getMessagesManager().getTempFlyFinished();
        player.sendMessage(getMessage(message));
    }

    private void checkForTimeWarning(int timeLeft, Player player) {
        if(!plugin.getConfigManager().shouldSendTempFlyWarning()){
            return;
        }
        if(plugin.getConfigManager().getTempFlyWarningTimes().contains(timeLeft)){
            String message = plugin.getMessagesManager().getPrefix()+plugin.getMessagesManager().getTempFlyWarningMessage();
            player.sendMessage(getMessage(message
                    .replace("%time%", String.valueOf(timeLeft))));
            return;
        }


    }

    public void addPlayer(Player player, int seconds){
        playerTimers.put(player, seconds);
    }
    public void removePlayer(Player player){
        playerTimers.remove(player);
    }

    public int getRemainingTime(Player player){
        if(playerTimers.containsKey(player)){
            return playerTimers.get(player);
        }
        return 0;
    }

    public void shutdown(){
        scheduler.shutdown();
    }
}
