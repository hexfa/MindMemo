package com.mindmemo.presentation.ui
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.DialogFragment
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.lifecycleScope
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment
//import com.mindmemo.R
//import com.mindmemo.data.entity.MemoEntity
//import com.mindmemo.data.utils.BUNDLE_ID
//import com.mindmemo.data.utils.EDIT
//import com.mindmemo.data.utils.NEW
//import com.mindmemo.data.utils.getIndexFromList
//import com.mindmemo.data.utils.setupListWithAdapter
//import com.mindmemo.databinding.FragmentNoteBinding
//import com.mindmemo.presentation.viewmodel.NoteViewModel
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.flow.collectLatest
//import javax.inject.Inject
//
//
//@AndroidEntryPoint
//class NoteFragment : BottomSheetDialogFragment() {
//    private lateinit var binding: FragmentNoteBinding
//    @Inject
//    lateinit var entity: MemoEntity
//
//    //Other
//    private val viewModel: NoteViewModel by viewModels()
//    private var category = ""
//    private var priority = ""
//    private var noteId = 0
//    private var type = ""
//    private var isEdit = false
//    private val categoriesList: MutableList<String> = mutableListOf()
//    private val prioriesList: MutableList<String> = mutableListOf()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
//
//    }
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        binding = FragmentNoteBinding.inflate(layoutInflater)
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        //Bundle
//        noteId = arguments?.getInt(BUNDLE_ID) ?: 0
//        //Type
//        if (noteId > 0) {
//            type = EDIT
//            isEdit = true
//        } else {
//            isEdit = false
//            type = NEW
//        }
//        //InitViews
//        binding.apply {
//            //Close
//            closeImg.setOnClickListener { dismiss() }
//            //Spinner Category
//            viewModel.loadCategoriesData()
//            viewModel.categoriesList.observe(viewLifecycleOwner) {
//                categoriesList.addAll(it)
//                categoriesSpinner.setupListWithAdapter(it) { itItem ->
//                    category = itItem
//                }
//            }
//            //Spinner priority
//            viewModel.loadPrioritiesData()
//            viewModel.prioritiesList.observe(viewLifecycleOwner) {
//                prioriesList.addAll(it)
//                prioritySpinner.setupListWithAdapter(it) { itItem -> priority = itItem }
//            }
//            if (type == EDIT) {
//                viewModel.getDetail(noteId)
//                lifecycleScope.launchWhenCreated {
//                    viewModel.detailNote.collectLatest {
//                        if (it != null) {
//                            it.data?.let { itData ->
//                                titleEdt.setText(itData.title)
//                                descEdt.setText(itData.disc)
//                                categoriesSpinner.setSelection(categoriesList.getIndexFromList(itData.category))
//                                prioritySpinner.setSelection(prioriesList.getIndexFromList(itData.priority))
//                            }
//                        }
//                    }
//                }
//            }
//            //Click
//            saveNote.setOnClickListener {
//                val title = titleEdt.text.toString()
//                val disc = descEdt.text.toString()
//                entity.id = noteId
//                entity.title = title
//                entity.disc = disc
//                entity.category = category
//                entity.priority = priority
//
//                if (title.isNotEmpty() && disc.isNotEmpty()) {
//                    viewModel.saveOrEditNote(isEdit, entity)
//                    dismiss()
//                }
//
//                if(title.isEmpty() || disc.isEmpty()){
//                    Toast.makeText(requireContext(),"Please fill all fields",Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }
//}