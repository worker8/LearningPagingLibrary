package beepbeep.learningpaginglibrary

import android.arch.paging.PageKeyedDataSource
import beepbeep.pixelsforreddit.Animal

class AnimalDataSource : PageKeyedDataSource<Int, Animal>() {
    val firstPage = 0
    val animalList = listOf(
            Animal("dog-1"),
            Animal("dog-2"),
            Animal("dog-3"),
            Animal("dog-4"),
            Animal("dog-5"),
            Animal("dog-6"),
            Animal("dog-7"),
            Animal("dog-8"),
            Animal("dog-9"),
            Animal("dog-10"),
            Animal("dog-11"),
            Animal("dog-12"),
            Animal("dog-13"),
            Animal("dog-14"),
            Animal("dog-15"),
            Animal("dog-16"),
            Animal("dog-17"),
            Animal("dog-18"),
            Animal("dog-19"),
            Animal("dog-20"),
            Animal("dog-21"),
            Animal("dog-22"),
            Animal("dog-23"))

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Animal>) {
        val initialData = animalList.subList(0, params.requestedLoadSize)
        val pageBefore = firstPage - 1
        val pageAfter = firstPage + 1
        callback.onResult(initialData, pageBefore, pageAfter)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Animal>) {
        val start = params.key * params.requestedLoadSize
        val afterData = animalList.subList(start, start + params.requestedLoadSize)
        callback.onResult(afterData, params.key + 1)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Animal>) {
        // do nothing
    }
}
