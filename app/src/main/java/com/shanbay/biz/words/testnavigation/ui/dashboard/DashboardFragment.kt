package com.shanbay.biz.words.testnavigation.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shanbay.biz.words.testnavigation.R
import com.shanbay.biz.words.testnavigation.databinding.FragmentDashboardBinding
import com.shanbay.biz.words.testnavigation.ui.home.HomeViewModel

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        val listString = ArrayList<String>()
        for (i in 0..100) {
            listString.add("item $i")
        }

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val listAdapter = SampleAdapter(inflater, listString)

        recyclerView.adapter = listAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class SampleAdapter(private val layoutInflater: LayoutInflater, private val data: ArrayList<String>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, pos: Int): RecyclerView.ViewHolder {
            return SampleViewHolder(layoutInflater, parent)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
            (holder as SampleViewHolder).bind(data[pos])
        }

        override fun getItemCount(): Int {
            return data.size
        }

        inner class SampleViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(android.R.layout.simple_list_item_1, parent, false)){

            fun bind(item:String){
                val tv = itemView.findViewById<TextView>(android.R.id.text1)
                tv.text = item
            }
        }
    }
}