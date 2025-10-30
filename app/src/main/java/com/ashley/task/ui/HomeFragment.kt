package com.ashley.task.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ashley.task.R
import com.ashley.task.databinding.FragmentHomeBinding
import com.ashley.task.ui.adapter.ViewPagerAdapter
import com.ashley.task.util.showBottomSheet
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        initListeners()

        initTabs()
    }

    private fun initListeners(){
        binding.btnLogout.setOnClickListener{
            showBottomSheet(
                tittleButton = R.string.text_button_confirm_logout,
                titleDialog = R.string.text_title_dialog_confira_logout,
                message = getString(R.string.text_message_dialog_comfirm_logout),
                onClick = {
                    auth.signOut()
                    findNavController().navigate(R.id.action_homeFragment_to_autentication)
                }
            )
        }
    }
    private fun initTabs() {
        val pageAdapter = ViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = pageAdapter
        pageAdapter.addFragment(TodoFragment(), R.string.status_task_todo)
        pageAdapter.addFragment(DoingFragment(), R.string.status_task_doing)
        pageAdapter.addFragment(DoneFragment(), R.string.status_task_done)

        binding.viewPager.offscreenPageLimit = pageAdapter.itemCount

        TabLayoutMediator(binding.tabs, binding.viewPager){tab, position ->
            tab.text = getString(pageAdapter.getTitle(position))
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}