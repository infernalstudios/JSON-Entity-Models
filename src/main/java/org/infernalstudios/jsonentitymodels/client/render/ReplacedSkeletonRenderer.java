package org.infernalstudios.jsonentitymodels.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.infernalstudios.jsonentitymodels.client.model.ReplacedSkeletonModel;
import org.infernalstudios.jsonentitymodels.entity.ReplacedSkeletonEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;

public class ReplacedSkeletonRenderer extends ExtendedGeoReplacedEntityRenderer<ReplacedSkeletonEntity, Skeleton> {

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
            replacedSkeleton.setAiming(skeletonEntity.getMainHandItem().getItem() instanceof BowItem && skeletonEntity.isUsingItem());
        }

        super.render(entity, animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    protected boolean isArmorBone(GeoBone bone) {
        return false;
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, Skeleton animatable) {
        return null;
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, Skeleton animatable) {
        if (boneName.equals("rightitem")) {
            return animatable.getMainHandItem();
        } else if (boneName.equals("leftitem")) {
            return animatable.getOffhandItem();
        }

        return null;
    }

    @Override
    protected ItemTransforms.TransformType getCameraTransformForItemAtBone(ItemStack stack, String boneName) {
        if (boneName.equals("rightitem")) {
            return ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
        } else if (boneName.equals("leftitem")) {
            return ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND;
        }

        return null;
    }

    @Nullable
    @Override
    protected BlockState getHeldBlockForBone(String boneName, Skeleton animatable) {
        return null;
    }

    @Override
    protected void preRenderItem(PoseStack poseStack, ItemStack stack, String boneName, Skeleton animatable, IBone bone) {

    }

    @Override
    protected void preRenderBlock(PoseStack poseStack, BlockState state, String boneName, Skeleton animatable) {

    }

    @Override
    protected void postRenderItem(PoseStack poseStack, ItemStack stack, String boneName, Skeleton animatable, IBone bone) {

    }

    @Override
    protected void postRenderBlock(PoseStack poseStack, BlockState state, String boneName, Skeleton animatable) {

    }
}
