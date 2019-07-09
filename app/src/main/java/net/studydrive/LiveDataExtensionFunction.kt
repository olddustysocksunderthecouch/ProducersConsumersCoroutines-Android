package net.studydrive

import androidx.lifecycle.*

fun <T : Any, L : MutableLiveData<T>?> LifecycleOwner.observeMutable(liveData: L, body: (T?) -> Unit) {
    liveData?.observe(this, Observer(body))
}
