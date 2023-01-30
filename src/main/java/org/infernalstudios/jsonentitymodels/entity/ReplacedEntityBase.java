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

import net.minecraft.world.entity.LivingEntity;
import org.infernalstudios.jsonentitymodels.client.model.HeadTurningAnimatedGeoModel;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public abstract class ReplacedEntityBase implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    protected boolean isHurt;
    protected boolean isBaby;
    protected boolean isDead;

    protected boolean inWater;
    protected LivingEntity originalEntity;

    private HeadTurningAnimatedGeoModel modelInstance;

    public void setHurt(boolean isHurt) {
        this.isHurt = isHurt;
    }

    public void setBaby(boolean isBaby) {
        this.isBaby = isBaby;
    }

    public boolean getBaby() {
        return this.isBaby;
    }

    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }

    public void setInWater(boolean inWater) {
        this.inWater = inWater;
    }

    public void setModelInstance(HeadTurningAnimatedGeoModel modelInstance) {
        this.modelInstance = modelInstance;
    }

    public HeadTurningAnimatedGeoModel getModelInstance() {
        return this.modelInstance;
    }

    public void setOriginalEntity(LivingEntity entity) {
        this.originalEntity = entity;
    }

    protected abstract <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event);

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        if (this.originalEntity instanceof IAnimatable animatable) {
            return animatable.getFactory();
        }

        return this.factory;
    }
}
