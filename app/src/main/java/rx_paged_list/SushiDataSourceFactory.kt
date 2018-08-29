package rx_paged_list

import android.arch.paging.DataSource
import beepbeep.learningpaginglibrary.SushiDataSource
import beepbeep.pixelsforreddit.Sushi

class SushiDataSourceFactory() : DataSource.Factory<Int, Sushi>() {
    val dataSource = SushiDataSource()
    override fun create(): DataSource<Int, Sushi> = dataSource
}