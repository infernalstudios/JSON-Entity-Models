package org.infernalstudios.jsonentitymodels.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.infernalstudios.jsonentitymodels.client.model.ReplacedChickenModel;
import org.infernalstudios.jsonentitymodels.entity.ReplacedChickenEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.processor.IBone;

public class ReplacedChickenRenderer extends ExtendedGeoReplacedEntityRenderer<ReplacedChickenEntity, Chicken> {

    public ReplacedChickenRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ReplacedChickenModel(), new ReplacedChickenEntity());
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, Chicken animatable) {
        return null;
    }

    @Nullable
    @Override
    protected BlockState getHeldBlockForBone(String boneName, Chicken animatable) {
        return null;
    }

    @Override
    protected void preRenderItem(PoseStack poseStack, ItemStack stack, String boneName, Chicken animatable, IBone bone) {

    }

    @Override
    protected void preRenderBlock(PoseStack poseStack, BlockState state, String boneName, Chicken animatable) {

    }

    @Override
    protected void postRenderItem(PoseStack poseStack, ItemStack stack, String boneName, Chicken animatable, IBone bone) {

    }

    @Override
    protected void postRenderBlock(PoseStack poseStack, BlockState state, String boneName, Chicken animatable) {

    }
}
