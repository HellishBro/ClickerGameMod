package me.hellishbro.clickergamemod.mixin;

import me.hellishbro.clickergamemod.ClickerGameMod;
import me.hellishbro.clickergamemod.TextUtil;
import net.minecraft.client.MinecraftClient;
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
        if (ClickerGameMod.GET_COSMOS) {
            Text content = packet.content();
            String raw = TextUtil.toSection(content);
            ClickerGameMod.LOGGER.info(raw);
            if (ClickerGameMod.STATS_COMMAND_RUNNER.isEmpty()) {
                if (raw.endsWith("'s tags:")) {
                    ClickerGameMod.STATS_COMMAND_RUNNER = raw.replaceAll("&r", "").split("'")[0];
                    ClickerGameMod.LOGGER.info("@stats runner: {}", ClickerGameMod.STATS_COMMAND_RUNNER);
                }
            } else {
                if (ClickerGameMod.STATS_COMMAND_RUNNER.equals(MinecraftClient.getInstance().getSession().getUsername())) {
                    ClickerGameMod.LOGGER.info("@stats runner == player name");
                    if (raw.startsWith("&r&f&r&#20332E") || raw.startsWith("&r&f&r&8[&r&#F155CE") || raw.startsWith("&r&f&r&8[&r&#CE55F1") || raw.startsWith("&r&f&r&8[&r&#3746EF")) {
                        ClickerGameMod.stats.cosmosFromText(content);
                        ClickerGameMod.LOGGER.info("Cosmos tag get.");
                        ClickerGameMod.GET_COSMOS = false;
                        ClickerGameMod.STATS_COMMAND_RUNNER = "";
                    }
                }
            }
            ci.cancel();
        }
    }
}
