package org.infernalstudios.jsonentitymodels.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.infernalstudios.jsonentitymodels.client.model.ReplacedIronGolemModel;
import org.infernalstudios.jsonentitymodels.client.render.layer.IronGolemCrackLayer;
import org.infernalstudios.jsonentitymodels.entity.ReplacedIronGolemEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.processor.IBone;

public class ReplacedIronGolemRenderer extends ExtendedGeoReplacedEntityRenderer<ReplacedIronGolemEntity, IronGolem> {

    public ReplacedIronGolemRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ReplacedIronGolemModel(), new ReplacedIronGolemEntity());
        this.addLayer(new IronGolemCrackLayer(this));
    }

    @Override
    public void render(Entity entity, IAnimatable animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity instanceof IronGolem ironGolem && animatable instanceof ReplacedIronGolemEntity replacedIronGolem) {
            replacedIronGolem.setAttacking(ironGolem.getAttackAnimationTick() > 0);
        }

        super.render(entity, animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, IronGolem animatable) {
        return null;
    }

    @Nullable
    @Override
    protected BlockState getHeldBlockForBone(String boneName, IronGolem animatable) {
        return null;
    }

    @Override
    protected void preRenderItem(PoseStack poseStack, ItemStack stack, String boneName, IronGolem animatable, IBone bone) {

    }

    @Override
    protected void preRenderBlock(PoseStack poseStack, BlockState state, String boneName, IronGolem animatable) {

    }

    @Override
    protected void postRenderItem(PoseStack poseStack, ItemStack stack, String boneName, IronGolem animatable, IBone bone) {

    }

    @Override
    protected void postRenderBlock(PoseStack poseStack, BlockState state, String boneName, IronGolem animatable) {

    }

}
