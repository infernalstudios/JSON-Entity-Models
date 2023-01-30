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
package org.infernalstudios.jsonentitymodels.client.model;

import net.minecraft.world.entity.animal.Sheep;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

public class ReplacedSheepModel extends QuardrupedAnimatedGeoModel {
    public ReplacedSheepModel() {
        super("sheep");
    }

    @Override
    public void setCustomAnimations(IAnimatable animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);

        if (this.getAnimationResource(animatable) == null) {
            IBone head = this.getAnimationProcessor().getBone("head");

            head.setPositionY(-((Sheep) this.getCurrentEntity()).getHeadEatPositionScale(animationEvent.getPartialTick()) * 9.0F);
            head.setRotationX(-((Sheep) this.getCurrentEntity()).getHeadEatAngleScale(animationEvent.getPartialTick()));
        }
    }
}
