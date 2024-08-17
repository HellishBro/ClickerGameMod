package me.hellishbro.clickergamemod.mixin;

import me.hellishbro.clickergamemod.ClickerGameMod;
import me.hellishbro.clickergamemod.TextUtil;
import me.hellishbro.clickergamemod.TimedScheduler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class MHandledScreen {
    @Inject(at=@At("RETURN"), method="onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V")
    private void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo ci) {
        if (slot != null) slotId = slot.id;
        Screen currentScreen = MinecraftClient.getInstance().currentScreen;
        assert currentScreen != null;
        String name = TextUtil.toSection(currentScreen.getTitle());
        if (
                (name.equals("Cosmos Enlightening") && slotId == 13)
                || (name.equals("Cosmos Awakening") && (slotId == 11 || slotId == 15))
                || (name.equals("Cosmos Perfection") && (slotId == 11 || slotId == 15))
        ) {
            TimedScheduler.scheduleTask(new TimedScheduler.ScheduledTask(2, () -> {
                ClickerGameMod.REQUEST_COSMOS = true;
            }));
        }
    }
}
