
package lf.jaime.utils;

import lf.jaime.LightFly;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import static lf.jaime.utils.MessageUtils.getMessage;

public class PlayerTimeManager implements Listener {

    private final Map<UUID, Integer> playerTimers = new HashMap<>();
    private final LightFly plugin;
    private final BukkitTask timerTask;

    public PlayerTimeManager(LightFly plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.timerTask = Bukkit.getScheduler().runTaskTimer(
                plugin,
                this::tickTimers,
                20L,
                20L
        );
    }

    private void tickTimers() {
        boolean isTempFlyOffline =
                plugin.getConfigManager().isTempFlyOffline();

        Iterator<Map.Entry<UUID, Integer>> iterator =
                playerTimers.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, Integer> entry = iterator.next();

            UUID uuid = entry.getKey();
            int timeLeft = entry.getValue();

            Player player = Bukkit.getPlayer(uuid);

            // Pause the timer while the player is offline.
            if (player == null && !isTempFlyOffline) {
                continue;
            }

            if (timeLeft <= 1) {
                iterator.remove();

                if (player != null) {
                    sendTempFlyFinishedMessage(player);
                    player.setAllowFlight(false);
                }

                continue;
            }

            checkForTimeWarning(timeLeft, player);

            entry.setValue(timeLeft - 1);

            if (player != null) {
                player.setAllowFlight(true);
            }
        }
    }

    private void sendTempFlyFinishedMessage(Player player) {
        if (!plugin.getConfigManager().isSendTempFlyFinished()) {
            return;
        }

        String message = plugin.getMessagesManager().getPrefix()
                + plugin.getMessagesManager().getTempFlyFinished();

        player.sendMessage(getMessage(message));
    }

    private void checkForTimeWarning(int timeLeft, Player player) {
        if (player == null
                || !plugin.getConfigManager().shouldSendTempFlyWarning()) {
            return;
        }

        if (plugin.getConfigManager()
                .getTempFlyWarningTimes().contains(timeLeft)) {

            String message = plugin.getMessagesManager().getPrefix()
                    + plugin.getMessagesManager()
                    .getTempFlyWarningMessage();

            player.sendMessage(getMessage(
                    message.replace("%time%", String.valueOf(timeLeft))
            ));
        }
    }

    public void addPlayer(Player player, int seconds) {
        playerTimers.put(player.getUniqueId(), seconds);
        player.setAllowFlight(true);
    }

    public void removePlayer(Player player) {
        playerTimers.remove(player.getUniqueId());
    }

    public int getRemainingTime(Player player) {
        return playerTimers.getOrDefault(player.getUniqueId(), 0);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (playerTimers.containsKey(player.getUniqueId())) {
            player.setAllowFlight(true);
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!playerTimers.containsKey(uuid)) {
            return;
        }

        boolean shouldKeepTempFly =
                plugin.getConfigManager().isKeepTempFlyOnWorldChange();

        if (shouldKeepTempFly) {
            player.setAllowFlight(true);
        } else {
            player.setAllowFlight(false);
            playerTimers.remove(uuid);
        }
    }

    public void shutdown() {
        timerTask.cancel();
    }
}
