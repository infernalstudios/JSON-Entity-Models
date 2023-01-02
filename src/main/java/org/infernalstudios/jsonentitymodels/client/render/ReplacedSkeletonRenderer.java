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
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.BowItem;
import org.infernalstudios.jsonentitymodels.client.model.ReplacedSkeletonModel;
import org.infernalstudios.jsonentitymodels.entity.ReplacedSkeletonEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoReplacedEntityRenderer;

public class ReplacedSkeletonRenderer extends GeoReplacedEntityRenderer<ReplacedSkeletonEntity> {
    Skeleton skeleton;
    public ResourceLocation whTexture;

    public ReplacedSkeletonRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ReplacedSkeletonModel(), new ReplacedSkeletonEntity());
    }


    @Override
    public RenderType getRenderType(Object animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(texture);
    }

    @Override
    public void render(Entity entity, IAnimatable animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity instanceof Skeleton skeletonEntity && animatable instanceof ReplacedSkeletonEntity replacedSkeleton) {
            this.skeleton = skeletonEntity;
            replacedSkeleton.setHurt(skeletonEntity.hurtTime > 0);
            replacedSkeleton.setAiming(skeletonEntity.getMainHandItem().getItem() instanceof BowItem && skeletonEntity.isUsingItem());
        }

        this.whTexture = this.getTextureLocation(animatable);

        super.render(entity, animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("rightitem") && this.skeleton != null && !this.skeleton.isInvisible()) {
            stack.pushPose();
            stack.translate(0.4D, 0.8D, 0.2D);
            stack.mulPose(Vector3f.XN.rotationDegrees(180.0F));
            Minecraft.getInstance().getItemRenderer().renderStatic(this.skeleton.getMainHandItem(), ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb, 0);
            stack.popPose();
            bufferIn = this.rtb.getBuffer(RenderType.entityTranslucent(this.whTexture));
        }

        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
