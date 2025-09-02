package at.tobfal.cosmicreactors.event;

import at.tobfal.cosmicreactors.CosmicReactors;
import at.tobfal.cosmicreactors.energy.ModEnergyStorage;
import at.tobfal.cosmicreactors.energy.ModMassStorage;
import at.tobfal.cosmicreactors.multiblock.PenroseReactorAPI;
import at.tobfal.cosmicreactors.util.PenroseReactorUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber(modid = CosmicReactors.MODID)
public class TickHandler {
    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        Level level = event.getLevel();
        if (level.isClientSide()) {
            return;
        }

        ServerLevel serverLevel = (ServerLevel) level;
        var reactors = PenroseReactorAPI.getAllReactors(serverLevel);
        for (var reactor : reactors.values()) {
            ModMassStorage massStorage = reactor.massStorage();
            ModEnergyStorage energyStorage = reactor.energyStorage();

            if (massStorage.getMass() <= 0) {
                continue;
            }

            int generatedEnergy = PenroseReactorUtil.getEnergyPerTick(massStorage.getMass());
            int massLost = PenroseReactorUtil.getMassLossPerTick(massStorage.getMass());

            energyStorage.receiveEnergy(generatedEnergy, false);
            massStorage.subtractMass(massLost, false);
            PenroseReactorAPI.setEnergyStorage(serverLevel, reactor.id(), energyStorage);
            PenroseReactorAPI.setMassStorage(serverLevel, reactor.id(), massStorage);
        }
    }
}
