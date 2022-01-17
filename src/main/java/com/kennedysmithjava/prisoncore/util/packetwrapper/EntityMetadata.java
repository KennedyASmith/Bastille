package com.kennedysmithjava.prisoncore.util.packetwrapper;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;

public class EntityMetadata {

    public enum EntityMetadataType {
        ENTITY_STATUS(0, Byte.class),
        ENTITY_AIR(1, Short.class),

        LIVING_ENITITY_NAMETAG(2, String.class),
        LIVING_ENITITY_ALWAYS_SHOW_NAMETAG(3, Byte.class),
        LIVING_ENITITY_HEALTH(6, Float.class),
        LIVING_ENTITY_POTION_EFFECT_COLOR(7, Integer.class),
        LIVING_ENTITY_IS_POTION_EFFECT_AMBIENT(8, Byte.class),
        LIVING_ENTITY_NUMBER_OF_ARROWS(9, Byte.class),
        LIVING_ENTITY_NO_AI(15, Byte.class),

        AGEABLE_AGE(12, Byte.class),

        ARMOUR_STAND_TYPE(10, Byte.class),
        ARMOUR_STAND_HEAD_POS(11, Object.class),
        ARMOUR_STAND_BODY_POS(12, Object.class),
        ARMOUR_STAND_LEFT_ARM_POS(13, Object.class),
        ARMOUR_STAND_RIGHT_ARM_POS(14, Object.class),
        ARMOUR_STAND_LEFT_LEG_POS(15, Object.class),
        ARMOUR_STAND_RIGHT_LEG_POS(16, Object.class),

        HUMAN_SKIN_FLAGS(10, Byte.class),
        HUMAN_CAPE_SETTINGS(16, Byte.class),
        HUMAN_ABSORPTION_HEARTS(17, Float.class),
        HUMAN_SCORE(18, Integer.class),

        HORSE_STATUS(16, Byte.class),
        HORSE_TYPE(19, Byte.class),
        HORSE_STYLE(20, Integer.class),
        HORSE_OWNER_NAME(21, String.class),
        HORSE_ARMOR(22, Integer.class),

        BAT_IS_HANGING(16, Byte.class),

        TAMEABLE_STATUS(16, Byte.class),
        TAMEABLE_OWNER_NAME(17, String.class),

        OCELOT_TYPE(18, Byte.class),

        WOLF_STATUS(16, Byte.class),
        WOLF_HEALTH(18, Float.class),
        WOLF_BEGGING(19, Byte.class),
        WOLF_COLLAR_COLOR(20, Byte.class),

        PIG_HAS_SADDLE(16, Byte.class),

        RABBIT_TYPE(18, Byte.class),

        SHEEP_COAT(16, Byte.class),

        VILLAGER_TYPE(16, Integer.class),

        ENDERMAN_CARRIED_BLOCK(16, Short.class),
        ENDERMAN_CARRIED_BLOCKDATA(17, Byte.class),
        ENDERMAN_IS_SCREAMING(18, Byte.class),

        ZOMBIE_IS_CHILD(12, Byte.class),
        ZOMBIE_IS_VILLAGER(13, Byte.class),
        ZOMBIE_IS_CONVERTING(14, Byte.class),

        BLAZE_IS_ON_FIRE(16, Byte.class),

        SPIDER_IS_CLIMBING(16, Byte.class),

        CREEPER_STATE(16, Byte.class),
        CREEPER_IS_POWERED(17, Byte.class),

        GHAST_IS_ATTACKING(16, Byte.class),

        SLIME_SIZE(16, Byte.class),

        SKELETON_TYPE(13, Byte.class), //Normal or Wither

        WITCH_IS_AGGRESSIVE(21, Byte.class),

        IRON_GOLEM_IS_PLAYER_CREATED(16, Byte.class),

        WITHER_WATCHED_TARGET_1(17, Integer.class),
        WITHER_WATCHED_TARGET_2(18, Integer.class),
        WITHER_WATCHED_TARGET_3(19, Integer.class),
        WITHER_INVULNERABLE_TIME(20, Integer.class),

        BOAT_TIME_SINCE_HIT(17, Integer.class),
        BOAT_FORWARD_DIRECTION(18, Integer.class),
        BOAT_DAMAGE_TAKEN(19, Float.class),

        MINECART_SHAKING_POWER(17, Integer.class),
        MINECART_SHAKING_DIRECTION(18, Integer.class),
        MINECART_DAMAGE_TAKEN(19, Float.class),
        MINECART_BLOCK_INSIDE(20, Integer.class),
        MINECART_BLOCK_Y_POSITION(21, Integer.class),
        MINECART_SHOW_BLOCK(22, Byte.class),

        FURNACE_MINECART_IS_POWERED(16, Byte.class),

        ITEM_SLOT(10, Integer.class),

        ARROW_IS_CRITICAL(16, Byte.class),

        FIREWORK_INFO(8, Integer.class),

        ITEMFRAME_ITEM(8, Integer.class),
        ITEMFRAME_ROTATION(9, Byte.class),

        ENDERCRYSTAL_HEALTH(8, Integer.class)

        ;

        private final int bit;
        private final Class<?> valueType;

        EntityMetadataType(int bit, Class<?> valueType) {
            this.bit = bit;
            this.valueType = valueType;
        }

        public int getBit() {
            return bit;
        }

        public Class<?> getValueType() {
            return valueType;
        }

        public void set(WrappedDataWatcher dataWatcher, Object value) {
            dataWatcher.setObject(this.bit, value);
        }
    }

    public enum EntityStatus {
        ON_FIRE(0x01),
        CROUCHED(0x02),
        SPRINTING(0x08),
        EATING_DRINKING_BLOCKING(0x10),
        INVISIBLE(0x20);

        private final byte bitmask;

        EntityStatus(int bitmask) {
            this.bitmask = (byte) bitmask;
        }

        public void set(WrappedDataWatcher dataWatcher, boolean value) {
            Byte currVal = dataWatcher.getByte(EntityMetadataType.ENTITY_STATUS.bit);
            if(currVal == null) currVal = 0;
            byte newVal = (byte) (value ? currVal | this.bitmask : currVal & ~this.bitmask);
            dataWatcher.setObject(0, newVal);
        }
    }


}
