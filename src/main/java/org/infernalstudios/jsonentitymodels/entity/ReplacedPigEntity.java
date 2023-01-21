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
package org.infernalstudios.jsonentitymodels.entity;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class ReplacedPigEntity extends ReplacedEntityBase {
    @Override
    protected <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.isDead) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("death", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        } else if (this.isHurt) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("hurt", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        } else if (this.inWater) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("swim", ILoopType.EDefaultLoopTypes.LOOP));
        } else if (!(event.getLimbSwingAmount() > -0.6F && event.getLimbSwingAmount() < 0.6F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("sprint", ILoopType.EDefaultLoopTypes.LOOP));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", ILoopType.EDefaultLoopTypes.LOOP));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }
}
