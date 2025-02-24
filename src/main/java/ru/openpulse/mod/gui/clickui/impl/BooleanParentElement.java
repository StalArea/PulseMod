package ru.openpulse.mod.gui.clickui.impl;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.lwjgl.glfw.GLFW;
import ru.openpulse.mod.core.Managers;
import ru.openpulse.mod.features.modules.client.HudEditor;
import ru.openpulse.mod.gui.clickui.AbstractElement;
import ru.openpulse.mod.gui.clickui.ClickGUI;
import ru.openpulse.mod.gui.font.FontRenderers;
import ru.openpulse.mod.setting.Setting;
import ru.openpulse.mod.setting.impl.BooleanSettingGroup;
import ru.openpulse.mod.utility.render.Render2DEngine;
import ru.openpulse.mod.utility.render.TextureStorage;

import java.awt.*;

import static ru.openpulse.mod.core.manager.IManager.mc;
import static ru.openpulse.mod.utility.render.animation.AnimationUtility.fast;

public class BooleanParentElement extends AbstractElement {
    private final Setting<BooleanSettingGroup> parentSetting;

    public BooleanParentElement(Setting setting) {
        super(setting);
        this.parentSetting = setting;
    }

    float animation, arrowAnimation;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        MatrixStack matrixStack = context.getMatrices();

        float tx = x + width - 11;
        float ty = y + 7.5f;

        arrowAnimation = fast(arrowAnimation, getParentSetting().getValue().isExtended() ? 0 : 1, 15f);

        matrixStack.push();
        matrixStack.translate(tx, ty, 0);
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-180f * arrowAnimation));
        matrixStack.translate(-tx, -ty, 0);
        matrixStack.translate((x + width - 14), (y + 4.5f), 0);
        context.drawTexture(TextureStorage.guiArrow, 0, 0, 0, 0, 6, 6, 6, 6);
        matrixStack.translate(-(x + width - 14), -(y + 4.5f), 0);
        matrixStack.pop();

        FontRenderers.sf_medium_mini.drawString(matrixStack, setting.getName(), x + 6, y + height / 2 - 1f, new Color(-1).getRGB());
        animation = fast(animation, getParentSetting().getValue().isEnabled() ? 1 : 0, 15f);
        float paddingX = 7f * animation;
        Color color = HudEditor.getColor(0);
        Render2DEngine.drawRound(context.getMatrices(), x + width - 36, y + height / 2 - 4, 15, 8, 1, paddingX > 4 ? color : new Color(0x28FFFFFF, true));
        Render2DEngine.drawRound(context.getMatrices(), x + width - 35f + paddingX, y + height / 2 - 3, 6, 6, 1, new Color(-1));

        if (7f * animation > 4) {
            FontRenderers.sf_bold_mini.drawString(context.getMatrices(), "v", x + width - 34f, y + height / 2 - 2f, new Color(-1).getRGB());
        } else {
            FontRenderers.sf_bold_mini.drawString(context.getMatrices(), "x", x + width - 27f, y + height / 2 - 2f, new Color(-1).getRGB());
        }

        if (Render2DEngine.isHovered(mouseX, mouseY, x + width - 36, y + height / 2 - 4, 15, 8)) {
            if (GLFW.glfwGetPlatform() != GLFW.GLFW_PLATFORM_WAYLAND) {
                GLFW.glfwSetCursor(mc.getWindow().getHandle(),
                        GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR));
            }
            ClickGUI.anyHovered = true;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (hovered) {
            if (button == 0) {
                getParentSetting().getValue().setEnabled(!getParentSetting().getValue().isEnabled());
                Managers.SOUND.playBoolean();
            } else {
                getParentSetting().getValue().setExtended(!getParentSetting().getValue().isExtended());
                if (getParentSetting().getValue().isExtended()) {
                    Managers.SOUND.playSwipeIn();
                } else {
                    Managers.SOUND.playSwipeOut();
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    public Setting<BooleanSettingGroup> getParentSetting() {
        return parentSetting;
    }
}
