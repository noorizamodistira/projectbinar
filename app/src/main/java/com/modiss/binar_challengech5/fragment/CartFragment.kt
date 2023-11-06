package com.modiss.binar_challengech5.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.modiss.binar_challengech5.adapter.CartAdapter
import com.modiss.binar_challengech5.databinding.FragmentCartBinding
import com.modiss.binar_challengech5.viewmodel.CartViewModel
import com.modiss.binar_challengech5.viewmodel.CartViewModelFactory


class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var viewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, CartViewModelFactory(requireActivity().application)).get(CartViewModel::class.java)


        setupRecyclerView()
        observeCartItems()

        binding.btnPlaceOrder.setOnClickListener {
            val navController = findNavController()
            val action = CartFragmentDirections.actionCartFragmentToConfirmOrderActivity()
            navController.navigate(action)
        }

        return binding.root
    }
    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(viewModel) { cartItem ->
            viewModel.deleteCartItem(cartItem)

        }

        binding.recyclerView.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeCartItems() {
        viewModel.allCartItems.observe(viewLifecycleOwner, Observer { cartItems ->
            cartAdapter.submitList(cartItems)
            val totalPrice = cartAdapter.calculateTotalPrice()
            binding.txtTotalPrice.text = "Total Price: Rp. $totalPrice"
        })
    }


}