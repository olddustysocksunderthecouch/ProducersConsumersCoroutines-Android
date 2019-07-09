package net.studydrive

import androidx.lifecycle.*

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) {
    liveData.observe(this, Observer(body))
}

fun <T : Any, L : MutableLiveData<T>?> LifecycleOwner.observeMutable(liveData: L, body: (T?) -> Unit) {
    liveData?.observe(this, Observer(body))
}
