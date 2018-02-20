/*
 * Copyright (c) 2018. Weilon Ying. All Rights Reserved. This software is provided as is, without warranty,
 * to the fullest extent of the law. You may freely copy, distribute and make derivatives of this work
 * for commercial and non-commercial purposes provided that you give credit to the author.
 */

package com.weilonying.moreslimes.services;

import com.weilonying.moreslimes.PluginHelper;

/**
 * Services are responsible for running a specific function of a plugin
 */
public abstract class Service {
    private final PluginHelper mPluginHelper;
    /**
     * Construct service and add self to a service list. DO NOT run any logic that requires
     * use of other services in the constructor. Do that in your implementation of {@link #enable()}
     * @param pluginHelper Plugin helper
     */
    public Service(PluginHelper pluginHelper) {
        mPluginHelper = pluginHelper;
    }

    protected final PluginHelper getPluginHelper() { return mPluginHelper; }

    /**
     * Called after all services are initialised. Called on plugin start or after reload.
     */
    public abstract void enable();

    /**
     * Called when plugin is to be stopped or reloaded.
     */
    public abstract void disable();
}
