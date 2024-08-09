package me.hellishbro.clickergamemod.mixin;

import me.hellishbro.clickergamemod.ClickerGameMod;
import me.hellishbro.clickergamemod.TextUtil;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MClientPlayNetworkHandler {
    @Inject(at=@At("HEAD"), method="onGameMessage", cancellable=true)
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        if (ClickerGameMod.REQUESTING_STATS != 0) {
            Text content = packet.content();
            String raw = TextUtil.toSection(content);
            if (ClickerGameMod.REQUESTING_STATS == 3) {
                if (!raw.endsWith("'s tags:")) {
                    ClickerGameMod.REQUESTING_STATS = 0;
                    return;
                }
            }
            ClickerGameMod.REQUESTING_STATS--;
            ClickerGameMod.stats.cosmosFromText(content);
            ci.cancel();
        }
    }
}
