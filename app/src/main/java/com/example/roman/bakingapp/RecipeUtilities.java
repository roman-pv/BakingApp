package com.example.roman.bakingapp;

import com.example.roman.bakingapp.data.model.Ingredient;

import java.util.List;

public class RecipeUtilities {

    public final static String EXTRA_RECIPE_ID = "com.example.roman.bakingapp.extra.recipe_id";
    public final static String EXTRA_STEP_NUMBER = "com.example.roman.bakingapp.extra.step_number";


    public static String ingredientsListToString(List<Ingredient> ingredients) {
        String ingredientsString = "";
        for (Ingredient ingredient : ingredients) {
            ingredientsString += "\u25BA " + ingredient.getIngredient() + ": " +
                    ingredient.getQuantity() + " " +
                    ingredient.getMeasure().toLowerCase() + "\n";
        }
        return ingredientsString;
    }

}
