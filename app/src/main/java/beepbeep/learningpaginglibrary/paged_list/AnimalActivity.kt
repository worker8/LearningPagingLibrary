package beepbeep.learningpaginglibrary

import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import beepbeep.pixelsforreddit.Animal
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_animal.*
import java.util.concurrent.Executors

class AnimalActivity : AppCompatActivity() {
    private val disposables = CompositeDisposable()
    private val buttonObservable by lazy {
        RxView.clicks(button).map { Unit }.subscribeOn(AndroidSchedulers.mainThread())
    }

    private val pagedListObservable = Observable.fromCallable {
        val config: PagedList.Config = PagedList.Config.Builder()
                .setInitialLoadSizeHint(2)
                .setPageSize(2)
                .build()
        PagedList.Builder(AnimalDataSource(), config)
                .setInitialKey(0)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .setNotifyExecutor(Executors.newSingleThreadExecutor())
                .build()
    }.subscribeOn(Schedulers.io())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal)
        Observable.combineLatest<PagedList<Animal>, Unit, String>(pagedListObservable, buttonObservable,
                BiFunction { _pagedList, _ ->
                    _pagedList.loadAround(_pagedList.size - 1)
                    _pagedList.snapshot().fold("") { output, element -> "${output}, ${element.name}" }.drop(1)
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ textView.text = it },
                        {
                            it.printStackTrace()
                            Toast.makeText(this@AnimalActivity, "BOOM! See log for error", Toast.LENGTH_SHORT).show()
                        }
                ).also { disposables.add(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}
