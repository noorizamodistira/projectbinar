package com.modiss.binar_challengech5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.modiss.binar_challengech5.databinding.ActivityDetailsBinding
import com.modiss.binar_challengech5.items.CartItem
import com.modiss.binar_challengech5.viewmodel.CartViewModel
import com.modiss.binar_challengech5.viewmodel.CartViewModelFactory

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var googleMapsUrl: String
    private var quantity = 1
    private var pricePerItem = 0
    private var totalPrice = 0
    private lateinit var viewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(
            this,
            CartViewModelFactory(application)
        )[CartViewModel::class.java]

        val bundle = intent.extras
        val name = bundle?.getString("name")
        val img = bundle?.getString("imageUrl")
        val price = bundle?.getInt("price")
        val description = bundle?.getString("description")
        val restaurantAddress = bundle?.getString("restaurantAddress")


        if (binding.btnAddToCart2.text == "Tambah Ke Keranjang - Rp.0"){
            binding.btnAddToCart2.text = "Tambah Ke Keranjang - Rp. $price"
        }

        pricePerItem = price!!

        updateUI(name, price.toString(), description, img.toString(), restaurantAddress)


        binding.btnBack.setOnClickListener{
            finish()
        }
        binding.btnIncrease2.setOnClickListener {
            quantity++
            updateQuantity(quantity)
        }

        binding.btnDecrease2.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updateQuantity(quantity)
            }
        }


        binding.btnAddToCart2.setOnClickListener {
            val cartItem = CartItem(
                foodName = name.toString(),
                totalPrice = totalPrice,
                price = price,
                quantity = quantity,
                catatan = "",
                imageUrl = img.toString()
            )

            viewModel.insertCartItem(cartItem)

            Toast.makeText(this@DetailsActivity, "Item ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun updateQuantity(quantity: Int) {
        binding.txtItemQuantity2.text = quantity.toString()
        totalPrice = quantity * pricePerItem
        binding.btnAddToCart2.text = "Tambah Ke Keranjang - Rp. $totalPrice"

    }


    private fun updateUI(name: String?, price: String?, description: String?, imgUrl: String, restaurantAddress: String?) {
        binding.apply {
            Glide.with(this@DetailsActivity)
                .load(imgUrl)
                .into(binding.imgFood)
            txtFoodName.text = name
            txtFoodPrice.text = "Rp. $price"
            txtFoodDescription.text = description
            txtLocation.text = restaurantAddress
        }
    }
}