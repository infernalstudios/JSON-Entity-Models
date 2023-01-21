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
package org.infernalstudios.jsonentitymodels.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.IronGolem;
import org.infernalstudios.jsonentitymodels.JSONEntityModels;
import org.infernalstudios.jsonentitymodels.client.model.HeadTurningAnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import javax.annotation.Nullable;

public class IronGolemCrackLayer extends GeoLayerRenderer {

    public IronGolemCrackLayer(IGeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Entity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityLivingBaseIn instanceof IronGolem ironGolem && this.getEntityModel() instanceof HeadTurningAnimatedGeoModel headTurningAnimatedGeoModel) {
            if (!ironGolem.isInvisible()) {

                IronGolem.Crackiness crackLevel = ironGolem.getCrackiness();

                if (crackLevel != IronGolem.Crackiness.NONE) {
                    ResourceLocation cracksResource = this.getCracksResource(crackLevel, headTurningAnimatedGeoModel.getTextureLocation(null));

                    if (cracksResource != null &&
                            Minecraft.getInstance().getResourceManager().hasResource(cracksResource)) {

                        RenderType renderType = RenderType.entityCutoutNoCull(cracksResource);

                        this.getRenderer().render(
                                headTurningAnimatedGeoModel.getModel(headTurningAnimatedGeoModel.getModelLocation(null)),
                                entityLivingBaseIn,
                                partialTicks,
                                renderType,
                                matrixStackIn,
                                bufferIn,
                                bufferIn.getBuffer(renderType),
                                packedLightIn,
                                LivingEntityRenderer.getOverlayCoords(ironGolem, 0.0F),
                                1f, 1f, 1f, 1f);
                    }
                }
            }
        }
    }

    @Nullable
    private ResourceLocation getCracksResource(IronGolem.Crackiness crackLevel, ResourceLocation textureLocation) {
        String cracksPath = textureLocation.getPath().replace(".png", "_crackiness_");

       return switch (crackLevel) {
            case HIGH -> new ResourceLocation(JSONEntityModels.MOD_ID, cracksPath + "high.png");
            case MEDIUM -> new ResourceLocation(JSONEntityModels.MOD_ID, cracksPath + "medium.png");
            case LOW -> new ResourceLocation(JSONEntityModels.MOD_ID, cracksPath + "low.png");
           default -> null;
        };
    }
}
