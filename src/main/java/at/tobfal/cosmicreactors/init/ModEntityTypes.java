package at.tobfal.cosmicreactors.init;

import at.tobfal.cosmicreactors.CosmicReactors;
import at.tobfal.cosmicreactors.entity.PulsarReactorCoreEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntityTypes {
    public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(CosmicReactors.MODID);

    public static final Supplier<EntityType<PulsarReactorCoreEntity>> PULSAR_REACTOR_CORE = ENTITY_TYPES.registerEntityType(
            "pulsar_reactor_core", PulsarReactorCoreEntity::new, MobCategory.MISC,
            builder -> builder.sized(1.0f, 1.0f).updateInterval(5)
    );

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
