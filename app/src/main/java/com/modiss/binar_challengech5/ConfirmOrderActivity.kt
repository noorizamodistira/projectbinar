package com.modiss.binar_challengech5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.modiss.binar_challengech5.adapter.ConfirmOrderAdapter
import com.modiss.binar_challengech5.api.ApiClient
import com.modiss.binar_challengech5.databinding.ActivityConfirmOrderBinding
import com.modiss.binar_challengech5.items.OrderItem
import com.modiss.binar_challengech5.response.OrderRequest
import com.modiss.binar_challengech5.response.OrderResponse
import com.modiss.binar_challengech5.viewmodel.CartViewModel
import com.modiss.binar_challengech5.viewmodel.CartViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ConfirmOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmOrderBinding
    private lateinit var viewModel: CartViewModel
    private lateinit var confirmOrderAdapter: ConfirmOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmOrderBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this, CartViewModelFactory(application)).get(
            CartViewModel::class.java)

        confirmOrderAdapter = ConfirmOrderAdapter()

        setupRecyclerView()
        observeCartItems()

        binding.btnPlaceOrder.setOnClickListener {
            val deliveryMethod = if (binding.btnPickUp.isChecked) {
                binding.btnPickUp.text.toString()
            } else {
                binding.btnDelivery.text.toString()
            }

            val paymentMethod = if (binding.btnCash.isChecked) {
                binding.btnCash.text.toString()
            } else {
                binding.btnDigital.text.toString()
            }

            viewModel.allCartItems.observe(this, Observer { cartItems ->
                val orders = cartItems.map { cartItem ->
                    OrderItem(
                        nama = cartItem.foodName,
                        qty = cartItem.quantity,
                        catatan = cartItem.catatan,
                        harga = cartItem.price
                    )
                }

                val totalPayment = confirmOrderAdapter.calculateTotalPrice()
                val orderRequest = OrderRequest(
                    username = getUsername(),
                    total = totalPayment,
                    orders = orders
                )

                sendOrderToAPI(orderRequest)

            })
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = confirmOrderAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeCartItems() {
        viewModel.allCartItems.observe(this, Observer { cartItems ->
            confirmOrderAdapter.submitList(cartItems)
            val totalPrice = confirmOrderAdapter.calculateTotalPrice()
            binding.txtTotalPayment.text = "Total Price: Rp. $totalPrice"
        })
    }

    private fun getUsername(): String {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.displayName ?: "Guest" // Replace "Guest" with a default username if needed
    }


    private fun sendOrderToAPI(orderRequest: OrderRequest) {
        val apiService = ApiClient.instance
        val call = apiService.orderFood(orderRequest)

        call.enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                if (response.isSuccessful) {
                    val orderResponse = response.body()
                    if (orderResponse != null && orderResponse.status) {
                        showSuccessDialog()
                    } else {
                        // Handle order failure
                    }
                } else {
                    // Handle API error
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                // Handle API failure
            }
        })
    }
    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Order Successful")
        builder.setMessage("Your order has been placed successfully.")
        builder.setPositiveButton("Kembali ke Home") { dialog, _ ->
            viewModel.deleteAllCartItems()
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}
