package com.devthrust.mynotes;

import com.devthrust.mynotes.Pojo.CourseInfo;
import com.devthrust.mynotes.Pojo.DataManager;
import com.devthrust.mynotes.Pojo.NoteInfo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;

//

@RunWith(AndroidJUnit4.class)
public class DataManagerTest {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);


    @Test
    public void createNotes() {

        CourseInfo course = DataManager.getInstance().getCourses().get(0);
        String title = "hello sir, how are you doing";
        String body = "how things dy go";

        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.spinner_courses)).perform(click());
        onData(allOf(instanceOf(CourseInfo.class), equalTo(course))).perform(click());
        onView(withId(R.id.et_course_title)).perform(typeText(title));
        onView(withId(R.id.et_course_body)).perform(typeText(body), closeSoftKeyboard());

        onView(withId(R.id.et_course_title)).check(matches(withText(containsString(title))));
        onView(withId(R.id.et_course_body)).check(matches(withText(containsString(body))));
        pressBack();


        int noteIndex = DataManager.getInstance().getNotesSize() - 1;
        NoteInfo noteInfo = DataManager.getInstance().getNotes().get(noteIndex);

        assertEquals(noteInfo.getCourse(), course);
        assertEquals(noteInfo.getTitle(),title);
        assertEquals(noteInfo.getText(), body);






    }
}