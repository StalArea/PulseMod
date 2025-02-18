package ru.openpulse.mod.injection;

import net.minecraft.client.render.block.entity.MobSpawnerBlockEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.openpulse.mod.core.manager.client.ModuleManager;
import ru.openpulse.mod.features.modules.Module;

@Mixin(MobSpawnerBlockEntityRenderer.class)
public class MixinMobSpawnerBlockEntityRenderer {

    @Inject(method = "render(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/Entity;Lnet/minecraft/client/render/entity/EntityRenderDispatcher;DD)V", at = @At("HEAD"), cancellable = true)
    private static void renderHook(CallbackInfo ci) {
        if (!Module.fullNullCheck() && ModuleManager.noRender.isOn() && ModuleManager.noRender.spawnerEntity.getValue())
            ci.cancel();
    }
}
