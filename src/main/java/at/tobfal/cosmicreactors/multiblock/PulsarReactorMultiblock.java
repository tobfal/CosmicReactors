package at.tobfal.cosmicreactors.multiblock;

import at.tobfal.cosmicreactors.block.BasePulsarReactorBlock;
import at.tobfal.cosmicreactors.block.PulsarReactorPortBlock;
import at.tobfal.cosmicreactors.block.entity.PulsarReactorPortBlockEntity;
import at.tobfal.cosmicreactors.data.PulsarReactorSavedData;
import at.tobfal.cosmicreactors.entity.PulsarReactorCoreEntity;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class PulsarReactorMultiblock {
    private PulsarReactorMultiblock() {}

    public record Result(boolean formed, AABB bounds, LongOpenHashSet members) {}

    public static Result scanAround(ServerLevel level, BlockPos around) {
        LongOpenHashSet members = flood(level, around);
        if (members.isEmpty()) {
            return new Result(false, new AABB(0,0,0,0,0,0), members);
        }
        AABB bounds = boundsOf(members);
        boolean formed = validateStructure(level, members, bounds);
        return new Result(formed, bounds, members);
    }

    public static void rebuildAt(ServerLevel level, BlockPos around) {
        Result r = scanAround(level, around);
        if (r.members().isEmpty()) return;

        boolean hasPort = false;
        for (long l : r.members()) {
            if (level.getBlockState(BlockPos.of(l)).getBlock() instanceof PulsarReactorPortBlock) {
                hasPort = true;
                break;
            }
        }
        boolean formed = r.formed() && hasPort;

        List<PulsarReactorPortBlockEntity> ports = new ArrayList<>();
        for (long l : r.members()) {
            BlockPos p = BlockPos.of(l);
            var be = level.getBlockEntity(p);
            if (be instanceof PulsarReactorPortBlockEntity port) {
                ports.add(port);
            }
        }

        PulsarReactorSavedData data = PulsarReactorSavedData.get(level);

        UUID id = null;

        if (formed) {
            for (var port : ports) {
                if (port.getReactorId() != null) {
                    id = port.getReactorId();
                    break;
                }
            }

            if (id == null) {
                id = UUID.randomUUID();
            }

            data.put(id, r.bounds());
            for (var port : ports) {
                port.setFormed(true);
                port.setReactorId(id);
            }

            if (!level.isClientSide()) {
                Vec3 center = r.bounds().getCenter();
                BlockPos centerPos = new BlockPos((int) center.x,(int) center.y,(int) center.z);
                level.sendParticles(ParticleTypes.SCRAPE, center.x, center.y, center.z,50,1f,1f,1f,0.5f);
                level.playSound(null, centerPos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 5.0f, 1.4f);
                level.addFreshEntity(new PulsarReactorCoreEntity(level, center.x + 0.5, center.y, center.z + 0.5));
            }
        } else {
            for (var port : ports) {
                if (port.getReactorId() != null) {
                    id = port.getReactorId();
                    break;
                }
            }

            if (id != null){
                data.remove(id);
            }

            for (var port : ports) {
                port.setFormed(false);
                port.setReactorId(null);
            }
        }
    }

    private static boolean isPart(BlockState state) {
        // TODO: Probably a BlockTag is the better way
        return state.getBlock() instanceof BasePulsarReactorBlock;
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

    private static AABB boundsOf(LongOpenHashSet set) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;
        for (long l : set) {
            BlockPos p = BlockPos.of(l);
            if (p.getX() < minX) minX = p.getX();
            if (p.getY() < minY) minY = p.getY();
            if (p.getZ() < minZ) minZ = p.getZ();
            if (p.getX() > maxX) maxX = p.getX();
            if (p.getY() > maxY) maxY = p.getY();
            if (p.getZ() > maxZ) maxZ = p.getZ();
        }
        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }
    private static boolean validateStructure(ServerLevel level, LongOpenHashSet group, AABB b) {
        int minX = (int) b.minX, minY = (int) b.minY, minZ = (int) b.minZ;
        int maxX = (int) b.maxX, maxY = (int) b.maxY, maxZ = (int) b.maxZ;

        if ((maxX - minX) != 2 || (maxY - minY) != 2 || (maxZ - minZ) != 2) return false;
        if (group.size() != 26) return false;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos p = new BlockPos(x, y, z);
                    boolean shell = x == minX || x == maxX || y == minY || y == maxY || z == minZ || z == maxZ;
                    if (shell) {
                        if (!group.contains(p.asLong())) return false;
                    } else {
                        if (!level.isEmptyBlock(p)) return false;
                    }
                }
            }
        }
        return true;
    }
}
