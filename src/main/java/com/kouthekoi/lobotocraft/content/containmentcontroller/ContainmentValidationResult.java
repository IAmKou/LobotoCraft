package com.kouthekoi.lobotocraft.content.containmentcontroller;

import net.minecraft.core.BlockPos;

import java.util.Collection;
import java.util.List;

public record ContainmentValidationResult(boolean valid, String reason, List<BlockPos> walls) {
    public static ContainmentValidationResult valid(String reason, Collection<BlockPos> walls) {
        return new ContainmentValidationResult(true, reason, List.copyOf(walls));
    }
    public static ContainmentValidationResult invalid(String reason) {
        return new ContainmentValidationResult(false, reason, List.of());
    }
}
