package com.gildedtros.application.gildedtros.service;

import com.gildedtros.domain.item.model.Item;

public final class TexttestFixtureIntegrationTest {
    private TexttestFixtureIntegrationTest() {
    }

    public static void main(final String[] args) {
        System.out.println("AXXES CODE KATA - GILDED TROS");

        final Item[] items = {
                new Item("Ring of Cleansening Code", 10, 20),
                new Item("Good Wine", 2, 0),
                new Item("Elixir of the SOLID", 5, 7),
                new Item("B-DAWG Keychain", 0, 80),
                new Item("B-DAWG Keychain", -1, 80),
                new Item("Backstage passes for Re:Factor", 15, 20),
                new Item("Backstage passes for Re:Factor", 10, 49),
                new Item("Backstage passes for HAXX", 5, 49),
                // these smelly items do not work properly yet
                new Item("Duplicate Code", 3, 6),
                new Item("Long Methods", 3, 6),
                new Item("Ugly Variable Names", 3, 6) };

        final GildedTrosService app = new GildedTrosService(items);

        int days = 2;
        if (args.length > 0) {
            days = Integer.parseInt(args[0]) + 1;
        }

        for (int i = 0; i < days; i++) {
            System.out.println("-------- day " + i + " --------");
            System.out.println("name, sellIn, quality");
            for (final Item item : items) {
                System.out.println(item);
            }
            System.out.println();
            app.updateQuality();
        }
    }

}
