package lf.jaime.files;

import lf.jaime.LightFly;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class MessagesManager {
    private final CustomConfig langFile;


    public MessagesManager(LightFly plugin, String lang){
        langFile = new CustomConfig(lang, "lang", plugin);
        loadMessages();
    }

    private String prefix;
    private String noPermission;
    private String playerOnly;
    private String badUsageFly;
    private String flyEnabled;
    private String flyEnabledOther;
    private String flyDisabled;
    private String flyDisabledOther;
    private String unknownPlayer;
    private String badUsageOnOff;
    private List<String> helpAdmin;
    private String reload;
    private String gamemodeNotAllowed;
    private String gamemodeNotAllowedOthers;
    private String badUsageTempfly;
    private String invalidNumber;
    private String tempFlySuccess;
    private String tempFlyMessage;
    private String tempFlyWarningMessage;
    private String tempFlyFinished;

    public void loadMessages(){
        langFile.registerConfig();
        FileConfiguration messages = langFile.getConfig();
        prefix = messages.getString("prefix");
        noPermission = messages.getString("no_permission");
        playerOnly = messages.getString("player_only");
        badUsageFly = messages.getString("bad_usage_fly");
        flyEnabled = messages.getString("fly_enabled");
        flyEnabledOther = messages.getString("fly_enabled_other");
        flyDisabled = messages.getString("fly_disabled");
        flyDisabledOther = messages.getString("fly_disabled_other");
        unknownPlayer = messages.getString("unknown_player");
        badUsageOnOff = messages.getString("bad_usage_on_off");
        helpAdmin = messages.getStringList("help_admin");
        reload = messages.getString("reload");
        gamemodeNotAllowed = messages.getString("gamemode_not_allowed");
        gamemodeNotAllowedOthers = messages.getString("gamemode_not_allowed_others");
        badUsageTempfly = messages.getString("bad_usage_tempfly");
        invalidNumber = messages.getString("invalid_number");
        tempFlySuccess = messages.getString("temp_fly_success");
        tempFlyMessage = messages.getString("temp_fly_message");
        tempFlyWarningMessage = messages.getString("temp_fly_warning_message");
        tempFlyFinished = messages.getString("temp_fly_finished");
    }

    public String getInvalidNumber() {
        return invalidNumber;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getNoPermission() {
        return noPermission;
    }

    public String getPlayerOnly() {
        return playerOnly;
    }

    public String getBadUsageFly(){
        return badUsageFly;
    }

    public String getFlyEnabled() {
        return flyEnabled;
    }

    public String getFlyEnabledOther() {
        return flyEnabledOther;
    }

    public String getFlyDisabled() {
        return flyDisabled;
    }

    public String getFlyDisabledOther() {
        return flyDisabledOther;
    }

    public String getUnknownPlayer() {
        return unknownPlayer;
    }

    public String getBadUsageOnOff() {
        return badUsageOnOff;
    }

    public List<String> getHelpAdmin() {
        return helpAdmin;
    }

    public String getReload(){
        return reload;
    }

    public String getGamemodeNotAllowed(){
        return gamemodeNotAllowed;
    }

    public String getGamemodeNotAllowedOthers() {
        return gamemodeNotAllowedOthers;
    }

    public String getBadUsageTempfly() {
        return badUsageTempfly;
    }

    public String getTempFlySuccess() {
        return tempFlySuccess;
    }

    public String getTempFlyMessage() {
        return tempFlyMessage;
    }

    public String getTempFlyWarningMessage() {
        return tempFlyWarningMessage;
    }

    public String getTempFlyFinished() {
        return tempFlyFinished;
    }
}
