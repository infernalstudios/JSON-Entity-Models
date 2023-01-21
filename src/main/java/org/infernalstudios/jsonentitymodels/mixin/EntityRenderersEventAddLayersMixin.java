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
