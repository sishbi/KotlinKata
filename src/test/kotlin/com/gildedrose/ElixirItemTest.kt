package com.gildedrose

import com.gildedrose.ItemType.ELIXIR_MONGOOSE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ElixirItemTest {
    @Test
    fun `elixir mongoose decreases by 1 before sell-in date`() {
        val app = GildedRose(listOf(Item(ELIXIR_MONGOOSE.itemName, 2, 2)))
        app.updateQuality()

        assertEquals(1, app.items[0].sellIn)
        assertEquals(1, app.items[0].quality)
    }

    @Test
    fun `elixir mongoose decreases by 1 before sell-in date, never less than zero`() {
        val app = GildedRose(listOf(Item(ELIXIR_MONGOOSE.itemName, 2, 0)))
        app.updateQuality()

        assertEquals(1, app.items[0].sellIn)
        assertEquals(0, app.items[0].quality)
    }

    @Test
    fun `elixir mongoose decreases by 2 at sell-in date`() {
        val app = GildedRose(listOf(Item(ELIXIR_MONGOOSE.itemName, 0, 3)))
        app.updateQuality()

        assertEquals(-1, app.items[0].sellIn)
        assertEquals(1, app.items[0].quality)
    }

    @Test
    fun `elixir mongoose decreases by 2 after sell-in date`() {
        val app = GildedRose(listOf(Item(ELIXIR_MONGOOSE.itemName, -1, 3)))
        app.updateQuality()

        assertEquals(-2, app.items[0].sellIn)
        assertEquals(1, app.items[0].quality)
    }

    @Test
    fun `elixir mongoose decreases by 2 after sell-in date, never less than zero`() {
        val app = GildedRose(listOf(Item(ELIXIR_MONGOOSE.itemName, -1, 1)))
        app.updateQuality()

        assertEquals(-2, app.items[0].sellIn)
        assertEquals(0, app.items[0].quality)
    }

    @Test
    fun `elixir mongoose never less than zero`() {
        val app = GildedRose(listOf(Item(ELIXIR_MONGOOSE.itemName, -2, 0)))
        app.updateQuality()

        assertEquals(-3, app.items[0].sellIn)
        assertEquals(0, app.items[0].quality)
    }
}