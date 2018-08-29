package beepbeep.learningpaginglibrary

import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import beepbeep.pixelsforreddit.Animal
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_animal.*
import java.util.concurrent.Executors

class AnimalActivity : AppCompatActivity() {

    val buttonSubject = PublishSubject.create<Unit>()
    val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal)
        load()
    }

    fun load() {
        val dataSource = AnimalDataSource() //GalleryImageDataSource(galleryService)
        val config: PagedList.Config = PagedList.Config.Builder()
                .setInitialLoadSizeHint(2)
                .setPageSize(2)
                .build()
        val pagedList = PagedList.Builder(dataSource, config)
                .setInitialKey(0)
                .setFetchExecutor(Executors.newScheduledThreadPool(10))
                .setNotifyExecutor(Executors.newScheduledThreadPool(10))
                .build()

        val pagedListObservable = Observable.fromCallable { pagedList }

        button.setOnClickListener {
            buttonSubject.onNext(Unit)
        }

//        buttonSubject
//                .doOnNext { pagedList.loadAround(pagedList.size - 1) }
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    val output = pagedList.snapshot().fold("") { output, element -> "${output}, ${element.name}" }.drop(1)
//                    textView.text = output
//                }
//                .also { disposables.add(it) }

        Observable.combineLatest<PagedList<Animal>, Unit, String>(pagedListObservable, buttonSubject,
                BiFunction { pagedList, _ ->
                    pagedList.loadAround(pagedList.size - 1)
                    pagedList.snapshot().fold("") { output, element -> "${output}, ${element.name}" }.drop(1)
                })
                .map {
                    pagedList.snapshot().fold("") { output, element -> "${output}, ${element.name}" }.drop(1)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            textView.text = it
                        },
                        {
                            it.printStackTrace()
                            Toast.makeText(this@AnimalActivity, "BOOM! See log for error", Toast.LENGTH_SHORT).show()
                            Log.d("ddw", "Error: ${it.message}")
                        }
                ).also { disposables.add(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}
