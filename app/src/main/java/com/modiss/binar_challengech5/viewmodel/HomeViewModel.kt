package com.modiss.binar_challengech5.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.modiss.binar_challengech5.items.FoodCategory
import com.modiss.binar_challengech5.items.FoodItem

class HomeViewModel : ViewModel() {
    private val foodItemsLiveData: MutableLiveData<List<FoodItem>> = MutableLiveData()
    private val foodCategoryLiveData: MutableLiveData<List<FoodCategory>> = MutableLiveData()

    fun getFoodItems(): LiveData<List<FoodItem>> {
        return foodItemsLiveData
    }

    fun setFoodItems(foodItems: List<FoodItem>) {
        foodItemsLiveData.postValue(foodItems)
    }

    fun getFoodCategory(): LiveData<List<FoodCategory>>{
        return foodCategoryLiveData
    }

    fun setFoodCategory(foodCategory: List<FoodCategory>){
        foodCategoryLiveData.postValue(foodCategory)
    }


}
