package org.infernalstudios.jsonentitymodels.entity;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class ReplacedZombieEntity extends ReplacedEntityBase implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private boolean isAggressive;

    public void setAggressive(boolean isAgressive) {
        this.isAggressive = isAgressive;
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.isHurt) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.isAggressive ? "attack_hurt" : "hurt", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        } else if (!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.isAggressive ? "attack_walk" : "walk", ILoopType.EDefaultLoopTypes.LOOP));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.isAggressive ? "attack_idle" : "idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    private <P extends IAnimatable> PlayState attackpredicate(AnimationEvent<P> event) {
        if (this.isAggressive) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("aggressive", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "main_controller", 0, this::predicate));
        data.addAnimationController(new AnimationController<>(this, "attack_controller", 0, this::attackpredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
