package com.rizalzaenal.uitest;

import androidx.test.espresso.IdlingResource;
import com.rizalzaenal.recipes.R;
import com.rizalzaenal.recipes.ui.main.MainActivity;
import com.rizalzaenal.recipes.utils.BasicIdlingResource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(JUnit4.class)
public class RecipeAppTest {

    private IdlingResource idlingResource;

    @Before
    public void setIdlingResource() {
        idlingResource = BasicIdlingResource.getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);


    @Test
    public void check_main_recycler_view_displayed(){
        onView(withId(R.id.rv_main)).check(matches(isDisplayed()));
    }

    @Test
    public void click_on_recipe_open_recipe_detail(){
        onView(withId(R.id.rv_main))
          .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.rv_steps)).check(matches(isDisplayed()));
    }

    @Test
    public void click_on_recipe_open_recipe_detail_and_back(){
        onView(withId(R.id.rv_main))
          .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.rv_steps)).check(matches(isDisplayed()));

        Espresso.pressBack();

        onView(withId(R.id.rv_main)).check(matches(isDisplayed()));

    }

    @Test
    public void check_step_detail_displayed(){
        onView(withId(R.id.rv_main))
          .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.rv_steps)).check(matches(isDisplayed()));

        onView(withId(R.id.rv_steps))
          .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.video_view)).check(matches(isDisplayed()));
    }

    @Test
    public void check_ingredient_card_displayed(){
        onView(withId(R.id.rv_main))
          .perform(actionOnItemAtPosition(0, click()));

        onView(withId(R.id.ingredients)).check(matches(isDisplayed()));



    }
    

}