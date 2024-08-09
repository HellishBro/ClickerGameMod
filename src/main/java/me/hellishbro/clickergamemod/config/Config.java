package me.hellishbro.clickergamemod.config;

import com.google.common.collect.ImmutableMap;
import me.hellishbro.clickergamemod.ClickerGameMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.util.*;

public class Config {
    public boolean horizontal = true;
    public int x = 0;
    public boolean xCenter = true;
    public int y = 0;
    public boolean yCenter = false;
    public String configFormat = "3";
    public String prestigeText = "&a+{} ";
    public String superprestigeText = "&5+{} ";
    public String rebirthText = "&c+{} ";
    public String apotheosisText = "&b+{} ";
    public String finalFruitText = "&e+{} ";
    public String reincarnationText = "&rainbow;+{}:inf: ";
    public String omegaText = "&mythical;+{}:omega: ";
    public String reformationText = "&fire;+{}:thiuth: ";
    public String enlightenText = "&#F155CE+{} ";
    public String awakenText = "&#CE55F1+{} ";
    public String perfectionText = "&mythical;+{}:squaredsquare: ";
    public String topText = "&a:emptydiamond: &l+1 Clicker&r &a:emptydiamond:";
    public String cosmosTopText = "&d:emptydiamond: &5&lCosmos&r &d:emptydiamond:";

    public ArrayList<FormattingSpice> formattingSpice = new ArrayList<>(List.of(
            new FormattingSpice("rainbow", true, 0, 100, 100, 360, 100, 100, 6.28318f),
            new FormattingSpice("mythical", false, 255, 100, 100, 280, 100, 100, 2),
            new FormattingSpice("fire", false, 10, 100, 100, 40, 100, 100, 2),
            new FormattingSpice("ecstatic", false, 60, 100, 100, 100, 100, 100, 1),
            new FormattingSpice("cool", false, 145, 100, 100, 190, 100, 100, 1.5f),
            new FormattingSpice("yinyang", false, 0, 0, 100, 0, 0, 0, 3),
            new FormattingSpice("slowrainbow", true, 0, 100, 100, 360, 100, 100, 60)
    ));
    public HashMap<String, String> symbolMap = new HashMap<>();

    private static Config instance;
    
    public Config() {
        symbolMap.put("inf", "∞");
        symbolMap.put("omega", "Ω");
        symbolMap.put("thiuth", "\uD800\uDF38");
        symbolMap.put("sum", "Σ");

        symbolMap.put("plusminus", "±");
        symbolMap.put("fire", "🔥");
        symbolMap.put("replacement", "�");
        symbolMap.put("thunderstorm", "☈");
        symbolMap.put("emptydiamond", "◇");
        symbolMap.put("diamond", "◆");
        symbolMap.put("doublelt", "«");
        symbolMap.put("doublegt", "»");
        symbolMap.put("triangleleft", "⏴");
        symbolMap.put("triangleright", "⏵");
        symbolMap.put("bullet", "◦");
        symbolMap.put("smallerbullet", "•");
        symbolMap.put("squaredsquare", "⧈");

        symbolMap.put("star", "★");
        symbolMap.put("moon", "☽");
        symbolMap.put("diamondsuit", "♦");
        symbolMap.put("emptydiamondsuit", "♢");
        symbolMap.put("sun", "☀");
        symbolMap.put("envelope", "✉");
        symbolMap.put("hryvnia", "₴");
        symbolMap.put("reference", "※");
        symbolMap.put("comet", "☄");
        symbolMap.put("othal", "\uD800\uDF49");
    }

    public static Config getInstance() {
        if (instance == null) {
            try {
                instance = load();
            } catch (Exception e) {
                instance = new Config();
                ClickerGameMod.LOGGER.error("There's an error with loading the config.");
                instance.save();
            }
        }
        return instance;
    }

    private static String serializePosition(Config config) {
        String string = config.horizontal ? "H" : "V";
        string += ";";
        string += config.xCenter ? "C" : config.x;
        string += ",";
        string += config.yCenter ? "C" : config.y;
        return string;
    }

    private static void parsePosition(Config config, String string) {
        String[] segments = string.split(";");
        String orientation = segments[0];
        config.horizontal = orientation.equals("H");
        String[] coordinate = segments[1].split(",");
        if (coordinate[0].equalsIgnoreCase("c")) {
            config.xCenter = true;
        } else {
            try {
                config.x = Integer.parseInt(coordinate[0]);
            } catch (NumberFormatException e) {
                config.x = 15;
            }
        }
        if (coordinate[1].equalsIgnoreCase("c")) {
            config.yCenter = true;
        } else {
            try {
                config.y = Integer.parseInt(coordinate[1]);
            } catch (NumberFormatException e) {
                config.y = 0;
            }
        }
    }

