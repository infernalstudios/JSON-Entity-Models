package org.infernalstudios.jsonentitymodels.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.infernalstudios.jsonentitymodels.client.model.ReplacedSpiderModel;
import org.infernalstudios.jsonentitymodels.entity.ReplacedSpiderEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.processor.IBone;

public class ReplacedSpiderRenderer extends ExtendedGeoReplacedEntityRenderer<ReplacedSpiderEntity, Spider> {

    public ReplacedSpiderRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ReplacedSpiderModel(), new ReplacedSpiderEntity());
    }

    @Override
    public void render(Entity entity, IAnimatable animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity instanceof Spider spider && animatable instanceof ReplacedSpiderEntity replacedSpider) {
            int skyLightLevel = (int) (((ClientLevel) spider.level).getSkyDarken(partialTick) * 15);
            int blockLightLevel = this.getBlockLightLevel(spider, new BlockPos(spider.getLightProbePosition(partialTick)));
            int maxLightLevel = Math.max(skyLightLevel, blockLightLevel);

            replacedSpider.setHostile(maxLightLevel <= 7);
        }

        super.render(entity, animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, Spider animatable) {
        return null;
    }

    @Nullable
    @Override
    protected BlockState getHeldBlockForBone(String boneName, Spider animatable) {
        return null;
    }

    @Override
    protected void preRenderItem(PoseStack poseStack, ItemStack stack, String boneName, Spider animatable, IBone bone) {

    }

    @Override
    protected void preRenderBlock(PoseStack poseStack, BlockState state, String boneName, Spider animatable) {

    }

    @Override
    protected void postRenderItem(PoseStack poseStack, ItemStack stack, String boneName, Spider animatable, IBone bone) {

    }

    @Override
    protected void postRenderBlock(PoseStack poseStack, BlockState state, String boneName, Spider animatable) {

    }
}
