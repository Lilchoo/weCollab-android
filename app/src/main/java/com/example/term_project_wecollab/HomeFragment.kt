package com.example.term_project_wecollab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.term_project_wecollab.databinding.FragmentHomeBinding
import com.example.term_project_wecollab.pager2_transformers.Pager2_AntiClockSpinTransformer
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    lateinit var categories:ArrayList<String>
    lateinit var clothing:ArrayList<ArrayList<Item>>
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    var shoeModels = ArrayList<Item>()
    var shirtModels = ArrayList<Item>()
    var pantsModels = ArrayList<Item>()
    var jacketModels = ArrayList<Item>()
    private val itemViewModel: ItemViewModel by activityViewModels()
    private val scope = CoroutineScope(Dispatchers.Main)

    private suspend fun setUpClothingModel() {
        if (shoeModels.size > 0 && shirtModels.size > 0 && pantsModels.size > 0 && jacketModels.size > 0) {
            return
        }
        shoeModels.addAll(itemViewModel.getItemsByCategory("Shoe"))
        shirtModels.addAll(itemViewModel.getItemsByCategory("Shirt"))
        pantsModels.addAll(itemViewModel.getItemsByCategory("Pant"))
        jacketModels.addAll(itemViewModel.getItemsByCategory("Jacket"))
    }

    private fun setupViewPager() {
        categories = arrayListOf(
            "Shoes",
            "Shirt",
            "Pants",
            "Jacket"
        )

        clothing = arrayListOf(
            shoeModels,
            shirtModels,
            pantsModels,
            jacketModels
        )

        val pager = binding.viewPagerHome
        val tabLayout = binding.TabLayoutHomeItem
        pager.adapter = PagerAdapter(activity as FragmentActivity)
        pager.setPageTransformer(Pager2_AntiClockSpinTransformer())

        TabLayoutMediator(tabLayout, pager) {tab, position ->
            tab.text = categories[position]
        }.attach()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:
    Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scope.launch {
            setUpClothingModel()
            setupViewPager()
        }
        searchQuery(view)
    }

    private fun searchQuery(view: View) {
        val search = binding.searchViewHomeMain
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val stringCheck = query?.lowercase()?.split(" ")?.joinToString(" ") { it.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                } }
                scope.launch {
                    val item = stringCheck?.let { itemViewModel.getItemsByName(it) }
                    if (item != null) {
                        if (item.isEmpty()) {
                            Snackbar.make(view, "No Item Found.", Snackbar.LENGTH_SHORT)
                                .show()
                        } else {
                            val bundle = Bundle()
                            val item1 = item[0]
                            bundle.putParcelable("item", item1)
                            bundle.putStringArrayList("sizes", item1.sizes)
                            findNavController().navigate(R.id.action_homeFragment_to_itemFragment, bundle)
                        }
                    }
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // Inner gives access to all the parent class data members. In this case its sports
    inner class PagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment = HomeItemFragment
            .newInstance(clothing[position])

        // How many fragments we want to create. 4 cards
        override fun getItemCount(): Int = 4
    }
}