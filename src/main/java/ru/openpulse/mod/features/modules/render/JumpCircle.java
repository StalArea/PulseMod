package ru.openpulse.mod.features.modules.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import ru.openpulse.mod.features.modules.Module;
import ru.openpulse.mod.features.modules.client.HudEditor;
import ru.openpulse.mod.setting.Setting;
import ru.openpulse.mod.utility.PulseUtility;
import ru.openpulse.mod.utility.Timer;
import ru.openpulse.mod.utility.render.Render2DEngine;
import ru.openpulse.mod.utility.render.TextureStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import static ru.openpulse.mod.features.modules.client.ClientSettings.isRu;
import static ru.openpulse.mod.utility.render.Render2DEngine.applyOpacity;

public class JumpCircle extends Module {
    public JumpCircle() {
        super("JumpCircle", Category.RENDER);
    }

    private final Setting<Mode> mode = new Setting<>("Mode", Mode.Default);
    private final Setting<Boolean> easeOut = new Setting<>("EaseOut", true);
    private final Setting<Float> rotateSpeed = new Setting<>("RotateSpeed", 2f, 0.5f, 5f);
    private final Setting<Float> circleScale = new Setting<>("CircleScale", 1f, 0.5f, 5f);
    private final Setting<Boolean> onlySelf = new Setting<>("OnlySelf", false);
    private final List<Circle> circles = new ArrayList<>();
    private final List<PlayerEntity> cache = new CopyOnWriteArrayList<>();
    private Identifier custom;

    @Override
    public void onEnable() {
        try {
            custom = PulseUtility.getCustomImg("circle");
        } catch (Exception e) {
            sendMessage(e.getMessage());
        }
    }

    @Override
    public void onUpdate() {
        if (mode.is(Mode.Custom) && custom == null) {
            try {
                custom = PulseUtility.getCustomImg("circle");
            } catch (Exception e) {
                sendMessage(isRu() ? "circle.png не найден, добавьте его по указанному нижу пути. Режим изменён на Default." : "circle.png not found, add it to the specified path below. Mode changed to Default.");
                sendMessage(".minecraft -> PlasmoVoice -> misc -> images -> circle.png");
                mode.setValue(Mode.Default);
            }
        }

        for (PlayerEntity pl : mc.world.getPlayers())
            if (!cache.contains(pl) && pl.isOnGround() && (mc.player == pl || !onlySelf.getValue()))
                cache.add(pl);

        cache.forEach(pl -> {
            if (pl != null && !pl.isOnGround()) {
                circles.add(new Circle(new Vec3d(pl.getX(), (int) Math.floor(pl.getY()) + 0.001f, pl.getZ()), new Timer()));
                cache.remove(pl);
            }
        });

        circles.removeIf(c -> c.timer.passedMs(easeOut.getValue() ? 5000 : 6000));
    }

    public void onRender3D(MatrixStack stack) {
        Collections.reverse(circles);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);

        switch (mode.getValue()) {
            case Portal -> RenderSystem.setShaderTexture(0, TextureStorage.bubble);
            case Default -> RenderSystem.setShaderTexture(0, TextureStorage.default_circle);
            case Custom ->
                    RenderSystem.setShaderTexture(0, Objects.requireNonNullElse(custom, TextureStorage.default_circle));
        }

        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        for (Circle c : circles) {
            float colorAnim = (float) (c.timer.getPassedTimeMs()) / 6000f;
            float sizeAnim = circleScale.getValue() - (float) Math.pow(1 - ((c.timer.getPassedTimeMs() * (easeOut.getValue() ? 2f : 1f)) / 5000f), 4);

            stack.push();
            stack.translate(c.pos().x - mc.getEntityRenderDispatcher().camera.getPos().getX(), c.pos().y - mc.getEntityRenderDispatcher().camera.getPos().getY(), c.pos().z - mc.getEntityRenderDispatcher().camera.getPos().getZ());
            stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
            stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(sizeAnim * rotateSpeed.getValue() * 1000f));
            float scale = sizeAnim * 2f;
            Matrix4f matrix = stack.peek().getPositionMatrix();

            buffer.vertex(matrix, -sizeAnim, -sizeAnim + scale, 0).texture(0, 1).color(applyOpacity(HudEditor.getColor(270), 1f - colorAnim).getRGB());
            buffer.vertex(matrix, -sizeAnim + scale, -sizeAnim + scale, 0).texture(1, 1).color(applyOpacity(HudEditor.getColor(0), 1f - colorAnim).getRGB());
            buffer.vertex(matrix, -sizeAnim + scale, -sizeAnim, 0).texture(1, 0).color(applyOpacity(HudEditor.getColor(180), 1f - colorAnim).getRGB());
            buffer.vertex(matrix, -sizeAnim, -sizeAnim, 0).texture(0, 0).color(applyOpacity(HudEditor.getColor(90), 1f - colorAnim).getRGB());

            stack.pop();
        }

        Render2DEngine.endBuilding(buffer);
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.enableDepthTest();
        Collections.reverse(circles);
    }

    public enum Mode {
        Default, Portal, Custom
    }

    public record Circle(Vec3d pos, Timer timer) {
    }
}