package com.mindmemo.presentation.ui

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Base64
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteappcleanarchitecture.R
import com.example.noteappcleanarchitecture.data.entity.NoteEntity
import com.example.noteappcleanarchitecture.data.utils.BUNDLE_ID
import com.example.noteappcleanarchitecture.data.utils.DELETE
import com.example.noteappcleanarchitecture.data.utils.EDIT
import com.example.noteappcleanarchitecture.databinding.FragmentHomeBinding
import com.example.noteappcleanarchitecture.databinding.HeaderDrawerLayoutBinding
import com.example.noteappcleanarchitecture.presentation.adapter.NoteAdapter
import com.example.noteappcleanarchitecture.presentation.viewmodel.HomeViewModel
import com.google.android.material.navigation.NavigationView
import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.databinding.FragmentHomeBinding
import com.mindmemo.databinding.HeaderDrawerLayoutBinding
import com.mindmemo.presentation.adapter.MemoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: FragmentHomeBinding
    lateinit var headerDrawerLayoutBinding: HeaderDrawerLayoutBinding


    @Inject
    lateinit var notesAdapter: MemoAdapter

    @Inject
    lateinit var noteEntity: MemoEntity

    private lateinit var navigationDrawer: ActionBarDrawerToggle

    //Other
    private val viewModel: HomeViewModel by viewModels()

    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null
    var ImgFromStore: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.notesToolbar.title = ""
        //initDrawerLayout()
        headerDrawerLayoutBinding = HeaderDrawerLayoutBinding.bind(binding.navView.getHeaderView(0));
        hideFabButtonScrolling()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(binding.notesToolbar)
        //InitViews
        binding.apply {
            //Note fragment
            addNoteBtn.setOnClickListener {
                NoteFragment().show(parentFragmentManager, NoteFragment().tag)
            }

        }
        //Get data
        viewModel.getAll()
        lifecycleScope.launchWhenCreated {
            viewModel.getAllNotes.collectLatest {
                if (it != null) {
                    showEmpty(it.isEmpty)
                }
                if (it != null) {
                    it.data?.let { itData ->
                        notesAdapter.setData(itData)
                    }
                }
                binding.noteList.apply {
                    layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    adapter = notesAdapter
                }
            }
        }

        //search data
        lifecycleScope.launchWhenCreated {
            viewModel.searchNotes.collectLatest {
                if (it != null) {
                    showEmpty(it.isEmpty)
                }
                if (it != null) {
                    it.data?.let { itData ->
                        notesAdapter.setData(itData)
                    }
                }
                binding.noteList.apply {
                    layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    adapter = notesAdapter
                }
            }
        }

        //Clicks
        notesAdapter.setOnItemClickListener { entity, type ->
            when (type) {
                EDIT -> {
                    val noteFragment = NoteFragment()
                    val bundle = Bundle()
                    bundle.putInt(BUNDLE_ID, entity.id)
                    noteFragment.arguments = bundle
                    noteFragment.show(parentFragmentManager, NoteFragment().tag)
                }
                DELETE -> {
                    noteEntity.id = entity.id
                    noteEntity.title = entity.title
                    noteEntity.disc = entity.disc
                    noteEntity.category = entity.category
                    noteEntity.priority = entity.priority
                    viewModel.deleteNote(noteEntity)
                }
            }
        }
    }

    private fun initDrawerLayout() {
        navigationDrawer = ActionBarDrawerToggle(
            requireActivity(),
            binding.drawerLayout,
            binding.notesToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(navigationDrawer)
        navigationDrawer.syncState()
        val navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)
        binding.drawerLayout.setViewScale(Gravity.START, 0.9f)
        binding.drawerLayout.setViewElevation(Gravity.START, 20f)

        initHeaderDrawerLayout()
    }

    private fun initHeaderDrawerLayout() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        ImgFromStore = sharedPrefs?.getString("profileImg", null)
        firstName = sharedPrefs?.getString("firstName", "")
        lastName = sharedPrefs?.getString("lastName", "")
        email = sharedPrefs?.getString("email", "")

        if (ImgFromStore == null) {
            headerDrawerLayoutBinding.imageView.setImageResource(R.drawable.profile_placeholder)
        } else {
            headerDrawerLayoutBinding.imageView.setImageBitmap(decodeBase64(ImgFromStore))
        }
        //set firstname and lastname
        if (firstName.equals("") || lastName.equals("")) {
            headerDrawerLayoutBinding.tvFullName.text = "Set your name in profile"
        } else {
            headerDrawerLayoutBinding.tvFullName.text = firstName + " " + lastName
        }

        if (email.equals("")){
            headerDrawerLayoutBinding.tvEmail.text = "Set your email"
        }else{
            headerDrawerLayoutBinding.tvEmail.text = email
        }
    }

    fun decodeBase64(input: String?): Bitmap? {
        val decodedByte = Base64.decode(input, 0)
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
    }

    private fun showEmpty(isShown: Boolean) {
        binding.apply {
            if (isShown) {
                emptyLay.visibility = View.VISIBLE
                noteList.visibility = View.GONE
            } else {
                emptyLay.visibility = View.GONE
                noteList.visibility = View.VISIBLE
            }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
        val search = menu.findItem(R.id.actionSearch)
        val searchView = search.actionView as SearchView
        searchView.queryHint = getString(R.string.search)
        initDrawerLayout()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchNote(newText)
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        if (item.itemId == R.id.profileFragment2) {
            //Toast.makeText(requireContext(),"prorile",Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment2)
        }
        if (item.itemId == R.id.exit) {
            requireActivity().finish()
        }

        if (item.itemId == R.id.settingFragment2) {
            findNavController().navigate(R.id.action_homeFragment_to_settingFragment2)
        }
        return true
    }

    private fun hideFabButtonScrolling(){
        binding.noteList.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy>0 || dy<0 && binding.addNoteBtn.isShown){
                    binding.addNoteBtn.hide()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                binding.addNoteBtn.show()
            }

        })
    }
}