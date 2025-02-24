package ru.openpulse.mod.injection;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.openpulse.mod.core.manager.client.ModuleManager;
import ru.openpulse.mod.features.modules.client.ClientSettings;
import ru.openpulse.mod.gui.mainmenu.MainMenuScreen;
import ru.openpulse.mod.gui.misc.DialogScreen;
import ru.openpulse.mod.utility.render.TextureStorage;

import static ru.openpulse.mod.features.modules.Module.mc;
import static ru.openpulse.mod.features.modules.client.ClientSettings.isRu;

@Mixin(TitleScreen.class)
public class MixinTitleScreen extends Screen {
    protected MixinTitleScreen(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void postInitHook(CallbackInfo ci) {
        if (ClientSettings.customMainMenu.getValue() && !MainMenuScreen.getInstance().confirm && ModuleManager.clickGui.getBind().getKey() != -1) {
            mc.setScreen(MainMenuScreen.getInstance());
        }
        if (ModuleManager.clickGui.getBind().getKey() == -1) {
            DialogScreen dialogScreen2 = new DialogScreen(
                    TextureStorage.cutie,
                    isRu() ? "Спасибо что скачали PulseMod!" : "Thank you for downloading PulseMod!",
                    isRu() ? "Меню с функциями клиента открывается на клавишу - RShift" : "Menu with client modules is opened with the key - P",
                    isRu() ? "Зайти в майн" : "Join on minecraft",
                    isRu() ? "Закрыть майн" : "Close minecraft",
                    () -> {
                        ModuleManager.clickGui.setBind(InputUtil.fromTranslationKey("key.keyboard.right.shift").getCode(), false, false);
                        mc.setScreen(MainMenuScreen.getInstance());
                    },
                    () -> {
                        ModuleManager.clickGui.setBind(InputUtil.fromTranslationKey("key.keyboard.right.shift").getCode(), false, false);
                        mc.stop();
                    }
            );
            DialogScreen dialogScreen1 = new DialogScreen(
                    TextureStorage.questionPic,
                    "Hello!",
                    "What's your language?",
                    "Русский",
                    "English",
                    () -> {
                        ClientSettings.language.setValue(ClientSettings.Language.RU);
                        mc.setScreen(dialogScreen2);
                    },
                    () -> {
                        ClientSettings.language.setValue(ClientSettings.Language.ENG);
                        mc.setScreen(dialogScreen2);
                    }
            );
            mc.setScreen(dialogScreen1);
        }

//        if (ThunderHack.isOutdated && !FabricLoader.getInstance().isDevelopmentEnvironment()) {
//            mc.setScreen(new ConfirmScreen(
//                    confirm -> {
//                        if (confirm) Util.getOperatingSystem().open(URI.create("https://github.com/StalArea/PulseMod/releases/download/latest/..."));
//                        else mc.stop();
//                    },
//                    Text.of(Formatting.RED + "You are using an outdated version of PulseMod"), Text.of("Please update to the latest release"), Text.of("Download"), Text.of("Quit Game")));
//        }
    }
}
