package com.gildedrose

import com.gildedrose.ext.ItemType.AGED_BRIE
import com.gildedrose.ext.ItemType.BACKSTAGE_PASSES
import com.gildedrose.ext.ItemType.CONJURED_MANA_CAKE
import com.gildedrose.ext.ItemType.SULFURAS_RAGNAROS

private const val MAX_QUALITY = 50
private const val SELL_IN_11_DAYS = 11
private const val SELL_IN_6_DAYS = 6

class GildedRose(val items: List<Item>) {

    fun updateQuality() {
        for (item in items) {
            when (item.name) {
                SULFURAS_RAGNAROS.itemName -> continue
                AGED_BRIE.itemName, BACKSTAGE_PASSES.itemName -> {
                    increaseQualityBeforeSellIn(item)
                }
                else -> {
                    if (item.quality > 0) {
                        decreaseQuality(item)
                    }
                }
            }

            item.sellIn = item.sellIn - 1
            if (item.sellIn < 0) {
                when (item.name) {
                    AGED_BRIE.itemName, BACKSTAGE_PASSES.itemName -> {
                        increaseQualityAfterSellIn(item)
                    }
                    else -> {
                        if (item.quality > 0) {
                            decreaseQuality(item)
                        }
                    }
                }
            }
        }
    }

    private fun increaseQualityAfterSellIn(item: Item) {
        when (item.name) {
            AGED_BRIE.itemName -> {
                if (item.quality < MAX_QUALITY) {
                    item.quality += 1
                }
            }

            BACKSTAGE_PASSES.itemName -> {
                item.quality = 0
            }
        }
    }

    private fun increaseQualityBeforeSellIn(item: Item) {
        if (item.quality < MAX_QUALITY) {
            item.quality = item.quality + 1

            if (item.name == BACKSTAGE_PASSES.itemName) {
                if (item.sellIn < SELL_IN_11_DAYS && item.quality < MAX_QUALITY) {
                    item.quality += 1
                }

                if (item.sellIn < SELL_IN_6_DAYS && item.quality < MAX_QUALITY) {
                    item.quality += 1
                }
            }
        }
    }

    private fun decreaseQuality(item: Item) {
        if (item.name == CONJURED_MANA_CAKE.itemName) {
            if (item.quality > 1) {
                item.quality -= 2
            } else {
                item.quality -= 1
            }
        }
        else {
            item.quality -= 1
        }
    }

}

