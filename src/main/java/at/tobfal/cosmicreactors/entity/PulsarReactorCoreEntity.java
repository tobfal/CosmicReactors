package at.tobfal.cosmicreactors.entity;

import at.tobfal.cosmicreactors.init.ModEntityTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class PulsarReactorCoreEntity extends Entity {
    public double baseY;

    public PulsarReactorCoreEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = false;
    }

    public PulsarReactorCoreEntity(Level level, double x, double y, double z) {
        this(ModEntityTypes.PULSAR_REACTOR_CORE.get(), level);
        this.setPos(x, y, z);
        this.baseY = y;
    }

    public PulsarReactorCoreEntity(Level level, Vec3 pos) {
        this(level, pos.x, pos.y, pos.z);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount % 2 != 0 || this.level().isClientSide()) {
            return;
        }

        ServerLevel serverLevel = (ServerLevel)this.level();
        serverLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, this.getX(), this.getY(), this.getZ(),
                1, 0.0, 0.0, 0.0, 0.01);
    }
}
