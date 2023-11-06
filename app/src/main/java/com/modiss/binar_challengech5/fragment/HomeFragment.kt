package com.modiss.binar_challengech5.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.modiss.binar_challengech5.R
import com.modiss.binar_challengech5.adapter.MenuAdapter
import com.modiss.binar_challengech5.adapter.FoodCategoryAdapter
import com.modiss.binar_challengech5.databinding.FragmentHomeBinding
import com.modiss.binar_challengech5.response.FoodCategoryResponse
import com.modiss.binar_challengech5.response.ListFoodResponse
import com.modiss.binar_challengech5.viewmodel.HomeViewModel
import com.modiss.binar_challengech5.api.ApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapterFood: MenuAdapter
    private lateinit var adapterCategories: FoodCategoryAdapter
    private lateinit var sharedPrefs: SharedPreferences
    private val PREF_NAME = "MyPrefs"
    private lateinit var layoutManagerGrid: GridLayoutManager
    private lateinit var layoutManagerLinear: LinearLayoutManager
    private lateinit var layoutHorizontal: LinearLayoutManager
    private lateinit var currentLayoutManager: RecyclerView.LayoutManager
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        sharedPrefs = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        layoutManagerGrid = GridLayoutManager(requireContext(), 2)
        layoutManagerLinear = LinearLayoutManager(requireContext())
        layoutHorizontal = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        setupLayout()
        setupMenu()
        getUserName()
        fetchFoodCategory()
        fetchFoodList()

        return binding.root
    }

    private fun setupLayout() {
        binding.rcCategories.layoutManager = layoutHorizontal
        adapterCategories = FoodCategoryAdapter()


        val savedLayout = sharedPrefs.getString("layout", "grid")
        if (savedLayout == "grid") {
            currentLayoutManager = layoutManagerGrid
            binding.changeLayout.setImageResource(R.drawable.categories)
        } else {
            currentLayoutManager = layoutManagerLinear
            binding.changeLayout.setImageResource(R.drawable.list)
        }

        binding.recyclerViewMenuGrid.layoutManager = currentLayoutManager

        binding.changeLayout.setOnClickListener {
            toggleLayoutManager()
        }
    }

    private fun toggleLayoutManager() {
        currentLayoutManager = if (currentLayoutManager == layoutManagerGrid) {
            layoutManagerLinear
        } else {
            layoutManagerGrid
        }

        binding.changeLayout.setImageResource(if (currentLayoutManager == layoutManagerGrid) {
            R.drawable.categories
        } else {
            R.drawable.list
        })

        sharedPrefs.edit().putString("layout", if (currentLayoutManager == layoutManagerGrid) {
            "grid"
        } else {
            "linear"
        }).apply()

        binding.recyclerViewMenuGrid.layoutManager = currentLayoutManager
    }

    private fun setupMenu() {
        adapterFood = MenuAdapter { selectedItem ->
            val bundle = Bundle()
            bundle.putString("name", selectedItem.nama)
            bundle.putInt("price", selectedItem.harga)
            bundle.putString("description", selectedItem.detail)
            bundle.putString("imageUrl", selectedItem.image_url)
            bundle.putString("restaurantAddress", selectedItem.alamat_resto)

            val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigate(R.id.detailsActivity, bundle)
        }

        viewModel.getFoodCategory().observe(viewLifecycleOwner) { foodCategories ->
            adapterCategories.submitList(foodCategories)
            Log.d("Adapter Categories", "Data size: ${foodCategories.size}")
        }

        viewModel.getFoodItems().observe(viewLifecycleOwner) { foodItems ->
            adapterFood.submitList(foodItems)
        }

        binding.rcCategories.adapter = adapterCategories
        binding.recyclerViewMenuGrid.adapter = adapterFood

    }

    private fun fetchFoodList() {
        val apiService = ApiClient.instance
        val call = apiService.getFoodItems()

        call.enqueue(object : Callback<ListFoodResponse> {
            override fun onResponse(call: Call<ListFoodResponse>, response: Response<ListFoodResponse>) {
                if (response.isSuccessful) {
                    val foodItems = response.body()?.data
                    if (foodItems != null) {
                        viewModel.setFoodItems(foodItems)
                    }
                } else {
                    Log.e("API Error", "Error fetching data from API: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ListFoodResponse>, t: Throwable) {
                Log.e("API Error", "Failed to fetch data from API: ${t.message}")
            }
        })
    }

    private fun fetchFoodCategory() {
        val apiService = ApiClient.instance
        val call = apiService.getFoodCategory()

        call.enqueue(object : Callback<FoodCategoryResponse> {
            override fun onResponse(call: Call<FoodCategoryResponse>, response: Response<FoodCategoryResponse>) {
                if (response.isSuccessful) {
                    val foodCategories = response.body()?.data
                    if (foodCategories != null) {
                        viewModel.setFoodCategory(foodCategories)
                    }
                } else {
                    Log.e("API Error", "Error fetching data from API: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<FoodCategoryResponse>, t: Throwable) {
                Log.e("API Error", "Failed to fetch data from API: ${t.message}")
            }
        })
    }

    private fun getUserName() {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid

        if (uid != null) {
            val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
            val usersReference = databaseReference.child("users").child(uid)

            usersReference.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val username = snapshot.child("username").getValue(String::class.java)
                    if (username != null) {
                        binding.txtUserName.text = "Hai $username!"
                    }
                }
            }.addOnFailureListener { exception ->
                // Handle errors when fetching data
            }
        }
    }
}
