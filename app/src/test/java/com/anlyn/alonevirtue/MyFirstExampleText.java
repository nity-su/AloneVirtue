package com.anlyn.alonevirtue;

import androidx.annotation.NonNull;

import com.anlyn.alonevirtue.main.FavoriteObjectItem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class
)
public class MyFirstExampleText {
    @Test
    public void parseBoolean_parsesInvalidFormat_throwsException(){
        FavoriteObjectItem item =new FavoriteObjectItem("path","ITZY");
        assertEquals(item.getName(),"ITZY");
    }


}
