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

package au.com.grieve.geyser.reversion;

import au.com.grieve.geyser.reversion.registry.BlockTranslatorRegistry;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.ActivatorRail;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.AgedBlock;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.AnvilBlock;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.AxisBlock;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Barrel;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Bed;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Bell;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.BrewingStand;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.BubbleColumn;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Button;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Cake;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Cauldron;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.ChiseledBlock;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.CobblestoneWall;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Cocoa;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.ColoredBlock;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.CommandBlock;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Comparator;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Composter;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Coral;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.CoralBlock;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.CoralFanHang;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.DetectorRail;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.DirectionBlock;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.DirtBlock;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Dispenser;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Door;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.DoublePlant;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Dropper;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Elements;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.EndPortalFrame;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.FacingBlock;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Farmland;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Fence;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.FenceGate;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Grindstone;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.GrowthBlock;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Hopper;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.InfestedBrick;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Kelp;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Lantern;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Leaves;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Lever;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Liquid;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Log;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.LoggerMapper;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Mushroom;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Observer;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Planks;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Portal;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Prismarine;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Rail;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.RedFlower;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.RedstoneSignalBlock;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Repeater;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Sand;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Sandstone;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Sapling;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Scaffolding;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.SeaPickle;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Seagrass;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.SimpleMapper;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.SnowLayer;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Sponge;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Stairs;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.StandingSign;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.StoneBlock;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.StoneBrick;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.StoneSlab;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.StoneSlab2;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.StoneSlab3;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.StoneSlab4;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.StrippedLog;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.StructureBlock;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.TallGrass;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Tnt;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Torch;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Trapdoor;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Tripwire;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.TripwireHook;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.TurtleEggs;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Vine;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.Wood;
import au.com.grieve.geyser.reversion.translators.blocks.mappers.WoodenSlab;
import au.com.grieve.geyser.reversion.translators.blocks.remappers.Basalt;
import au.com.grieve.geyser.reversion.translators.blocks.remappers.SimpleRemapper;

public class Registration {

    private final EduSupportPlugin plugin;
    private final BlockTranslatorRegistry blockTranslatorRegistry;

    public Registration(EduSupportPlugin plugin) {
        this.plugin = plugin;
        this.blockTranslatorRegistry = new BlockTranslatorRegistry(plugin);

        // Register BlockTranslators Remappers
        blockTranslatorRegistry
                .register(new SimpleRemapper())
                .register(new Basalt());

        // Register BlockTranslators Mappers
        blockTranslatorRegistry
                .register(new StoneBlock())
                .register(new DirtBlock())
                .register(new Planks())
                .register(new Sapling())
                .register(new Liquid())
                .register(new Sand())
                .register(new Log())
                .register(new StrippedLog())
                .register(new Wood())
                .register(new Leaves())
                .register(new Sponge())
                .register(new Dispenser())
                .register(new Sandstone())
                .register(new Bed())
                .register(new DetectorRail())
                .register(new FacingBlock())
                .register(new TallGrass())
                .register(new Seagrass())
                .register(new ColoredBlock())
                .register(new RedFlower())
                .register(new Tnt())
                .register(new Torch())
                .register(new AgedBlock())
                .register(new Stairs())
                .register(new RedstoneSignalBlock())
                .register(new GrowthBlock())
                .register(new Farmland())
                .register(new StandingSign())
                .register(new Door())
                .register(new Rail())
                .register(new Lever())
                .register(new Button())
                .register(new SnowLayer())
                .register(new Fence())
                .register(new Portal())
                .register(new DirectionBlock())
                .register(new Cake())
                .register(new Repeater())
                .register(new Trapdoor())
                .register(new StoneBrick())
                .register(new InfestedBrick())
                .register(new Mushroom())
                .register(new Vine())
                .register(new FenceGate())
                .register(new BrewingStand())
                .register(new Cauldron())
                .register(new EndPortalFrame())
                .register(new Cocoa())
                .register(new TripwireHook())
                .register(new Tripwire())
                .register(new CommandBlock())
                .register(new CobblestoneWall())
                .register(new AnvilBlock())
                .register(new Comparator())
                .register(new Hopper())
                .register(new ChiseledBlock())
                .register(new ActivatorRail())
                .register(new Dropper())
                .register(new Prismarine())
                .register(new AxisBlock())
                .register(new DoublePlant())
                .register(new WoodenSlab())
                .register(new StoneSlab())
                .register(new StoneSlab2())
                .register(new StoneSlab3())
                .register(new StoneSlab4())
                .register(new Observer())
                .register(new Kelp())
                .register(new TurtleEggs())
                .register(new CoralBlock())
                .register(new Coral())
                .register(new CoralFanHang())
                .register(new SeaPickle())
                .register(new BubbleColumn())
                .register(new Scaffolding())
                .register(new Barrel())
                .register(new Grindstone())
                .register(new Bell())
                .register(new Lantern())
                .register(new StructureBlock())
                .register(new Composter())
                .register(new Elements())
                .register(new SimpleMapper())
                .register(new LoggerMapper());

    }
}
