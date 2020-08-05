package com.lukasstancikas.amrotestvenues.base

import androidx.lifecycle.ViewModel
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel : ViewModel() {

    private val _loading = BehaviorSubject.create<Boolean>()
    val loading: Observable<Boolean> get() = _loading.hide()

    protected val _error = PublishSubject.create<String>()
    val error: Observable<String> get() = _error.hide()

    protected fun <T> Observable<T>.withLoadingEvents(): Observable<T> {
        return doOnSubscribe { _loading.onNext(true) }
            .doOnNext { _loading.onNext(false) }
    }
}