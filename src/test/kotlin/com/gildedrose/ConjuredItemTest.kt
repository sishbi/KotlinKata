package com.gildedrose

import com.gildedrose.ext.ItemType.CONJURED_MANA_CAKE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ConjuredItemTest {
    @Test
    fun `conjured mana cake decreases by 1 before sell-in date`() {
        val app = GildedRose(listOf(Item(CONJURED_MANA_CAKE.itemName, 2, 2)))
        app.updateQuality()

        assertEquals(1, app.items[0].sellIn)
        assertEquals(1, app.items[0].quality)
    }

    @Test
    fun `conjured mana cake decreases by 1 before sell-in date, never less than zero`() {
        val app = GildedRose(listOf(Item(CONJURED_MANA_CAKE.itemName, 2, 0)))
        app.updateQuality()

        assertEquals(1, app.items[0].sellIn)
        assertEquals(0, app.items[0].quality)
    }

    @Test
    fun `conjured mana cake decreases by 2 at sell-in date`() {
        val app = GildedRose(listOf(Item(CONJURED_MANA_CAKE.itemName, 0, 3)))
        app.updateQuality()

        assertEquals(-1, app.items[0].sellIn)
        assertEquals(1, app.items[0].quality)
    }

    @Test
    fun `conjured mana cake decreases by 2 after sell-in date`() {
        val app = GildedRose(listOf(Item(CONJURED_MANA_CAKE.itemName, -1, 3)))
        app.updateQuality()

        assertEquals(-2, app.items[0].sellIn)
        assertEquals(1, app.items[0].quality)
    }

    @Test
    fun `conjured mana cake decreases by 2 after sell-in date, never less than zero`() {
        val app = GildedRose(listOf(Item(CONJURED_MANA_CAKE.itemName, -1, 1)))
        app.updateQuality()

        assertEquals(-2, app.items[0].sellIn)
        assertEquals(0, app.items[0].quality)
    }
}