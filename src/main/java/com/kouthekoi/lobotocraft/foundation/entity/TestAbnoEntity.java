package com.kouthekoi.lobotocraft.foundation.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TestAbnoEntity extends PathfinderMob implements GeoEntity {
    private final AnimatableInstanceCache geoCache =
            GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE =
            RawAnimation.begin()
                    .thenLoop("animation.idle");

    private static final RawAnimation WALK =
            RawAnimation.begin()
                    .thenLoop("move");

    public TestAbnoEntity(
            EntityType<? extends PathfinderMob> entityType,
            Level level
    ) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(
                0,
                new FloatGoal(this)
        );

        this.goalSelector.addGoal(
                2,
                new MeleeAttackGoal(this, 1.2D, false)
        );

        this.goalSelector.addGoal(
                7,
                new WaterAvoidingRandomStrollGoal(this, 1.0D)
        );

        this.goalSelector.addGoal(
                8,
                new LookAtPlayerGoal(
                        this,
                        Player.class,
                        8.0F
                )
        );

        this.goalSelector.addGoal(
                9,
                new RandomLookAroundGoal(this)
        );

        this.targetSelector.addGoal(
                1,
                new NearestAttackableTargetGoal<>(
                        this,
                        Player.class,
                        true
                )
        );
    }

    @Override
    public void registerControllers(
            AnimatableManager.ControllerRegistrar controllers
    ) {
        controllers.add(
                new AnimationController<>(
                        this,
                        "main_controller",
                        5,
                        this::animationPredicate
                )
        );
    }

    private <E extends TestAbnoEntity> PlayState animationPredicate(
            AnimationState<E> state
    ) {
        if (state.isMoving()) {
            return state.setAndContinue(WALK);
        }

        return state.setAndContinue(IDLE);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
