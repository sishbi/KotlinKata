package com.gildedrose

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

@Suppress("SpellCheckingInspection")
enum class ItemType(
    val itemName: String,
) {
    PLUS5_DEXTERITY_VEST("+5 Dexterity Vest"),
    AGED_BRIE("Aged Brie"),
    SULFURAS_RAGNAROS("Sulfuras, Hand of Ragnaros"),
    ELIXIR_MONGOOSE("Elixir of the Mongoose"),
    BACKSTAGE_PASSES("Backstage passes to a TAFKAL80ETC concert"),
    CONJURED_MANA_CAKE("Conjured Mana Cake")
}

internal class GildedRoseTextTest {
    private val items = listOf(
        Item(ItemType.PLUS5_DEXTERITY_VEST.itemName, 10, 20),
        Item(ItemType.AGED_BRIE.itemName, 2, 0),
        Item(ItemType.ELIXIR_MONGOOSE.itemName, 5, 7),
        Item(ItemType.SULFURAS_RAGNAROS.itemName, 0, 80),
        Item(ItemType.SULFURAS_RAGNAROS.itemName, -1, 80),
        Item(ItemType.BACKSTAGE_PASSES.itemName, 15, 20),
        Item(ItemType.BACKSTAGE_PASSES.itemName, 10, 49),
        Item(ItemType.BACKSTAGE_PASSES.itemName, 5, 49),
        Item(ItemType.CONJURED_MANA_CAKE.itemName, 3, 6),
    )
    private val app = GildedRose(items)

    @Test
    fun `text test`() {
        log("OMGHAI!", start = true)
        for (d in 0..30) {
            log("-------- day $d --------")
            log("name, sellIn, quality")
            log(items.joinToString("\n"))
            log("")
            app.updateQuality()
        }

        val expectedFile = File("src/test/resources/expected_output.txt")
        println("Checking: ${expectedFile.absolutePath}")
        val expectedText = expectedFile.readText()
        assertEquals("OMGHAI!", expectedText.take(7)) // verify the expected text

        val actualText = outputFile.readText()
        assertEquals(expectedText, actualText)
        println(actualText) // print the actual output if the test passes, will throw exception if not
    }

    private val outputFile = File("build/output.txt")
    private fun log(msg: String, start: Boolean = false) {
        if (start) {
            outputFile.writeText("$msg\n")
        } else {
            outputFile.appendText("$msg\n")
        }
    }
}
