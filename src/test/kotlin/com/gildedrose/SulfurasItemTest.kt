package com.gildedrose

import com.gildedrose.ItemType.SULFURAS_RAGNAROS
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SulfurasItemTest {
    @Test
    fun `sulfuras never increases or decreases, always 8`() {
        val app = GildedRose(listOf(Item(SULFURAS_RAGNAROS.itemName, 20, 8)))
        app.updateQuality()

        assertEquals(20, app.items[0].sellIn)
        assertEquals(8, app.items[0].quality)
    }

    @Test
    fun `sulfuras never increases or decreases, always 80`() {
        val app = GildedRose(listOf(Item(SULFURAS_RAGNAROS.itemName, 2, 80)))
        app.updateQuality()

        assertEquals(2, app.items[0].sellIn)
        assertEquals(80, app.items[0].quality)
    }

    @Test
    fun `sulfuras never increases or decreases, always 1`() {
        val app = GildedRose(listOf(Item(SULFURAS_RAGNAROS.itemName, -1, 1)))
        app.updateQuality()

        assertEquals(-1, app.items[0].sellIn)
        assertEquals(1, app.items[0].quality)
    }

    @Test
    fun `sulfuras never increases or decreases, always 0`() {
        val app = GildedRose(listOf(Item(SULFURAS_RAGNAROS.itemName, -1, 0)))
        app.updateQuality()

        assertEquals(-1, app.items[0].sellIn)
        assertEquals(0, app.items[0].quality)
    }

    @Test
    fun `sulfuras never increases or decreases, always -1`() {
        val app = GildedRose(listOf(Item(SULFURAS_RAGNAROS.itemName, -1, -1)))
        app.updateQuality()

        assertEquals(-1, app.items[0].sellIn)
        assertEquals(-1, app.items[0].quality)
    }
}