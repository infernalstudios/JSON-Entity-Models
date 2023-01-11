package org.infernalstudios.jsonentitymodels.mixin;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.EntityRenderersEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(EntityRenderersEvent.AddLayers.class)
public class EntityRenderersEventAddLayersMixin<T extends LivingEntity, R extends LivingEntityRenderer> {

    @Shadow @Final private Map<EntityType<?>, EntityRenderer<?>> renderers;

    @Inject(method = "getRenderer", at = @At("HEAD"), cancellable = true, remap = false)
    private void jsonentitymodels_checkRendererCast(EntityType<? extends T> entityType, CallbackInfoReturnable<R> cir) {
        if (!(this.renderers.get(entityType) instanceof LivingEntityRenderer<?,?>)) {
            cir.setReturnValue(null);
        }
    }
}
