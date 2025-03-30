package com.threeliky.fix_by_lp.entity;

import com.threeliky.fix_by_lp.FixByLp;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, FixByLp.MOD_ID);

    public static final RegistryObject<EntityType<FakePlayerEntity>> FAKE_PLAYER = ENTITIES.register(
        "fake_player",
        () -> EntityType.Builder.<FakePlayerEntity>of(FakePlayerEntity::new, MobCategory.MISC)
            .sized(0.6F, 1.8F)
            .build(FixByLp.MOD_ID + ":fake_player")
    );
}