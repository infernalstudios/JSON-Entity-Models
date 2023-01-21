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
import net.minecraft.world.entity.LivingEntity;
import org.infernalstudios.jsonentitymodels.JSONEntityModels;
import org.infernalstudios.jsonentitymodels.client.JEMsRenderTypes;
import org.infernalstudios.jsonentitymodels.client.model.HeadTurningAnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class AutomatedGlowLayer extends GeoLayerRenderer {

    public AutomatedGlowLayer(IGeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Entity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (this.getEntityModel() instanceof HeadTurningAnimatedGeoModel headTurningAnimatedGeoModel) {
            ResourceLocation glowLocation = new ResourceLocation(JSONEntityModels.MOD_ID, headTurningAnimatedGeoModel.getTextureResource(null).getPath().replace(".png", "_glow.png"));

            if (Minecraft.getInstance().getResourceManager().getResource(glowLocation).isPresent()) {
                RenderType renderType = JEMsRenderTypes.eyes(glowLocation);

                matrixStackIn.pushPose();

                this.getRenderer().render(
                        this.getEntityModel().getModel(headTurningAnimatedGeoModel.getModelResource(null)),
                        entityLivingBaseIn,
                        partialTicks,
                        renderType,
                        matrixStackIn,
                        bufferIn,
                        bufferIn.getBuffer(renderType),
                        packedLightIn,
                        LivingEntityRenderer.getOverlayCoords((LivingEntity) entityLivingBaseIn, 0.0F),
                        1f, 1f, 1f, 1f);

                matrixStackIn.popPose();
            }
        }
    }

    @Override
    public RenderType getRenderType(ResourceLocation textureLocation) {
        return RenderType.eyes(textureLocation);
    }
}
