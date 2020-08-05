package com.lukasstancikas.amrotestvenues.base

import androidx.lifecycle.ViewModel
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

abstract class BaseViewModel : ViewModel() {

    private val _loading = BehaviorSubject.create<Boolean>()
    val loading: Observable<Boolean> get() = _loading.hide()

    protected fun <T> Maybe<T>.withLoadingEvents(): Maybe<T> {
        return doOnSubscribe { _loading.onNext(true) }
            .doFinally { _loading.onNext(false) }
    }
}