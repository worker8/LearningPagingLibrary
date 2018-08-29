package beepbeep.learningpaginglibrary

import android.arch.paging.PageKeyedDataSource
import android.util.Log
import beepbeep.pixelsforreddit.Sushi

class SushiDataSource : PageKeyedDataSource<Int, Sushi>() {
    val firstPage = 0
    val SushiList = listOf(
            Sushi("salmon-1"),
            Sushi("salmon-2"),
            Sushi("salmon-3"),
            Sushi("salmon-4"),
            Sushi("salmon-5"),
            Sushi("salmon-6"),
            Sushi("salmon-7"),
            Sushi("salmon-8"),
            Sushi("salmon-9"),
            Sushi("salmon-10"),
            Sushi("salmon-11"),
            Sushi("salmon-12"),
            Sushi("salmon-13"),
            Sushi("salmon-14"),
            Sushi("salmon-15"),
            Sushi("salmon-16"),
            Sushi("salmon-17"),
            Sushi("salmon-18"),
            Sushi("salmon-19"),
            Sushi("salmon-20"),
            Sushi("salmon-21"),
            Sushi("salmon-22"),
            Sushi("salmon-23"))

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Sushi>) {
        callback.onResult(SushiList.subList(0, params.requestedLoadSize),
                firstPage - 1, firstPage + 1)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Sushi>) {
        Log.d("ddw", "params.key: ${params.key}")
        val start = params.key * params.requestedLoadSize
        Log.d("ddw", "start: ${start}")
        val tempList = SushiList.subList(start, start + params.requestedLoadSize)
        Log.d("ddw", "tempList: ${tempList}")
        callback.onResult(tempList, params.key + 1)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Sushi>) {
        // do nothing
    }
}