package com.gildedrose

import com.gildedrose.ItemType.SULFURAS_RAGNAROS
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SulfurasItemTest {
    @Test
    fun `sulfuras never increases or decreases`() {
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
}