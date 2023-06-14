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

public class ReplacedIronGolemEntity extends ReplacedEntityBase {
    protected static final RawAnimation ATTACK_HURT = RawAnimation.begin().thenPlay("attack_hurt");
    protected static final RawAnimation OFFER_FLOWER = RawAnimation.begin().thenPlay("start_offer_flower").thenPlay("offer_flower_loop");
    protected static final RawAnimation ATTACK_WALK = RawAnimation.begin().thenPlay("attack_walk");
    protected static final RawAnimation ATTACK_IDLE = RawAnimation.begin().thenPlay("attack_idle");
    protected static final RawAnimation ATTACK = RawAnimation.begin().thenPlay("attack");
    private boolean isAttacking;
    private boolean offeringFlower;

    public ReplacedIronGolemEntity(EntityType<?> type) {
        super(type);
    }

    public void setAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    public void setOfferingFlower(boolean offeringFlower) {
        this.offeringFlower = offeringFlower;
    }

    @Override
    protected <P extends GeoReplacedEntity> PlayState predicate(AnimationState<P> event) {
        if (this.isDead) {
            event.getController().setAnimation(DEATH);
        } else if (this.isHurt) {
            event.getController().setAnimation(this.isAttacking ? ATTACK_HURT : HURT);
        } else if (this.offeringFlower) {
            event.getController().setAnimation(OFFER_FLOWER);
        } else if (!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F)) {
            event.getController().setAnimation(this.isAttacking ? ATTACK_WALK : WALK);
        } else {
            event.getController().setAnimation(this.isAttacking ? ATTACK_IDLE : IDLE);
        }
        return PlayState.CONTINUE;
    }

    protected <P extends GeoReplacedEntity> PlayState attackPredicate(AnimationState<P> event) {
        if (this.isAttacking) {
            event.getController().setAnimation(ATTACK);
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
