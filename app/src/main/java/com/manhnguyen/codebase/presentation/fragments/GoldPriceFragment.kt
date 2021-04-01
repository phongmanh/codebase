package com.manhnguyen.codebase.presentation.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.appbar.MaterialToolbar
import com.manhnguyen.codebase.R
import com.manhnguyen.codebase.base.FragmentBase
import com.manhnguyen.codebase.domain.model.GoldPriceModel
import com.manhnguyen.codebase.presentation.DialogBuilder
import com.manhnguyen.codebase.presentation.adapters.GoldPriceAdapter
import com.manhnguyen.codebase.util.DateTimeUtils
import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import org.koin.android.ext.android.inject


class GoldPriceFragment : FragmentBase() {

    companion object {
        fun newInstance() = GoldPriceFragment()
    }

    private val goldPriceViewModel: GoldPriceViewModel by inject()
    private lateinit var goldPriceAdapter: GoldPriceAdapter
    private var profileDialog: Dialog? = null
    private lateinit var dialogView: View


    @BindView(R.id.lineChart)
    lateinit var lineChart: LineChart

    @BindView(R.id.recyclerView)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.progressBar)
    lateinit var progressBar: MaterialProgressBar

    @BindView(R.id.dataContainer)
    lateinit var dataContainer: LinearLayout

    @BindView(R.id.noDataContainer)
    lateinit var noDataContainer: LinearLayout

    @BindView(R.id.dayTextView)
    lateinit var dayTextView: TextView

    @BindView(R.id.dayOfWeekTextView)
    lateinit var dayOfWeekTextView: TextView

    @BindView(R.id.monthAndYearTextView)
    lateinit var monthAndYearTextView: TextView


    @BindView(R.id.main_toolbar)
    lateinit var toolbar: MaterialToolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.gold_price_fragment, container, false)
        ButterKnife.bind(this, view)
        showProgressBar(progressBar)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initToolbar()
        setDateTitle()
        initChart()
        initData()
    }

    private fun initToolbar() {
        dialogView = layoutInflater.inflate(R.layout.dialog_view_layout, null)
        profileDialog = DialogBuilder.alertDialog2(
            requireContext(),
            dialogView
        )
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            profileDialog!!.show()
        }
        val profile = goldPriceViewModel.getProfile()
        dialogView.findViewById<TextView>(R.id.nameTextView).text = profile.name
        dialogView.findViewById<TextView>(R.id.emailTextView).text = profile.email

        dialogView.findViewById<Button>(R.id.closeDialogBtn).setOnClickListener {
            profileDialog!!.dismiss()
        }
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
                if (!emptyDataHandle(it.isEmpty())) {
                    goldPriceAdapter.bindData(it)
                    data = generateLineData(it)
                    invalidate()
                }
            }
            closeProgressbar(progressBar)
        })
        goldPriceViewModel.loadGoldPrice()
    }

    /**
     * set current data title
     */
    private fun setDateTitle() {
        val result = DateTimeUtils.getCurrentDate()
        dayTextView.text = result[0]
        dayOfWeekTextView.text = result[1]
        monthAndYearTextView.text = result[2]
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
            setNoDataText("")
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

    private fun emptyDataHandle(isEmpty: Boolean): Boolean {
        if (isEmpty) {
            noDataContainer.visibility = View.VISIBLE
            dataContainer.visibility = View.GONE
            lineChart.apply {
                setNoDataText(resources.getString(R.string.no_data_title))
                getPaint(Chart.PAINT_INFO).apply {
                    color = resources.getColor(R.color.chart_main_color)
                    textSize = resources.getDimension(R.dimen.chart_medium_text_size)
                }
                invalidate()
            }
        } else {
            noDataContainer.visibility = View.GONE
            dataContainer.visibility = View.VISIBLE
        }

        return isEmpty

    }

    @OnClick(R.id.refreshButton)
    fun onClick() {
        noDataContainer.visibility = View.GONE
        showProgressBar(progressBar)
        goldPriceViewModel.loadGoldPrice()
    }

}