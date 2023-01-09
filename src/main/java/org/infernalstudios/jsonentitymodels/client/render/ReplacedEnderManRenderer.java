package org.infernalstudios.jsonentitymodels.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.infernalstudios.jsonentitymodels.client.model.ReplacedEnderManModel;
import org.infernalstudios.jsonentitymodels.entity.ReplacedEnderManEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.processor.IBone;

public class ReplacedEnderManRenderer extends ExtendedGeoReplacedEntityRenderer<ReplacedEnderManEntity, EnderMan> {

    public ReplacedEnderManRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ReplacedEnderManModel(), new ReplacedEnderManEntity());
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

    }

    @Override
    protected void preRenderBlock(PoseStack poseStack, BlockState state, String boneName, EnderMan animatable) {

    }

    @Override
    protected void postRenderItem(PoseStack poseStack, ItemStack stack, String boneName, EnderMan animatable, IBone bone) {

    }

    @Override
    protected void postRenderBlock(PoseStack poseStack, BlockState state, String boneName, EnderMan animatable) {

    }
}
