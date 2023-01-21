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
package org.infernalstudios.jsonentitymodels.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class JEMsRenderTypes extends RenderType {

    public JEMsRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int buffer,
                           boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, buffer, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    public static @NotNull RenderType eyes(@NotNull ResourceLocation texture) {
        return RenderType.create("jsonentitymodels-eyes", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, CompositeState.builder()
                .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER)
                .setCullState(NO_CULL)
                .setTextureState(new TextureStateShard(texture, false, false))
                .setTransparencyState(ADDITIVE_TRANSPARENCY)
                .setOverlayState(OVERLAY)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(true));
    }
}
