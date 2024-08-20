package me.hellishbro.clickergamemod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.Strictness;
import me.hellishbro.clickergamemod.ClickerGameMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Config {
    public boolean horizontal = true;
    public int x = 0;
    public boolean xCenter = true;
    public int y = 0;
    public boolean yCenter = false;
    public String prestigeText = "&a+{} ";
    public String superprestigeText = "&5+{} ";
    public String rebirthText = "&c+{} ";
    public String apotheosisText = "&b+{} ";
    public String finalFruitText = "&e+{} ";
    public String reincarnationText = "&rainbow;+{}∞ ";
    public String omegaText = "&mythical;+{}Ω ";
    public String reformationText = "&fire;+{}\uD800\uDF38 ";
    public String enlightenText = "&#F155CE+{} ";
    public String awakenText = "&#CE55F1+{} ";
    public String perfectionText = "&mythical;+{}⧈ ";
    public String topText = "&a◇ &l+1 Clicker&r &a◇";
    public String cosmosTopText = "&d◇ &5&lCosmos&r &d◇";

    public ArrayList<FormattingSpice> formattingSpice = new ArrayList<>(List.of(
            new FormattingSpice("rainbow", true, 0, 100, 100, 360, 100, 100, 6.28318f),
            new FormattingSpice("mythical", false, 255, 100, 100, 280, 100, 100, 2),
            new FormattingSpice("fire", false, 10, 100, 100, 40, 100, 100, 2),
            new FormattingSpice("ecstatic", false, 60, 100, 100, 100, 100, 100, 1),
            new FormattingSpice("cool", false, 145, 100, 100, 190, 100, 100, 1.5f),
            new FormattingSpice("yinyang", false, 0, 0, 100, 0, 0, 0, 3),
            new FormattingSpice("slowrainbow", true, 0, 100, 100, 360, 100, 100, 60)
    ));
    private static Config instance;
    private static final Gson GSON = new GsonBuilder().setStrictness(Strictness.LENIENT).disableHtmlEscaping().setPrettyPrinting().create();
    
    public Config() {}

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

    public static void setConfig(Config cfg) {
        instance = cfg;
    }

    public static Config load() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configPath()), StandardCharsets.UTF_16));
        Config cfg = GSON.fromJson(reader, Config.class);
        reader.close();
        return cfg;
    }

    public void save() {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(configPath()), StandardCharsets.UTF_16);
            String json = GSON.toJson(this);
            writer.write(json);
            writer.close();

            BufferedWriter helpWriter = new BufferedWriter(new FileWriter(configHelpPath()));
            helpWriter.write(
                    """
                            # ClickerGameMod
                            
                            A mod for the game +1 clicker.
                            Tiers will be **bolded** if that tier can ascend. This is only calculated base on count. Currency requirements are therefore not bolded.
                            You can also customize the formatting of the display! (See below)
                            
                            ## Positioning
                            
                            Here is how a position look: `V;15,0`.
                            The positioning is split into two parts: orientation and coordinates.
                            The orientation can be either **V**ertical or **H**orizontal. Each component of the coordinate can have different values and will change the placement.
                            - Positive number `[0, inf]`: distance away from the top-left corner.
                            - Negative number `[-1, -inf]`: distance away the bottom-right corner.
                            - `C`: centers the HUD automatically.
                            
                            `H;C,-30` means display the HUD horizontally, with the X centered and Y being `30` units away from the bottom edge.
                            
                            
                            ## Text Formatting Specification
                            
                            Everything uses `&` color codes. You can also do `&#RRGGBB` for a custom color with hex codes.
                            `{}` is used to insert the prestige layer count.
                            
                            ## Formatting Spices
                            
                            You can insert a spice like this: `&name;`. This will make the following text color animated.
                            
                            The spice name is self-explanatory, it is the name that you put in `&name;`.
                            The transition is split into three additional parts: loop type, start color, and end color.
                            Loop type can be either **L**oop or **B**ounce. `Loop` type means it will loop from start to end. `Bounce` type means it will start from start, go to the end, and then bounce back to start.
                            Start and End colors are in the HSV / HSB color space. `H'S'V`. Hue falls in the range `[0, 360]`. Saturation and Value both falls in `[0, 100]`.
                            The transitioning is linear.
                            
                            Lastly, the cycle time is in seconds. It is how long should each cycle last.
                            """
            );
            helpWriter.close();
        } catch (Exception e) {
            ClickerGameMod.LOGGER.error("Cannot save Config.");
        }
    }

    private static String configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("clickergamemod.json").toString();
    }

    private static String configHelpPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("README-clickergamemod.md").toString();
    }
}
