package com.gildedtros.application.gildedtros.service;

import com.gildedtros.domain.item.model.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static com.gildedtros.domain.item.model.ItemMother.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedTrosServiceTest {

    @Test
    @DisplayName("Adding items will not change the properties.")
    void addItemWithoutUpdate() {
        final Item[] items = {defaultItem(25, 25)};
        final GildedTrosService app = new GildedTrosService(items);

        assertEquals(25, app.items[0].sellIn);
        assertEquals(25, app.items[0].quality);
    }

    @Test
    @DisplayName("Add a item and update the quality when not past sell date will decrease quality and sellIn by 1.")
    void addItemAndUpdate() {
        final Item[] items = {defaultItem(1, 50)};

        final GildedTrosService app = new GildedTrosService(items);
        app.updateQuality();

        assertEquals(0, app.items[0].sellIn);
        assertEquals(49, app.items[0].quality);
    }

    @Test
    @DisplayName("Once the sell by date has passed, Quality degrades twice as fast.")
    void addItemAndUpdatePastSellIn() {
        final Item[] items = {defaultItem(0, 50)};

        final GildedTrosService app = new GildedTrosService(items);
        app.updateQuality();

        assertEquals(-1, app.items[0].sellIn);
        assertEquals(48, app.items[0].quality);
    }

    @Test
    @DisplayName("The Quality of an item is never negative.")
    void qualityIsNeverNegative() {
        final Item[] items = {defaultItem(1, 0)};

        final GildedTrosService app = new GildedTrosService(items);
        app.updateQuality();

        assertEquals(0, app.items[0].quality);
    }

    @Test
    @DisplayName("'Good Wine' actually increases in Quality the older it gets.")
    void goodWineIncreasesInQuality() {
        final Item[] items = {goodWine(1, 0)};

        final GildedTrosService app = new GildedTrosService(items);
        app.updateQuality();

        assertEquals(1, app.items[0].quality);
    }

    @Test
    @DisplayName("The Quality of an item is never more than 50.")
    void QualityIsMax50() {
        final Item[] items = {goodWine(1, 50)};

        final GildedTrosService app = new GildedTrosService(items);
        app.updateQuality();

        assertEquals(50, app.items[0].quality);
    }

    @Test
    @DisplayName("'B-DAWG Keychain' always has quality 80.")
    void BDawgKeychainQualityAlways80() {
        final Item[] items = {new Item("B-DAWG Keychain", Integer.MAX_VALUE, 50)};

        final GildedTrosService app = new GildedTrosService(items);
        app.updateQuality();

        assertEquals(80, app.items[0].quality);
    }

    @Test
    @DisplayName("'B-DAWG Keychain' never has to be sold or decreases in Quality.")
    void BDawgKeychainPropsNeverChange() {
        final Item[] items = {bDawgKeychain()};

        final GildedTrosService app = new GildedTrosService(items);
        app.updateQuality();

        assertEquals(Integer.MAX_VALUE, app.items[0].sellIn);
        assertEquals(80, app.items[0].quality);
    }

    @ParameterizedTest
    @MethodSource("provideBackstagePassesWithQualityToBe")
    @DisplayName(
            "Backstage quality :" +
                    "-increases by 1 when sellIn > 10." +
                    "-increases by 2 when 5 < sellIn <= 10." +
                    "-increases by 3 when sellIn <= 5." +
                    "-drops to 0 when sellIn < 0."
    )
    void backStagePasses(final Item backstagePasses, final int qualityToBe) {
        final Item[] items = {backstagePasses};

        final GildedTrosService app = new GildedTrosService(items);
        app.updateQuality();

        assertEquals(qualityToBe, app.items[0].quality);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Duplicate Code", "Long Methods", "Ugly Variable Names"})
    @DisplayName("Smelly items degrade in Quality twice as fast as normal items")
    void backStagePasses(final String smellyItem) {
        final Item[] items = {new Item(smellyItem, 25, 20)};

        final GildedTrosService app = new GildedTrosService(items);
        app.updateQuality();

        assertEquals(18, app.items[0].quality);
    }

    private static Stream<Arguments> provideBackstagePassesWithQualityToBe() {
        return Stream.of(
                Arguments.of(backstagePasses(25, 20), 21), //increases by 1 when sellIn > 10
                Arguments.of(backstagePasses(10, 20), 22), //increases by 2 when 5 < sellIn <= 10
                Arguments.of(backstagePasses(5, 20), 23), //increases by 1 when sellIn > 10
                Arguments.of(backstagePasses(0, 20), 0) //increases by 1 when sellIn > 10
        );
    }
}
