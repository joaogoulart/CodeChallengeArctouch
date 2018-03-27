package com.arctouch.codechallenge.common.utils

import android.support.v7.widget.SearchView
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

object RxSearchView {
    fun fromSearchView(searchView: SearchView): Observable<String> {
        val observable = BehaviorSubject.create<String>()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                observable.onNext(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                observable.onNext(newText)
                return true
            }
        })

        searchView.setOnClickListener {
            searchView.setQuery("", false)
            searchView.onActionViewCollapsed()
            observable.onNext("")
        }

        return observable
    }
}