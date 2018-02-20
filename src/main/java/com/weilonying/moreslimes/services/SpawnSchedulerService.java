/*
 * Copyright (c) 2018. Weilon Ying. All Rights Reserved. This software is provided as is, without warranty,
 * to the fullest extent of the law. You may freely copy, distribute and make derivatives of this work
 * for commercial and non-commercial purposes provided that you give credit to the author.
 */

package com.weilonying.moreslimes.services;

import com.weilonying.moreslimes.PluginHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import java.util.Collection;

/**
 * This class is responsible for scheduling the spawning of slimes
 */
public class SpawnSchedulerService extends Service {
    private boolean mStarted = false;
    private BukkitTask mTask;

    public SpawnSchedulerService(PluginHelper pluginHelper) {
        super(pluginHelper);
    }

    @Override
    public void enable() {
        if (!mStarted) {
            mTask = Bukkit.getServer().getScheduler()
                    .runTaskTimer(getPluginHelper().getJavaPlugin(), getSpawnRunner(), 0, 10);
            mStarted = true;
        }
    }

    @Override
    public void disable() {
        if (mStarted) {
            mTask.cancel();
            mStarted = false;
        }
    }

    private Runnable getSpawnRunner() {
        return new Runnable() {
            @Override
            public void run() {
                SpawnerService spawnerService = getPluginHelper()
                        .getServiceMap()
                        .getInstance(SpawnerService.class);
                Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
                for (Player p : players) {
                    spawnerService.attemptSpawnSlimesNearPlayer(p);
                }
            }
        };
    }
}
