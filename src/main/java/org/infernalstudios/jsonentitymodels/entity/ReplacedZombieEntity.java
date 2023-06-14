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

public class ReplacedZombieEntity extends ReplacedEntityBase {
    protected static final RawAnimation AGGRESSIVE_HURT = RawAnimation.begin().thenPlay("aggressive_hurt");
    protected static final RawAnimation SINK = RawAnimation.begin().thenPlay("sink");
    protected static final RawAnimation AGGRESSIVE_WALK = RawAnimation.begin().thenPlay("aggressive_walk");
    protected static final RawAnimation AGGRESSIVE_IDLE = RawAnimation.begin().thenPlay("aggressive_idle");
    protected static final RawAnimation AGGRESSIVE = RawAnimation.begin().thenPlay("aggressive");
    private boolean isAggressive;

    public ReplacedZombieEntity(EntityType<?> type) {
        super(type);
    }

    public void setAggressive(boolean isAgressive) {
        this.isAggressive = isAgressive;
    }

    @Override
    protected <P extends GeoReplacedEntity> PlayState predicate(AnimationState<P> event) {
        if (this.isDead) {
            event.getController().setAnimation(DEATH);
        } else if (this.isHurt) {
            event.getController().setAnimation(this.isAggressive ? AGGRESSIVE_HURT : HURT);
        } else if (this.inWater) {
            event.getController().setAnimation(SINK);
        } else if (!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F)) {
            event.getController().setAnimation(this.isAggressive ? AGGRESSIVE_WALK : WALK);
        } else {
            event.getController().setAnimation(this.isAggressive ? AGGRESSIVE_IDLE : IDLE);
        }
        return PlayState.CONTINUE;
    }

    protected <P extends GeoReplacedEntity> PlayState attackPredicate(AnimationState<P> event) {
        if (this.isAggressive) {
            event.getController().setAnimation(AGGRESSIVE);
            return PlayState.CONTINUE;
        }

        event.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        super.registerControllers(controllerRegistrar);
        controllerRegistrar.add(new AnimationController<>(this, "attack_controller", 0, this::attackPredicate));
    }


}
