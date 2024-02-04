package arctic.frosty.advancedtransfer;

@SuppressWarnings({"unused"})
public class Util {
    //Colors code for Console
    public static String convertToAnsi(String input) {
        //Colours code
        final String[] ansiNumColors = {
                "[0;30m",
                "[0;34m",
                "[0;32m",
                "[0;36m",
                "[0;31m",
                "[0;35m",
                "[0;33m",
                "[0;37m",
                "[0;90m",
                "[0;94m",
        };
        for (int i = 0; i < ansiNumColors.length; i++) {
            input = input.replaceAll("[§&]" + i, "\u001B" + ansiNumColors[i]);
        }
        //Additional Formatting codes
        final String[] charCodes = {
                "a",
                "b",
                "c",
                "d",
                "e",
                "f",
        };
        final String[] ansiCharColors = {
                "[0;92m",
                "[0;96m",
                "[0;91m",
                "[0;95m",
                "[0;93m",
                "[0;97m",
        };
        for (int i = 0; i < charCodes.length; i++) {
            input = input.replaceAll("[§&]" + charCodes[i], "\u001B" + ansiCharColors[i]);
        }
        return input;
    }

    //Colours code for Minecraft
    public static String convertToColoredText(String input) {
        //Colours code
        for (int i = 0; i <= 9; i++) {
            input = input.replaceAll("&" + i, "§" + i);
        }
        for (char c = 'a'; c <= 'f'; c++) {
            input = input.replaceAll("&" + c, "§" + c);
        }
        //Additional Formatting codes
        final char[] additionalFormatting = {'l', 'o', 'n', 'm', 'r'};
        for (char c : additionalFormatting) {
            input = input.replaceAll("&" + c, "§" + c);
        }
        return input;
    }
}