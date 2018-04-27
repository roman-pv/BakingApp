package com.example.roman.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import com.example.roman.bakingapp.data.model.Ingredient;
import com.example.roman.bakingapp.data.model.RecipeEntity;
import com.example.roman.bakingapp.data.model.RecipeWithStepsAndIngredients;
import com.example.roman.bakingapp.data.model.Step;
import com.example.roman.bakingapp.testing.SingleFragmentActivity;
import com.example.roman.bakingapp.ui.detail.RecipeDetailsActivity;
import com.example.roman.bakingapp.ui.detail.StepsActivity;
import com.example.roman.bakingapp.ui.detail.StepsFragment;
import com.example.roman.bakingapp.ui.detail.StepsViewModel;
import com.example.roman.bakingapp.ui.main.MainActivity;
import com.example.roman.bakingapp.util.RecyclerViewMatcher;
import com.example.roman.bakingapp.util.TaskExecutorWithIdlingResourceRule;
import com.example.roman.bakingapp.util.TestRecipeCreator;
import com.example.roman.bakingapp.util.ViewModelUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.roman.bakingapp.ui.main.MainActivity.EXTRA_RECIPE_ID;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class StepsFragmentTest {

    @Rule
    public final ActivityTestRule<SingleFragmentActivity> activityRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);

    @Rule
    public TaskExecutorWithIdlingResourceRule executorRule =
            new TaskExecutorWithIdlingResourceRule();

    private final MutableLiveData<RecipeWithStepsAndIngredients> recipe = new MutableLiveData<>();

    private RecipeWithStepsAndIngredients testRecipe = TestRecipeCreator.createTestRecipe();

    @Before
    public void init() {

        StepsFragment stepsFragment = new StepsFragment();
        StepsViewModel viewModel = mock(StepsViewModel.class);

        when(viewModel.getRecipe(0)).thenReturn(recipe);

        stepsFragment.mFactory = ViewModelUtil.createFor(viewModel);

        activityRule.getActivity().setFragmentwithRecipeId(stepsFragment);
    }

    @Test
    public void loadSteps() {

        recipe.postValue(testRecipe);

        onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("make it"))));

    }


    @Test
    public void checkTransitionToRecipeDetails() {

        recipe.postValue(testRecipe);

        Intent intent = new Intent();
        intent.putExtra(MainActivity.EXTRA_RECIPE_ID, 0);
        Instrumentation.ActivityResult activityResult = new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);
        Instrumentation.ActivityMonitor am = new Instrumentation.ActivityMonitor(RecipeDetailsActivity.class.getName(), activityResult, true);
        getInstrumentation().addMonitor(am);

        onView(listMatcher().atPosition(0)).perform(click());

        assertTrue(getInstrumentation().checkMonitorHit(am, 1));
    }


    @NonNull
    private RecyclerViewMatcher listMatcher() {
        return new RecyclerViewMatcher(R.id.recycler_view_steps);
    }
}
