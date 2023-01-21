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
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class ReplacedSkeletonEntity extends ReplacedEntityBase {
    private boolean isAiming;

    private boolean isAggressive;

    private boolean hasBow;

    public void setAiming(boolean isAiming) {
        this.isAiming = isAiming;
    }

    public void setAggressive(boolean isAggressive) {
        this.isAggressive = isAggressive;
    }

    public void setHasBow(boolean hasBow) {
        this.hasBow = hasBow;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.isDead) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("death", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        } else if (this.isHurt) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.isAiming || this.isAggressive ? "aggressive_hurt" : "hurt", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        } else if (!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.isAiming || this.isAggressive ? "aggressive_walk" : "walk", ILoopType.EDefaultLoopTypes.LOOP));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.isAiming || this.isAggressive ? "aggressive_idle" : "idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    private <P extends IAnimatable> PlayState aimPredicate(AnimationEvent<P> event) {
        if (this.isAiming) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("aim", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        } else if (this.isAggressive) {
            if (this.hasBow) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("aggressive_bow", ILoopType.EDefaultLoopTypes.LOOP));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("aggressive_melee", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        super.registerControllers(data);
        data.addAnimationController(new AnimationController<>(this, "aim_controller", 0, this::aimPredicate));
    }
}
