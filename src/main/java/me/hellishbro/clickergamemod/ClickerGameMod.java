package me.hellishbro.clickergamemod;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.hellishbro.clickergamemod.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class ClickerGameMod implements ClientModInitializer {
    public static Logger LOGGER = LogManager.getLogger("ClickerGameMod");
    public static PlusOneClicker stats = new PlusOneClicker();
    public static boolean CLICKING = false;
    public static boolean GET_COSMOS = false;
    public static boolean REQUEST_COSMOS = false; // for other threads
    public static String STATS_COMMAND_RUNNER = "";

    @Override
    public void onInitializeClient() {
        LOGGER.info("Mod initialized.");

        TimedScheduler.init();

        ClientTickEvents.START_CLIENT_TICK.register((client -> {
            boolean prevClicking = CLICKING;
            CLICKING = false;
            if (client.world != null) {
                for (Entity entity : client.world.getEntities()) {
                    boolean isThisEntity;
                    assert client.player != null;
                    double x = client.player.getX();
                    double y = client.player.getY();
                    double z = client.player.getZ();
                    if (6616 <= z && z <= 6667 && 7284 <= x && x <= 7334 && 50 <= y && y <= 76) { // in memory
                        isThisEntity = entity.getPos().isInRange(new Vec3d(7291.5, 51.0, 6623.5), 0.1) && entity.getType() == EntityType.INTERACTION;
                    } else {
                        isThisEntity = entity.getPos().isInRange(new Vec3d(7042.5, 51.0, 6622.5), 0.1) && entity.getType() == EntityType.INTERACTION;
                    }
                    if (isThisEntity && MinecraftClient.getInstance().getNetworkHandler() != null) {
                        String address = Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler().getServerInfo()).address.replaceFirst(":25565", "");
                        if (address.startsWith("mcdiamondfire")) {
                            stats = PlusOneClicker.fromText(Objects.requireNonNull(entity.getCustomName()), stats);
                            if (!prevClicking) TimedScheduler.scheduleTask(new TimedScheduler.ScheduledTask(10, ClickerGameMod::getCosmos));
                            CLICKING = true;
                            break;
                        }
                    }
                }
            }
            if (REQUEST_COSMOS) {
                REQUEST_COSMOS = false;
                getCosmos();
            }
        }));

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
            dispatcher.register(literalArgumentBuilder("requeststats").executes(context -> {
                MinecraftClient.getInstance().player.sendMessage(TextUtil.fromString("§aClickerGameMod§f: Getting Cosmos tags."));
                getCosmos();
                return 1;
            }));
        });
    }

    private static LiteralArgumentBuilder<FabricClientCommandSource> literalArgumentBuilder(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    public static void getCosmos() {
        GET_COSMOS = true;
        MinecraftClient.getInstance().getNetworkHandler().sendChatMessage("@stats");
        TimedScheduler.scheduleTask(new TimedScheduler.ScheduledTask(10, () -> {
            if (GET_COSMOS) {
                GET_COSMOS = false;
                MinecraftClient.getInstance().player.sendMessage(TextUtil.fromString("§aClickerGameMod§f: §cCosmos tag get timed out. Please run §b/requeststats§c a bit later."));
            }
        }));
    }
}
