package com.kennedysmithjava.prisonmines.pouch;

import java.util.*;

public class PlayerPouchHandler {
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final PlayerPouchHandler i = new PlayerPouchHandler();

    public static PlayerPouchHandler get() {
        return i;
    }

    public PlayerPouchHandler() {
        this.playerPouches = new HashMap<>();
    }

    private final Map<UUID, Set<Pouch>> playerPouches;

    public void registerPouch(UUID uuid, Pouch pouch) {
        Set<Pouch> found = this.playerPouches.getOrDefault(uuid, new HashSet<>());
        found.add(pouch);
        this.playerPouches.put(uuid, found);
    }

    public Set<Pouch> getPouches(UUID uuid) {
        return this.playerPouches.getOrDefault(uuid, new HashSet<>());
    }

    public void emptyPouches(UUID uuid) {
        Set<Pouch> found = this.playerPouches.get(uuid);
        if (found == null) {
            return;
        }

        found.forEach(Pouch::emptyPouch);
    }

    public void tryFulfill(UUID uuid, Pouchable pouchable, int amount) throws PouchFullException, NoPouchFoundException {
        Set<Pouch> found = this.playerPouches.get(uuid);
        if (found == null) {
            throw new NoPouchFoundException();
        }

        for (Pouch pouch : found) {
            try {
                pouch.pouch(pouchable, amount);
                return;
            } catch (PouchFullException e) {
                amount = e.getAmountRemaining();
            }
        }

        throw new PouchFullException(amount);
    }


    public void remove(UUID uuid) {
        this.playerPouches.remove(uuid);
    }
}
