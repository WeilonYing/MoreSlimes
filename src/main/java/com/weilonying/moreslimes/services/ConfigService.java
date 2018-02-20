/*
 * Copyright (c) 2018. Weilon Ying. All Rights Reserved. This software is provided as is, without warranty,
 * to the fullest extent of the law. You may freely copy, distribute and make derivatives of this work
 * for commercial and non-commercial purposes provided that you give credit to the author.
 */

package com.weilonying.moreslimes.services;

import com.weilonying.moreslimes.PluginHelper;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class ConfigService extends Service {
    // config param name constants
    public static final String SPAWN_ATTEMPT_CHANCE_PER_TICK = "spawn_attempt_chance_per_tick";
    public static final String SPAWN_MIN_AMOUNT_PER_SUCCESSFUL_ATTEMPT =
            "spawn_min_amount_per_successful_attempt";
    public static final String SPAWN_MAX_AMOUNT_PER_SUCCESSFUL_ATTEMPT =
            "spawn_max_amount_per_successful_attempt";

    public static final double SPAWN_ATTEMPT_CHANCE_PER_TICK_DEFAULT = 0.01D;
    public static final int SPAWN_MIN_AMOUNT_PER_SUCCESSFUL_ATTEMPT_DEFAULT = 1;
    public static final int SPAWN_MAX_AMOUNT_PER_SUCCESSFUL_ATTEMPT_DEFAULT = 3;

    // store config values in map for faster access
    private HashMap<String, Object> mConfigMap = createDefaultConfigMap();
    private static final HashMap<String, Object> mDefaultConfigMap = createDefaultConfigMap();

    private FileConfiguration mConfig;

    public ConfigService(PluginHelper pluginHelper) {
        super(pluginHelper);
        mConfig = getPluginHelper().getJavaPlugin().getConfig();
    }

    /**
     * Create config hashmap initialised with default values
     * @return Hash map with default values initialised
     */
    private static HashMap<String, Object> createDefaultConfigMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(SPAWN_ATTEMPT_CHANCE_PER_TICK, SPAWN_ATTEMPT_CHANCE_PER_TICK_DEFAULT);
        map.put(SPAWN_MIN_AMOUNT_PER_SUCCESSFUL_ATTEMPT, SPAWN_MIN_AMOUNT_PER_SUCCESSFUL_ATTEMPT_DEFAULT);
        map.put(SPAWN_MAX_AMOUNT_PER_SUCCESSFUL_ATTEMPT, SPAWN_MAX_AMOUNT_PER_SUCCESSFUL_ATTEMPT_DEFAULT);
        return map;
    }

    @Override
    public void enable() {
        getPluginHelper().getJavaPlugin().reloadConfig(); // Called in case plugin was reloaded
        mConfig = getPluginHelper().getJavaPlugin().getConfig();
        setConfigDefaults();
        retrieveConfigFromFile();
    }

    @Override
    public void disable() {
        // do nothing
    }

    /**
     * Set default configuration values if they don't exist yet
     */
    public void setConfigDefaults() {
        for (String val : mConfigMap.keySet()) {
            mConfig.addDefault(val, mConfigMap.get(val));
        }
    }

    /**
     * Get configuration values from config file and put it into the config map
     */
    public void retrieveConfigFromFile() {
        for (String val : mConfigMap.keySet()) {
            mConfigMap.put(val, mConfig.get(val));
        }
    }

    /**
     * Retrieve value of config param if parameter exists.
     * Throws {@link IndexOutOfBoundsException} if it doesn't exist.
     *
     * @param param The config parameter to get the value of
     * @return The value of the specified parameter
     */
    public Object getConfigValue(String param) {
        if (!mConfigMap.containsKey(param)) {
            throw new IndexOutOfBoundsException("Parameter " + param + " does not exist in config");
        }
        return mConfigMap.get(param);
    }

    /**
     * Set value of config parameter if parameter exists.
     * Throws {@link IndexOutOfBoundsException} if it doesn't exist.
     *
     * @param param The config parameter to set the value for
     * @param val The value to set the parameter to
     */
    public void setConfigValue(String param, Object val) {
        if (!mConfigMap.containsKey(param)) {
            throw new IndexOutOfBoundsException("Parameter " + param + " does not exist in config");
        }
        mConfigMap.put(param, val);
        mConfig.set(param, val);
        getPluginHelper().getJavaPlugin().saveConfig();
    }

    public void resetConfigValue(String param) {
        if (!mConfigMap.containsKey(param)) {
            throw new IndexOutOfBoundsException("Parameter " + param + " does not exist in config");
        }
        mConfigMap.put(param, mDefaultConfigMap.get(param));
    }

}
