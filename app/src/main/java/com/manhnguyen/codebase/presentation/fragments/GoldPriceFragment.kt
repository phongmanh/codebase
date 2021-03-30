package com.manhnguyen.codebase.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.manhnguyen.codebase.R
import com.manhnguyen.codebase.base.FragmentBase
import com.manhnguyen.codebase.domain.model.GoldPriceModel
import com.manhnguyen.codebase.presentation.adapters.GoldPriceAdapter
import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import org.koin.android.ext.android.inject

class GoldPriceFragment : FragmentBase() {

    companion object {
        fun newInstance() = GoldPriceFragment()
    }

    private val goldPriceViewModel: GoldPriceViewModel by inject()
    private lateinit var goldPriceAdapter: GoldPriceAdapter

    @BindView(R.id.lineChart)
    lateinit var lineChart: LineChart

    @BindView(R.id.recyclerView)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.progressBar)
    lateinit var progressBar: MaterialProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.gold_price_fragment, container, false)
        ButterKnife.bind(this, view)
        showProgressBar(progressBar)
        initChart()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    /**
     * get data from internet
     */
    private fun initData() {

        goldPriceAdapter = GoldPriceAdapter(requireContext())
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = goldPriceAdapter
        }

        goldPriceViewModel.getGoldPrice().observe(viewLifecycleOwner, Observer {
            lineChart.apply {
                goldPriceAdapter.bindData(it)
                data = generateLineData(it)
                invalidate()
                closeProgressbar(progressBar)
            }

        })
        goldPriceViewModel.loadGoldPrice()
    }

    private fun initChart() {
        lineChart.apply {
            setTouchEnabled(false)
            setPinchZoom(false)
            legend.apply {
                isEnabled = false
            }
            setBackgroundColor(resources.getColor(R.color.chart_white_background))
            setDrawGridBackground(false)
            setDrawBorders(false)
            description.isEnabled = false
            setNoDataText(resources.getString(R.string.no_data_title))
            axisRight.apply {
                setDrawGridLines(false)
                setDrawAxisLine(false)
                setDrawLabels(false)
                axisRight.axisMinimum = 0f
            }
            axisLeft.apply {
                setDrawGridLines(false)
                setDrawAxisLine(false)
                setDrawLabels(false)
                axisMinimum = 0f
            }
            xAxis.apply {
                setDrawGridLines(false)
                setDrawAxisLine(false)
                setDrawLabels(false)
                axisMinimum = 0f
            }
            invalidate()
        }
    }

    private fun generateLineData(data: ArrayList<GoldPriceModel>): LineData {
        val lineData = LineData()
        val entries: ArrayList<Entry> = ArrayList()
        for (index in 0 until data.size) {
            entries.add(
                Entry(
                    index + 0.5f,
                    data[index].amount.toFloat()
                )
            )
        }
        //for (index in 0 until 6) entries.add(Entry(index + 0.5f, Random.nextInt(5, 15).toFloat()))

        val set = LineDataSet(entries, "")
        val colorMain = resources.getColor(R.color.chart_main_color)
        set.color = colorMain
        set.lineWidth = 2.5f
        set.setCircleColor(colorMain)
        set.circleRadius = 5f
        set.circleHoleColor = colorMain
        set.fillColor = colorMain
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.setDrawValues(true)
        set.setDrawValues(false)
        set.axisDependency = YAxis.AxisDependency.LEFT
        lineData.addDataSet(set)
        return lineData
    }


}