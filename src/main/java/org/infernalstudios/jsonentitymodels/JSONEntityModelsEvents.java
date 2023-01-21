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
package org.infernalstudios.jsonentitymodels;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.io.IOException;

@Mod.EventBusSubscriber(modid = JSONEntityModels.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class JSONEntityModelsEvents {

    @Nullable
    private static ShaderInstance renderTypeEntityTranslucentEmissive;

    @SubscribeEvent
    public static void registerShaders(final RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceManager(),
                new ResourceLocation(JSONEntityModels.MOD_ID, "rendertype_entity_translucent_emissive"),
                DefaultVertexFormat.NEW_ENTITY),
                shaderInstance -> renderTypeEntityTranslucentEmissive = shaderInstance);
    }

    @Nullable
    public static ShaderInstance getRenderTypeEntityTranslucentEmissive() {
        return renderTypeEntityTranslucentEmissive;
    }
}
