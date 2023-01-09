package org.infernalstudios.jsonentitymodels.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.infernalstudios.jsonentitymodels.client.model.ReplacedPigModel;
import org.infernalstudios.jsonentitymodels.entity.ReplacedPigEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.processor.IBone;

public class ReplacedPigRenderer extends ExtendedGeoReplacedEntityRenderer<ReplacedPigEntity, Pig> {

    public ReplacedPigRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ReplacedPigModel(), new ReplacedPigEntity());
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, Pig animatable) {
        return null;
    }

    @Nullable
    @Override
    protected BlockState getHeldBlockForBone(String boneName, Pig animatable) {
        return null;
    }

    @Override
    protected void preRenderItem(PoseStack poseStack, ItemStack stack, String boneName, Pig animatable, IBone bone) {

    }

    @Override
    protected void preRenderBlock(PoseStack poseStack, BlockState state, String boneName, Pig animatable) {

    }

    @Override
    protected void postRenderItem(PoseStack poseStack, ItemStack stack, String boneName, Pig animatable, IBone bone) {

    }

    @Override
    protected void postRenderBlock(PoseStack poseStack, BlockState state, String boneName, Pig animatable) {

    }
}
