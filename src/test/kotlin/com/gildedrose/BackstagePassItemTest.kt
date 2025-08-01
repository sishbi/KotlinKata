package com.gildedrose

import com.gildedrose.ItemType.BACKSTAGE_PASSES
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BackstagePassItemTest {
    @Test
    fun `backstage pass increases by 1 each day, 11 days before concert`() {
        val app = GildedRose(listOf(Item(BACKSTAGE_PASSES.itemName, 11, 0)))
        app.updateQuality()

        assertEquals(10, app.items[0].sellIn)
        assertEquals(1, app.items[0].quality)
    }

    @Test
    fun `backstage pass increases by 1 from 10 each day, 11 days before concert`() {
        val app = GildedRose(listOf(Item(BACKSTAGE_PASSES.itemName, 11, 10)))
        app.updateQuality()

        assertEquals(10, app.items[0].sellIn)
        assertEquals(11, app.items[0].quality)
    }

    @Test
    fun `backstage pass increases by 1 each day, 11 days before concert, never more than 50`() {
        val app = GildedRose(listOf(Item(BACKSTAGE_PASSES.itemName, 11, 50)))
        app.updateQuality()

        assertEquals(10, app.items[0].sellIn)
        assertEquals(50, app.items[0].quality)
    }

    @Test
    fun `backstage pass drops to zero on day of concert`() {
        val app = GildedRose(listOf(Item(BACKSTAGE_PASSES.itemName, 0, 50)))
        app.updateQuality()

        assertEquals(-1, app.items[0].sellIn)
        assertEquals(0, app.items[0].quality)
    }

    @Test
    fun `backstage pass increases by 2 each day, 10 days before concert`() {
        val app = GildedRose(listOf(Item(BACKSTAGE_PASSES.itemName, 10, 0)))
        app.updateQuality()

        assertEquals(9, app.items[0].sellIn)
        assertEquals(2, app.items[0].quality)
    }

    @Test
    fun `backstage pass increases by 2 each day, 10 days before concert, never more than 50`() {
        val app = GildedRose(listOf(Item(BACKSTAGE_PASSES.itemName, 10, 49)))
        app.updateQuality()

        assertEquals(9, app.items[0].sellIn)
        assertEquals(50, app.items[0].quality)
    }

    @Test
    fun `backstage pass increases by 2 each day, 9 days before concert, never more than 50`() {
        val app = GildedRose(listOf(Item(BACKSTAGE_PASSES.itemName, 9, 49)))
        app.updateQuality()

        assertEquals(8, app.items[0].sellIn)
        assertEquals(50, app.items[0].quality)
    }

    @Test
    fun `backstage pass drops to zero after concert, never more than 50`() {
        val app = GildedRose(listOf(Item(BACKSTAGE_PASSES.itemName, -1, 49)))
        app.updateQuality()

        assertEquals(-2, app.items[0].sellIn)
        assertEquals(0, app.items[0].quality)
    }

    @Test
    fun `backstage pass increases by 3 each day, 5 days before concert`() {
        val app = GildedRose(listOf(Item(BACKSTAGE_PASSES.itemName, 5, 0)))
        app.updateQuality()

        assertEquals(4, app.items[0].sellIn)
        assertEquals(3, app.items[0].quality)
    }

    @Test
    fun `backstage pass increases by 3 each day, 5 days before concert, never more than 50`() {
        val app = GildedRose(listOf(Item(BACKSTAGE_PASSES.itemName, 5, 48)))
        app.updateQuality()

        assertEquals(4, app.items[0].sellIn)
        assertEquals(50, app.items[0].quality)
    }

    @Test
    fun `backstage pass increases by 3 each day, 4 days before concert, never more than 50`() {
        val app = GildedRose(listOf(Item(BACKSTAGE_PASSES.itemName, 4, 48)))
        app.updateQuality()

        assertEquals(3, app.items[0].sellIn)
        assertEquals(50, app.items[0].quality)
    }
}