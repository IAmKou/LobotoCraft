package com.kouthekoi.lobotocraft.content.containmentcontroller;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

import java.util.Collection;
import java.util.List;

public record ContainmentValidationResult(boolean valid, String reason, List<BlockPos> walls, AABB room) {
    public static ContainmentValidationResult valid(String reason, Collection<BlockPos> walls, AABB room) {
        return new ContainmentValidationResult(true, reason, List.copyOf(walls), room);
    }

    public static ContainmentValidationResult invalid(String reason) {
        return new ContainmentValidationResult(false, reason, List.of(), null);
    }
}
