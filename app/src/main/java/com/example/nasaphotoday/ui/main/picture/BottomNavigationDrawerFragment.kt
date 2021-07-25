package com.example.nasaphotoday.ui.main.picture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.nasaphotoday.R
import com.example.nasaphotoday.databinding.BottomNavigationLayoutBinding
import com.example.nasaphotoday.databinding.MainFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {
    private lateinit var binding: BottomNavigationLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomNavigationLayoutBinding.inflate(inflater, container, false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_one -> Toast.makeText(context, getString(R.string.one), Toast.LENGTH_SHORT).show()
                R.id.navigation_two -> Toast.makeText(context, getString(R.string.two), Toast.LENGTH_SHORT).show()
            }
            true
        }
    }
}