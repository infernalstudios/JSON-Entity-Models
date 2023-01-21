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
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.infernalstudios.jsonentitymodels.client.model.ReplacedZombieModel;
import org.infernalstudios.jsonentitymodels.entity.ReplacedZombieEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.processor.IBone;

public class ReplacedZombieRenderer extends ExtendedGeoReplacedEntityRenderer<ReplacedZombieEntity, Zombie> {
    public ReplacedZombieRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ReplacedZombieModel(), new ReplacedZombieEntity());
    }

    @Override
    public void render(Entity entity, IAnimatable animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (entity instanceof Zombie zombieEntity && animatable instanceof ReplacedZombieEntity replacedZombie) {
            replacedZombie.setAggressive(zombieEntity.isAggressive());
        }

        super.render(entity, animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, Zombie animatable) {
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
