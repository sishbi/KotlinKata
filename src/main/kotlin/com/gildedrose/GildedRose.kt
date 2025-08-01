package com.gildedrose

import com.gildedrose.ext.AgedBrieItem
import com.gildedrose.ext.BackstagePassesItem
import com.gildedrose.ext.ConjuredManaCakeItem
import com.gildedrose.ext.ElixirMongooseItem
import com.gildedrose.ext.ItemList
import com.gildedrose.ext.ItemType
import com.gildedrose.ext.Plus5DexterityVestItem
import com.gildedrose.ext.SulfurasRagnarosItem


class GildedRose(val items: List<Item>) {
    val itemList: ItemList = items.map {
        when (ItemType.of(it.name)) {
            AgedBrieItem.type -> AgedBrieItem(it)
            BackstagePassesItem.type -> BackstagePassesItem(it)
            ConjuredManaCakeItem.type -> ConjuredManaCakeItem(it)
            ElixirMongooseItem.type -> ElixirMongooseItem(it)
            Plus5DexterityVestItem.type -> Plus5DexterityVestItem(it)
            SulfurasRagnarosItem.type -> SulfurasRagnarosItem(it)
            else -> throw IllegalArgumentException("Invalid item: $it")
        }
    }

    fun updateQuality() {
        for (item in itemList) {
            item.updateQuality()
        }
    }
}
