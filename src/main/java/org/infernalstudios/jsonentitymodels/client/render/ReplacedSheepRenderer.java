package org.infernalstudios.jsonentitymodels.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.infernalstudios.jsonentitymodels.client.model.ReplacedSheepModel;
import org.infernalstudios.jsonentitymodels.entity.ReplacedSheepEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.processor.IBone;

public class ReplacedSheepRenderer extends ExtendedGeoReplacedEntityRenderer<ReplacedSheepEntity, Sheep> {

    public ReplacedSheepRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ReplacedSheepModel(), new ReplacedSheepEntity());
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, Sheep animatable) {
        return null;
    }

    @Nullable
    @Override
    protected BlockState getHeldBlockForBone(String boneName, Sheep animatable) {
        return null;
    }

    @Override
    protected void preRenderItem(PoseStack poseStack, ItemStack stack, String boneName, Sheep animatable, IBone bone) {

    }

    @Override
    protected void preRenderBlock(PoseStack poseStack, BlockState state, String boneName, Sheep animatable) {

    }

    @Override
    protected void postRenderItem(PoseStack poseStack, ItemStack stack, String boneName, Sheep animatable, IBone bone) {

    }

    @Override
    protected void postRenderBlock(PoseStack poseStack, BlockState state, String boneName, Sheep animatable) {

    }
}
