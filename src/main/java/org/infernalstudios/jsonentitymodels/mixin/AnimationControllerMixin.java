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

import org.infernalstudios.jsonentitymodels.entity.ReplacedEntityBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.controller.AnimationController;

@Mixin(AnimationController.class)
public class AnimationControllerMixin<T extends IAnimatable> {

    @Shadow protected T animatable;

    @ModifyVariable(method = "process",
            at = @At(value = "INVOKE_ASSIGN",
                    target = "Lsoftware/bernie/geckolib3/core/controller/AnimationController;getModel(Lsoftware/bernie/geckolib3/core/IAnimatable;)Lsoftware/bernie/geckolib3/core/IAnimatableModel;"),
            remap = false)
    private IAnimatableModel<T> jsonentitymodels_getAnimatableModel(IAnimatableModel<T> original) {
        if (this.animatable instanceof ReplacedEntityBase replacedEntityBase) {
            return replacedEntityBase.getModelInstance();
        }

        return original;
    }
}
