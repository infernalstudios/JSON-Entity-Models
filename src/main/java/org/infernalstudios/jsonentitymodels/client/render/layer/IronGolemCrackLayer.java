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
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import org.infernalstudios.jsonentitymodels.client.model.HeadTurningAnimatedGeoModel;
import org.infernalstudios.jsonentitymodels.util.RandomUtil;
import org.infernalstudios.jsonentitymodels.util.ResourceCache;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class IronGolemCrackLayer extends GeoRenderLayer {

    public IronGolemCrackLayer(GeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, GeoAnimatable animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (animatable instanceof IronGolem ironGolem && this.getGeoModel() instanceof HeadTurningAnimatedGeoModel headTurningAnimatedGeoModel) {
            if (!ironGolem.isInvisible()) {

                IronGolem.Crackiness crackLevel = ironGolem.getCrackiness();

                if (crackLevel != IronGolem.Crackiness.NONE) {
                    List<ResourceLocation> cracksResources = this.getCracksResource(crackLevel, ((LivingEntity) animatable).isBaby(), headTurningAnimatedGeoModel.getTextureResource(null));

                    if (cracksResources != null && !cracksResources.isEmpty()) {

                        RenderType newRenderType = RenderType.entityCutoutNoCull(cracksResources.get(RandomUtil.getPseudoRandomInt(((LivingEntity) animatable).getUUID().getLeastSignificantBits(), RandomUtil.textureUUID.getLeastSignificantBits(), cracksResources.size())));


                        this.getRenderer().actuallyRender(
                                poseStack,
                                animatable,
                                bakedModel,
                                newRenderType,
                                bufferSource,
                                bufferSource.getBuffer(newRenderType),
                                true,
                                partialTick,
                                packedLight,
                                LivingEntityRenderer.getOverlayCoords(ironGolem, 0.0F),
                                1f, 1f, 1f, 1f);
                    }
                }
            }
        }
    }

    @Nullable
    private List<ResourceLocation> getCracksResource(IronGolem.Crackiness crackLevel, boolean isBaby, ResourceLocation textureLocation) {
        Map<String, List<ResourceLocation>> textures = isBaby ? ResourceCache.getInstance().getBabyTextures() : ResourceCache.getInstance().getAdultTextures();

        String[] splitPath = textureLocation.getPath().split("/");

        String cracksKey = splitPath[2] + ":" + splitPath[3] + "/" + splitPath[5] + "/" + splitPath[splitPath.length - 1].replace(".png", "") + "_crackiness_";

        switch (crackLevel) {
            case HIGH:
                cracksKey += "high";
                break;
            case MEDIUM:
                cracksKey += "medium";
                break;
            case LOW:
                cracksKey += "low";
                break;
        }

        return textures.get(cracksKey);
    }
}
