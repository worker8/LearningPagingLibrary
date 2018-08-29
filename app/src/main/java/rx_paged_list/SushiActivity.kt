package beepbeep.learningpaginglibrary

import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import beepbeep.pixelsforreddit.Sushi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_sushi.*
import rx_paged_list.SushiDataSourceFactory

class SushiActivity : AppCompatActivity() {
    var disposable = Disposables.empty()
    val buttonSubject = PublishSubject.create<Unit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sushi)
        load()
    }

    fun load() {
        val config: PagedList.Config = PagedList.Config.Builder()
                .setInitialLoadSizeHint(2)
                .setPageSize(2)
                .build()

        sushiButton.setOnClickListener {
            buttonSubject.onNext(Unit)
        }

        val obs = RxPagedListBuilder(SushiDataSourceFactory(), config)
                .buildObservable()

        disposable = Observable.combineLatest<PagedList<Sushi>, Unit, String>(obs, buttonSubject,
                BiFunction { pagedList, _ ->
                    pagedList.loadAround(pagedList.size - 1)
                    pagedList.snapshot().fold("") { output, element -> "${output}, ${element.name}" }.drop(1)
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            sushiTextView.text = it
                        },
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