    public static void setConfig(Config cfg) {
        instance = cfg;
    }

    public static Config load() throws Exception {
        Config config = new Config();
        BufferedReader reader = new BufferedReader(new FileReader(configPath()));
        String line = reader.readLine();
        int index = 0;

        boolean parsingFmtSpice = false;
        boolean parsingSymbolReplacement = false;
        while (line != null) {
            ClickerGameMod.LOGGER.info("Config line#{}: {}", index, line);
            switch (index) {
                case 0:
                    parsePosition(config, line);
                    break;
                case 1:
                    if (!line.equals(config.configFormat)) {
                        ClickerGameMod.LOGGER.error("Config format not equal! {} != {}", line, config.configFormat);
                        throw new Exception();
                    }
                    break;
                case 2:
                    config.prestigeText = line;
                    break;
                case 3:
                    config.superprestigeText = line;
                    break;
                case 4:
                    config.rebirthText = line;
                    break;
                case 5:
                    config.apotheosisText = line;
                    break;
                case 6:
                    config.finalFruitText = line;
                    break;
                case 7:
                    config.reincarnationText = line;
                    break;
                case 8:
                    config.omegaText = line;
                    break;
                case 9:
                    config.reformationText = line;
                    break;
                case 10:
                    config.topText = line;
                    break;
                case 11:
                    config.enlightenText = line;
                    break;
                case 12:
                    config.awakenText = line;
                    break;
                case 13:
                    config.perfectionText = line;
                    break;
                case 14:
                    config.cosmosTopText = line;
                    break;
            }
            if (index > 14) {
                if (line.equals("Formatting Spices")) {
                    parsingFmtSpice = true;
                    parsingSymbolReplacement = false;
                    config.formattingSpice.clear();
                } else if (line.equals("Symbol Replacement")) {
                    parsingFmtSpice = false;
                    parsingSymbolReplacement = true;
                    config.symbolMap.clear();
                } else {
                    if (parsingFmtSpice) {
                        FormattingSpice spice = FormattingSpice.fromString(line);
                        config.formattingSpice.add(spice);
                    } else if (parsingSymbolReplacement) {
                        String[] segments = line.split(":");
                        String name = segments[0];
                        String[] hexParts = segments[1].split(" ");
                        StringBuilder res = new StringBuilder();
                        for (String part : hexParts) {
                            int codePoint = Integer.parseInt(part, 16);
                            if (Character.isBmpCodePoint(codePoint)) {
                                res.append((char) codePoint);
                            } else {
                                res.append(Character.toChars(codePoint));
                            }
                        }
                        config.symbolMap.put(name, res.toString());
                    } else {
                        ClickerGameMod.LOGGER.error("Config parsing error! Line#{}: \"{}\". Not in format spice nor symbol replacement!", index, line);
                    }
                }
            }
            line = reader.readLine();
            index ++;
        }
        reader.close();
        return config;
    }

    private String serializeFormatSpices(ArrayList<FormattingSpice> spices) {
        ArrayList<String> arrList = new ArrayList<>();
        ArrayList<String> spiceNames = new ArrayList<>();
        for (FormattingSpice spice : spices) {
            String name = spice.name;
            if (!spiceNames.contains(name)) {
                String loop = spice.loop ? "L" : "B";
                String start = spice.startHue + "'" + spice.startSaturation + "'" + spice.startBrightness;
                String end = spice.endHue + "'" + spice.endSaturation + "'" + spice.endBrightness;
                String cycleTime = String.valueOf(spice.cycleTime);
                arrList.add(name + ":" + loop + "-" + start + "-" + end + ":" + cycleTime);
                spiceNames.add(name);
            }
        }
        return String.join("\n", arrList.stream().toList());
    }

