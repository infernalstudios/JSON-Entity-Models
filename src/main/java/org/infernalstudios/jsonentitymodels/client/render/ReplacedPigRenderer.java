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
