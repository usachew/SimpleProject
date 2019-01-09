package com.kapmayn.domain.mappers

import com.kapmayn.coremodels.StubModel
import com.kapmayn.data.models.Stub
import javax.inject.Inject

class StubMapper
@Inject constructor() {

    fun map(item: Stub): StubModel {
        return with(item) {
            StubModel(
                id,
                stubFieldFirst,
                stubFieldSecond
            )
        }
    }
}