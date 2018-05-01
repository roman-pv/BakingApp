package com.example.roman.bakingapp;

import com.example.roman.bakingapp.data.model.Ingredient;

import java.util.List;

/**
 * Helper functions and commonly used constants
 */
public class RecipeUtilities {

    public final static String EXTRA_RECIPE_ID = "com.example.roman.bakingapp.extra.recipe_id";
    public final static String EXTRA_STEP_NUMBER = "com.example.roman.bakingapp.extra.step_number";

    public static String ingredientsListToString(List<Ingredient> ingredients) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Ingredient ingredient : ingredients) {

            stringBuilder
                    .append("\u25BA ")
                    .append(ingredient.getIngredient())
                    .append(": ")
                    .append(formatQuantity(ingredient))
                    .append("\u00A0")
                    .append(ingredient.getMeasure().toLowerCase())
                    .append("\n");
        }
        return stringBuilder.toString();
    }

    private static String formatQuantity(Ingredient ingredient) {
        double quantity = ingredient.getQuantity();

        if (quantity % 1 == 0) {
            int intQuantity = (int) quantity;
            return String.valueOf(intQuantity);
        }
        return String.valueOf(quantity);
    }

    public static String clearDescriptionString(String string) {
        return string.replaceAll("\ufffd", " ");
    }

}
