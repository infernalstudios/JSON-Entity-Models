package org.infernalstudios.jsonentitymodels.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.infernalstudios.jsonentitymodels.client.model.ReplacedSheepModel;
import org.infernalstudios.jsonentitymodels.entity.ReplacedSheepEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;

public class ReplacedSheepRenderer extends ExtendedGeoReplacedEntityRenderer<ReplacedSheepEntity, Sheep> {

    public ReplacedSheepRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ReplacedSheepModel(), new ReplacedSheepEntity());
    }

    @Override
    public void render(Entity entity, IAnimatable animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity instanceof Sheep sheep && animatable instanceof ReplacedSheepEntity replacedSheep) {
            replacedSheep.setGrazing(sheep.getHeadEatPositionScale(partialTick) > 0);
        }

        super.render(entity, animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("wool")) {
            bone.setHidden(this.currentEntityBeingRendered.isSheared(), this.currentEntityBeingRendered.isSheared());

            if (this.currentEntityBeingRendered.hasCustomName() && "jeb_".equals(this.currentEntityBeingRendered.getName().getString())) {
                int i = this.currentEntityBeingRendered.tickCount / 25 + this.currentEntityBeingRendered.getId();
                int j = DyeColor.values().length;
                int k = i % j;
                int l = (i + 1) % j;
                float f3 = (float) (this.currentEntityBeingRendered.tickCount % 25) / 25.0F;
                float[] afloat1 = Sheep.getColorArray(DyeColor.byId(k));
                float[] afloat2 = Sheep.getColorArray(DyeColor.byId(l));
                red = afloat1[0] * (1.0F - f3) + afloat2[0] * f3;
                green = afloat1[1] * (1.0F - f3) + afloat2[1] * f3;
                blue = afloat1[2] * (1.0F - f3) + afloat2[2] * f3;
            } else {
                float[] afloat = Sheep.getColorArray(this.currentEntityBeingRendered.getColor());
                red = afloat[0];
                green = afloat[1];
                blue = afloat[2];
            }
        }

        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
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
