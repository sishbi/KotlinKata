package com.gildedrose

import com.gildedrose.ext.ItemType
import com.gildedrose.ext.ItemType.AGED_BRIE
import com.gildedrose.ext.ItemType.BACKSTAGE_PASSES
import com.gildedrose.ext.ItemType.CONJURED_MANA_CAKE
import com.gildedrose.ext.ItemType.ELIXIR_MONGOOSE
import com.gildedrose.ext.ItemType.PLUS5_DEXTERITY_VEST
import com.gildedrose.ext.ItemType.SULFURAS_RAGNAROS
import com.gildedrose.ext.copy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import kotlin.math.abs

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class GildedRoseTextTest {
    private val originalItems = listOf(
        Item(PLUS5_DEXTERITY_VEST.itemName, 10, 20),
        Item(AGED_BRIE.itemName, 2, 0),
        Item(ELIXIR_MONGOOSE.itemName, 5, 7),
        Item(SULFURAS_RAGNAROS.itemName, 0, 80),
        Item(SULFURAS_RAGNAROS.itemName, -1, 80),
        Item(BACKSTAGE_PASSES.itemName, 15, 20),
        Item(BACKSTAGE_PASSES.itemName, 10, 49),
        Item(BACKSTAGE_PASSES.itemName, 5, 49),
        Item(CONJURED_MANA_CAKE.itemName, 3, 6),
    )
    private lateinit var app: GildedRose

    @BeforeEach
    fun beforeEach() {
        app = GildedRose(originalItems.map { it.copy() })
    }

    @Test
    fun `text test`() {
        log("OMGHAI!", start = true)
        for (d in 0..30) {
            log("-------- day $d --------")
            log("name, sellIn, quality")
            log(app.items.joinToString("\n"))
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

    data class TestArg(
        val pos: Int,
        val itemType: ItemType,
        val beforeSellIn: Int,
        val afterSellIn: Int,
        val beforeQuality: Int,
        val afterQuality: Int,
    )

    fun verifyUpdateQualityArgsDay1(): List<TestArg> = listOf(
        // index, name, before sell-in, before quality, after sell-in, after quality
        TestArg(0, PLUS5_DEXTERITY_VEST, 10, 9, 20, 19), // quality decreases by 1 each day
        TestArg(1, AGED_BRIE, 2, 1, 0, 1), // quality increases by 1 each day before sell-in date
        TestArg(2, ELIXIR_MONGOOSE, 5, 4, 7, 6), // quality decreases by 1 each day
        TestArg(3, SULFURAS_RAGNAROS, 0, 0, 80, 80), // never sold or increases in quality (always 80)
        TestArg(4, SULFURAS_RAGNAROS, -1, -1, 80, 80), // never sold or increases in quality (always 80)
        TestArg(5, BACKSTAGE_PASSES, 15, 14, 20, 21), // quality increases by 1 each day >= 10 before concert
        TestArg(6, BACKSTAGE_PASSES, 10, 9, 49, 50), // quality never more than 50
        TestArg(7, BACKSTAGE_PASSES, 5, 4, 49, 50), // quality never more than 50
        TestArg(8, CONJURED_MANA_CAKE, 3, 2, 6, 4), // quality decreases by 2 each day
    )

    @ParameterizedTest
    @MethodSource("verifyUpdateQualityArgsDay1")
    fun `verify updateQuality, day 1`(arg: TestArg) {
        verify(1, arg)
    }

    fun verifyUpdateQualityArgsDay2(): List<TestArg> = listOf(
        // index, name, before sell-in, before quality, after sell-in, after quality
        TestArg(0, PLUS5_DEXTERITY_VEST, 10, 8, 20, 18), // quality decreases by 1 each day
        TestArg(1, AGED_BRIE, 2, 0, 0, 2), // quality increases by 2 each day at sell-in date
        TestArg(2, ELIXIR_MONGOOSE, 5, 3, 7, 5), // quality decreases by 1 each day
        TestArg(3, SULFURAS_RAGNAROS, 0, 0, 80, 80), // never sold or increases in quality (always 80)
        TestArg(4, SULFURAS_RAGNAROS, -1, -1, 80, 80), // never sold or increases in quality (always 80)
        TestArg(5, BACKSTAGE_PASSES, 15, 13, 20, 22), // quality increases by 1 each day >= 10 before concert
        TestArg(6, BACKSTAGE_PASSES, 10, 8, 49, 50), // quality never more than 50
        TestArg(7, BACKSTAGE_PASSES, 5, 3, 49, 50), // quality never more than 50
        TestArg(8, CONJURED_MANA_CAKE, 3, 1, 6, 2), // quality decreases by 2 each day
    )

    @ParameterizedTest
    @MethodSource("verifyUpdateQualityArgsDay2")
    fun `verify updateQuality, day 2`(arg: TestArg) {
        verify(2, arg)
    }

    fun verifyUpdateQualityArgsDay3(): List<TestArg> = listOf(
        // index, name, before sell-in, before quality, after sell-in, after quality
        TestArg(0, PLUS5_DEXTERITY_VEST, 10, 7, 20, 17), // quality decreases by 1 each day
        TestArg(1, AGED_BRIE, 2, -1, 0, 4), // increases in quality by 2 after sell-in date past
        TestArg(2, ELIXIR_MONGOOSE, 5, 2, 7, 4), // quality decreases by 1 each day
        TestArg(3, SULFURAS_RAGNAROS, 0, 0, 80, 80), // never sold or increases in quality (always 80)
        TestArg(4, SULFURAS_RAGNAROS, -1, -1, 80, 80), // never sold or increases in quality (always 80)
        TestArg(5, BACKSTAGE_PASSES, 15, 12, 20, 23), // quality increases by 1 each day >= 10 before concert
        TestArg(6, BACKSTAGE_PASSES, 10, 7, 49, 50), // quality never more than 50
        TestArg(7, BACKSTAGE_PASSES, 5, 2, 49, 50), // quality never more than 50
        TestArg(8, CONJURED_MANA_CAKE, 3, 0, 6, 0), // quality decreases by 2 each day
    )

    @ParameterizedTest
    @MethodSource("verifyUpdateQualityArgsDay3")
    fun `verify updateQuality, day 3`(arg: TestArg) {
        verify(3, arg)
    }

    fun verifyUpdateQualityArgsDay4(): List<TestArg> = listOf(
        // index, name, before sell-in, before quality, after sell-in, after quality
        TestArg(0, PLUS5_DEXTERITY_VEST, 10, 6, 20, 16), // quality decreases by 1 each day
        TestArg(1, AGED_BRIE, 2, -2, 0, 6), // increases in quality by 2 after sell-in date past
        TestArg(2, ELIXIR_MONGOOSE, 5, 1, 7, 3), // quality decreases by 1 each day
        TestArg(3, SULFURAS_RAGNAROS, 0, 0, 80, 80), // never sold or increases in quality (always 80)
        TestArg(4, SULFURAS_RAGNAROS, -1, -1, 80, 80), // never sold or increases in quality (always 80)
        TestArg(5, BACKSTAGE_PASSES, 15, 11, 20, 24), // quality increases by 1 each day >= 10 before concert
        TestArg(6, BACKSTAGE_PASSES, 10, 6, 49, 50), // quality never more than 50
        TestArg(7, BACKSTAGE_PASSES, 5, 1, 49, 50), // quality never more than 50
        TestArg(8, CONJURED_MANA_CAKE, 3, -1, 6, 0), // quality never below zero
    )

    @ParameterizedTest
    @MethodSource("verifyUpdateQualityArgsDay4")
    fun `verify updateQuality, day 4`(arg: TestArg) {
        verify(4, arg)
    }

    fun verifyUpdateQualityArgsDay5(): List<TestArg> = listOf(
        // index, name, before sell-in, before quality, after sell-in, after quality
        TestArg(0, PLUS5_DEXTERITY_VEST, 10, 5, 20, 15), // quality decreases by 1 each day
        TestArg(1, AGED_BRIE, 2, -3, 0, 8), // increases in quality by 2 after sell-in date past
        TestArg(2, ELIXIR_MONGOOSE, 5, 0, 7, 2), // quality decreases by 1 each day
        TestArg(3, SULFURAS_RAGNAROS, 0, 0, 80, 80), // never sold or increases in quality (always 80)
        TestArg(4, SULFURAS_RAGNAROS, -1, -1, 80, 80), // never sold or increases in quality (always 80)
        TestArg(5, BACKSTAGE_PASSES, 15, 10, 20, 25), // quality increases by 2 each day <= 10 before concert
        TestArg(6, BACKSTAGE_PASSES, 10, 5, 49, 50), // quality never more than 50
        TestArg(7, BACKSTAGE_PASSES, 5, 0, 49, 50), // quality never more than 50
        TestArg(8, CONJURED_MANA_CAKE, 3, -2, 6, 0), // quality never below zero
    )

    @ParameterizedTest
    @MethodSource("verifyUpdateQualityArgsDay5")
    fun `verify updateQuality, day 5`(arg: TestArg) {
        verify(5, arg)
    }

    fun verifyUpdateQualityArgsDay6(): List<TestArg> = listOf(
        // index, name, before sell-in, before quality, after sell-in, after quality
        TestArg(0, PLUS5_DEXTERITY_VEST, 10, 4, 20, 14), // quality decreases by 1 each day
        TestArg(1, AGED_BRIE, 2, -4, 0, 10), // increases in quality by 2 after sell-in date passed
        TestArg(2, ELIXIR_MONGOOSE, 5, -1, 7, 0), // quality drops to zero after sell-in date passed
        TestArg(3, SULFURAS_RAGNAROS, 0, 0, 80, 80), // never sold or increases in quality (always 80)
        TestArg(4, SULFURAS_RAGNAROS, -1, -1, 80, 80), // never sold or increases in quality (always 80)
        TestArg(5, BACKSTAGE_PASSES, 15, 9, 20, 27), // quality increases by 2 each day <= 10 before concert
        TestArg(6, BACKSTAGE_PASSES, 10, 4, 49, 50), // quality never more than 50
        TestArg(7, BACKSTAGE_PASSES, 5, -1, 49, 0), // quality drops to zero after the concert
        TestArg(8, CONJURED_MANA_CAKE, 3, -3, 6, 0), // quality never below zero
    )

    @ParameterizedTest
    @MethodSource("verifyUpdateQualityArgsDay6")
    fun `verify updateQuality, day 6`(arg: TestArg) {
        verify(6, arg)
    }

    fun verifyUpdateQualityArgsDay7(): List<TestArg> = listOf(
        // index, name, before sell-in, before quality, after sell-in, after quality
        TestArg(0, PLUS5_DEXTERITY_VEST, 10, 3, 20, 13), // quality decreases by 1 each day
        TestArg(1, AGED_BRIE, 2, -5, 0, 12), // increases in quality by 2 after sell-in date past
        TestArg(2, ELIXIR_MONGOOSE, 5, -2, 7, 0), // quality drops to zero after sell-in date passed
        TestArg(3, SULFURAS_RAGNAROS, 0, 0, 80, 80), // never sold or increases in quality (always 80)
        TestArg(4, SULFURAS_RAGNAROS, -1, -1, 80, 80), // never sold or increases in quality (always 80)
        TestArg(5, BACKSTAGE_PASSES, 15, 8, 20, 29), // quality increases by 2 each day <= 10 before concert
        TestArg(6, BACKSTAGE_PASSES, 10, 3, 49, 50), // quality never more than 50
        TestArg(7, BACKSTAGE_PASSES, 5, -2, 49, 0), // quality drops to zero after the concert
        TestArg(8, CONJURED_MANA_CAKE, 3, -4, 6, 0), // quality never below zero
    )

    @ParameterizedTest
    @MethodSource("verifyUpdateQualityArgsDay7")
    fun `verify updateQuality, day 7`(arg: TestArg) {
        verify(7, arg)
    }

    fun verifyUpdateQualityArgsDay8(): List<TestArg> = listOf(
        // index, name, before sell-in, before quality, after sell-in, after quality
        TestArg(0, PLUS5_DEXTERITY_VEST, 10, 2, 20, 12), // quality decreases by 1 each day
        TestArg(1, AGED_BRIE, 2, -6, 0, 14), // increases in quality by 2 after sell-in date past
        TestArg(2, ELIXIR_MONGOOSE, 5, -3, 7, 0), // quality drops to zero after sell-in date passed
        TestArg(3, SULFURAS_RAGNAROS, 0, 0, 80, 80), // never sold or increases in quality (always 80)
        TestArg(4, SULFURAS_RAGNAROS, -1, -1, 80, 80), // never sold or increases in quality (always 80)
        TestArg(5, BACKSTAGE_PASSES, 15, 7, 20, 31), // quality increases by 2 each day <= 10 before concert
        TestArg(6, BACKSTAGE_PASSES, 10, 2, 49, 50), // quality never more than 50
        TestArg(7, BACKSTAGE_PASSES, 5, -3, 49, 0), // quality drops to zero after the concert
        TestArg(8, CONJURED_MANA_CAKE, 3, -5, 6, 0), // quality never below zero
    )

    @ParameterizedTest
    @MethodSource("verifyUpdateQualityArgsDay8")
    fun `verify updateQuality, day 8`(arg: TestArg) {
        verify(8, arg)
    }

    fun verifyUpdateQualityArgsDay9(): List<TestArg> = listOf(
        // index, name, before sell-in, before quality, after sell-in, after quality
        TestArg(0, PLUS5_DEXTERITY_VEST, 10, 1, 20, 11), // quality decreases by 1 each day
        TestArg(1, AGED_BRIE, 2, -7, 0, 16), // increases in quality by 2 after sell-in date past
        TestArg(2, ELIXIR_MONGOOSE, 5, -4, 7, 0), // quality drops to zero after sell-in date passed
        TestArg(3, SULFURAS_RAGNAROS, 0, 0, 80, 80), // never sold or increases in quality (always 80)
        TestArg(4, SULFURAS_RAGNAROS, -1, -1, 80, 80), // never sold or increases in quality (always 80)
        TestArg(5, BACKSTAGE_PASSES, 15, 6, 20, 33), // quality increases by 2 each day <= 10 before concert
        TestArg(6, BACKSTAGE_PASSES, 10, 1, 49, 50), // quality never more than 50
        TestArg(7, BACKSTAGE_PASSES, 5, -4, 49, 0), // quality drops to zero after the concert
        TestArg(8, CONJURED_MANA_CAKE, 3, -6, 6, 0), // quality never below zero
    )

    @ParameterizedTest
    @MethodSource("verifyUpdateQualityArgsDay9")
    fun `verify updateQuality, day 9`(arg: TestArg) {
        verify(9, arg)
    }

    fun verifyUpdateQualityArgsDay10(): List<TestArg> = listOf(
        // index, name, before sell-in, before quality, after sell-in, after quality
        TestArg(0, PLUS5_DEXTERITY_VEST, 10, 0, 20, 10), // quality decreases by 1 each day
        TestArg(1, AGED_BRIE, 2, -8, 0, 18), // increases in quality by 2 after sell-in date past
        TestArg(2, ELIXIR_MONGOOSE, 5, -5, 7, 0), // quality drops to zero after sell-in date passed
        TestArg(3, SULFURAS_RAGNAROS, 0, 0, 80, 80), // never sold or increases in quality (always 80)
        TestArg(4, SULFURAS_RAGNAROS, -1, -1, 80, 80), // never sold or increases in quality (always 80)
        TestArg(5, BACKSTAGE_PASSES, 15, 5, 20, 35), // quality increases by 2 each day <= 10 before concert
        TestArg(6, BACKSTAGE_PASSES, 10, 0, 49, 50), // quality never more than 50
        TestArg(7, BACKSTAGE_PASSES, 5, -5, 49, 0), // quality drops to zero after the concert
        TestArg(8, CONJURED_MANA_CAKE, 3, -7, 6, 0), // quality never below zero
    )

    @ParameterizedTest
    @MethodSource("verifyUpdateQualityArgsDay10")
    fun `verify updateQuality, day 10`(arg: TestArg) {
        verify(10, arg)
    }

    fun verifyUpdateQualityArgsDay11(): List<TestArg> = listOf(
        // index, name, before sell-in, before quality, after sell-in, after quality
        TestArg(0, PLUS5_DEXTERITY_VEST, 10, -1, 20, 8), // quality decreases by 2 each day after sell-in date
        TestArg(1, AGED_BRIE, 2, -9, 0, 20), // increases in quality by 2 after sell-in date past
        TestArg(2, ELIXIR_MONGOOSE, 5, -6, 7, 0), // quality drops to zero after sell-in date passed
        TestArg(3, SULFURAS_RAGNAROS, 0, 0, 80, 80), // never sold or increases in quality (always 80)
        TestArg(4, SULFURAS_RAGNAROS, -1, -1, 80, 80), // never sold or increases in quality (always 80)
        TestArg(5, BACKSTAGE_PASSES, 15, 4, 20, 38), // quality increases by 3 each day <= 5 before concert
        TestArg(6, BACKSTAGE_PASSES, 10, -1, 49, 0), // quality drops to zero after the concert
        TestArg(7, BACKSTAGE_PASSES, 5, -6, 49, 0), // quality drops to zero after the concert
        TestArg(8, CONJURED_MANA_CAKE, 3, -8, 6, 0), // quality never below zero
    )

    @ParameterizedTest
    @MethodSource("verifyUpdateQualityArgsDay11")
    fun `verify updateQuality, day 11`(arg: TestArg) {
        verify(11, arg)
    }

    fun verifyUpdateQualityArgsDay12(): List<TestArg> = listOf(
        // index, name, before sell-in, before quality, after sell-in, after quality
        TestArg(0, PLUS5_DEXTERITY_VEST, 10, -2, 20, 6), // quality decreases by 2 each day after sell-in date
        TestArg(1, AGED_BRIE, 2, -10, 0, 22), // increases in quality by 2 after sell-in date past
        TestArg(2, ELIXIR_MONGOOSE, 5, -7, 7, 0), // quality drops to zero after sell-in date passed
        TestArg(3, SULFURAS_RAGNAROS, 0, 0, 80, 80), // never sold or increases in quality (always 80)
        TestArg(4, SULFURAS_RAGNAROS, -1, -1, 80, 80), // never sold or increases in quality (always 80)
        TestArg(5, BACKSTAGE_PASSES, 15, 3, 20, 41), // quality increases by 3 each day <= 5 before concert
        TestArg(6, BACKSTAGE_PASSES, 10, -2, 49, 0), // quality drops to zero after the concert
        TestArg(7, BACKSTAGE_PASSES, 5, -7, 49, 0), // quality drops to zero after the concert
        TestArg(8, CONJURED_MANA_CAKE, 3, -9, 6, 0), // quality never below zero
    )

    @ParameterizedTest
    @MethodSource("verifyUpdateQualityArgsDay12")
    fun `verify updateQuality, day 12`(arg: TestArg) {
        verify(12, arg)
    }

    fun verifyUpdateQualityArgsDay13(): List<TestArg> = listOf(
        // index, name, before sell-in, before quality, after sell-in, after quality
        TestArg(0, PLUS5_DEXTERITY_VEST, 10, -3, 20, 4), // quality decreases by 2 each day after sell-in date
        TestArg(1, AGED_BRIE, 2, -11, 0, 24), // increases in quality by 2 after sell-in date past
        TestArg(2, ELIXIR_MONGOOSE, 5, -8, 7, 0), // quality drops to zero after sell-in date passed
        TestArg(3, SULFURAS_RAGNAROS, 0, 0, 80, 80), // never sold or increases in quality (always 80)
        TestArg(4, SULFURAS_RAGNAROS, -1, -1, 80, 80), // never sold or increases in quality (always 80)
        TestArg(5, BACKSTAGE_PASSES, 15, 2, 20, 44), // quality increases by 3 each day <= 5 before concert
        TestArg(6, BACKSTAGE_PASSES, 10, -3, 49, 0), // quality drops to zero after the concert
        TestArg(7, BACKSTAGE_PASSES, 5, -8, 49, 0), // quality drops to zero after the concert
        TestArg(8, CONJURED_MANA_CAKE, 3, -10, 6, 0), // quality never below zero
    )

    @ParameterizedTest
    @MethodSource("verifyUpdateQualityArgsDay13")
    fun `verify updateQuality, day 13`(arg: TestArg) {
        verify(13, arg)
    }

    fun verifyUpdateQualityArgsDay14(): List<TestArg> = listOf(
        // index, name, before sell-in, before quality, after sell-in, after quality
        TestArg(0, PLUS5_DEXTERITY_VEST, 10, -4, 20, 2), // quality decreases by 2 each day after sell-in date
        TestArg(1, AGED_BRIE, 2, -12, 0, 26), // increases in quality by 2 after sell-in date past
        TestArg(2, ELIXIR_MONGOOSE, 5, -9, 7, 0), // quality drops to zero after sell-in date passed
        TestArg(3, SULFURAS_RAGNAROS, 0, 0, 80, 80), // never sold or increases in quality (always 80)
        TestArg(4, SULFURAS_RAGNAROS, -1, -1, 80, 80), // never sold or increases in quality (always 80)
        TestArg(5, BACKSTAGE_PASSES, 15, 1, 20, 47), // quality increases by 3 each day <= 5 before concert
        TestArg(6, BACKSTAGE_PASSES, 10, -4, 49, 0), // quality drops to zero after the concert
        TestArg(7, BACKSTAGE_PASSES, 5, -9, 49, 0), // quality drops to zero after the concert
        TestArg(8, CONJURED_MANA_CAKE, 3, -11, 6, 0), // quality never below zero
    )

    @ParameterizedTest
    @MethodSource("verifyUpdateQualityArgsDay14")
    fun `verify updateQuality, day 14`(arg: TestArg) {
        verify(14, arg)
    }

    fun verifyUpdateQualityArgsDay15(): List<TestArg> = listOf(
        // index, name, before sell-in, before quality, after sell-in, after quality
        TestArg(0, PLUS5_DEXTERITY_VEST, 10, -5, 20, 0), // quality decreases by 2 each day after sell-in date
        TestArg(1, AGED_BRIE, 2, -13, 0, 28), // increases in quality by 2 after sell-in date past
        TestArg(2, ELIXIR_MONGOOSE, 5, -10, 7, 0), // quality drops to zero after sell-in date passed
        TestArg(3, SULFURAS_RAGNAROS, 0, 0, 80, 80), // never sold or increases in quality (always 80)
        TestArg(4, SULFURAS_RAGNAROS, -1, -1, 80, 80), // never sold or increases in quality (always 80)
        TestArg(5, BACKSTAGE_PASSES, 15, 0, 20, 50), // quality increases by 3 each day <= 5 before concert
        TestArg(6, BACKSTAGE_PASSES, 10, -5, 49, 0), // quality drops to zero after the concert
        TestArg(7, BACKSTAGE_PASSES, 5, -10, 49, 0), // quality drops to zero after the concert
        TestArg(8, CONJURED_MANA_CAKE, 3, -12, 6, 0), // quality never below zero
    )

    @ParameterizedTest
    @MethodSource("verifyUpdateQualityArgsDay15")
    fun `verify updateQuality, day 15`(arg: TestArg) {
        verify(15, arg)
    }

    fun verifyUpdateQualityArgsDay16(): List<TestArg> = listOf(
        // index, name, before sell-in, before quality, after sell-in, after quality
        TestArg(0, PLUS5_DEXTERITY_VEST, 10, -6, 20, 0), // quality never below zero
        TestArg(1, AGED_BRIE, 2, -14, 0, 30), // increases in quality by 2 after sell-in date past
        TestArg(2, ELIXIR_MONGOOSE, 5, -11, 7, 0), // quality drops to zero after sell-in date passed
        TestArg(3, SULFURAS_RAGNAROS, 0, 0, 80, 80), // never sold or increases in quality (always 80)
        TestArg(4, SULFURAS_RAGNAROS, -1, -1, 80, 80), // never sold or increases in quality (always 80)
        TestArg(5, BACKSTAGE_PASSES, 15, -1, 20, 0), // quality drops to zero after the concert
        TestArg(6, BACKSTAGE_PASSES, 10, -6, 49, 0), // quality drops to zero after the concert
        TestArg(7, BACKSTAGE_PASSES, 5, -11, 49, 0), // quality drops to zero after the concert
        TestArg(8, CONJURED_MANA_CAKE, 3, -13, 6, 0), // quality never below zero
    )

    @ParameterizedTest
    @MethodSource("verifyUpdateQualityArgsDay16")
    fun `verify updateQuality, day 16`(arg: TestArg) {
        verify(16, arg)
    }

    fun verifyUpdateQualityArgsDay17(): List<TestArg> = listOf(
        // index, name, before sell-in, before quality, after sell-in, after quality
        TestArg(0, PLUS5_DEXTERITY_VEST, 10, -7, 20, 0), // quality never below zero
        TestArg(1, AGED_BRIE, 2, -15, 0, 32), // increases in quality by 2 after sell-in date past
        TestArg(2, ELIXIR_MONGOOSE, 5, -12, 7, 0), // quality drops to zero after sell-in date passed
        TestArg(3, SULFURAS_RAGNAROS, 0, 0, 80, 80), // never sold or increases in quality (always 80)
        TestArg(4, SULFURAS_RAGNAROS, -1, -1, 80, 80), // never sold or increases in quality (always 80)
        TestArg(5, BACKSTAGE_PASSES, 15, -2, 20, 0), // quality drops to zero after the concert
        TestArg(6, BACKSTAGE_PASSES, 10, -7, 49, 0), // quality drops to zero after the concert
        TestArg(7, BACKSTAGE_PASSES, 5, -12, 49, 0), // quality drops to zero after the concert
        TestArg(8, CONJURED_MANA_CAKE, 3, -14, 6, 0), // quality never below zero
    )

    @ParameterizedTest
    @MethodSource("verifyUpdateQualityArgsDay17")
    fun `verify updateQuality, day 17`(arg: TestArg) {
        verify(17, arg)
    }

    fun verifyUpdateQualityArgsDay18(): List<TestArg> = listOf(
        // index, name, before sell-in, before quality, after sell-in, after quality
        TestArg(0, PLUS5_DEXTERITY_VEST, 10, -8, 20, 0), // quality never below zero
        TestArg(1, AGED_BRIE, 2, -16, 0, 34), // increases in quality by 2 after sell-in date past
        TestArg(2, ELIXIR_MONGOOSE, 5, -13, 7, 0), // quality drops to zero after sell-in date passed
        TestArg(3, SULFURAS_RAGNAROS, 0, 0, 80, 80), // never sold or increases in quality (always 80)
        TestArg(4, SULFURAS_RAGNAROS, -1, -1, 80, 80), // never sold or increases in quality (always 80)
        TestArg(5, BACKSTAGE_PASSES, 15, -3, 20, 0), // quality drops to zero after the concert
        TestArg(6, BACKSTAGE_PASSES, 10, -8, 49, 0), // quality drops to zero after the concert
        TestArg(7, BACKSTAGE_PASSES, 5, -13, 49, 0), // quality drops to zero after the concert
        TestArg(8, CONJURED_MANA_CAKE, 3, -15, 6, 0), // quality never below zero
    )

    @ParameterizedTest
    @MethodSource("verifyUpdateQualityArgsDay18")
    fun `verify updateQuality, day 18`(arg: TestArg) {
        verify(18, arg)
    }

    fun verifyUpdateQualityArgsDay28(): List<TestArg> = listOf(
        // index, name, before sell-in, before quality, after sell-in, after quality
        TestArg(0, PLUS5_DEXTERITY_VEST, 10, -18, 20, 0), // quality never below zero
        TestArg(1, AGED_BRIE, 2, -26, 0, 50), // quality never more than 50
        TestArg(2, ELIXIR_MONGOOSE, 5, -23, 7, 0), // quality drops to zero after sell-in date passed
        TestArg(3, SULFURAS_RAGNAROS, 0, 0, 80, 80), // never sold or increases in quality (always 80)
        TestArg(4, SULFURAS_RAGNAROS, -1, -1, 80, 80), // never sold or increases in quality (always 80)
        TestArg(5, BACKSTAGE_PASSES, 15, -13, 20, 0), // quality drops to zero after the concert
        TestArg(6, BACKSTAGE_PASSES, 10, -18, 49, 0), // quality drops to zero after the concert
        TestArg(7, BACKSTAGE_PASSES, 5, -23, 49, 0), // quality drops to zero after the concert
        TestArg(8, CONJURED_MANA_CAKE, 3, -25, 6, 0), // quality never below zero
    )

    @ParameterizedTest
    @MethodSource("verifyUpdateQualityArgsDay28")
    fun `verify updateQuality, day 28`(arg: TestArg) {
        verify(28, arg)
    }

    fun verifyUpdateQualityArgsDay30(): List<TestArg> = listOf(
        // index, name, before sell-in, before quality, after sell-in, after quality
        TestArg(0, PLUS5_DEXTERITY_VEST, 10, -20, 20, 0), // quality never below zero
        TestArg(1, AGED_BRIE, 2, -28, 0, 50), // quality never more than 50
        TestArg(2, ELIXIR_MONGOOSE, 5, -25, 7, 0), // quality drops to zero after sell-in date passed
        TestArg(3, SULFURAS_RAGNAROS, 0, 0, 80, 80), // never sold or increases in quality (always 80)
        TestArg(4, SULFURAS_RAGNAROS, -1, -1, 80, 80), // never sold or increases in quality (always 80)
        TestArg(5, BACKSTAGE_PASSES, 15, -15, 20, 0), // quality drops to zero after the concert
        TestArg(6, BACKSTAGE_PASSES, 10, -20, 49, 0), // quality drops to zero after the concert
        TestArg(7, BACKSTAGE_PASSES, 5, -25, 49, 0), // quality drops to zero after the concert
        TestArg(8, CONJURED_MANA_CAKE, 3, -27, 6, 0), // quality never below zero
    )

    @ParameterizedTest
    @MethodSource("verifyUpdateQualityArgsDay30")
    fun `verify updateQuality, day 30`(arg: TestArg) {
        verify(30, arg)
    }

    private fun verify(dayNum: Int, arg: TestArg) {
        val itemBefore = app.items[arg.pos].copy()
        println("Day: $dayNum, Before ${arg.pos}: name=${itemBefore.name}, sell-in=${itemBefore.sellIn}, quality=${itemBefore.quality}")
        assertEquals(arg.itemType.itemName, itemBefore.name, "before name")
        assertEquals(arg.beforeSellIn, itemBefore.sellIn, "before sell-in")
        assertEquals(arg.beforeQuality, itemBefore.quality, "before quality")

        repeat(dayNum) {
            app.updateQuality()
        }

        val itemAfter = app.items[arg.pos]
        val diff = abs(itemBefore.quality - itemAfter.quality)
        println("Day: $dayNum, After ${arg.pos}: name=${itemAfter.name}, sell-in=${itemAfter.sellIn}, quality=${itemAfter.quality} ($diff)")
        assertEquals(arg.itemType.itemName, itemAfter.name, "after name")
        assertEquals(arg.afterSellIn, itemAfter.sellIn, "after sell-in")
        assertEquals(arg.afterQuality, itemAfter.quality, "after quality")
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
