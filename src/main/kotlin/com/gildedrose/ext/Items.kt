package com.gildedrose.ext

import com.gildedrose.Item

const val SELL_BY_DATE = 0
const val MIN_QUALITY = 0
const val MAX_QUALITY = 50
const val SELL_IN_10_DAYS = 10
const val SELL_IN_5_DAYS = 5

abstract class ItemUpdate(val item: Item) {
    abstract fun updateQuality()
}

typealias ItemList = List<ItemUpdate>

abstract class DecreasingItem(item: Item): ItemUpdate(item) {
    override fun updateQuality() {
        item.sellIn -= 1
        if (item.sellIn >= SELL_BY_DATE) {
            item.quality -= 1
        } else {
            item.quality -= 2
        }
        if (item.quality < MIN_QUALITY) {
            item.quality = MIN_QUALITY
        }
    }
}

class Plus5DexterityVestItem(item: Item): DecreasingItem(item) {
    companion object {
        val type = ItemType.PLUS5_DEXTERITY_VEST
    }
}
class ElixirMongooseItem(item: Item): DecreasingItem(item) {
    companion object {
        val type = ItemType.ELIXIR_MONGOOSE
    }
}

class SulfurasRagnarosItem(item: Item): ItemUpdate(item) {
    companion object {
        val type = ItemType.SULFURAS_RAGNAROS
    }
    override fun updateQuality() = Unit
}

class ConjuredManaCakeItem(item: Item): ItemUpdate(item) {
    companion object {
        val type = ItemType.CONJURED_MANA_CAKE
    }
    override fun updateQuality() {
        item.sellIn -= 1
        if (item.sellIn > SELL_BY_DATE) {
            item.quality -= 2
        } else {
            item.quality -= 4
        }
        if (item.quality < MIN_QUALITY) {
            item.quality = MIN_QUALITY
        }
    }
}

abstract class IncreasingItem(item: Item): ItemUpdate(item) {
    override fun updateQuality() {
        item.sellIn -= 1
        if (item.sellIn >= SELL_BY_DATE) {
            item.quality += 1
        } else {
            item.quality += 2
        }
        if (item.quality > MAX_QUALITY) {
            item.quality = MAX_QUALITY
        }
    }
}

class AgedBrieItem(item: Item): IncreasingItem(item) {
    companion object {
        val type = ItemType.AGED_BRIE
    }
}

class BackstagePassesItem(item: Item): IncreasingItem(item) {
    companion object {
        val type = ItemType.BACKSTAGE_PASSES
    }
    override fun updateQuality() {
        super.updateQuality()
        if (item.sellIn >= SELL_BY_DATE) {
            if (item.sellIn < SELL_IN_10_DAYS) {
                item.quality += 1
            }
            if (item.sellIn < SELL_IN_5_DAYS) {
                item.quality += 1
            }
            if (item.quality > MAX_QUALITY) {
                item.quality = MAX_QUALITY
            }
        } else {
            item.quality = MIN_QUALITY
        }
    }
}
