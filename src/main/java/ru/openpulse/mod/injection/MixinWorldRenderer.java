package ru.openpulse.mod.injection;

import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.openpulse.mod.core.Managers;
import ru.openpulse.mod.core.manager.client.ModuleManager;
import ru.openpulse.mod.core.manager.client.ShaderManager;

import static ru.openpulse.mod.features.modules.Module.mc;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;setupTerrain(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/Frustum;ZZ)V"), index = 3)
    private boolean renderSetupTerrainModifyArg(boolean spectator) {
        return ModuleManager.freeCam.isEnabled() || spectator;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/PostEffectProcessor;render(F)V", ordinal = 0))
    private void replaceShaderHook(PostEffectProcessor instance, float tickDelta) {
        ShaderManager.Shader shaders = ModuleManager.shaders.mode.getValue();
        if (ModuleManager.shaders.isEnabled() && mc.world != null) {
            if (Managers.SHADER.fullNullCheck()) return;
            Managers.SHADER.setupShader(shaders, Managers.SHADER.getShaderOutline(shaders));
        } else {
            instance.render(tickDelta);
        }
    }

    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    private void renderWeatherHook(LightmapTextureManager manager, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        if (ModuleManager.noRender.isEnabled() && ModuleManager.noRender.noWeather.getValue()) {
            ci.cancel();
        }
    }
}
