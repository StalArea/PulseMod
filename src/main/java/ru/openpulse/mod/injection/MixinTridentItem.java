package ru.openpulse.mod.injection;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.openpulse.mod.PulseMod;
import ru.openpulse.mod.core.manager.client.ModuleManager;
import ru.openpulse.mod.events.impl.UseTridentEvent;

import static ru.openpulse.mod.PulseMod.mc;

@Mixin(TridentItem.class)
public abstract class MixinTridentItem {

    @Inject(method = "onStoppedUsing", at = @At(value = "HEAD"), cancellable = true)
    public void onStoppedUsingHook(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (user == mc.player && EnchantmentHelper.getTridentSpinAttackStrength(stack, mc.player) > 0) {
            UseTridentEvent e = new UseTridentEvent();
            PulseMod.EVENT_BUS.post(e);
            if (e.isCancelled())
                ci.cancel();
        }
    }

    @Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
    public void useHook(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (EnchantmentHelper.getTridentSpinAttackStrength(itemStack, user) > 0 && !user.isTouchingWaterOrRain() && ModuleManager.tridentBoost.isEnabled() && ModuleManager.tridentBoost.anyWeather.getValue()) {
            user.setCurrentHand(hand);
            cir.setReturnValue(TypedActionResult.consume(itemStack));
        }
    }
}
