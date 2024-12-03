package me.hellishbro.clickergamemod.mixin;

import me.hellishbro.clickergamemod.ClickerGameMod;
import me.hellishbro.clickergamemod.TextUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MClientPlayNetworkHandler {
    @Inject(at=@At("HEAD"), method="onPlayerListHeader")
    private void onGameMessage(PlayerListHeaderS2CPacket packet, CallbackInfo ci) {
        if (ClickerGameMod.CLICKING) {
            Text content = packet.footer();
            if (inCosmos(MinecraftClient.getInstance())) {
                ClickerGameMod.stats.cosmosFromText(content);
            } else {
                ClickerGameMod.stats.fromText(content);
            }
        }
        ClickerGameMod.CLICKING = TextUtil.toSection(packet.header()).contains("&a+1 clicker");
    }

    @Unique
    private static boolean inCosmos(MinecraftClient client) {
        if (client.player == null) return false;
        double x = client.player.getX();
        double y = client.player.getY();
        double z = client.player.getZ();
        return 7185 <= x && x <= 7334 && 2 <= y && y <= 65 && 6765 <= z && z <= 6914;
    }
}
