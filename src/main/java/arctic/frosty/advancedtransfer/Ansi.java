package arctic.frosty.advancedtransfer;

public class Ansi {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[0;30m";
    private static final String ANSI_DARK_BLUE = "\u001B[0;34m";
    private static final String ANSI_DARK_GREEN = "\u001B[0;32m";
    private static final String ANSI_DARK_AQUA = "\u001B[0;36m";
    private static final String ANSI_DARK_RED = "\u001B[0;31m";
    private static final String ANSI_DARK_PURPLE = "\u001B[0;35m";
    private static final String ANSI_GOLD = "\u001B[0;33m";
    private static final String ANSI_GRAY = "\u001B[0;37m";
    private static final String ANSI_DARK_GRAY = "\u001B[0;90m";
    private static final String ANSI_BLUE = "\u001B[0;94m";
    private static final String ANSI_GREEN = "\u001B[0;92m";
    private static final String ANSI_AQUA = "\u001B[0;96m";
    private static final String ANSI_RED = "\u001B[0;91m";
    private static final String ANSI_LIGHT_PURPLE = "\u001B[0;95m";
    private static final String ANSI_YELLOW = "\u001B[0;93m";
    private static final String ANSI_WHITE = "\u001B[0;97m";

    private static final String ANSI_BOLD = "\u001B[1m";
    private static final String ANSI_ITALIC = "\u001B[3m";
    private static final String ANSI_UNDERLINE = "\u001B[4m";
    private static final String ANSI_STRIKETHROUGH = "\u001B[9m";

    public static String convertToAnsi(String input) {
        input = input.replaceAll("[§&]0", ANSI_BLACK);
        input = input.replaceAll("[§&]1", ANSI_DARK_BLUE);
        input = input.replaceAll("[§&]2", ANSI_DARK_GREEN);
        input = input.replaceAll("[§&]3", ANSI_DARK_AQUA);
        input = input.replaceAll("[§&]4", ANSI_DARK_RED);
        input = input.replaceAll("[§&]5", ANSI_DARK_PURPLE);
        input = input.replaceAll("[§&]6", ANSI_GOLD);
        input = input.replaceAll("[§&]7", ANSI_GRAY);
        input = input.replaceAll("[§&]8", ANSI_DARK_GRAY);
        input = input.replaceAll("[§&]9", ANSI_BLUE);
        input = input.replaceAll("[§&]a", ANSI_GREEN);
        input = input.replaceAll("[§&]b", ANSI_AQUA);
        input = input.replaceAll("[§&]c", ANSI_RED);
        input = input.replaceAll("[§&]d", ANSI_LIGHT_PURPLE);
        input = input.replaceAll("[§&]e", ANSI_YELLOW);
        input = input.replaceAll("[§&]f", ANSI_WHITE);

        input = input.replaceAll("[§&]l", ANSI_BOLD);
        input = input.replaceAll("[§&]o", ANSI_ITALIC);
        input = input.replaceAll("[§&]n", ANSI_UNDERLINE);
        input = input.replaceAll("[§&]m", ANSI_STRIKETHROUGH);
        input = input.replaceAll("[§&]r", ANSI_RESET);

        return input;
    }
}
