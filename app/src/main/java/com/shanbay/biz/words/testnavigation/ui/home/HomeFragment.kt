package com.shanbay.biz.words.testnavigation.ui.home

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.util.Pools
import androidx.core.util.Pools.SynchronizedPool
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.shanbay.biz.words.testnavigation.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        val sRectPool: Pools.Pool<Rect> = SynchronizedPool(12)
    }

    private fun getRect(): Rect {
        var rect = sRectPool.acquire()
        if (rect == null) {
            rect = Rect()
            Log.d("GaoChang22", "onCreateView: null")
        } else {
            Log.d("GaoChang22", "onCreateView: rect = $rect")
        }
        return rect
    }

    private fun releaseRect(rect: Rect) {
        // 可能需要重置rect的状态，确保其为初始状态，避免潜在的数据泄露或错误复用
        rect.setEmpty()
        sRectPool.release(rect)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("GaoChang", "onCreateView: "+this.hashCode())
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        val numText : TextView = binding.textHome2
        homeViewModel.number.observe(viewLifecycleOwner) {
            numText.text = it.toString()
        }

        val button = binding.buttonHome
        button.setOnClickListener {
            homeViewModel.numberAdd()
        }
        for (i in 0..30) {
            val rect = getRect()
            releaseRect(rect)
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}