    private String serializeSymbolMap(HashMap<String, String> map) {
        ArrayList<String> arrList = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            StringBuilder res = new StringBuilder();
            int i = 0;
            while (i < entry.getValue().length()) {
                int codepoint = entry.getValue().codePointAt(i);
                res.append(String.format("%04X", codepoint));
                if (Character.isSupplementaryCodePoint(codepoint)) {
                    i += 2;
                } else {
                    i++;
                }

                if (i < entry.getValue().length()) {
                    res.append(" ");
                }
            }
            arrList.add(entry.getKey() + ":" + res);
        }
        return String.join("\n", arrList.stream().toList());
    }

    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(configPath()));
            writer.write(
                    serializePosition(getInstance()) + "\n"
                    + getInstance().configFormat + "\n"
                    + getInstance().prestigeText + "\n"
                    + getInstance().superprestigeText + "\n"
                    + getInstance().rebirthText + "\n"
                    + getInstance().apotheosisText + "\n"
                    + getInstance().finalFruitText + "\n"
                    + getInstance().reincarnationText + "\n"
                    + getInstance().omegaText + "\n"
                    + getInstance().reformationText + "\n"
                    + getInstance().topText + "\n"
                    + getInstance().enlightenText + "\n"
                    + getInstance().awakenText + "\n"
                    + getInstance().perfectionText + "\n"
                    + getInstance().cosmosTopText + "\nFormatting Spices\n"
                    + serializeFormatSpices(getInstance().formattingSpice) + "\nSymbol Replacement\n"
                    + serializeSymbolMap(getInstance().symbolMap)
            );
            writer.close();

            BufferedWriter helpWriter = new BufferedWriter(new FileWriter(configHelpPath()));
            helpWriter.write(
                    """
                            # ClickerGameMod
                            
                            A mod for the game +1 clicker.
                            Tiers will be **bolded** if that tier can ascend. This is only calculated base on count. Currency requirements are therefore not bolded.
                            You can also customize the formatting of the display! (See below)
                            
                            ## The Config File
                            
                            The config file is separated into lines. Each line have a special meaning.
                            ```
                            Config Format
                            Positioning
                            < Prestige Text >
                            < Superprestige Text >
                            < Rebirth Text >
                            < Apotheosis Text >
                            < Final Fruit Text >
                            < Reincarnation Text >
                            < Omega Text >
                            < Reformation Text >
                            < Top Text >
                            < Cosmos: Enlightening Text >
                            < Cosmos: Awakening Text >
                            < Cosmos: Perfection Text >
                            < Cosmos: Top Text >
                            "Formatting Spices"
                            [ Spice#1 ]
                            [ Spice#2 ]
                            ...
                            [ Spice#N ]
                            "Symbol Replacement"
                            [ Replacement#1 ]
                            [ Replacement#2 ]
                            ...
                            [ Replacement#N ]
                            ```
                            
                            Lines 3 (`< Prestige Text >`) to 15 (`< Cosmos: Top Text >`) are text formatting specifications.
                            Section "Formatting Spices" is for spices like `&rainbow;`, `&mythical;`, etc.
                            Section "Symbol Replacement" is for symbols like `:inf:`, `:tm:`, etc.
                            
                            ## Positioning
                            
                            Here is how a position look: `V;15,0`.
                            The positioning is split into two parts: orientation and coordinates.
                            The orientation can be either **V**ertical or **H**orizontal. Each component of the coordinate can have different values and will change the placement.
                            - Positive number `[0, ∞]`: distance away from the top-left corner.
                            - Negative number `[-1, -∞]`: distance away the bottom-right corner.
                            - `C`: centers the HUD automatically.
                            
                            `H;C,-30` means display the HUD horizontally, with the X centered and Y being `30` units away from the bottom edge.
                            
                            
                            ## Text Formatting Specification
                            
                            Everything uses `&` color codes. You can also do `&#RRGGBB` for a custom color with hex codes.
                            `{}` is used to insert the prestige layer count.
                            
                            ## Formatting Spices
                            
                            You can insert a spice like this: `&name;`. This will make the following text color animated.
                            
                            Each line is a formatting spice, which might look like this: `rainbow:L-0'100'100-360'100'100:1`.
                            Each line is split into three segments: spice name, transitions, cycle time.
                            
                            The spice name is self-explanatory, it is the name that you put in `&name;`.
                            The transition is split into three additional parts: loop type, start color, and end color.
                            Loop type can be either **L**oop or **B**ounce. `Loop` type means it will loop from start to end. `Bounce` type means it will start from start, go to the end, and then bounce back to start.
                            Start and End colors are in the HSV / HSB color space. `H'S'V`. Hue falls in the range `[0, 360]`. Saturation and Value both falls in `[0, 100]`.
                            The transitioning is linear.
                            
                            Lastly, the cycle time is in seconds. It is how long should each cycle last.
                            
                            ## Symbol Replacement
                            
                            You can insert a custom symbol like this: `:name:`.
                            
                            Each line look something like this: `inf:221E`.
                            Each line is split into two segments: symbol name and codepoint(s).
                            
                            The codepoints are in hexadecimal, and some symbols that requires two characters should have a space in between.
                            """
            );
            helpWriter.close();
        } catch (Exception e) {
            ClickerGameMod.LOGGER.error("Cannot save Config.");
        }
    }

    private static String configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("clickergamemod.config").toString();
    }

    private static String configHelpPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("README-clickergamemod.md").toString();
    }
}
