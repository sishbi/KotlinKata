package com.gildedrose

import com.gildedrose.ext.ItemType.AGED_BRIE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BrieItemTest {
    @Test
    fun `brie should increase by 1 before sell-in date`() {
        val app = GildedRose(listOf(Item(AGED_BRIE.itemName, 2, 0)))
        app.updateQuality()

        assertEquals(1, app.items[0].sellIn, "sell-in")
        assertEquals(1, app.items[0].quality, "quality")
    }

    @Test
    fun `brie should increase by 1 before sell-in date, never more than 50`() {
        val app = GildedRose(listOf(Item(AGED_BRIE.itemName, 2, 50)))
        app.updateQuality()

        assertEquals(1, app.items[0].sellIn, "sell-in")
        assertEquals(50, app.items[0].quality, "quality")
    }

    @Test
    fun `brie should increase by 2 at sell-in date`() {
        val app = GildedRose(listOf(Item(AGED_BRIE.itemName, 0, 0)))
        app.updateQuality()

        assertEquals(-1, app.items[0].sellIn, "sell-in")
        assertEquals(2, app.items[0].quality, "quality")
    }

    @Test
    fun `brie should increase by 2 after sell-in date`() {
        val app = GildedRose(listOf(Item(AGED_BRIE.itemName, -1, 0)))
        app.updateQuality()

        assertEquals(-2, app.items[0].sellIn, "sell-in")
        assertEquals(2, app.items[0].quality, "quality")
    }

    @Test
    fun `brie should increase by 2 after sell-in date, never more than 50`() {
        val app = GildedRose(listOf(Item(AGED_BRIE.itemName, -1, 49)))
        app.updateQuality()

        assertEquals(-2, app.items[0].sellIn, "sell-in")
        assertEquals(50, app.items[0].quality, "quality")
    }
}