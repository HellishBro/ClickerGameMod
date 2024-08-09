package me.hellishbro.clickergamemod.config;

import me.hellishbro.clickergamemod.ClickerGameMod;

import java.awt.*;

public class FormattingSpice {
    public boolean loop;
    public float cycleTime;
    public String name;

    public int startHue, startSaturation, startBrightness;
    public int endHue, endSaturation, endBrightness;

    public FormattingSpice(String name, boolean loop, int startHue, int startSaturation, int startBrightness, int endHue, int endSaturation, int endBrightness, float cycleTime) {
        this.name = name;
        this.loop = loop;
        this.startHue = startHue;
        this.startSaturation = startSaturation;
        this.startBrightness = startBrightness;
        this.endHue = endHue;
        this.endSaturation = endSaturation;
        this.endBrightness = endBrightness;
        this.cycleTime = cycleTime;
        ClickerGameMod.LOGGER.info("Loading {}:{}-{}'{}'{}-{}'{}'{}:{}", name, loop ? "L" : "B", startHue, startSaturation, startBrightness, endHue, endSaturation, endBrightness, cycleTime);
    }

    public static FormattingSpice fromString(String string) {
        ClickerGameMod.LOGGER.info("Loading spice from string {}", string);
        String[] segments = string.split(":");
        String[] transitions = segments[1].split("-");
        boolean loop = transitions[0].equals("L");
        String[] start = transitions[1].split("'");
        int startHue = Integer.parseInt(start[0]);
        int startSaturation = Integer.parseInt(start[1]);
        int startBrightness = Integer.parseInt(start[2]);
        String[] end = transitions[2].split("'");
        int endHue = Integer.parseInt(end[0]);
        int endSaturation = Integer.parseInt(end[1]);
        int endBrightness = Integer.parseInt(end[2]);
        float cycleTime = Float.parseFloat(segments[2]);
        return new FormattingSpice(segments[0], loop, startHue, startSaturation, startBrightness, endHue, endSaturation, endBrightness, cycleTime);
    }

    public int color(double time) {
        double loopTime = time % cycleTime;
        double percent = loopTime / cycleTime;
        if (!loop) {
            if (percent >= 0.5) {
                percent = 2 - percent * 2;
            } else {
                percent = percent * 2;
            }
        }
        double hue = (double) (endHue - startHue) * percent + startHue;
        double saturation = (double) (endSaturation - startSaturation) * percent + startSaturation;
        double brightness = (double) (endBrightness - startBrightness) * percent + startBrightness;

        return Color.HSBtoRGB((float) (hue % 360 / 360), (float) Math.min(Math.max(saturation, 0), 100) / 100, (float) Math.min(Math.max(brightness, 0), 100) / 100);
    }
}
