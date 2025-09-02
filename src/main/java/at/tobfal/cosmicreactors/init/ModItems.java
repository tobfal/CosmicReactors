package at.tobfal.cosmicreactors.init;

import at.tobfal.cosmicreactors.CosmicReactors;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CosmicReactors.MODID);

    public static final DeferredItem<Item> DENSE_MATTER_PARTICLE = ITEMS.registerItem("dense_matter_particle", Item::new, new Item.Properties());
    public static final DeferredItem<Item> DENSE_MATTER_CLUMP = ITEMS.registerItem("dense_matter_clump", Item::new, new Item.Properties());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
