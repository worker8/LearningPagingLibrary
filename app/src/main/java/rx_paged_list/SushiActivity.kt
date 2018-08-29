package beepbeep.learningpaginglibrary

import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import beepbeep.pixelsforreddit.Sushi
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_sushi.*
import rx_paged_list.SushiDataSourceFactory

class SushiActivity : AppCompatActivity() {
    var disposable = Disposables.empty()

    private val buttonObservable by lazy {
        RxView.clicks(sushiButton).map { Unit }.subscribeOn(AndroidSchedulers.mainThread())
    }


    private val pagedListObservable by lazy {
        val config: PagedList.Config = PagedList.Config.Builder()
                .setInitialLoadSizeHint(2)
                .setPageSize(2)
                .build()
        RxPagedListBuilder(SushiDataSourceFactory(), config).buildObservable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sushi)

        disposable = Observable.combineLatest<PagedList<Sushi>, Unit, String>(pagedListObservable, buttonObservable,
                BiFunction { pagedList, _ ->
                    pagedList.loadAround(pagedList.size - 1)
                    pagedList.snapshot().fold("") { output, element -> "${output}, ${element.name}" }.drop(1)
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ sushiTextView.text = it },
                        {
                            it.printStackTrace()
                            Toast.makeText(this@SushiActivity, "BOOM! See log for error", Toast.LENGTH_SHORT).show()
                        }
                )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
