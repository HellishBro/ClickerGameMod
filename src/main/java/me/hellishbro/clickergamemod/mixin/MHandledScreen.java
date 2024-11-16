package me.hellishbro.clickergamemod.mixin;

import me.hellishbro.clickergamemod.TextUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class MHandledScreen {
    @Inject(at=@At("RETURN"), method="render")
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (screen == null) return;
        String title = TextUtil.toSection(screen.getTitle());
        if (title.equals("&rCosmos Daily Reward ⇒ Puzzle ⁂")) {

        }
    }
}
