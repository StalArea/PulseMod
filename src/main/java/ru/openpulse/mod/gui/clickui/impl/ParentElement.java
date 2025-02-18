package ru.openpulse.mod.gui.clickui.impl;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import ru.openpulse.mod.core.Managers;
import ru.openpulse.mod.gui.clickui.AbstractElement;
import ru.openpulse.mod.gui.font.FontRenderers;
import ru.openpulse.mod.setting.Setting;
import ru.openpulse.mod.setting.impl.SettingGroup;
import ru.openpulse.mod.utility.render.TextureStorage;

import java.awt.*;

import static ru.openpulse.mod.utility.render.animation.AnimationUtility.fast;

public class ParentElement extends AbstractElement {
    private final Setting<SettingGroup> parentSetting;
    private float animation;

    public ParentElement(Setting setting) {
        super(setting);
        this.parentSetting = setting;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        MatrixStack matrixStack = context.getMatrices();

        float tx = x + width - 11;
        float ty = y + 7.5f;

        animation = fast(animation, getParentSetting().getValue().isExtended() ? 0 : 1, 15f);

        matrixStack.push();
        matrixStack.translate(tx, ty, 0);
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-180f * animation));
        matrixStack.translate(-tx, -ty, 0);
        matrixStack.translate((x + width - 14), (y + 4.5f), 0);
        context.drawTexture(TextureStorage.guiArrow, 0, 0, 0, 0, 6, 6, 6, 6);
        matrixStack.translate(-(x + width - 14), -(y + 4.5f), 0);
        matrixStack.pop();

        FontRenderers.sf_medium_mini.drawString(matrixStack, setting.getName(), x + 6 + (6 * getParentSetting().getValue().getHierarchy()), y + height / 2 - 1f, new Color(-1).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (hovered) {
            getParentSetting().getValue().setExtended(!getParentSetting().getValue().isExtended());
            if (getParentSetting().getValue().isExtended()) {
                Managers.SOUND.playSwipeIn();
            } else {
                Managers.SOUND.playSwipeOut();
            }
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    public Setting<SettingGroup> getParentSetting() {
        return parentSetting;
    }
}