package at.tobfal.cosmicreactors.multiblock;

import at.tobfal.cosmicreactors.block.BasePenroseReactorBlock;
import at.tobfal.cosmicreactors.block.PenroseReactorPortBlock;
import at.tobfal.cosmicreactors.block.entity.PenroseReactorPortBlockEntity;
import at.tobfal.cosmicreactors.energy.ModEnergyStorage;
import at.tobfal.cosmicreactors.entity.PenroseReactorCoreEntity;
import at.tobfal.cosmicreactors.menu.PenroseReactorMenu;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class PenroseReactorMultiblock {
    private PenroseReactorMultiblock() {}

    public record Result(boolean formed, AABB bounds, LongOpenHashSet members) {}

    public static Result scanAround(ServerLevel level, BlockPos around) {
        LongOpenHashSet members = flood(level, around);
        if (members.isEmpty()) {
            return new Result(false, new AABB(0,0,0,0,0,0), members);
        }
        AABB bounds = getValidAABB(level, members);
        return new Result(bounds != null, bounds, members);
    }

    public static void rebuildAt(ServerLevel level, BlockPos around) {
        Result r = scanAround(level, around);
        if (r.members().isEmpty()) {
            return;
        }

        boolean hasPort = false;
        for (long l : r.members()) {
            if (level.getBlockState(BlockPos.of(l)).getBlock() instanceof PenroseReactorPortBlock) {
                hasPort = true;
                break;
            }
        }
        boolean formed = r.formed() && hasPort;
        List<BlockPos> memberPositions = new ArrayList<>();

        List<PenroseReactorPortBlockEntity> ports = new ArrayList<>();
        for (long l : r.members()) {
            BlockPos p = BlockPos.of(l);
            memberPositions.add(p);
            var be = level.getBlockEntity(p);
            if (be instanceof PenroseReactorPortBlockEntity port) {
                ports.add(port);
            }
        }

        UUID id = null;

        if (formed) {
            for (var port : ports) {
                if (port.getReactorId() != null) {
                    id = port.getReactorId();
                    break;
                }
            }

            if (id != null) {
                return;
            }

            id = UUID.randomUUID();

            if (!level.isClientSide()) {
                Vec3 center = r.bounds().getCenter();
                BlockPos centerPos = new BlockPos((int) center.x,(int) center.y,(int) center.z);
                level.sendParticles(ParticleTypes.SCRAPE, center.x, center.y, center.z,50,1f,1f,1f,0.5f);
                level.playSound(null, centerPos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 2.0f, 0.4f);
                level.addFreshEntity(new PenroseReactorCoreEntity(level, center.x, center.y, center.z));

                PenroseReactorAPI.getOrCreateRecord(level, id, new ModEnergyStorage(1_000_000, 1_000_000, 1_000_000, 500_000), memberPositions);
            }

            for (var port : ports) {
                port.setFormed(true);
                port.setReactorId(id);
            }
        } else {
            for (var port : ports) {
                if (port.getReactorId() != null) {
                    id = port.getReactorId();
                    break;
                }
            }

            if (!level.isClientSide() && id != null){
                PenroseReactorAPI.removeRecord(level, id);
            }

            for (var port : ports) {
                port.setFormed(false);
                port.setReactorId(null);
            }
        }
    }

    private static boolean isPart(BlockState state) {
        // TODO: Probably a BlockTag is the better way
        return state.getBlock() instanceof BasePenroseReactorBlock;
    }

    private static LongOpenHashSet flood(ServerLevel level, BlockPos start) {
        LongOpenHashSet out = new LongOpenHashSet();
        LongArrayFIFOQueue q = new LongArrayFIFOQueue();
        q.enqueue(start.asLong());

        while (!q.isEmpty()) {
            long cur = q.dequeueLong();
            if (!out.add(cur)) continue;

            BlockPos pos = BlockPos.of(cur);
            BlockState state = level.getBlockState(pos);
            if (!isPart(state)) {
                out.remove(cur);
                continue;
            }
            for (Direction d : Direction.values()) {
                q.enqueue(pos.relative(d).asLong());
            }
        }
        return out;
    }

    private static AABB getValidAABB(ServerLevel level, LongOpenHashSet group) {
        for (long l : group) {
            BlockPos center = BlockPos.of(l).above();
            if (!level.getBlockState(center).isAir()) {
                continue;
            }

            if (matchesAt(level, group, center)) {
                return new AABB(center).inflate(1);
            }
        }

        return null;
    }

    private static boolean matchesAt(ServerLevel level, LongOpenHashSet group, BlockPos center) {
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos p = center.offset(dx, dy, dz);

                    boolean required;
                    if (dy == 0) {
                        required = !(dx == 0 && dz == 0);
                    } else {
                        required = (Math.abs(dx) + Math.abs(dz)) <= 1;
                    }

                    if (!required) {
                        continue;
                    }

                    if (!group.contains(p.asLong())) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
