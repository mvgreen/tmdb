package com.mvgreen.data.network.search

import com.mvgreen.data.exception.UnexpectedResponseException
import com.mvgreen.data.network.search.api.SearchApi
import com.mvgreen.data.network.search.entity.Genre
import com.mvgreen.data.network.search.entity.MovieObject
import com.mvgreen.data.network.search.entity.SearchResponse
import com.mvgreen.data.utils.getOrUnexpected
import com.mvgreen.domain.entity.GenreData
import com.mvgreen.domain.entity.MovieContainer
import com.mvgreen.domain.entity.MovieData
import com.mvgreen.domain.repository.GenreStorage
import com.mvgreen.domain.repository.SearchRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi,
    private val genreStorage: GenreStorage
) : SearchRepository {

    override fun loadGenres(): Single<Map<Int, GenreData>> {
        return searchApi
            .getGenreList()
            .map { result ->
                val genreList = result.genres ?: throw UnexpectedResponseException()
                genreList.associate { genre ->
                    val id = getOrUnexpected(genre.id)
                    val name = getOrUnexpected(genre.name)
                    Pair(id, GenreData(id, name))
                }
            }
    }

    override fun search(query: String, page: Int): Single<MovieContainer> {
        return searchApi
            .search(query, page)
            .convertToMovieContainer()
    }

    private fun Single<SearchResponse>.convertToMovieContainer(): Single<MovieContainer> {
        return flatMap { response ->
            // Сохраняем данные о количестве страниц чтобы включить его в итоговый объект
            val pagesTotal = getOrUnexpected(response.totalPages)
            val currentPage = getOrUnexpected(response.page)

            // Разбиваем полученные элементы и обрабатываем их по очереди
            Single.just(getOrUnexpected(response.results))
                .flatMapObservable { list ->
                    list.forEachIndexed { index, item -> item.itemIndex = index }
                    Observable.fromIterable(list)
                }
                // Для каждого айтема посылаем запрос для получения длительности фильма.
                // Дополнительно присваиваем каждому запросу индекс чтобы потом восстановить порядок.
                .flatMapSingle { listItem ->
                    searchApi
                        .getMovieDetails(getOrUnexpected(listItem.id))
                        .onErrorReturnItem(listItem)
                        .map { item ->
                            item.itemIndex = listItem.itemIndex
                            item
                        }
                        // Обеспечивает параллельность отправки запросов
                        .subscribeOn(Schedulers.io())
                }
                // Собираем все запросы, восстанавливаем порядок списка
                .toSortedList { item1, item2 ->
                    val index1 = getOrUnexpected(item1.itemIndex)
                    val index2 = getOrUnexpected(item2.itemIndex)
                    index1 - index2
                }
                // Конвертируем в сущности
                .map { list ->
                    MovieContainer(
                        currentPage,
                        pagesTotal,
                        list
                            .map { item -> toMovieData(item) }
                            .toMutableList()
                    )
                }
        }
    }

    override fun getMovieDetails(id: Int): Single<MovieData> {
        return searchApi
            .getMovieDetails(id)
            .map { response ->
                toMovieData(response)
            }
    }

    private fun toMovieData(response: MovieObject) = MovieData(
        getOrUnexpected(response.id),
        response.posterPath,
        response.title,
        response.originalTitle,
        parseNullableDate(response.releaseDate),
        parseGenres(response.genres),
        response.voteAverage,
        response.voteCount,
        response.runtime,
        response.overview
    )

    private fun parseNullableDate(str: String?): DateTime? {
        return if (str.isNullOrEmpty()) null else DateTime.parse(str)
    }

    private fun parseGenres(list: List<Genre>?): List<GenreData> {
        return list?.mapNotNull { item ->
            if (item.id == null) return@mapNotNull null
            val genre = genreStorage.getGenre(item.id)
            if (genre == null) null else GenreData(item.id, genre)
        } ?: listOf()
    }

}