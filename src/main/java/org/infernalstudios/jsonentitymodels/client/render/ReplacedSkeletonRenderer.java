/*
 * Copyright 2023 Infernal Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.infernalstudios.jsonentitymodels.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
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

public class ReplacedSkeletonRenderer extends ExtendedGeoReplacedEntityRenderer<ReplacedSkeletonEntity, Skeleton> {

    public ReplacedSkeletonRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ReplacedSkeletonModel(), new ReplacedSkeletonEntity());
    }

    public ReplacedSkeletonRenderer(EntityRendererProvider.Context renderManager, String entityTypeName) {
        super(renderManager, new ReplacedSkeletonModel(entityTypeName), new ReplacedSkeletonEntity());
    }

    @Override
    public void render(Entity entity, IAnimatable animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity instanceof Skeleton skeletonEntity && animatable instanceof ReplacedSkeletonEntity replacedSkeleton) {
            replacedSkeleton.setAiming(skeletonEntity.getMainHandItem().getItem() instanceof BowItem && skeletonEntity.isUsingItem());
            replacedSkeleton.setAggressive(skeletonEntity.isAggressive());
            replacedSkeleton.setHasBow(skeletonEntity.getMainHandItem().getItem() instanceof BowItem);
        }

        super.render(entity, animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, Skeleton animatable) {
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
