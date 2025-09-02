package at.tobfal.cosmicreactors;

import at.tobfal.cosmicreactors.client.render.PenroseReactorCoreEntityModel;
import at.tobfal.cosmicreactors.client.render.PenroseReactorCoreRenderer;
import at.tobfal.cosmicreactors.client.screen.PenroseReactorScreen;
import at.tobfal.cosmicreactors.init.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(CosmicReactors.MODID)
public class CosmicReactors {
    public static final String MODID = "cosmicreactors";
    public static final String MODNAME = ModList.get().getModFileById(MODID).moduleName();
     static final String VERSION = ModList.get().getModFileById(MODID).versionString();

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public CosmicReactors(IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModEntityTypes.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }

        @SubscribeEvent
        public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(
                    PenroseReactorCoreRenderer.LAYER_LOCATION,
                    PenroseReactorCoreEntityModel::createBodyLayer
            );
        }

        @SubscribeEvent
        public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntityTypes.PENROSE_REACTOR_CORE.get(), PenroseReactorCoreRenderer::new);
        }

        @SubscribeEvent
        public static void registerMenuScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.PENROSE_REACTOR_MENU.get(), PenroseReactorScreen::new);
        }
    }

    public static ResourceLocation getResourceLocation(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name);
    }
}
