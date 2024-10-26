package me.hellishbro.clickergamemod.mixin;

import me.hellishbro.clickergamemod.ClickerGameMod;
import me.hellishbro.clickergamemod.PlusOneClicker;
import me.hellishbro.clickergamemod.TextUtil;
import me.hellishbro.clickergamemod.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
@Environment(EnvType.CLIENT)
public abstract class MInGameHud {
    @Shadow public abstract TextRenderer getTextRenderer();

    @Inject(at=@At("RETURN"), method="render")
    public void render(DrawContext context, float tickDelta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!client.options.hudHidden && ClickerGameMod.CLICKING) {
            Config config = Config.getInstance();
            PlusOneClicker clicker = ClickerGameMod.stats;
            if (!config.horizontal) {
                if (inCosmos(client)) {
                    drawText(context, this.getTextRenderer(), TextUtil.format(config.cosmosTopText), config, 0);
                    drawText(context, this.getTextRenderer(), TextUtil.presLayer(config.enlightenText, clicker.enlightening, 30), config, 1);
                    if (clicker.awakening != 0 || clicker.perfection != 0)
                        drawText(context, this.getTextRenderer(), TextUtil.presLayer(config.awakenText, clicker.awakening, 30), config, 2);
                    if (clicker.perfection != 0)
                        drawText(context, this.getTextRenderer(), TextUtil.presLayer(config.perfectionText, clicker.perfection, 30), config, 3);
                } else {
                    drawText(context, this.getTextRenderer(), TextUtil.format(config.topText), config, 0);
                    drawText(context, this.getTextRenderer(), TextUtil.presLayer(config.prestigeText, clicker.prestige, -1), config, 1);
                    if (clicker.superprestige != 0 || clicker.rebirth != 0 || clicker.apotheosis != 0 || clicker.finalFruit != 0 || clicker.reincarnation != 0 || clicker.omega != 0 || clicker.reformation != 0 || clicker.reformation2 != 0)
                        drawText(context, this.getTextRenderer(), TextUtil.presLayer(config.superprestigeText, clicker.superprestige, 30), config, 2);
                    if (clicker.rebirth != 0 || clicker.apotheosis != 0 || clicker.finalFruit != 0 || clicker.reincarnation != 0 || clicker.omega != 0 || clicker.reformation != 0 || clicker.reformation2 != 0)
                        drawText(context, this.getTextRenderer(), TextUtil.presLayer(config.rebirthText, clicker.rebirth, 30), config, 3);
                    if (clicker.apotheosis != 0 || clicker.finalFruit != 0 || clicker.reincarnation != 0 || clicker.omega != 0 || clicker.reformation != 0 || clicker.reformation2 != 0)
                        drawText(context, this.getTextRenderer(), TextUtil.presLayer(config.apotheosisText, clicker.apotheosis, -1), config, 4);
                    if (clicker.finalFruit != 0 || clicker.reincarnation != 0 || clicker.omega != 0 || clicker.reformation != 0 || clicker.reformation2 != 0)
                        drawText(context, this.getTextRenderer(), TextUtil.presLayer(config.finalFruitText, clicker.finalFruit, 100), config, 5);
                    if (clicker.reincarnation != 0 || clicker.omega != 0 || clicker.reformation != 0 || clicker.reformation2 != 0)
                        drawText(context, this.getTextRenderer(), TextUtil.presLayer(config.reincarnationText, clicker.reincarnation, -1), config, 6);
                    if (clicker.omega != 0 || clicker.reformation != 0 || clicker.reformation2 != 0)
                        drawText(context, this.getTextRenderer(), TextUtil.presLayer(config.omegaText, clicker.omega, -1), config, 7);
                    if (clicker.reformation != 0 || clicker.reformation2 != 0)
                        drawText(context, this.getTextRenderer(), TextUtil.presLayer(config.reformationText, clicker.reformation, -1), config, 8);
                    if (clicker.reformation2 != 0)
                        drawText(context, this.getTextRenderer(), TextUtil.presLayer(config.reformation2Text, clicker.reformation2, -1), config, 9);
                }
            } else {
                if (inCosmos(client)) {
                    drawText(context, this.getTextRenderer(), TextUtil.format(config.cosmosTopText), config, 0);
                    drawText(context, this.getTextRenderer(), clicker.getCosmosText(config), config, 1);
                } else {
                    drawText(context, this.getTextRenderer(), TextUtil.format(config.topText), config, 0);
                    drawText(context, this.getTextRenderer(), clicker.getText(config), config, 1);
                }
            }
        }
    }

    @Unique
    private static boolean inCosmos(MinecraftClient client) {
        if (client.player == null) return false;
        double x = client.player.getX();
        double y = client.player.getY();
        double z = client.player.getZ();
        return 7185 <= x && x <= 7334 && 2 <= y && y <= 65 && 6765 <= z && z <= 6914;
    }

    @Unique
    private static void drawText(DrawContext context, TextRenderer renderer, Text text, Config cfg, int yOffset) {
        int windowWidth = MinecraftClient.getInstance().getWindow().getWidth();
        int windowHeight = MinecraftClient.getInstance().getWindow().getHeight();
        int textWidth = renderer.getWidth(text);

        int x = cfg.x;
        if (cfg.xCenter) {
            x = windowWidth / 2 / MinecraftClient.getInstance().options.getGuiScale().getValue();
        } else {
            if (x < 0) {
                x = (windowWidth + x - textWidth) / 2;
            } else {
                x = (x + textWidth) / 2;
            }
        }

        int y = cfg.y;
        if (cfg.yCenter) {
            y = windowHeight / 2 / MinecraftClient.getInstance().options.getGuiScale().getValue();
        } else {
            if (y < 0) {
                y = (windowHeight + y) / 2;
            }
        }

        context.drawCenteredTextWithShadow(renderer, text, x, 3 + y + 9 * yOffset, 1);
    }
}
