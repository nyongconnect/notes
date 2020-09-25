package com.devthrust.mynotes.Pojo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataManagerTest {

    private static DataManager mInstance;
    @BeforeClass
    public static void classSetUp(){
        mInstance = DataManager.getInstance();
    }
    @Before
    public void setUp(){
        mInstance.getNotes().clear();
    }

    @Test
    public void createNotes() {

        final CourseInfo courseInfo = mInstance.getCourses().get(0);
        final String title = "God is good";
        final String body = "we are living a peaceful life";


        NoteInfo noteInfo = new NoteInfo("courseInfo",title,body);
        int noteIndex = mInstance.createNotes(noteInfo);

        NoteInfo compareNote = mInstance.getNotes().get(noteIndex);


        assertEquals(compareNote.getCourse(),courseInfo);
        assertEquals(compareNote.getTitle(),title);
        assertEquals(compareNote.getText(), body);
    }

    @Test
    public void updateNotes(){

        CourseInfo courseInfo = mInstance.getCourses().get(0);
        String title = "What a world";
        String body = "all we have said so far will be easy to do if we accept our fate";

        NoteInfo noteInfo = new NoteInfo("", title, body);

        int noteIndex = mInstance.updateNotes(noteInfo, 0);

        NoteInfo noteInfo1 = mInstance.getNotes().get(0);

        assertEquals(noteInfo1.getCourse(), courseInfo);
        assertEquals(noteInfo1.getTitle(), title);
        assertEquals(noteInfo1.getText(), noteInfo.getText());
    }
}