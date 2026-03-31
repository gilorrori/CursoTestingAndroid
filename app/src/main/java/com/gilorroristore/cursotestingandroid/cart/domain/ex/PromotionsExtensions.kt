package com.gilorroristore.cursotestingandroid.cart.domain.ex

import com.gilorroristore.cursotestingandroid.productlist.domain.models.Promotion
import java.time.Instant

fun List<Promotion>.activeAt(now: Instant): List<Promotion> = this.filter {
    it.startTime <= now && it.endTime >= now
}