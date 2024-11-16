package me.hellishbro.clickergamemod;

import me.hellishbro.clickergamemod.config.Config;
import me.hellishbro.clickergamemod.config.FormattingSpice;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Unique;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {
    public static HashMap<String, String> hexLookup() {
        HashMap<String, String> map = new HashMap<>();
        map.put("#000000", "0");
        map.put("#0000AA", "1");
        map.put("#00AA00", "2");
        map.put("#00AAAA", "3");
        map.put("#AA0000", "4");
        map.put("#AA00AA", "5");
        map.put("#FFAA00", "6");
        map.put("#AAAAAA", "7");
        map.put("#555555", "8");
        map.put("#5555FF", "9");
        map.put("#55FF55", "a");
        map.put("#55FFFF", "b");
        map.put("#FF5555", "c");
        map.put("#FF55FF", "d");
        map.put("#FFFF55", "e");
        map.put("#FFFFFF", "f");
        return map;
    }

    public static MutableText fromString(String message) {
        // https://github.com/DFScripting/DFScript/blob/1.20.4/src/main/java/io/github/techstreet/dfscript/util/ComponentUtil.java#L13

        MutableText result = Text.literal("");

        try {
            Pattern pattern = Pattern.compile("(§[a-f0-9lonmkrA-FLONMRK]|§#[a-f0-9A-F]{6})");
            Matcher matcher = pattern.matcher(message);

            Style s = Style.EMPTY;

            int lastIndex = 0;
            while (matcher.find()) {
                int start = matcher.start();
                String text = message.substring(lastIndex, start);
                if (!text.isEmpty()) {
                    MutableText t = Text.literal(text);
                    t.setStyle(s);
                    result.append(t);
                }
                String col = matcher.group();

                if (col.length() == 2) {
                    s = s.withFormatting(Formatting.byCode(col.charAt(1)));
                } else {
                    s = Style.EMPTY.withColor(
                            TextColor.parse(col.substring(1)).getOrThrow());
                }
                lastIndex = matcher.end();
            }
            String text = message.substring(lastIndex);
            if (!text.isEmpty()) {
                MutableText t = Text.literal(text);
                t.setStyle(s);
                result.append(t);
            }
        } catch (Exception ignored) {}

        result.append(Text.literal("§r"));
        return result;
    }

    public static String toSection(Text message) {
        // https://github.com/DFScripting/DFScript/blob/1.20.4/src/main/java/io/github/techstreet/dfscript/util/ComponentUtil.java#L55

        StringBuilder result = new StringBuilder();
        HashMap<String, String> replacement = hexLookup();

        if(message.getSiblings().isEmpty()){
            Style style = message.getStyle();
            String format = "";

            if (style.getColor() != null) {
                String color = "#" + String.join("", String.format("%06X", style.getColor().getRgb()).split(""));
                color = replacement.getOrDefault(color, color);
                format += "&" + color;
            }

            if (style.isBold()) format += "&l";
            if (style.isItalic()) format += "&o";
            if (style.isUnderlined()) format += "&n";
            if (style.isStrikethrough()) format += "&m";
            if (style.isObfuscated()) format += "&k";

            result.append(format);
            result.append(message.getString());
        } else {
            for (Text sibling : message.getSiblings()) {
                result.append("&r");
                result.append(toSection(sibling));
            }
        }

        return result.toString();
    }

    public static Text presLayer(String format, long amount, long ascendLimit) {
        boolean canAscend = amount >= ascendLimit && ascendLimit != -1;
        Text text = format(format.replaceAll("\\{}", String.valueOf(amount)));

        Style style = text.getStyle();
        style = style.withBold(canAscend);
        List<Text> segments = text.getWithStyle(style);

        MutableText text1 = Text.literal("");
        for (Text segment : segments) {
            text1.append(segment);
        }
        return text1;
    }

    public static Text format(String format) {
        format = format.replaceAll("&", "§");

        for (FormattingSpice spice : Config.getInstance().formattingSpice) {
            int color = spice.color(((double) new Date().getTime()) / 1000d);
            String s = Integer.toHexString(color).substring(2);
            format = format.replaceAll("§" + spice.name + ";", "§#" + s);
        }

        return TextUtil.fromString(format);
    }
}
