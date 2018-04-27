package com.example.roman.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.roman.bakingapp.data.model.Ingredient;
import com.example.roman.bakingapp.data.model.RecipeEntity;
import com.example.roman.bakingapp.data.model.RecipeWithStepsAndIngredients;
import com.example.roman.bakingapp.data.model.Step;
import com.example.roman.bakingapp.testing.SingleFragmentActivity;
import com.example.roman.bakingapp.ui.detail.StepsActivity;
import com.example.roman.bakingapp.ui.main.RecipesFragment;
import com.example.roman.bakingapp.ui.main.RecipesViewModel;
import com.example.roman.bakingapp.util.RecyclerViewMatcher;
import com.example.roman.bakingapp.util.TaskExecutorWithIdlingResourceRule;
import com.example.roman.bakingapp.util.ViewModelUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.roman.bakingapp.ui.main.MainActivity.EXTRA_RECIPE_ID;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
public class RecipesFragmentTest {
 @Rule
    public final ActivityTestRule<SingleFragmentActivity> activityRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);

    @Rule
    public TaskExecutorWithIdlingResourceRule executorRule =
            new TaskExecutorWithIdlingResourceRule();

    private final MutableLiveData<List<RecipeWithStepsAndIngredients>> recipes = new MutableLiveData<>();

    @Before
    public void init() {

        RecipesFragment recipesFragment = new RecipesFragment();
        RecipesViewModel viewModel = mock(RecipesViewModel.class);

        when(viewModel.getRecipes()).thenReturn(recipes);

        recipesFragment.mFactory = ViewModelUtil.createFor(viewModel);

        activityRule.getActivity().setFragment(recipesFragment);
    }

    @Test
    public void loadRecipes() {
        List<Ingredient> ingredientList = new ArrayList<Ingredient>();
        ingredientList.add(
                new Ingredient(0, 0, 1.5, "kg", "potato"));
        List<Step> steps = new ArrayList<Step>();
        steps.add(new Step(0, 0, "make it", "make it", "", ""));

        RecipeWithStepsAndIngredients testRecipe = new RecipeWithStepsAndIngredients(
                new RecipeEntity(0, "borsh", 1, ""), ingredientList, steps);

        recipes.postValue(Arrays.asList(testRecipe));

        onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("borsh"))));
    }

    @Test
    public void checkTransitionToSteps() {

        List<Ingredient> ingredientList = new ArrayList<Ingredient>();
        ingredientList.add(
                new Ingredient(0, 0, 1.5, "kg", "potato"));
        List<Step> steps = new ArrayList<Step>();
        steps.add(new Step(0, 0, "make it", "make it", "", ""));

        RecipeWithStepsAndIngredients testRecipe = new RecipeWithStepsAndIngredients(
                new RecipeEntity(0, "borsh", 1, ""), ingredientList, steps);

        recipes.postValue(Arrays.asList(testRecipe));

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_RECIPE_ID, 0);
        intent.putExtras(bundle);
        Instrumentation.ActivityResult activityResult = new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);
        Instrumentation.ActivityMonitor am = new Instrumentation.ActivityMonitor(StepsActivity.class.getName(), activityResult, true);
        InstrumentationRegistry.getInstrumentation().addMonitor(am);

        onView(listMatcher().atPosition(0)).perform(click());

        assertTrue(InstrumentationRegistry.getInstrumentation().checkMonitorHit(am, 1));
    }


    @NonNull
    private RecyclerViewMatcher listMatcher() {
        return new RecyclerViewMatcher(R.id.recycler_view_recipes);
    }
}
