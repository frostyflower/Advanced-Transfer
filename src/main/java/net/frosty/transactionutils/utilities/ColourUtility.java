package net.frosty.transactionutils.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ColourUtility {
    public static Component colourise(String text) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
    }
}