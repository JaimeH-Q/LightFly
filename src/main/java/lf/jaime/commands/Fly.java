package lf.jaime.commands;

import lf.jaime.LightFly;
import lf.jaime.files.MessagesManager;
import lf.jaime.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static lf.jaime.utils.MessageUtils.getMessage;

public class Fly implements CommandExecutor {
    private final LightFly plugin;
    public Fly(LightFly plugin){ this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args){
        MessagesManager messages = plugin.getMessagesManager();
        boolean preventFlyInCreative = plugin.getConfigManager().isPreventInGamemode();

        String prefix = messages.getPrefix();
        Player player;

        if(args.length == 0){
            if (!sender.hasPermission("lf.admin") &&
                    !sender.hasPermission("lf.fly.*") &&
                    !sender.hasPermission("lf.fly")) {

                sender.sendMessage(getMessage(prefix + messages.getNoPermission()));
                return true;
            }

            if(!isPlayer(sender)){
                sender.sendMessage(getMessage(messages.getPrefix() + messages.getBadUsageFly()));
                return true;
            }
            player = (Player) sender;
            if(player.getGameMode() == GameMode.CREATIVE && preventFlyInCreative){
                player.sendMessage(getMessage(prefix + messages.getGamemodeNotAllowed()));
                return true;
            }

            boolean result = alternateFly(player);
            if(result){
                player.sendMessage(getMessage(prefix + messages.getFlyEnabled()));

            } else {
                player.sendMessage(getMessage(prefix + messages.getFlyDisabled()));
            }
            return true;
        }
        if(args.length == 1){
            if (!sender.hasPermission("lf.admin") &&
                    !sender.hasPermission("lf.fly.*") &&
                    !sender.hasPermission("lf.fly")
                    ) {

                sender.sendMessage(getMessage(prefix + messages.getNoPermission()));
                return true;
            }


            if(isValidFlyState(args[0]) && sender instanceof Player playerSender){
                toggleFlightSelf(playerSender, args[0].equals("on"));
                return true;
            } else if (!sender.hasPermission("lf.fly.others")){
                sender.sendMessage(getMessage(prefix + messages.getNoPermission()));
                return true;
            }

            player = Bukkit.getPlayer(args[0]);
            if(player == null){
                sender.sendMessage(getMessage(prefix + messages.getUnknownPlayer().replace("%player%", args[0])));
                return true;
            }
            if(player.getGameMode() == GameMode.CREATIVE && preventFlyInCreative){
                player.sendMessage(getMessage(prefix + messages.getGamemodeNotAllowedOthers()
                        .replace("%player%", player.getName())));
                return true;
            }
            boolean enable = !player.getAllowFlight();
            toggleFlightOther(player, sender, enable);
            return true;
        }

        if(args.length == 2){
            if (!sender.hasPermission("lf.admin") &&
                    !sender.hasPermission("lf.fly.*") &&
                    !sender.hasPermission("lf.fly") &&
                    !sender.hasPermission("lf.fly.others")) {

                sender.sendMessage(getMessage(prefix + messages.getNoPermission()));
                return true;
            }

            player = Bukkit.getPlayer(args[0]);
            if(player == null){
                sender.sendMessage(getMessage(prefix + messages.getUnknownPlayer().replace("%player%", args[0])));
                return true;
            }
            if(player.getGameMode() == GameMode.CREATIVE && preventFlyInCreative){
                player.sendMessage(getMessage(prefix + messages.getGamemodeNotAllowedOthers()
                        .replace("%player%", player.getName())));
                return true;
            }

            if(!isValidFlyState(args[1])){
                sender.sendMessage(getMessage(prefix + messages.getBadUsageOnOff()));
                return true;
            }

            boolean bool = args[1].equals("on");
            toggleFlightOther(player, sender, bool);
        }
        return true;
    }

    private boolean isValidFlyState(String state) {
        return state.equals("on") || state.equals("off");
    }

    private void toggleFlightOther(Player player, CommandSender sender, boolean enable) {
        MessagesManager messages = plugin.getMessagesManager();

        String playerMessage = enable ? messages.getFlyEnabled() : messages.getFlyDisabled();
        String senderMessage = enable ? messages.getFlyEnabledOther() : messages.getFlyDisabledOther();

        if (enable && !player.getAllowFlight()) {
            player.sendMessage(getMessage(messages.getPrefix() + playerMessage));
        } else if (!enable && player.getAllowFlight()) {
            player.sendMessage(getMessage(messages.getPrefix() + playerMessage));
        }

        sender.sendMessage(getMessage(messages.getPrefix() + senderMessage
                .replace("%player%", player.getName())));
        player.setAllowFlight(enable);
    }

    private void toggleFlightSelf(Player player, boolean enable){
        MessagesManager messages = plugin.getMessagesManager();
        String playerMessage = enable ? messages.getFlyEnabled() : messages.getFlyDisabled();
        player.sendMessage(getMessage(messages.getPrefix() + playerMessage));
        player.setAllowFlight(enable);
    }

    public static boolean alternateFly(Player player){
        if(player.getAllowFlight()){
            player.setAllowFlight(false);
            return false;
        } else {
            player.setAllowFlight(true);
            return true;
        }
    }

    public static Boolean isPlayer(CommandSender sender){
        return sender instanceof Player;
    }
}
