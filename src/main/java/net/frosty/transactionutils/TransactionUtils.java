package net.frosty.transactionutils;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.frosty.transactionutils.utilities.ColourUtility.colourise;
import static net.frosty.transactionutils.utilities.TransactionProcessor.processTransaction;

public class TransactionUtils extends JavaPlugin {
    public static JavaPlugin plugin;
    public static Economy economy;

    @Override
    public void onEnable() {
        long timeStart = System.currentTimeMillis();
        plugin = this;
        saveDefaultConfig();

        if (!setupEconomy()) {
            getLogger().severe("Vault is missing!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (getConfig().getInt("Max") <= 0 || getConfig().getInt("Min") <= 0 || getConfig().getInt("Max") < getConfig().getInt("Min")) {
            getLogger().warning("Invalid Min/Max configuration detected!");
            getLogger().warning("Setting Min to 10 and Max to 1000000.");
            getConfig().set("Min", 10);
            getConfig().set("Max", 1000000);
            this.saveConfig();
        }

        long timeEnded = System.currentTimeMillis();
        long timeElapsed = timeEnded - timeStart;
        Bukkit.getConsoleSender().sendMessage(colourise("&aPlugin enabled in " + timeElapsed + "ms."));
    }

    //Setup Vault Economy.
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        if (getServer().getPluginManager().getPlugin("Essentials") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    //Commands.
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("transactionutil")) {
            if (args.length == 0) {
                return false;
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                this.reloadConfig();
                sender.sendMessage(colourise("&aConfig reloaded."));
                return true;
            }
        }
        if (command.getName().equalsIgnoreCase("transfer")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(colourise("&cOnly players can use this command."));
                return true;
            }
            if (args.length != 2) {
                sender.sendMessage(colourise("&cUsage: /transfer <player> <amount>"));
                return true;
            }
            Player player = (Player) sender;
            Player targetPlayer = Bukkit.getPlayer(args[0]);
            processTransaction(player, targetPlayer, args[1]);
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("transfer")) {
            if (args.length == 1) {
                return null;
            }
            if (args.length == 2) {
                return Arrays.asList("1", "10", "100", "1k", "1m", "1b");
            }
        }
        if (command.getName().equalsIgnoreCase("transactionutil")) {
            if (args.length == 1) {
                return Collections.singletonList("reload");
            }
        }
        return Collections.emptyList();
    }
}