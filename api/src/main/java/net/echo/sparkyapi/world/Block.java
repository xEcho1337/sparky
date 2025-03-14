package net.echo.sparkyapi.world;

import java.util.HashMap;
import java.util.Map;

public enum Block {

    AIR(0),
    STONE(1),
    GRASS(2),
    DIRT(3),
    COBBLESTONE(4),
    WOOD(5),
    SAPLING(6),
    BEDROCK(7),
    WATER(8),
    STATIONARY_WATER(9),
    LAVA(10),
    STATIONARY_LAVA(11),
    SAND(12),
    GRAVEL(13),
    GOLD_ORE(14),
    IRON_ORE(15),
    COAL_ORE(16),
    LOG(17),
    LEAVES(18),
    SPONGE(19),
    GLASS(20),
    LAPIS_ORE(21),
    LAPIS_BLOCK(22),
    DISPENSER(23),
    SANDSTONE(24),
    NOTE_BLOCK(25),
    BED_BLOCK(26),
    POWERED_RAIL(27),
    DETECTOR_RAIL(28),
    PISTON_STICKY_BASE(29),
    WEB(30),
    LONG_GRASS(31),
    DEAD_BUSH(32),
    PISTON_BASE(33),
    PISTON_EXTENSION(34),
    WOOL(35),
    PISTON_MOVING_PIECE(36),
    YELLOW_FLOWER(37),
    RED_ROSE(38),
    BROWN_MUSHROOM(39),
    RED_MUSHROOM(40),
    GOLD_BLOCK(41),
    IRON_BLOCK(42),
    DOUBLE_STEP(43),
    STEP(44),
    BRICK(45),
    TNT(46),
    BOOKSHELF(47),
    MOSSY_COBBLESTONE(48),
    OBSIDIAN(49),
    TORCH(50),
    FIRE(51),
    MOB_SPAWNER(52),
    WOOD_STAIRS(53),
    CHEST(54),
    REDSTONE_WIRE(55),
    DIAMOND_ORE(56),
    DIAMOND_BLOCK(57),
    WORKBENCH(58),
    CROPS(59),
    SOIL(60),
    FURNACE(61),
    BURNING_FURNACE(62),
    SIGN_POST(63),
    WOODEN_DOOR(64),
    LADDER(65),
    RAILS(66),
    COBBLESTONE_STAIRS(67),
    WALL_SIGN(68),
    LEVER(69),
    STONE_PLATE(70),
    IRON_DOOR_BLOCK(71),
    WOOD_PLATE(72),
    REDSTONE_ORE(73),
    GLOWING_REDSTONE_ORE(74),
    REDSTONE_TORCH_OFF(75),
    REDSTONE_TORCH_ON(76),
    STONE_BUTTON(77),
    SNOW(78),
    ICE(79),
    SNOW_BLOCK(80),
    CACTUS(81),
    CLAY(82),
    SUGAR_CANE_BLOCK(83),
    JUKEBOX(84),
    FENCE(85),
    PUMPKIN(86),
    NETHERRACK(87),
    SOUL_SAND(88),
    GLOWSTONE(89),
    PORTAL(90),
    JACK_O_LANTERN(91),
    CAKE_BLOCK(92),
    DIODE_BLOCK_OFF(93),
    DIODE_BLOCK_ON(94),
    STAINED_GLASS(95),
    TRAP_DOOR(96),
    MONSTER_EGGS(97),
    SMOOTH_BRICK(98),
    HUGE_MUSHROOM_1(99),
    HUGE_MUSHROOM_2(100),
    IRON_FENCE(101),
    THIN_GLASS(102),
    MELON_BLOCK(103),
    PUMPKIN_STEM(104),
    MELON_STEM(105),
    VINE(106),
    FENCE_GATE(107),
    BRICK_STAIRS(108),
    SMOOTH_STAIRS(109),
    MYCEL(110),
    WATER_LILY(111),
    NETHER_BRICK(112),
    NETHER_FENCE(113),
    NETHER_BRICK_STAIRS(114),
    NETHER_WARTS(115),
    ENCHANTMENT_TABLE(116),
    BREWING_STAND(117),
    CAULDRON(118),
    ENDER_PORTAL(119),
    ENDER_PORTAL_FRAME(120),
    ENDER_STONE(121),
    DRAGON_EGG(122),
    REDSTONE_LAMP_OFF(123),
    REDSTONE_LAMP_ON(124),
    WOOD_DOUBLE_STEP(125),
    WOOD_STEP(126),
    COCOA(127),
    SANDSTONE_STAIRS(128),
    EMERALD_ORE(129),
    ENDER_CHEST(130),
    TRIPWIRE_HOOK(131),
    TRIPWIRE(132),
    EMERALD_BLOCK(133),
    SPRUCE_WOOD_STAIRS(134),
    BIRCH_WOOD_STAIRS(135),
    JUNGLE_WOOD_STAIRS(136),
    COMMAND(137),
    BEACON(138),
    COBBLE_WALL(139),
    FLOWER_POT(140),
    CARROT(141),
    POTATO(142),
    WOOD_BUTTON(143),
    SKULL(144),
    ANVIL(145),
    TRAPPED_CHEST(146),
    GOLD_PLATE(147),
    IRON_PLATE(148),
    REDSTONE_COMPARATOR_OFF(149),
    REDSTONE_COMPARATOR_ON(150),
    DAYLIGHT_DETECTOR(151),
    REDSTONE_BLOCK(152),
    QUARTZ_ORE(153),
    HOPPER(154),
    QUARTZ_BLOCK(155),
    QUARTZ_STAIRS(156),
    ACTIVATOR_RAIL(157),
    DROPPER(158),
    STAINED_CLAY(159),
    STAINED_GLASS_PANE(160),
    LEAVES_2(161),
    LOG_2(162),
    ACACIA_STAIRS(163),
    DARK_OAK_STAIRS(164),
    SLIME_BLOCK(165),
    BARRIER(166),
    IRON_TRAPDOOR(167),
    PRISMARINE(168),
    SEA_LANTERN(169),
    HAY_BLOCK(170),
    CARPET(171),
    HARD_CLAY(172),
    COAL_BLOCK(173),
    PACKED_ICE(174),
    DOUBLE_PLANT(175),
    STANDING_BANNER(176),
    WALL_BANNER(177),
    DAYLIGHT_DETECTOR_INVERTED(178),
    RED_SANDSTONE(179),
    RED_SANDSTONE_STAIRS(180),
    DOUBLE_STONE_SLAB2(181),
    STONE_SLAB2(182),
    SPRUCE_FENCE_GATE(183),
    BIRCH_FENCE_GATE(184),
    JUNGLE_FENCE_GATE(185),
    DARK_OAK_FENCE_GATE(186),
    ACACIA_FENCE_GATE(187),
    SPRUCE_FENCE(188),
    BIRCH_FENCE(189),
    JUNGLE_FENCE(190),
    DARK_OAK_FENCE(191),
    ACACIA_FENCE(192),
    SPRUCE_DOOR(193),
    BIRCH_DOOR(194),
    JUNGLE_DOOR(195),
    ACACIA_DOOR(196),
    DARK_OAK_DOOR(197);

    private static final Map<Short, Block> blockStates = new HashMap<>();

    static {
        for (Block value : values()) {
            blockStates.put(value.getId(), value);
        }
    }

    private final short id;
    private int data;

    Block(int id) {
        this.id = (short) id;
    }

    public static Block fromId(short id) {
        return blockStates.get(id);
    }

    public static Block fromCombinedId(int combinedId) {
        short id = (short) (combinedId >> 4);
        int data = combinedId & 15;

        Block block = blockStates.get(id);
        block.setData(data);

        return block;
    }

    public short getId() {
        return id;
    }

    public int getState() {
        return id << 4 | data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
