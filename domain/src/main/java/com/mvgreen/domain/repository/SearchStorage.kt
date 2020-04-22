package com.mvgreen.domain.repository

import androidx.paging.PagedList
import com.mvgreen.domain.entity.MovieData

interface SearchStorage {

    var savedList: PagedList<MovieData>?

    var savedListPosition: Int

    var savedQuery: String

}