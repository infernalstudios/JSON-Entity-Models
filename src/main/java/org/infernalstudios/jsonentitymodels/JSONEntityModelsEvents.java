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
