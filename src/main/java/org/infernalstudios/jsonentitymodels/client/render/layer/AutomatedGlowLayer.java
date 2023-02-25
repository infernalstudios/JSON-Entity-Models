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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.infernalstudios.jsonentitymodels.client.JEMsRenderTypes;
import org.infernalstudios.jsonentitymodels.client.model.HeadTurningAnimatedGeoModel;
import org.infernalstudios.jsonentitymodels.util.RandomUtil;
import org.infernalstudios.jsonentitymodels.util.ResourceCache;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import java.util.List;
import java.util.Map;

public class AutomatedGlowLayer extends GeoLayerRenderer {

    public AutomatedGlowLayer(IGeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Entity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (this.getEntityModel() instanceof HeadTurningAnimatedGeoModel headTurningAnimatedGeoModel) {
            Map<String, List<ResourceLocation>> textures = ((LivingEntity) entityLivingBaseIn).isBaby() ? ResourceCache.getInstance().getBabyTextures() : ResourceCache.getInstance().getAdultTextures();

            String[] splitPath = headTurningAnimatedGeoModel.getTextureLocation(null).getPath().split("/");

            String glowKey = splitPath[2] + ":" + splitPath[3] + "/" + splitPath[5] + "/" + splitPath[splitPath.length - 1].replace(".png", "") + "_glow";

            List<ResourceLocation> glowResources = textures.get(glowKey);

            if (glowResources != null && !glowResources.isEmpty()) {
                RenderType renderType = JEMsRenderTypes.eyes(glowResources.get(RandomUtil.getPseudoRandomInt(entityLivingBaseIn.getUUID().getLeastSignificantBits(), RandomUtil.textureUUID.getMostSignificantBits(), glowResources.size())));

                matrixStackIn.pushPose();

                this.getRenderer().render(
                        this.getEntityModel().getModel(headTurningAnimatedGeoModel.getModelLocation(null)),
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
