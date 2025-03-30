package com.threeliky.fix_by_lp.entity;

import com.threeliky.fix_by_lp.FixByLp;
import com.threeliky.fix_by_lp.entity.FakePlayerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, FixByLp.MOD_ID);

    public static final RegistryObject<EntityType<FakePlayerEntity>> FAKE_PLAYER = ENTITY_TYPES.register(
        "fake_player",
        () -> EntityType.Builder.of(FakePlayerEntity::new, MobCategory.MISC)
            .sized(0.6F, 1.8F)
            .clientTrackingRange(10)
            .build("fake_player")
    );

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}