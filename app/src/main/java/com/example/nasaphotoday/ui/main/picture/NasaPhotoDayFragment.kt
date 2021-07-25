package com.example.nasaphotoday.ui.main.picture

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.nasaphotoday.MainActivity
import com.example.nasaphotoday.R
import com.example.nasaphotoday.databinding.MainFragmentBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class NasaPhotoDayFragment : Fragment() {
    private lateinit var binding: MainFragmentBinding

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var descriptionHeader: TextView
    private lateinit var description: TextView
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private val viewModel: NasaPhotoDayViewModel by lazy {
        ViewModelProvider(this).get(NasaPhotoDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData(sdf.format(Date()))
            .observe(viewLifecycleOwner, { renderData(it) })
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
        descriptionHeader = view.findViewById(R.id.bottom_sheet_description_header)
        description = view.findViewById(R.id.bottom_sheet_description)
        setBottomAppBar()
        binding.chipGroup.setOnCheckedChangeListener { _, checkedId:Int ->
            var minusDays = 0
            when(checkedId){
                R.id.today -> minusDays = 0
                R.id.yesterday -> minusDays = -1
                R.id.before_yesterday -> minusDays = -2
            }
            val requestedDate = Calendar.getInstance()
            requestedDate.time = Date()
            requestedDate.add(Calendar.DATE, minusDays)
            viewModel.getData(sdf.format(requestedDate.time))
                .observe(viewLifecycleOwner, { renderData(it) })
        }
    }

    private fun renderData(data: NasaPhotoDayData) = with(binding) {
        when (data) {
            is NasaPhotoDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    //Отобразите ошибку
                    toast(getString(R.string.error_url))
                } else {

                    //Отобразите фото
                    //showSuccess()
                    //Coil в работе: достаточно вызвать у нашего ImageView
                    //нужную extension-функцию и передать ссылку и заглушки для placeholder
                    imageView.load(url) {
                        lifecycle(this@NasaPhotoDayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    descriptionHeader.text = serverResponseData.title
                    description.text = serverResponseData.explanation
                }
            }

            is NasaPhotoDayData.Loading -> {
                //showLoading()
            }
            is NasaPhotoDayData.Error -> {
                //showError(data.error.message)
                toast(data.error.message)
            }
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> Toast.makeText(
                context,
                getString(R.string.favorite),
                Toast.LENGTH_SHORT
            ).show()
            R.id.app_bar_search -> Toast.makeText(
                context,
                getString(R.string.search),
                Toast.LENGTH_SHORT
            ).show()
            R.id.app_bar_settings -> Toast.makeText(context, R.string.settings, Toast.LENGTH_SHORT)
                .show()
            //activity?.supportFragmentManager?.beginTransaction()
            //?.add(R.id.container, ChipsFragment())?.addToBackStack(null)?.commit()
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, TAG)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar() = with(binding) {
        val context = activity as MainActivity
        context.setSupportActionBar(bottomAppBar)
        setHasOptionsMenu(true)
        fab.setOnClickListener {
            if (isMain) {
                isMain = false
                bottomAppBar.navigationIcon = null
                bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_back_fab))
                bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other)
            } else {
                isMain = true
                bottomAppBar.navigationIcon =
                    ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_plus_fab))
                bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

    companion object {
        fun newInstance() = NasaPhotoDayFragment()
        private var isMain = true
        private var TAG = "tag"
    }
}