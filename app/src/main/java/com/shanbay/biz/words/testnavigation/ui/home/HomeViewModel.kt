package com.shanbay.biz.words.testnavigation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SaveLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _number = SaveLiveData<Int>().apply {
        value = 0
    }
    val number: LiveData<Int> = _number

    fun setText(text: String) {
        _text.postValue(text)
    }

    fun numberAdd() {
        _number.postValue(_number.value?.plus(1) ?: 0)
    }
}