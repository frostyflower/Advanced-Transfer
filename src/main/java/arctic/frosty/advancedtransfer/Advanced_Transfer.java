package arctic.frosty.advancedtransfer;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
public class Advanced_Transfer extends JavaPlugin {
    private static Economy economy;
    private static File logFile;

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();
        this.saveDefaultConfig();

        if (!setupEconomy()) {
            getLogger().severe("Probably dependency is missing!");
            getServer().getPluginManager().disablePlugin(this);
        }

        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdir();
                Bukkit.getLogger().info(Util.convertToAnsi(getPrefix() + "&ePlease disable /pay command in Essentials config for better functionality!&f"));
                Bukkit.getLogger().info(Util.convertToAnsi(getPrefix() + "Created data folder.&f"));
            }
            logFile = new File(getDataFolder(), "Transaction_log.txt");
            if (!getLogFile().exists()) {
                getLogFile().createNewFile();
                FileWriter writer = new FileWriter(getLogFile(), true);
                writer.write("==============================[Transaction Logs]==============================\n");
                writer.close();
            }
        } catch (IOException e) {
            getServer().getPluginManager().disablePlugin(this);
            throw new RuntimeException(e);
        }

        Objects.requireNonNull(getCommand("transfer")).setExecutor(this);
        Objects.requireNonNull(getCommand("advanced-transfer")).setExecutor(this);

        if (getConfig().getInt("Max") <= 0) {
            Bukkit.getLogger().warning("Max must be greater than 0!");
            Bukkit.getLogger().warning("Setting Max to 10000.");
            getConfig().set("Max", 1000000);
        }
        if (getConfig().getInt("Threshold") <= 0) {
            Bukkit.getLogger().warning("Threshold must be greater than 0!");
            Bukkit.getLogger().warning("Setting Threshold to 10000.");
            getConfig().set("Threshold", 10000);
        }

        this.saveConfig();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        Bukkit.getLogger().info(Util.convertToAnsi(getPrefix() + "&aPlugin has been enabled! [" + elapsedTime + "ms]&f"));
    }

    //Setup Vault Economy
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

    //Commands Handler
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("advanced-transfer")) {
            if (args.length == 0) {
                return false;
            } else if ((args.length == 1) && (args[0].equalsIgnoreCase("reload"))) {
                if (!(sender instanceof Player)) {
                    this.reloadConfig();
                    Bukkit.getLogger().info(Util.convertToAnsi(getPrefix() + "&aPlugin configuration reloaded.&f"));
                } else if (sender.hasPermission("advancedtransfer.admin")) {
                    this.reloadConfig();
                    sender.sendMessage(Util.convertToColoredText(getPrefix() + "&aPlugin configuration reloaded."));
                } else {
                    sender.sendMessage(Util.convertToColoredText("&cYou do not have permission."));
                }
                return true;
            } else {
                return false;
            }
        }
        if (command.getName().equalsIgnoreCase("transfer")) {
            if (!(sender instanceof Player)) {
                Bukkit.getLogger().info(Util.convertToAnsi("&4This command cannot be used on console!"));
            } else if (args.length == 1) {
                return false;
            } else if (args.length == 2) {
                Player player = (Player) sender;
                Player target = Bukkit.getServer().getPlayerExact(args[0]);
                if (target != sender) {
                    if (target != null) {
                        if (args[1].length() <= 12) {
                            processTransfer(player, target, args[1]);
                        } else {
                            sender.sendMessage(Util.convertToColoredText("&cError: &4Illegal input."));
                        }
                    } else {
                        sender.sendMessage(Util.convertToColoredText("&cError: &4Player not found."));
                    }
                } else {
                    sender.sendMessage(Util.convertToColoredText("&cError: &4Can't transfer to yourself."));
                }
                return true;
            } else {
                return false;
            }
        }
        return true;
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
        if (command.getName().equalsIgnoreCase("advanced-transfer")) {
            if (args.length == 1) {
                return Collections.singletonList("reload");
            }
        }
        return new ArrayList<>();
    }

    //Process Handler
    private void processTransfer(Player player, Player target, @NotNull String strAmount) throws RuntimeException {
        long max = getConfig().getLong("Max");
        long balance = (long) getEconomy().getBalance(player);
        try {
            if (!(strAmount.startsWith("0"))) {
                long amount = parseAmount(strAmount);
                if (amount >= 1) {
                    if (balance >= amount) {
                        if (amount <= max) {
                            NumberFormat numberFormat = NumberFormat.getIntegerInstance();
                            String number = numberFormat.format(amount);

                            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss"));
                            if (!(Objects.requireNonNull(getConfig().getString("Timezone")).equalsIgnoreCase("default"))) {
                                currentTime = LocalDateTime.now(ZoneId.of(Objects.requireNonNull(getConfig().getString("Timezone")))).format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss"));
                            }
                            if (amount >= getConfig().getInt("Threshold")) {
                                if (getConfig().getInt("Threshold") > 0) {
                                    getEconomy().withdrawPlayer(player, amount);
                                    getEconomy().depositPlayer(target, amount);

                                    player.sendMessage("§aTransferred " + getSymbol() + number + " to " + target.getName() + ".");
                                    target.sendMessage("§aYou received " + getSymbol() + number + " from " + player.getName() + ".");

                                    if (getConfig().getBoolean("Log")) {
                                        Bukkit.getLogger().info(Util.convertToAnsi("&a[Log] " + player.getName() + " transferred " + getSymbol() + number + " to " + target.getName() + ".&f"));
                                    }

                                    FileWriter write_log = new FileWriter(getLogFile(), true);
                                    String logEntry = "[" + currentTime + "]: " + player.getName() + " ---[" + getSymbol() + number + "]---> " + target.getName() + "\n";
                                    write_log.write(logEntry);
                                    write_log.close();
                                } else {
                                    throw new RuntimeException("Error: Threshold is set to 0 or negative number.");
                                }
                            } else if (getConfig().getInt("Threshold") > 0) {
                                getEconomy().withdrawPlayer(player, amount);
                                getEconomy().depositPlayer(target, amount);

                                player.sendMessage("§aTransferred " + getSymbol() + number + " to " + target.getName() + ".");
                                target.sendMessage("§aYou received " + getSymbol() + number + " from " + player.getName() + ".");

                                if (getConfig().getBoolean("Log")) {
                                    Bukkit.getLogger().info(Util.convertToAnsi("&a[Log] " + player.getName() + " transferred " + getSymbol() + number + " to " + target.getName() + ".&f"));
                                }
                            } else {
                                throw new RuntimeException("Error: Threshold is set to 0 or negative number.");
                            }
                        } else {
                            NumberFormat numberFormat = NumberFormat.getIntegerInstance();
                            String numberMax = numberFormat.format(max);
                            player.sendMessage("§cYou can't transfer more than " + getSymbol() + numberMax + ".");
                        }
                    } else {
                        player.sendMessage("§cYou don't have enough balance.");
                    }
                } else {
                    player.sendMessage("§cError: §4Illegal input.");
                }
            } else {
                player.sendMessage("§cError: §4Illegal input.");
            }
        } catch (NumberFormatException | IOException e) {
            player.sendMessage("§cError: §4Illegal input.");
        }
    }

    //Format Conversion
    private static long parseAmount(@NotNull String strAmount) {
        long amount = 0;
        try {
            strAmount = strAmount.toLowerCase();
            long suffixAmt = 0;
            if (strAmount.length() > 1) {
                suffixAmt = Long.parseLong(strAmount.substring(0, strAmount.length() - 1));
            } else if (strAmount.length() == 1) {
                suffixAmt = Long.parseLong(strAmount);
            }
            if (!(strAmount.startsWith("0"))) {
                if (strAmount.matches("\\d+k")) {
                    amount = suffixAmt * 1000;
                } else if (strAmount.matches("\\d+m")) {
                    amount = suffixAmt * 1000000;
                } else if (strAmount.matches("\\d+b")) {
                    amount = suffixAmt * 1000000000;
                } else if (strAmount.matches("^\\d+$")) {
                    amount = Long.parseLong(strAmount);
                }
            }
        } catch (NumberFormatException ignored) {
        }
        return amount;
    }

    //Getter constructors
    private static Economy getEconomy() {
        return economy;
    }
    private static File getLogFile() {
        return logFile;
    }
    private String getPrefix() {
        return getConfig().getString("Prefix");
    }
    private String getSymbol() {
        return getConfig().getString("Currency");
    }
}