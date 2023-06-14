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
import static org.infernalstudios.jsonentitymodels.entity.ReplacedDefaultEntity.SWIM;
import static org.infernalstudios.jsonentitymodels.entity.ReplacedDefaultEntity.WALK;

public class ReplacedEnderManEntity extends ReplacedEntityBase {
    protected static final RawAnimation HOLD_BLOCK_SCREAM_RUN = RawAnimation.begin().thenPlay("hold_block_scream_run");
    protected static final RawAnimation HOLD_BLOCK_WALK = RawAnimation.begin().thenPlay("hold_block_walk");
    protected static final RawAnimation SCREAM_RUN = RawAnimation.begin().thenPlay("scream_run");
    protected static final RawAnimation HOLD_BLOCK_IDLE = RawAnimation.begin().thenPlay("hold_block_idle");
    protected static final RawAnimation SCREAM = RawAnimation.begin().thenPlay("scream_start").thenPlay("scream_loop");
    protected static final RawAnimation HOLD_BLOCK = RawAnimation.begin().thenPlay("hold_block");
    private boolean isScreaming;

    private boolean holdingBlock;

    public ReplacedEnderManEntity(EntityType<?> type) {
        super(type);
    }

    public void setScreaming(boolean isScreaming) {
        this.isScreaming = isScreaming;
    }

    public void setHoldingBlock(boolean holdingBlock) {
        this.holdingBlock = holdingBlock;
    }

    @Override
    protected <P extends GeoReplacedEntity> PlayState predicate(AnimationState<P> event) {
        if (this.isDead) {
            event.getController().setAnimation(DEATH);
        } else if (this.isHurt) {
            event.getController().setAnimation(HURT);
        } else if (this.inWater) {
            event.getController().setAnimation(SWIM);
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(this.holdingBlock ? (this.isScreaming ? HOLD_BLOCK_SCREAM_RUN : HOLD_BLOCK_WALK) : (this.isScreaming ? SCREAM_RUN : WALK));
        } else {
            event.getController().setAnimation(this.holdingBlock ? HOLD_BLOCK_IDLE : IDLE);
        }
        return PlayState.CONTINUE;
    }

    protected <P extends GeoReplacedEntity> PlayState screamPredicate(AnimationState<P> event) {
        if (this.isScreaming) {
            event.getController().setAnimation(SCREAM);
            return PlayState.CONTINUE;
        }

        event.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    protected <P extends GeoReplacedEntity> PlayState holdBlockPredicate(AnimationState<P> event) {
        if (this.holdingBlock) {
            event.getController().setAnimation(HOLD_BLOCK);
            return PlayState.CONTINUE;
        }

        event.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        super.registerControllers(controllerRegistrar);
        controllerRegistrar.add(new AnimationController(this, "scream_controller", 0, this::screamPredicate));
        controllerRegistrar.add(new AnimationController(this, "hold_block_controller", 0, this::holdBlockPredicate));
    }
}
