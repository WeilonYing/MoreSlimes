/*
 * Copyright (c) 2018. Weilon Ying. All Rights Reserved. This software is provided as is, without warranty,
 * to the fullest extent of the law. You may freely copy, distribute and make derivatives of this work
 * for commercial and non-commercial purposes provided that you give credit to the author.
 */

package com.weilonying.moreslimes.services;

import com.weilonying.moreslimes.PluginHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Contains the logic for setting up and running commands
 */
public class CommandService extends Service {
    private static final String mMainCommandString = "moreslimes";
    private final CommandExecutor mMainCommand = getMainCommand();

    public CommandService(PluginHelper pluginHelper) {
        super(pluginHelper);
    }

    @Override
    public void enable() {
        setCommands();
    }

    @Override
    public void disable() {
        unsetCommands();
    }

    private void setCommands() {
        JavaPlugin plugin = getPluginHelper().getJavaPlugin();
        plugin.getCommand(mMainCommandString).setExecutor(mMainCommand);
    }

    private void unsetCommands() {
        JavaPlugin plugin = getPluginHelper().getJavaPlugin();
        plugin.getCommand(mMainCommandString).setExecutor(null);
    }

    private CommandExecutor getMainCommand() {
        return new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
                if (strings.length == 0) {
                    return false;
                }

                String arg = strings[0];
                switch(arg) {
                    case "enable": // enable spawning
                        getPluginHelper()
                                .getServiceMap()
                                .getInstance(SpawnSchedulerService.class)
                                .enable();
                        return true;
                    case "disable": // disable spawning
                        getPluginHelper()
                                .getServiceMap()
                                .getInstance(SpawnSchedulerService.class)
                                .disable();
                        return true;
                }

                return false;
            }
        };
    }
}
