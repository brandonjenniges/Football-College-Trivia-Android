package com.college.football.trivia.Title;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TitleScreenPresenterTest {

    @Mock
    private TitleScreenView view;
    private TitleScreenPresenter presenter;

    @Before
    public void setup() throws Exception {
        presenter = new TitleScreenPresenter(view);
    }

    @Test
    public void testyTest() throws Exception {
    }
}