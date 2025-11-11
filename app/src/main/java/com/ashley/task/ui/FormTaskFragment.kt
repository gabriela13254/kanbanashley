package com.ashley.task.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.ashley.task.R
import com.ashley.task.data.model.Status
import com.ashley.task.data.model.Task
import com.ashley.task.databinding.FragmentFormTaskBinding
import com.ashley.task.util.initToolbar
import com.ashley.task.util.showBottomSheet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import java.lang.ref.Reference

class FormTaskFragment : Fragment() {
    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: Task
    private var newTask: Boolean = true
    private var status: Status = Status.TODO

    private lateinit var reference: DatabaseReference

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)

        reference = Firebase.database.reference
        auth = Firebase.auth

        initListener()
    }
    private fun initListener(){
        binding.buttonSave.setOnClickListener {
            valideData()
        }
    }

    private fun valideData(){
        val description = binding.editTextDescricao.text.toString().trim()
        if (description.isNotBlank()){
            Toast.makeText(requireContext(),"Perfeito!", Toast.LENGTH_SHORT).show()
        }else{
            showBottomSheet(message = getString(R.string.description_empty_form_task_fragment))
        }
    }

    private fun saveTask(){
        reference
            .child("task")
            .child(auth.currentUser?.uid ?: "")
            .child(task.id)
            .setValue(task).addOnCompleteListener { result ->
                if (result.isSuccessful){
                    Toast.makeText(
                        requireContext(),
                        R.string.text_save_sucess_form_task_fragment,
                        Toast.LENGTH_SHORT).show()

                    if (newTask){
                        findNavController().popBackStack()

                    }else{
                        binding.progressBar.isVisible = false
                    }
                }else{
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = getString(R.string.error_generic))
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}