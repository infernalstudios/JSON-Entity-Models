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
