package org.infernalstudios.jsonentitymodels.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.infernalstudios.jsonentitymodels.client.model.ReplacedEnderManModel;
import org.infernalstudios.jsonentitymodels.entity.ReplacedEnderManEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.processor.IBone;

public class ReplacedEnderManRenderer extends ExtendedGeoReplacedEntityRenderer<ReplacedEnderManEntity, EnderMan> {

    public ReplacedEnderManRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ReplacedEnderManModel(), new ReplacedEnderManEntity());
    }

    @Override
    public void render(Entity entity, IAnimatable animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity instanceof EnderMan enderman && animatable instanceof ReplacedEnderManEntity replacedEnderMan) {
            replacedEnderMan.setScreaming(enderman.isCreepy());
            replacedEnderMan.setHoldingBlock(enderman.getCarriedBlock() != null);
        }

        super.render(entity, animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EnderMan animatable) {
        if (boneName.equals("block")) {
            return animatable.getCarriedBlock().getBlock().asItem().getDefaultInstance().copy();
        }

        return super.getHeldItemForBone(boneName, animatable);
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EnderMan animatable) {
        return null;
    }

    @Nullable
    @Override
    protected BlockState getHeldBlockForBone(String boneName, EnderMan animatable) {
        return null;
    }

    @Override
    protected void preRenderItem(PoseStack poseStack, ItemStack stack, String boneName, EnderMan animatable, IBone bone) {
        if (boneName.equals("block")) {
            poseStack.pushPose();
            poseStack.scale(0.7F, 0.7F, 0.7F);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        }
    }

    @Override
    protected void preRenderBlock(PoseStack poseStack, BlockState state, String boneName, EnderMan animatable) {

    }

    @Override
    protected void postRenderItem(PoseStack poseStack, ItemStack stack, String boneName, EnderMan animatable, IBone bone) {
        if (boneName.equals("block")) {
            poseStack.popPose();
        }
    }

    @Override
    protected void postRenderBlock(PoseStack poseStack, BlockState state, String boneName, EnderMan animatable) {

    }
}
