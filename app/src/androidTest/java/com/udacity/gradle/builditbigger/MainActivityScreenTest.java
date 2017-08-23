package com.udacity.gradle.builditbigger;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

//THIS TEST, CLICKS ON THE FIRST ELEMENT OF THE RECYCLER VIEW ONCE IT'S BEEN LOADED
//AND CHECKS THAT THE RECIPE ACTIVITY IS OPENED WITH THE RECIPE'S DATA (OR AT LEAST THE TITLE)

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityScreenTest {

    private IdlingResource mIdlingResource;
    private static String EMPTY_STRING = "";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIddlingResource(){

        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void getJokeNotNull() {
        onView(withId(R.id.bttn_tell_joke)).perform(click());
        onView(withId(R.id.tv_joke)).check(matches(withText(not(EMPTY_STRING))));
    }

    @After
    public void unregisterIdlingResource(){
        if(mIdlingResource != null){
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}
