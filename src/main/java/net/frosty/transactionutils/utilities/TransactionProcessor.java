package net.frosty.transactionutils.utilities;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.frosty.transactionutils.TransactionUtils.economy;
import static net.frosty.transactionutils.TransactionUtils.plugin;
import static net.frosty.transactionutils.utilities.ColourUtility.colourise;

public class TransactionProcessor {

    public static void processTransaction(@NotNull Player player, Player targetPlayer, String strAmount) {
        final FileConfiguration config = plugin.getConfig();
        final double playerBalance = economy.getBalance(player);
        final int minPayment = config.getInt("Min");
        final double maxPayment = config.getDouble("Max");
        if (economy == null) {
            player.sendMessage(colourise("&cError: &4Economy service unavailable."));
            return;
        }
        if (targetPlayer == null) {
            player.sendMessage(colourise("&cError: &4Player not found."));
            return;
        }
        if (targetPlayer.getName().equals(player.getName())) {
            player.sendMessage(colourise("&cError: &4You cannot transfer to yourself."));
            return;
        }
        final long amount = convertToWholeNumber(strAmount);
        if (strAmount.length() > 10) {
            player.sendMessage(colourise("&cError: &4Amount is too large."));
            return;
        }
        if (amount <= 0) {
            player.sendMessage(colourise("&cError: &4Invalid Arguments."));
            return;
        }
        if (amount > maxPayment) {
            player.sendMessage(colourise("&cError: &4Amount is too large."));
            return;
        }
        if (amount < minPayment) {
            player.sendMessage(colourise("&cError: &4Amount is too small."));
            return;
        }
        if (playerBalance < amount) {
            player.sendMessage(colourise("&cError: &4Insufficient funds."));
            return;
        }
        try {
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formattedAmount = formatter.format(amount);
            final String currencyName = economy.currencyNameSingular();
            final EconomyResponse withdrawResponse = economy.withdrawPlayer(player, amount);
            if (!withdrawResponse.transactionSuccess()) {
                player.sendMessage(colourise("&cError: &4Failed to process withdrawal."));
                return;
            }
            final EconomyResponse depositResponse = economy.depositPlayer(targetPlayer, amount);
            if (!depositResponse.transactionSuccess()) {
                economy.depositPlayer(player, amount);
                player.sendMessage(colourise("&cError: &4Failed to process deposit."));
                return;
            }
            player.sendMessage(colourise("&aYou transferred " + currencyName + formattedAmount + " to " + targetPlayer.getName() + "."));
            targetPlayer.sendMessage(colourise("&aYou received " + currencyName + formattedAmount + " from " + player.getName() + "."));
        } catch (Exception e) {
            economy.depositPlayer(player, amount);
            player.sendMessage(colourise("&cError: &4Transaction failed."));
        }
    }


    //Utility.
    private static long convertToWholeNumber(String strAmount) {
        if (strAmount == null) {
            return 0;
        }
        if (strAmount.startsWith("0")) {
            return 0;
        }
        final String input = strAmount.toLowerCase();
        final String pattern = "^(\\d+)([kmb])?$";
        if (!input.matches(pattern)) {
            return 0;
        }
        try {
            final Pattern p = Pattern.compile(pattern);
            final Matcher m = p.matcher(input);
            if (m.find()) {
                long baseAmount = Long.parseLong(m.group(1));
                final String suffix = m.group(2);
                if (suffix == null) {
                    return baseAmount;
                }
                switch (suffix) {
                    case "k":
                        return baseAmount * 1000;
                    case "m":
                        return baseAmount * 1000000;
                    case "b":
                        return baseAmount * 1000000000;
                    default:
                        return 0;
                }
            }
            return 0;
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }
}
