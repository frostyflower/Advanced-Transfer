package arctic.frosty.advancedtransfer;
@SuppressWarnings({"unused"})
public class Util {
    public static String convertToAnsi(String input) {
        //Colours code
        input = input.replaceAll("[§&]0", "\u001B[0;30m");
        input = input.replaceAll("[§&]1", "\u001B[0;34m");
        input = input.replaceAll("[§&]2", "\u001B[0;32m");
        input = input.replaceAll("[§&]3", "\u001B[0;36m");
        input = input.replaceAll("[§&]4", "\u001B[0;31m");
        input = input.replaceAll("[§&]5", "\u001B[0;35m");
        input = input.replaceAll("[§&]6", "\u001B[0;33m");
        input = input.replaceAll("[§&]7", "\u001B[0;37m");
        input = input.replaceAll("[§&]8", "\u001B[0;90m");
        input = input.replaceAll("[§&]9", "\u001B[0;94m");
        input = input.replaceAll("[§&]a", "\u001B[0;92m");
        input = input.replaceAll("[§&]b", "\u001B[0;96m");
        input = input.replaceAll("[§&]c", "\u001B[0;91m");
        input = input.replaceAll("[§&]d", "\u001B[0;95m");
        input = input.replaceAll("[§&]e", "\u001B[0;93m");
        input = input.replaceAll("[§&]f", "\u001B[0;97m");
        //Additional Formatting codes
        input = input.replaceAll("[§&]l", "\u001B[1m");
        input = input.replaceAll("[§&]o", "\u001B[3m");
        input = input.replaceAll("[§&]n", "\u001B[4m");
        input = input.replaceAll("[§&]m", "\u001B[9m");
        input = input.replaceAll("[§&]r", "\u001B[0m");

        return input;
    }

    public static String convertToColoredText(String input) {
        //Colours code
        for (int i = 0; i <= 9; i++) {
            input = input.replaceAll("&" + i, "§" + i);
        }
        for (char c = 'a'; c <= 'f'; c++) {
            input = input.replaceAll("&" + c, "§" + c);
        }
        //Additional Formatting codes
        char[] additionalFormatting = {'l', 'o', 'n', 'm', 'r'};
        for (char c : additionalFormatting) {
            input = input.replaceAll("&" + c, "§" + c);
        }

        return input;
    }
}