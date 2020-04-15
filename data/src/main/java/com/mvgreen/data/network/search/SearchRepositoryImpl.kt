package com.mvgreen.data.network.search

import com.mvgreen.data.exception.UnexpectedResponseException
import com.mvgreen.data.network.search.api.SearchApi
import com.mvgreen.data.network.search.entity.MovieObject
import com.mvgreen.data.utils.getOrUnexpected
import com.mvgreen.domain.entity.GenreData
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.repository.GenresStorage
import com.mvgreen.domain.repository.SearchRepository
import io.reactivex.Observable
import io.reactivex.Single
import org.joda.time.DateTime
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi,
    private val genresStorage: GenresStorage
) : SearchRepository {

    override fun loadGenres(): Single<Map<Int, GenreData>> {
        return searchApi
            .getGenreList()
            .map { result ->
                val genreList = result.genres ?: throw UnexpectedResponseException()
                genreList.associateBy { genre ->
                    getOrUnexpected(genre.name)
                    getOrUnexpected(genre.id)
                }
                return@map
            }
    }

    override fun search(query: String, page: Int): Single<List<MovieData>> {
        return searchApi
            .search(query, page)
            // Извлекаем список
            .map { response ->
                getOrUnexpected(response.results)
            }
            // Разбиваем полученные элементы и обрабатываем их по очереди
            .flatMapObservable { list ->
                list.forEachIndexed { index, item -> item.itemIndex = index }
                Observable.fromIterable(list)
            }
            // Для каждого айтема посылаем запрос для получения длительности фильма.
            // Дополнительно присваиваем каждому запросу индекс чтобы потом восстановить порядок.
            .flatMapSingle { listItem ->
                // TODO проверить тред в котором происходит ожидание запроса
                searchApi
                    .getMovieDetails(getOrUnexpected(listItem.id))
                    .map { item ->
                        item.itemIndex = listItem.itemIndex
                        item
                    }
            }
            // Собираем все запросы, восстанавливаем порядок списка
            .toSortedList { item1, item2 ->
                val index1 = getOrUnexpected(item1.itemIndex)
                val index2 = getOrUnexpected(item2.itemIndex)
                index1 - index2
            }
            // Конвертируем в сущности
            .map { list ->
                list.map { item ->
                    toMovieData(item)
                }
            }
    }

    private fun toMovieData(response: MovieObject) = MovieData(
        getOrUnexpected(response.id),
        response.posterPath,
        getOrUnexpected(response.title),
        getOrUnexpected(response.originalTitle),
        parseNullableDate(response.releaseDate),
        parseGenres(response.genreIds),
        response.voteAverage,
        response.voteCount,
        getOrUnexpected(response.runtime)
    )

    private fun parseNullableDate(str: String?): DateTime? {
        return if (str == null) null else DateTime.parse(str)
    }

    private fun parseGenres(list: List<Int>?): List<GenreData> {
        return list?.map { GenreData(it, genresStorage.getGenre(it)) } ?: listOf()
    }

}