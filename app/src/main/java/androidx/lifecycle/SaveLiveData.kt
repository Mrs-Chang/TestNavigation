package androidx.lifecycle

import android.util.Log

class SaveLiveData<T> : MutableLiveData<T>() {

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val interceptor = InterceptorObserver(observer, this.version == START_VERSION)
        Log.d("GaoChang", "observe: " + this.version)
        super.observe(owner, interceptor)
    }

    inner class InterceptorObserver<T>(
        private val observer: Observer<in T>,
        private var preventDispatch: Boolean = false
    ) : Observer<T> {
        override fun onChanged(value: T) {
//            if (preventDispatch) {
//                preventDispatch = false
//                return
//            }
            Log.d("GaoChang", "onChanged: $value")
            observer.onChanged(value)
        }
    }
}