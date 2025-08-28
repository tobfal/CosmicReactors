package at.tobfal.cosmicreactors.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class AabbCodecs {
    private AabbCodecs() {}

    public static final Codec<AABB> AABB_CODEC = Codec.DOUBLE.listOf().comapFlatMap(list -> {
        if (list.size() != 6) {
            return DataResult.error(() -> "AABB needs 6 doubles (minX,minY,minZ,maxX,maxY,maxZ)");
        }
        return DataResult.success(new AABB(
                list.get(0), list.get(1), list.get(2),
                list.get(3), list.get(4), list.get(5)
        ));
    }, bb -> List.of(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ));
}
