package lf.jaime.files;

import lf.jaime.LightFly;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {
    private final CustomConfig configFile;
    private LightFly plugin;
    private boolean preventInGamemode;
    private boolean sendTempFlyMessage;
    private String lang;
    private boolean sendTempFlyWarning;
    private List<Integer> tempFlyWarningTimes;
    private boolean sendTempFlyFinished;

    private boolean tempFlyOffline;
    private boolean keepTempFlyOnWorldChange;


    public ConfigManager(LightFly plugin){
        configFile = new CustomConfig("config.yml", null, plugin);
        LoadConfig();
    }

    public void LoadConfig(){
        configFile.registerConfig();
        FileConfiguration fileConfiguration = configFile.getConfig();
        lang = fileConfiguration.getString("lang");
        preventInGamemode = fileConfiguration.getBoolean("prevent_fly_in_creative");
        sendTempFlyMessage = fileConfiguration.getBoolean("send_temp_fly_message");
        sendTempFlyWarning = fileConfiguration.getBoolean("send_temp_fly_warning");
        tempFlyWarningTimes = fileConfiguration.getIntegerList("time_warning_seconds");
        sendTempFlyFinished = fileConfiguration.getBoolean("send_temp_fly_finished");
        tempFlyOffline = fileConfiguration.getBoolean("temp_fly_time_offline", true);
        keepTempFlyOnWorldChange = fileConfiguration.getBoolean("keep_temp_fly_on_world_change", true);
    }

    public boolean isPreventInGamemode() {
        return preventInGamemode;
    }

    public String getLang() {
        return lang;
    }

    public boolean isSendTempFlyMessage() {
        return sendTempFlyMessage;
    }

    public List<Integer> getTempFlyWarningTimes() {
        return tempFlyWarningTimes;
    }

    public boolean shouldSendTempFlyWarning() {
        return sendTempFlyWarning;
    }

    public boolean isSendTempFlyFinished() {
        return sendTempFlyFinished;
    }

    public boolean isKeepTempFlyOnWorldChange() {
        return keepTempFlyOnWorldChange;
    }

    public boolean isTempFlyOffline() {
        return tempFlyOffline;
    }
}
