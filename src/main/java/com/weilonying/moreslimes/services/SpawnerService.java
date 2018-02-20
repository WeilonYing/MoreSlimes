/*
 * Copyright (c) 2018. Weilon Ying. All Rights Reserved. This software is provided as is, without warranty,
 * to the fullest extent of the law. You may freely copy, distribute and make derivatives of this work for
 * commercial and non-commercial purposes provided that you give credit to the author.
 */

package com.weilonying.moreslimes.services;

import com.weilonying.moreslimes.PluginHelper;
import org.apache.commons.math3.util.FastMath;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

import java.util.Random;

/**
 * This class is responsible for spawning slimes
 */
public class SpawnerService extends Service {
    public static final int MINIMUM_SPAWN_DISTANCE = 24;
    public static final int MAXIMUM_SPAWN_DISTANCE = 128;
    public static final int MINIMUM_SPAWN_HEIGHT = 2;
    public static final int MAXIMUM_SPAWN_HEIGHT = 40;

    private final Random rng = new Random();
    private double mChance;

    public SpawnerService(PluginHelper pluginHelper) {
        super(pluginHelper);
    }

    @Override
    public void enable() {
        ConfigService configService = getPluginHelper().getServiceMap().getInstance(ConfigService.class);
        try {
            mChance = (double) configService
                    .getConfigValue(ConfigService.SPAWN_ATTEMPT_CHANCE_PER_TICK);
        } catch (ClassCastException e) {
            mChance = resetSpawnAttemptChanceConfigValue();
        } finally {
            if (mChance > 1 || mChance < 0) { // reset to default value
                mChance = resetSpawnAttemptChanceConfigValue();
            }
        }
    }

    @Override
    public void disable() {
        // do nothing
    }

    /**
     * Spawn slimes near the specified player if chance check is successful
     * @param p Player to spawn the slimes in the vicinity of
     * @return whether slimes were spawned
     */
    public boolean maybeAttemptSpawnSlimesNearPlayer(Player p) {
        if (shouldSpawnSlimes()) {
            return attemptSpawnSlimesNearPlayer(p);
        }
        return false;
    }

    /**
     * Spawn slimes near the specified player.
     * @param p Player to spawn the slimes in the vicinity of.
     * @return whether slimes were spawned
     */
    public boolean attemptSpawnSlimesNearPlayer(Player p) {
        World playerWorld = p.getWorld();
        if (playerWorld.getEnvironment() != World.Environment.NORMAL) {
            return false; // only allow spawning in overworld
        }
        Location targetSpawnLocation = getCandidateSpawningLocation(p.getLocation());
        ConfigService configService = getPluginHelper().getServiceMap().getInstance(ConfigService.class);

        // get min and max number of slimes
        int minSlimes = 0;
        int maxSlimes = 0;
        int numSlimesToSpawn;
        try {
            minSlimes = (int) configService.getConfigValue(ConfigService.SPAWN_MIN_AMOUNT_PER_SUCCESSFUL_ATTEMPT);
            maxSlimes = (int) configService.getConfigValue(ConfigService.SPAWN_MAX_AMOUNT_PER_SUCCESSFUL_ATTEMPT);

        } catch (ClassCastException e) {
            configService.resetConfigValue(ConfigService.SPAWN_MIN_AMOUNT_PER_SUCCESSFUL_ATTEMPT);
            configService.resetConfigValue(ConfigService.SPAWN_MIN_AMOUNT_PER_SUCCESSFUL_ATTEMPT);
            minSlimes = ConfigService.SPAWN_MIN_AMOUNT_PER_SUCCESSFUL_ATTEMPT_DEFAULT;
            maxSlimes = ConfigService.SPAWN_MAX_AMOUNT_PER_SUCCESSFUL_ATTEMPT_DEFAULT;
        } finally {
            if (minSlimes < 0) {
                configService.resetConfigValue(ConfigService.SPAWN_MIN_AMOUNT_PER_SUCCESSFUL_ATTEMPT);
                minSlimes = ConfigService.SPAWN_MIN_AMOUNT_PER_SUCCESSFUL_ATTEMPT_DEFAULT;
            }
            if (maxSlimes < 0) {
                configService.resetConfigValue(ConfigService.SPAWN_MIN_AMOUNT_PER_SUCCESSFUL_ATTEMPT);
                maxSlimes = ConfigService.SPAWN_MAX_AMOUNT_PER_SUCCESSFUL_ATTEMPT_DEFAULT;
            }
            maxSlimes = Math.max(minSlimes, maxSlimes); // maxSlimes can never be less than minSlimes
            numSlimesToSpawn = minSlimes + rng.nextInt(maxSlimes - minSlimes);
        }

        for (int i = 0; i < numSlimesToSpawn; i++) {
            try {
                playerWorld.spawn(targetSpawnLocation, Slime.class);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }

        return true;
    }

    public boolean shouldSpawnSlimes() {
        return rng.nextDouble() <= mChance;
    }

    private Location getCandidateSpawningLocation(Location centreLocation) {
        int radius = MINIMUM_SPAWN_DISTANCE +
                (rng.nextInt(MAXIMUM_SPAWN_DISTANCE - MINIMUM_SPAWN_DISTANCE));
        double angle = rng.nextDouble() * 2 * FastMath.PI; // randomly generate angle in radians
        double xDiff = FastMath.sin(angle) * radius;
        double zDiff = FastMath.cos(angle) * radius;
        double yLocation = MINIMUM_SPAWN_HEIGHT
                + rng.nextInt(MAXIMUM_SPAWN_HEIGHT - MINIMUM_SPAWN_HEIGHT);

        Location spawnLocation = centreLocation.clone();

        spawnLocation.setX(spawnLocation.getX() + xDiff);
        spawnLocation.setY(yLocation);
        spawnLocation.setX(spawnLocation.getZ() + zDiff);

        return spawnLocation;
    }

    /**
     * Reset the spawn attempt chance parameter {@link ConfigService#SPAWN_ATTEMPT_CHANCE_PER_TICK}
     * to its default value
     * @return The default value
     */
    private double resetSpawnAttemptChanceConfigValue() {
        ConfigService configService = getPluginHelper().getServiceMap().getInstance(ConfigService.class);
        getPluginHelper()
                .getJavaPlugin()
                .getLogger()
                .info(
                        ConfigService.SPAWN_ATTEMPT_CHANCE_PER_TICK + " has invalid value " +
                                mChance + ". Resetting it to default.");
        configService.resetConfigValue(
                ConfigService.SPAWN_ATTEMPT_CHANCE_PER_TICK);
        return ConfigService.SPAWN_ATTEMPT_CHANCE_PER_TICK_DEFAULT;
    }
}
