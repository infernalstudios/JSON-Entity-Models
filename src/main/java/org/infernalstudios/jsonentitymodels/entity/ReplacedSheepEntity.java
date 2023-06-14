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
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import static org.infernalstudios.jsonentitymodels.entity.ReplacedDefaultEntity.DEATH;
import static org.infernalstudios.jsonentitymodels.entity.ReplacedDefaultEntity.HURT;
import static org.infernalstudios.jsonentitymodels.entity.ReplacedDefaultEntity.IDLE;
import static org.infernalstudios.jsonentitymodels.entity.ReplacedDefaultEntity.SWIM;
import static org.infernalstudios.jsonentitymodels.entity.ReplacedDefaultEntity.WALK;

public class ReplacedSheepEntity extends ReplacedEntityBase {
    protected static final RawAnimation SPRINT = RawAnimation.begin().thenPlay("sprint");
    protected static final RawAnimation GRAZE = RawAnimation.begin().thenPlay("graze");
    private boolean isGrazing;

    public ReplacedSheepEntity(EntityType<?> type) {
        super(type);
    }

    public void setGrazing(boolean isGrazing) {
        this.isGrazing = isGrazing;
    }

    @Override
    protected <P extends GeoReplacedEntity> PlayState predicate(AnimationState<P> event) {
        if (this.isDead) {
            event.getController().setAnimation(DEATH);
        } else if (this.isGrazing) {
            event.getController().setAnimation(GRAZE);
        } else if (this.isHurt) {
            event.getController().setAnimation(HURT);
        } else if (this.inWater) {
            event.getController().setAnimation(SWIM);
        } else if (!(event.getLimbSwingAmount() > -0.6F && event.getLimbSwingAmount() < 0.6F)) {
            event.getController().setAnimation(SPRINT);
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(WALK);
        } else {
            event.getController().setAnimation(IDLE);
        }
        return PlayState.CONTINUE;
    }
}
