package com.gildedrose.ext

import com.gildedrose.Item


fun Item.copy() =
    Item(name = this.name, sellIn = this.sellIn, quality = this.quality)
