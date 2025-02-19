package ru.openpulse.mod.injection;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.Window;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.openpulse.mod.PulseMod;
import ru.openpulse.mod.core.manager.client.ModuleManager;
import ru.openpulse.mod.events.impl.*;
import ru.openpulse.mod.features.modules.Module;
import ru.openpulse.mod.gui.clickui.ClickGUI;
import ru.openpulse.mod.gui.font.FontRenderers;
import ru.openpulse.mod.utility.render.WindowResizeCallback;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Shadow
    @Final
    private Window window;

    @Shadow
    public abstract void setScreen(@Nullable Screen screen);

    @Unique
    private String[] shittyServers = {
            "mineblaze",
            "musteryworld",
            "dexland",
            "masedworld",
            "vimeworld",
            "hypemc",
            "vimemc"
    };

    @Inject(method = "<init>", at = @At("TAIL"))
    void postWindowInit(RunArgs args, CallbackInfo ci) {
        try {
            FontRenderers.settings = FontRenderers.create(12f, "comfortaa");
            FontRenderers.modules = FontRenderers.create(15f, "comfortaa");
            FontRenderers.categories = FontRenderers.create(18f, "comfortaa");
            FontRenderers.thglitch = FontRenderers.create(36f, "glitched");
            FontRenderers.thglitchBig = FontRenderers.create(72f, "glitched");
            FontRenderers.monsterrat = FontRenderers.create(18f, "monsterrat");
            FontRenderers.sf_bold = FontRenderers.create(16f, "sf_bold");
            FontRenderers.sf_medium = FontRenderers.create(16f, "sf_medium");
            FontRenderers.sf_medium_mini = FontRenderers.create(12f, "sf_medium");
            FontRenderers.sf_medium_modules = FontRenderers.create(14f, "sf_medium");
            FontRenderers.sf_bold_mini = FontRenderers.create(14f, "sf_bold");
            FontRenderers.sf_bold_micro = FontRenderers.create(12f, "sf_bold");
            FontRenderers.profont = FontRenderers.create(16f, "profont");
            FontRenderers.icons = FontRenderers.create(20, "icons");
            FontRenderers.mid_icons = FontRenderers.create(46, "icons");
            FontRenderers.big_icons = FontRenderers.create(72, "icons");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    void preTickHook(CallbackInfo ci) {
        if (!Module.fullNullCheck()) PulseMod.EVENT_BUS.post(new EventTick());
    }

    @Inject(method = "tick", at = @At("RETURN"))
    void postTickHook(CallbackInfo ci) {
        if (!Module.fullNullCheck()) PulseMod.EVENT_BUS.post(new EventPostTick());
    }

    @Inject(method = "onResolutionChanged", at = @At("TAIL"))
    private void captureResize(CallbackInfo ci) {
        WindowResizeCallback.EVENT.invoker().onResized((MinecraftClient) (Object) this, this.window);
    }


    @Inject(method = "doItemPick", at = @At("HEAD"), cancellable = true)
    private void doItemPickHook(CallbackInfo ci) {
        if (ModuleManager.middleClick.isEnabled() && ModuleManager.middleClick.antiPickUp.getValue())
            ci.cancel();
    }

    @Inject(method = "setOverlay", at = @At("HEAD"))
    public void setOverlay(Overlay overlay, CallbackInfo ci) {
        //   if (overlay instanceof SplashOverlay)
        //  Managers.SHADER.reloadShaders();
    }

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    public void setScreenHookPre(Screen screen, CallbackInfo ci) {
        if (Module.fullNullCheck()) return;
        EventScreen event = new EventScreen(screen);
        PulseMod.EVENT_BUS.post(event);
        if (event.isCancelled() || (ClickGUI.close && screen == null)) ci.cancel();
    }

    @Inject(method = "setScreen", at = @At("RETURN"))
    public void setScreenHookPost(Screen screen, CallbackInfo ci) {
        if (Module.fullNullCheck()) return;
        if (screen instanceof MultiplayerScreen mScreen && ModuleManager.antiServerAdd.isEnabled() && mScreen.getServerList() != null) {
            for (int i = 0; i < mScreen.getServerList().size(); i++) {
                ServerInfo info = mScreen.getServerList().get(i);
                for (String server : shittyServers) {
                    if (info != null && info.address != null && info.address.toLowerCase().contains(server.toLowerCase())) {
                        mScreen.getServerList().remove(info);
                        mScreen.getServerList().saveFile();
                        setScreen(screen);
                        break;
                    }
                }
            }
        }
    }

    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void doAttackHook(CallbackInfoReturnable<Boolean> cir) {
        final EventAttack event = new EventAttack(null, true);
        PulseMod.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    private void handleBlockBreakingHook(boolean breaking, CallbackInfo ci) {
        EventHandleBlockBreaking event = new EventHandleBlockBreaking();
        PulseMod.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
