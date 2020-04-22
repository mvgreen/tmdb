package com.mvgreen.domain.bean

interface ListMode {

    companion object {
        const val LIST_MODE_LINEAR = 0
        const val LIST_MODE_GRID = 1
    }

    var modeId: Int

    val listPosition: Int

}