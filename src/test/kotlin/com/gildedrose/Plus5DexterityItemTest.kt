package com.gildedrose

import com.gildedrose.ItemType.PLUS5_DEXTERITY_VEST
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Plus5DexterityItemTest {
    @Test
    fun `plus5 dexterity decreases by 1 before sell-in date`() {
        val app = GildedRose(listOf(Item(PLUS5_DEXTERITY_VEST.itemName, 2, 2)))
        app.updateQuality()

        assertEquals(1, app.items[0].sellIn)
        assertEquals(1, app.items[0].quality)
    }

    @Test
    fun `plus5 dexterity decreases by 1 before sell-in date, never less than zero`() {
        val app = GildedRose(listOf(Item(PLUS5_DEXTERITY_VEST.itemName, 2, 0)))
        app.updateQuality()

        assertEquals(1, app.items[0].sellIn)
        assertEquals(0, app.items[0].quality)
    }

    @Test
    fun `plus5 dexterity decreases by 2 at sell-in date`() {
        val app = GildedRose(listOf(Item(PLUS5_DEXTERITY_VEST.itemName, 0, 3)))
        app.updateQuality()

        assertEquals(-1, app.items[0].sellIn)
        assertEquals(1, app.items[0].quality)
    }

    @Test
    fun `plus5 dexterity decreases by 2 after sell-in date`() {
        val app = GildedRose(listOf(Item(PLUS5_DEXTERITY_VEST.itemName, -1, 3)))
        app.updateQuality()

        assertEquals(-2, app.items[0].sellIn)
        assertEquals(1, app.items[0].quality)
    }

    @Test
    fun `plus5 dexterity decreases by 2 after sell-in date, never less than zero`() {
        val app = GildedRose(listOf(Item(PLUS5_DEXTERITY_VEST.itemName, -1, 1)))
        app.updateQuality()

        assertEquals(-2, app.items[0].sellIn)
        assertEquals(0, app.items[0].quality)
    }

    @Test
    fun `plus5 dexterity never less than zero`() {
        val app = GildedRose(listOf(Item(PLUS5_DEXTERITY_VEST.itemName, -2, 0)))
        app.updateQuality()

        assertEquals(-3, app.items[0].sellIn)
        assertEquals(0, app.items[0].quality)
    }
}