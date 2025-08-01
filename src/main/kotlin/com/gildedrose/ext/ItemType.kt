package com.gildedrose.ext

enum class ItemType(
    val itemName: String,
) {
    AGED_BRIE("Aged Brie"),
    BACKSTAGE_PASSES("Backstage passes to a TAFKAL80ETC concert"),
    CONJURED_MANA_CAKE("Conjured Mana Cake"),
    ELIXIR_MONGOOSE("Elixir of the Mongoose"),
    PLUS5_DEXTERITY_VEST("+5 Dexterity Vest"),
    SULFURAS_RAGNAROS("Sulfuras, Hand of Ragnaros");

    companion object {
        fun of(itemName: String): ItemType =
            entries.find { it.itemName == itemName } ?: throw IllegalArgumentException("Invalid type: $itemName")
    }
}
