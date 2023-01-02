package org.infernalstudios.jsonentitymodels.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ShieldItem;
import org.infernalstudios.jsonentitymodels.client.model.ReplacedZombieModel;
import org.infernalstudios.jsonentitymodels.entity.ReplacedZombieEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoReplacedEntityRenderer;

public class ReplacedZombieRenderer extends GeoReplacedEntityRenderer<ReplacedZombieEntity> {
    Zombie zombie;
    public ResourceLocation whTexture;
    public ReplacedZombieRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ReplacedZombieModel(), new ReplacedZombieEntity());
    }

    @Override
    public void render(Entity entity, IAnimatable animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity instanceof Zombie zombieEntity && animatable instanceof ReplacedZombieEntity replacedZombie) {
            this.zombie = zombieEntity;
            replacedZombie.setHurt(zombieEntity.hurtTime > 0);
            replacedZombie.setAttacking(zombieEntity.isAggressive());
        }

        this.whTexture = this.getTextureLocation(animatable);

        super.render(entity, animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("rightarm") && this.zombie != null && !this.zombie.isInvisible()) {
            stack.pushPose();

            if (this.zombie.getMainHandItem().getItem() instanceof ShieldItem) {
                stack.translate(0.1D, 1.4D, -0.5D);
                stack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
            } else {
                stack.translate(0.35D, 1.3D, -0.7D);
            }

            Minecraft.getInstance().getItemRenderer().renderStatic(this.zombie.getMainHandItem(), ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb, 0);
            stack.popPose();
            bufferIn = this.rtb.getBuffer(RenderType.entityTranslucent(this.whTexture));
        } else if (bone.getName().equals("leftarm") && this.zombie != null && !this.zombie.isInvisible()) {
            stack.pushPose();

            if (this.zombie.getOffhandItem().getItem() instanceof ShieldItem) {
                stack.translate(-1.6D, 1.4D, -0.5D);
                stack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
            } else {
                stack.translate(-0.35D, 1.6D, -0.92D);
                stack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            }

            Minecraft.getInstance().getItemRenderer().renderStatic(this.zombie.getOffhandItem(), ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb, 0);
            stack.popPose();
            bufferIn = this.rtb.getBuffer(RenderType.entityTranslucent(this.whTexture));
        }

        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

}
