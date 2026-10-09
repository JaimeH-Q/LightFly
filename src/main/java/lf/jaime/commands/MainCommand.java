package lf.jaime.commands;

import lf.jaime.LightFly;
import lf.jaime.files.MessagesManager;
import lf.jaime.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {
    private final LightFly plugin;
    public MainCommand(LightFly plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args){
        MessagesManager messages = plugin.getMessagesManager();
        if(!sender.hasPermission("lf.admin")){
            sender.sendMessage(MessageUtils.getMessage(messages.getPrefix() + messages.getNoPermission()));
            return true;
        }
        if(args.length == 0){
            sendHelpMessage(sender);
            return true;
        }
        if(args[0].equalsIgnoreCase("reload")){
            plugin.reloadPlugin();
            sender.sendMessage(MessageUtils.getMessage(messages.getPrefix() + messages.getReload()));
            return true;
        }
        if(args[0].equalsIgnoreCase("version")){
            String version = plugin.getDescription().getVersion();
            sender.sendMessage(MessageUtils.getMessage(messages.getPrefix() + version));
        }
        return true;
    }

    private void sendHelpMessage(CommandSender sender){
        MessagesManager messages = plugin.getMessagesManager();
        for(String m: messages.getHelpAdmin()){
            sender.sendMessage(MessageUtils.getMessage(m));
        }
    }
}
