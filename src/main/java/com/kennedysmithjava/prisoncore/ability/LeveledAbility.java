package com.kennedysmithjava.prisoncore.ability;


import com.kennedysmithjava.prisoncore.event.EventAbilityUse;
import com.kennedysmithjava.prisoncore.tools.Buffer;

import java.util.*;
import java.util.stream.Collectors;

public class LeveledAbility {

    private final AbilityType abilityType;
    private final int level;
    private final Map<Buffer, Integer> bufferLevels = new HashMap<>();

    public LeveledAbility(AbilityType abilityType) {
        this(abilityType, 1, Collections.emptyMap());
    }

    public LeveledAbility(AbilityType abilityType, int level, Map<Buffer, Integer> bufferLevels) {
        this.abilityType = abilityType;
        this.level = level;
        this.bufferLevels.putAll(
                bufferLevels.entrySet().stream()
                        .filter(bufferLevel -> bufferLevel.getValue() > 0)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

    public AbilityType getAbilityType() {
        return abilityType;
    }

    public Ability<?> getAbility() {
        return Ability.getByType(abilityType);
    }

    public int getLevel() {
        return level;
    }

    /**
     * Amount of buffer slots
     * @return int
     */
    public int getMaxBufferCount() {
        return level;
    }

    public Map<Buffer, Integer> getBufferLevels() {
        return bufferLevels;
    }

    public Set<Buffer> getBuffers() {
        return bufferLevels.keySet();
    }

    public Integer getBufferLevel(Buffer buffer) {
        return bufferLevels.get(buffer);
    }

    public Set<Buffer> getAllowedBuffers() {
        return getAbility().getAllowedBuffers();
    }

    public boolean isAllowedBuffer(Buffer buffer) {
        return getAbility().isAllowedBuffer(buffer);
    }

    public Map<Buffer, Integer> getMaxBufferLevels() {
        return getAbility().getMaxBufferLevels();
    }

    public boolean canUpgradeBuffer(Buffer buffer) {
        Ability<?> ability = getAbility();
        return (this.getBuffers().contains(buffer) || this.getBuffers().size() < this.getMaxBufferCount())
                && ability.isAllowedBuffer(buffer)
                && ability.getMaxBufferLevel(buffer) > getBufferLevel(buffer);
    }

    public void upgradeBuffer(Buffer buffer) {
        this.upgradeBuffer(buffer, 1);
    }

    public void upgradeBuffer(Buffer buffer, int amount) {
        bufferLevels.put(buffer, bufferLevels.getOrDefault(buffer, 0) + amount);
    }

    public boolean downgradeBuffer(Buffer buffer) {
        return this.downgradeBuffer(buffer, 1);
    }

    public boolean downgradeBuffer(Buffer buffer, int amount) {
        if(bufferLevels.containsKey(buffer)) {
            bufferLevels.put(buffer, bufferLevels.get(buffer) - amount);
            return true;
        }
        return false;
    }

    public void perform(EventAbilityUse event) {
        getAbility().run(event, this.bufferLevels);
    }

}
