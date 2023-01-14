package org.infernalstudios.jsonentitymodels.entity;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class ReplacedIronGolemEntity extends ReplacedEntityBase {
    private boolean isAttacking;
    private boolean offeringFlower;

    public void setAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    public void setOfferingFlower(boolean offeringFlower) {
        this.offeringFlower = offeringFlower;
    }

    @Override
    protected <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (this.isDead) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("death", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        } else if (this.isHurt) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.isAttacking ? "attack_hurt" : "hurt", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        } else if (this.offeringFlower) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("offer_flower", ILoopType.EDefaultLoopTypes.LOOP));
        } else if (!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.isAttacking ? "attack_walk" : "walk", ILoopType.EDefaultLoopTypes.LOOP));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.isAttacking ? "attack_idle" : "idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    private <P extends IAnimatable> PlayState attackPredicate(AnimationEvent<P> event) {
        if (this.isAttacking) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        super.registerControllers(data);
        data.addAnimationController(new AnimationController<>(this, "attack_controller", 0, this::attackPredicate));
    }
}
