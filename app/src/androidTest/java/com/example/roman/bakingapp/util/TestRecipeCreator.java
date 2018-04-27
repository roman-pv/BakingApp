package com.example.roman.bakingapp.util;

import com.example.roman.bakingapp.data.model.Ingredient;
import com.example.roman.bakingapp.data.model.RecipeEntity;
import com.example.roman.bakingapp.data.model.RecipeWithStepsAndIngredients;
import com.example.roman.bakingapp.data.model.Step;

import java.util.ArrayList;
import java.util.List;

public class TestRecipeCreator {

    public static RecipeWithStepsAndIngredients createTestRecipe() {
        List<Ingredient> ingredientList = new ArrayList<Ingredient>();
        ingredientList.add(
                new Ingredient(0, 0, 1.5, "kg", "potato"));
        List<Step> steps = new ArrayList<Step>();
        steps.add(new Step(0, 0, "make it", "make it", "", ""));

        RecipeWithStepsAndIngredients testRecipe = new RecipeWithStepsAndIngredients(
                new RecipeEntity(0, "borsh", 1, ""), ingredientList, steps);

        return testRecipe;
    }
}
