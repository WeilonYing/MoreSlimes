/*
 * Copyright (c) 2018. Weilon Ying. All Rights Reserved. This software is provided as is, without warranty,
 * to the fullest extent of the law. You may freely copy, distribute and make derivatives of this work
 * for commercial and non-commercial purposes provided that you give credit to the author.
 */

package com.weilonying.moreslimes;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import com.weilonying.moreslimes.services.*;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Helper class to set up and run the plugin
 */
public class PluginHelper {
    private JavaPlugin mJavaPlugin;
    private ClassToInstanceMap<Service> mServiceMap;

    public PluginHelper(JavaPlugin plugin) {
        mJavaPlugin = plugin;
        initServices();
    }

    /**
     * Initialise the service classes
     */
    public void initServices() {
        ImmutableClassToInstanceMap.Builder<Service> builder =
                ImmutableClassToInstanceMap.builder();

        mServiceMap = builder
                .put(ConfigService.class, new ConfigService(this))
                .put(CommandService.class, new CommandService(this))
                .put(SpawnSchedulerService.class, new SpawnSchedulerService(this))
                .put(SpawnerService.class, new SpawnerService(this))
                .build();
    }

    public void enableServices() {
        for (Service s : mServiceMap.values()) {
            s.enable();
        }
    }

    public void disableServices() {
        for (Service s : mServiceMap.values()) {
            s.disable();
        }
    }

    public JavaPlugin getJavaPlugin() {
        return mJavaPlugin;
    }

    public ClassToInstanceMap<Service> getServiceMap() {
        if (mServiceMap == null) {
            throw new NullPointerException("Attempted to get uninitialised service map");
        }
        return mServiceMap;
    }
}
