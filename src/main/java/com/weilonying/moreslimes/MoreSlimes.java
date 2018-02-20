/*
 * Copyright (c) 2018. Weilon Ying. All Rights Reserved. This software is provided as is, without warranty,
 * to the fullest extent of the law. You may freely copy, distribute and make derivatives of this work
 * for commercial and non-commercial purposes provided that you give credit to the author.
 */

package com.weilonying.moreslimes;

import org.bukkit.plugin.java.JavaPlugin;

public class MoreSlimes extends JavaPlugin {
    private PluginHelper mPluginHelper;

    @Override
    public void onEnable() {
        mPluginHelper = new PluginHelper(this);
        mPluginHelper.enableServices();
        getLogger().info("MoreSlimes enabled");
    }

    @Override
    public void onDisable() {
        mPluginHelper.disableServices();
        getLogger().info("MoreSlimes disabled");
    }
}
