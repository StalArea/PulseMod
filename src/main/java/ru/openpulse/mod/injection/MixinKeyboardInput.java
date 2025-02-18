package ru.openpulse.mod.injection;

import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.openpulse.mod.PulseMod;
import ru.openpulse.mod.events.impl.EventKeyboardInput;
import ru.openpulse.mod.features.modules.Module;

@Mixin(KeyboardInput.class)
public class MixinKeyboardInput {
    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/input/KeyboardInput;sneaking:Z", shift = At.Shift.BEFORE), cancellable = true)
    private void onSneak(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
        if(Module.fullNullCheck()) return;
        EventKeyboardInput event = new EventKeyboardInput();
        PulseMod.EVENT_BUS.post(event);
        if (event.isCancelled()) ci.cancel();
    }
}
