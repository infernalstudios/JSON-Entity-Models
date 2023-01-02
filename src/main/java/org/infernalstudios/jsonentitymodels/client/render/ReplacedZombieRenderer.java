package org.infernalstudios.jsonentitymodels.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.infernalstudios.jsonentitymodels.client.model.ReplacedZombieModel;
import org.infernalstudios.jsonentitymodels.entity.ReplacedZombieEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;

public class ReplacedZombieRenderer extends ExtendedGeoReplacedEntityRenderer<ReplacedZombieEntity, Zombie> {
    public ReplacedZombieRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ReplacedZombieModel(), new ReplacedZombieEntity());
    }

    @Override
    public void render(Entity entity, IAnimatable animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity instanceof Zombie zombieEntity && animatable instanceof ReplacedZombieEntity replacedZombie) {
            replacedZombie.setHurt(zombieEntity.hurtTime > 0);
            replacedZombie.setAttacking(zombieEntity.isAggressive());
        }

        super.render(entity, animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    protected boolean isArmorBone(GeoBone bone) {
        return false;
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, Zombie animatable) {
        return null;
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, Zombie animatable) {
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
    protected BlockState getHeldBlockForBone(String boneName, Zombie animatable) {
        return null;
    }

    @Override
    protected void preRenderItem(PoseStack poseStack, ItemStack stack, String boneName, Zombie animatable, IBone bone) {

    }

    @Override
    protected void preRenderBlock(PoseStack poseStack, BlockState state, String boneName, Zombie animatable) {

    }

    @Override
    protected void postRenderItem(PoseStack poseStack, ItemStack stack, String boneName, Zombie animatable, IBone bone) {

    }

    @Override
    protected void postRenderBlock(PoseStack poseStack, BlockState state, String boneName, Zombie animatable) {

    }

}
