package me.hellishbro.clickergamemod;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.hellishbro.clickergamemod.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClickerGameMod implements ClientModInitializer {
    public static Logger LOGGER = LogManager.getLogger("ClickerGameMod");
    public static PlusOneClicker stats = new PlusOneClicker();
    public static boolean CLICKING = false;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Mod initialized.");

        TimedScheduler.init();

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            Config.getInstance().save();
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(literalArgumentBuilder("cgmloadcfg").executes(context -> {
                try {
                    Config.setConfig(Config.load());
                } catch (Exception e) {
                    Config.setConfig(new Config());
                }
                return 1;
            }));
        });
    }

    private static LiteralArgumentBuilder<FabricClientCommandSource> literalArgumentBuilder(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }
}
