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

import net.minecraft.world.entity.EntityType;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import static org.infernalstudios.jsonentitymodels.entity.ReplacedDefaultEntity.DEATH;
import static org.infernalstudios.jsonentitymodels.entity.ReplacedDefaultEntity.HURT;
import static org.infernalstudios.jsonentitymodels.entity.ReplacedDefaultEntity.IDLE;
import static org.infernalstudios.jsonentitymodels.entity.ReplacedDefaultEntity.WALK;

public class ReplacedSkeletonEntity extends ReplacedEntityBase {
    protected static final RawAnimation AGGRESSIVE_HURT = RawAnimation.begin().thenPlay("aggressive_hurt");
    protected static final RawAnimation AGGRESSIVE_WALK = RawAnimation.begin().thenPlay("aggressive_walk");
    protected static final RawAnimation AGGRESSIVE_IDLE = RawAnimation.begin().thenPlay("aggressive_idle");
    protected static final RawAnimation AIM = RawAnimation.begin().thenPlay("aim");
    protected static final RawAnimation AGGRESSIVE_BOW = RawAnimation.begin().thenPlay("aggressive_bow");
    protected static final RawAnimation AGGRESSIVE_MELEE = RawAnimation.begin().thenPlay("aggressive_melee");
    private boolean isAiming;

    private boolean isAggressive;

    private boolean hasBow;

    public ReplacedSkeletonEntity(EntityType<?> type) {
        super(type);
    }

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
    protected <P extends GeoReplacedEntity> PlayState predicate(AnimationState<P> event) {
        if (this.isDead) {
            event.getController().setAnimation(DEATH);
        } else if (this.isHurt) {
            event.getController().setAnimation(this.isAiming || this.isAggressive ? AGGRESSIVE_HURT : HURT);
        } else if (!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F)) {
            event.getController().setAnimation(this.isAiming || this.isAggressive ? AGGRESSIVE_WALK : WALK);
        } else {
            event.getController().setAnimation(this.isAiming || this.isAggressive ? AGGRESSIVE_IDLE : IDLE);
        }
        return PlayState.CONTINUE;
    }

    protected <P extends GeoReplacedEntity> PlayState aimPredicate(AnimationState<P> event) {
        if (this.isAiming) {
            event.getController().setAnimation(AIM);
            return PlayState.CONTINUE;
        } else if (this.isAggressive) {
            if (this.hasBow) {
                event.getController().setAnimation(AGGRESSIVE_BOW);
            } else {
                event.getController().setAnimation(AGGRESSIVE_MELEE);
            }
            return PlayState.CONTINUE;
        }

        event.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        super.registerControllers(controllerRegistrar);
        controllerRegistrar.add(new AnimationController<>(this, "aim_controller", 0, this::aimPredicate));
    }
}
