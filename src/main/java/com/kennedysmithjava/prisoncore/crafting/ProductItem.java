package com.kennedysmithjava.prisoncore.crafting;

import java.util.function.Consumer;

public class ProductItem {
    Consumer<CraftingRequest> productConsumer;

    public ProductItem(Consumer<CraftingRequest> productConsumer) {
        this.productConsumer = productConsumer;
    }

    public void get(CraftingRequest craftingRequest) {
        productConsumer.accept(craftingRequest);
    }
}
