package me.hellishbro.clickergamemod;

import me.hellishbro.clickergamemod.config.Config;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlusOneClicker {
    public long prestige;
    public long superprestige;
    public long rebirth;
    public long apotheosis;
    public long finalFruit;
    public long reincarnation;
    public long omega;
    public long reformation;

    public int enlightening;
    public int awakening;
    public int perfection;

    public boolean challenge;

    public PlusOneClicker(long pr, long sp, long reb, long apoth, long ff, long rein, long omega, long rfm, boolean chall, int enlight, int awake, int perf) {
        prestige = pr;
        superprestige = sp;
        rebirth = reb;
        apotheosis = apoth;
        finalFruit = ff;
        reincarnation = rein;
        this.omega = omega;
        reformation = rfm;
        challenge = chall;
        enlightening = enlight;
        awakening = awake;
        perfection = perf;
    }

    public void cosmosFromText(Text text) {
        String raw = TextUtil.toSection(text);
        raw = raw.replaceAll("&r", "").replaceAll("&f", "").replaceAll("&8\\[", "[").replaceAll("&8]", "]");
        // example tag: &#20332E[&#99FFE6◦⁑&#50E0B0⧈&#99FFE6⁑◦&#20332E][&#3746EF+30][&#CE55F1+30][&#F155CE+30]
        // enlightening color: #F155CE
        // awakening color: #CE55F1
        // perfection color: #3746EF
        // 100%: &#20332E[&#99FFE6◦⁑&#50E0B0⧈&#99FFE6⁑◦&#20332E]
        ClickerGameMod.LOGGER.info(raw);
        Matcher enlighteningMatch = Pattern.compile("\\[&#F155CE\\+(\\d+)]").matcher(raw);
        enlightening = enlighteningMatch.find() ? Integer.parseInt(enlighteningMatch.group(1)) : 0;

        Matcher awakeningMatch = Pattern.compile("\\[&#CE55F1\\+(\\d+)]").matcher(raw);
        awakening = awakeningMatch.find() ? Integer.parseInt(awakeningMatch.group(1)) : 0;

        Matcher perfectionMatch = Pattern.compile("\\[&#3746EF\\+(\\d+)]").matcher(raw);
        perfection = perfectionMatch.find() ? Integer.parseInt(perfectionMatch.group(1)) : 0;
    }

    public PlusOneClicker() {
        this(0, 0, 0, 0, 0, 0, 0, 0, false, 0, 0, 0);
    }

    public static PlusOneClicker fromText(Text text, PlusOneClicker old) {
        String raw = TextUtil.toSection(text);
        raw = raw.replaceAll("&r&f&r&8« ", "").replaceAll(" &r&8»", "");
        raw = raw.replaceAll("&r", "");
        raw = raw.replaceAll(" ", "");
        raw = raw.replaceAll("&8\\[", "[").replaceAll("&8]", "]");

        // badges
        raw = raw.replaceAll("\\[&#5176E2⧈]", ""); // congregation
        raw = raw.replaceAll("\\[&#FF8811\uD83D\uDD25]", ""); // infernal
        raw = raw.replaceAll("\\[&#FFD744★]", ""); // stellar
        raw = raw.replaceAll("\\[&#99BBDD\uD83C\uDF27]", ""); // seeker

        // actual processing nor way
        boolean challenge = Pattern.compile("&c❁").matcher(raw).find();

        Matcher presMatch = Pattern.compile("\\[&a\\+(\\d+)]").matcher(raw);
        long prestige = presMatch.find() ? Long.parseLong(presMatch.group(1)) : 0;

        Matcher spMatch = Pattern.compile("\\[&5\\+(\\d+)]").matcher(raw);
        long superprestige = spMatch.find() ? Long.parseLong(spMatch.group(1)) : 0;

        Matcher rebMatch = Pattern.compile("\\[&c\\+(\\d+)]").matcher(raw);
        long rebirth = rebMatch.find() ? Long.parseLong(rebMatch.group(1)) : 0;

        Matcher apothMatch = Pattern.compile("\\[&b\\+(\\d+)]").matcher(raw);
        long apotheosis = apothMatch.find() ? Long.parseLong(apothMatch.group(1)) : 0;

        Matcher ffMatch = Pattern.compile("\\[&e\\+(\\d+)]").matcher(raw);
        long finalFruit = ffMatch.find() ? Long.parseLong(ffMatch.group(1)) : 0;

        Matcher reinMatch = Pattern.compile("\\[&#?[0-9A-Fa-f]+\\+(\\d+)∞]").matcher(raw);
        long reincarnation = reinMatch.find() ? Long.parseLong(reinMatch.group(1)) : 0;

        Matcher omegaMatch = Pattern.compile("\\[&#?[0-9A-Fa-f]+\\+(\\d+)Ω(⁺[⁰¹²³⁴⁵⁶⁷⁸⁹]+)?]").matcher(raw);
        long omega = omegaMatch.find() ? Long.parseLong(omegaMatch.group(1)) : 0;

        Matcher rfmMatch = Pattern.compile("\\[&#?[0-9A-Fa-f]+\\+\\d+Ω(⁺[⁰¹²³⁴⁵⁶⁷⁸⁹]+)]").matcher(raw);
        long reformation = rfmMatch.find() ? parseSuperscriptToInt(rfmMatch.group(1).substring(1)) : 0;

        return new PlusOneClicker(prestige, superprestige, rebirth, apotheosis, finalFruit, reincarnation, omega, reformation, challenge, old.enlightening, old.awakening, old.perfection);
    }

    private static long parseSuperscriptToInt(String superscript) {
        List<Character> numbers = List.of('⁰', '¹', '²', '³', '⁴', '⁵', '⁶', '⁷', '⁸', '⁹');
        long res = 0;
        for (char digit : superscript.toCharArray()) {
            int index = numbers.indexOf(digit);
            res *= 10;
            res += index;
        }
        return res;
    }
    
    public Text getText(Config config) {
        MutableText text = Text.literal("");
        if (reformation != 0)
            text.append(TextUtil.presLayer(config.reformationText, reformation, -1));
        if (omega != 0 || reformation != 0)
            text.append(TextUtil.presLayer(config.omegaText, omega, -1));
        if (reincarnation != 0 || omega != 0 || reformation != 0)
            text.append(TextUtil.presLayer(config.reincarnationText, reincarnation, -1));
        if (finalFruit != 0 || reincarnation != 0 || omega != 0 || reformation != 0)
            text.append(TextUtil.presLayer(config.finalFruitText, finalFruit, 100));
        if (apotheosis != 0 || finalFruit != 0 || reincarnation != 0 || omega != 0 || reformation != 0)
            text.append(TextUtil.presLayer(config.apotheosisText, apotheosis, -1));
        if (rebirth != 0 || apotheosis != 0 || finalFruit != 0 || reincarnation != 0 || omega != 0 || reformation != 0)
            text.append(TextUtil.presLayer(config.rebirthText, rebirth, 30));
        if (superprestige != 0 || rebirth != 0 || apotheosis != 0 || finalFruit != 0 || reincarnation != 0 || omega != 0 || reformation != 0)
            text.append(TextUtil.presLayer(config.superprestigeText, superprestige, 30));
        text.append(TextUtil.presLayer(config.prestigeText, prestige, -1));
        return text;
    }

    public Text getCosmosText(Config config) {
        MutableText text = Text.literal("");
        if (perfection != 0)
            text.append(TextUtil.presLayer(config.perfectionText, perfection, 30));
        if (perfection != 0 || awakening != 0)
            text.append(TextUtil.presLayer(config.awakenText, awakening, 30));
        text.append(TextUtil.presLayer(config.enlightenText, enlightening, 30));
        return text;
    }
}
