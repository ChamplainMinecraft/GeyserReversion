/*
 * EduSupport - Minecraft Protocol Support for MultiVersion in Geyser
 * Copyright (C) 2020 EduSupport Developers
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package au.com.grieve.geyser.reversion.translators.blocks.remappers;

import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtMapBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class SimpleRemapper extends BaseNbtRemapper {

    private final Map<String, Remap> mapping = new HashMap<>() {{
        put("minecraft:light_block", Remap.of("minecraft:air"));
        put("minecraft:soul_soil", Remap.of("minecraft:dirt"));
        put("minecraft:soul_fire", Remap.of("minecraft:fire"));
        put("minecraft:soul_torch", Remap.of("minecraft:colored_torch_bp"));
        put("minecraft:soul_lantern", Remap.of("minecraft:lantern"));
        put("minecraft:soul_campfire", Remap.of("minecraft:campfire"));
        put("minecraft:nether_gold_ore", Remap.of("minecraft:gold_ore"));
        put("minecraft:stickyPistonArmCollision", Remap.of("minecraft:pistonArmCollision"));
        put("minecraft:chain", Remap.of("minecraft:iron_bars"));
        put("minecraft:structure_void", Remap.of("minecraft:air"));
        put("minecraft:void_air", Remap.of("minecraft:air"));
        put("minecraft:cave_air", Remap.of("minecraft:air"));
        put("minecraft:stripped_warped_stem", Remap.of("minecraft:stripped_dark_oak_log"));
        put("minecraft:warped_nylium", Remap.of("minecraft:mycelium"));
        put("minecraft:warped_fungus", Remap.of("minecraft:brown_mushroom"));
        put("minecraft:nether_sprouts", Remap.of("minecraft:grass"));
        put("minecraft:stripped_crimson_stem", Remap.of("minecraft:stripped_acacia_log"));
        put("minecraft:crimson_nylium", Remap.of("minecraft:mycelium"));
        put("minecraft:crimson_fungus", Remap.of("minecraft:red_mushroom"));
        put("minecraft:shroomlight", Remap.of("minecraft:lit_pumpkin"));
        put("minecraft:crimson_pressure_plate", Remap.of("minecraft:acacia_pressure_plate"));
        put("minecraft:warped_pressure_plate", Remap.of("minecraft:dark_oak_pressure_plate"));
        put("minecraft:crimson_trapdoor", Remap.of("minecraft:acacia_trapdoor"));
        put("minecraft:warped_trapdoor", Remap.of("minecraft:dark_oak_trapdoor"));
        put("minecraft:crimson_fence_gate", Remap.of("minecraft:acacia_fence_gate"));
        put("minecraft:warped_fence_gate", Remap.of("minecraft:dark_oak_fence_gate"));
        put("minecraft:crimson_stairs", Remap.of("minecraft:acacia_stairs"));
        put("minecraft:warped_stairs", Remap.of("minecraft:dark_oak_stairs"));
        put("minecraft:crimson_button", Remap.of("minecraft:acacia_button"));
        put("minecraft:warped_button", Remap.of("minecraft:dark_oak_button"));
        put("minecraft:crimson_door", Remap.of("minecraft:acacia_door"));
        put("minecraft:warped_door", Remap.of("minecraft:dark_oak_door"));
        put("minecraft:crimson_standing_sign", Remap.of("minecraft:acacia_standing_sign"));
        put("minecraft:warped_standing_sign", Remap.of("minecraft:darkoak_standing_sign"));
        put("minecraft:crimson_wall_sign", Remap.of("minecraft:acacia_wall_sign"));
        put("minecraft:warped_wall_sign", Remap.of("minecraft:darkoak_wall_sign"));
        put("minecraft:target", Remap.of("minecraft:hay_block"));
        put("minecraft:beehive", Remap.of("minecraft:dispenser"));
        put("minecraft:crimson_fence", Remap.of("minecraft:fence", NbtMap.builder()
                .putString("wood_type", "acacia")
                .build()));
        put("minecraft:bee_nest", Remap.of("minecraft:shulker_box", NbtMap.builder()
                .putString("color", "yellow")
                .build()));
        put("minecraft:crimson_hyphae", Remap.of("minecraft:wood", NbtMap.builder()
                .putString("wood_type", "acacia")
                .putBoolean("stripped_bit", false)
                .build()));
        put("minecraft:crimson_planks", Remap.of("minecraft:planks", NbtMap.builder()
                .putString("wood_type", "acacia")
                .build()));
        put("minecraft:crimson_slab", Remap.of("minecraft:wooden_slab", NbtMap.builder()
                .putString("wood_type", "acacia")
                .build()));
        put("minecraft:crimson_double_slab", Remap.of("minecraft:double_wooden_slab", NbtMap.builder()
                .putString("wood_type", "acacia")
                .build()));
        put("minecraft:crimson_stem", Remap.of("minecraft:log2", NbtMap.builder()
                .putString("new_log_type", "acacia")
                .build()));
        put("minecraft:stripped_crimson_hyphae", Remap.of("minecraft:wood", NbtMap.builder()
                .putString("wood_type", "acacia")
                .putBoolean("stripped_bit", true)
                .build()));
        put("minecraft:stripped_warped_hyphae", Remap.of("minecraft:wood", NbtMap.builder()
                .putString("wood_type", "dark_oak")
                .putBoolean("stripped_bit", true)
                .build()));
        put("minecraft:warped_fence", Remap.of("minecraft:fence", NbtMap.builder()
                .putString("wood_type", "dark_oak")
                .build()));
        put("minecraft:warped_hyphae", Remap.of("minecraft:wood", NbtMap.builder()
                .putString("wood_type", "dark_oak")
                .putBoolean("stripped_bit", false)
                .build()));
        put("minecraft:warped_planks", Remap.of("minecraft:planks", NbtMap.builder()
                .putString("wood_type", "dark_oak")
                .build()));
        put("minecraft:crimson_roots", Remap.of("minecraft:nether_wart", NbtMap.builder()
                .putString("age", "3")
                .build()));
        put("minecraft:warped_roots", Remap.of("minecraft:nether_wart", NbtMap.builder()
                .putString("age", "3")
                .build()));
        put("minecraft:warped_slab", Remap.of("minecraft:wooden_slab", NbtMap.builder()
                .putString("wood_type", "dark_oak")
                .build()));
        put("minecraft:warped_double_slab", Remap.of("minecraft:double_wooden_slab", NbtMap.builder()
                .putString("wood_type", "dark_oak")
                .build()));
        put("minecraft:warped_stem", Remap.of("minecraft:log2", NbtMap.builder()
                .putString("new_log_type", "dark_oak")
                .build()));
        put("minecraft:warped_wart_block", Remap.of("minecraft:wool", NbtMap.builder()
                .putString("color", "cyan")
                .build()));
        put("minecraft:weeping_vines", Remap.of("minecraft:vine", NbtMap.builder()
                .putInt("vine_direction_bits", 15)
                .build()));
        put("minecraft:twisting_vines", Remap.of("minecraft:vine", NbtMap.builder()
                .putInt("vine_direction_bits", 15)
                .build()));
        put("minecraft:wither_rose", Remap.of("minecraft:red_flower", NbtMap.builder()
                .putString("flower_type", "poppy")
                .build()));
        put("minecraft:honey_block", Remap.of("minecraft:slime"));
        put("minecraft:honeycomb_block", Remap.of("minecraft:coral_block", NbtMap.builder()
                .putString("coral_color", "yellow")
                .build()));
        put("minecraft:netherite_block", Remap.of("minecraft:obsidian"));
        put("minecraft:ancient_debris", Remap.of("minecraft:obsidian"));
        put("minecraft:crying_obsidian", Remap.of("minecraft:glowingobsidian"));
        put("minecraft:respawn_anchor", Remap.of("minecraft:obsidian"));
        put("minecraft:potted_crimson_fungus", Remap.of("minecraft:flower_pot"));
        put("minecraft:potted_warped_fungus", Remap.of("minecraft:flower_pot"));
        put("minecraft:potted_crimson_roots", Remap.of("minecraft:flower_pot"));
        put("minecraft:potted_warped_roots", Remap.of("minecraft:flower_pot"));
        put("minecraft:lodestone", Remap.of("minecraft:quartz_block", NbtMap.builder()
                .putString("chisel_type", "default")
                .putString("pillar_axis", "y")
                .build()));
        put("minecraft:blackstone", Remap.of("minecraft:nether_brick"));
        put("minecraft:blackstone_stairs", Remap.of("minecraft:nether_brick_stairs"));
        put("minecraft:blackstone_wall", Remap.of("minecraft:cobblestone_wall", NbtMap.builder()
                .putString("wall_block_type", "cobblestone")
                .build()));
        put("minecraft:blackstone_slab", Remap.of("minecraft:stone_slab", NbtMap.builder()
                .putString("stone_slab_type", "nether_brick")
                .putBoolean("top_slot_bit", false)
                .build()));
        put("minecraft:blackstone_double_slab", Remap.of("minecraft:double_stone_slab", NbtMap.builder()
                .putString("stone_slab_type", "nether_brick")
                .putBoolean("top_slot_bit", false)
                .build()));

        put("minecraft:polished_blackstone", Remap.of("minecraft:red_nether_brick"));
        put("minecraft:polished_blackstone_bricks", Remap.of("minecraft:red_nether_brick"));
        put("minecraft:cracked_polished_blackstone_bricks", Remap.of("minecraft:red_nether_brick"));
        put("minecraft:chiseled_polished_blackstone", Remap.of("minecraft:red_nether_brick"));
        put("minecraft:polished_blackstone_brick_slab", Remap.of("minecraft:stone_slab2", NbtMap.builder()
                .putString("stone_slab_type_2", "red_nether_brick")
                .putBoolean("top_slot_bit", false)
                .build()));
        put("minecraft:polished_blackstone_brick_double_slab", Remap.of("minecraft:double_stone_slab2", NbtMap.builder()
                .putString("stone_slab_type_2", "red_nether_brick")
                .putBoolean("top_slot_bit", false)
                .build()));
        put("minecraft:polished_blackstone_brick_stairs", Remap.of("minecraft:red_nether_brick_stairs"));
        put("minecraft:polished_blackstone_brick_wall", Remap.of("minecraft:cobblestone_wall", NbtMap.builder()
                .putString("wall_block_type", "cobblestone")
                .build()));
        put("minecraft:gilded_blackstone", Remap.of("minecraft:red_nether_brick"));
        put("minecraft:polished_blackstone_stairs", Remap.of("minecraft:brick_stairs"));
        put("minecraft:polished_blackstone_slab", Remap.of("minecraft:stone_slab2", NbtMap.builder()
                .putString("stone_slab_type_2", "red_nether_brick")
                .putBoolean("top_slot_bit", false)
                .build()
        ));
        put("minecraft:polished_blackstone_double_slab", Remap.of("minecraft:double_stone_slab2", NbtMap.builder()
                .putString("stone_slab_type_2", "red_nether_brick")
                .putBoolean("top_slot_bit", false)
                .build()
        ));
        put("minecraft:polished_blackstone_pressure_plate", Remap.of("minecraft:stone_pressure_plate"));
        put("minecraft:polished_blackstone_wall", Remap.of("minecraft:cobblestone_wall", NbtMap.builder()
                .putString("wall_block_type", "cobblestone")
                .build()));

        put("minecraft:polished_blackstone_button", Remap.of("minecraft:stone_button"));
        put("minecraft:chiseled_nether_bricks", Remap.of("minecraft:nether_brick"));
        put("minecraft:cracked_nether_bricks", Remap.of("minecraft:nether_brick"));
        put("minecraft:quartz_bricks", Remap.of("minecraft:quartz_block", NbtMap.builder()
                .putString("chisel_type", "default")
                .putString("pillar_axis", "y")
                .build()));
    }};

    @Override
    public NbtMap map(NbtMap original) {
        String originalName = original.getCompound("block").getString("name");
        if (!mapping.containsKey(originalName)) {
            return original;
        }

        Remap remap = mapping.get(originalName);

        NbtMap states = original.getCompound("block").getCompound("states");
        if (states == null) {
            states = NbtMap.builder().build();
        }

        if (remap.getStates() != null) {
            NbtMapBuilder statesBuilder = states.toBuilder();
            for (Map.Entry<String, Object> entry : remap.getStates().entrySet()) {
                statesBuilder.put(entry.getKey(), entry.getValue());
            }
            states = statesBuilder.build();
        }

        return original.toBuilder()
                .putCompound("block", original.getCompound("block").toBuilder()
                        .putString("name", remap.getName())
                        .putCompound("states", states)
                        .build())
                .build();
    }

    @Getter
    @AllArgsConstructor
    public static class Remap {
        String name;
        NbtMap states;

        public Remap(String name) {
            this(name, null);
        }

        public static Remap of(String name, NbtMap states) {
            return new Remap(name, states);
        }

        public static Remap of(String name) {
            return new Remap(name);
        }

    }
}
