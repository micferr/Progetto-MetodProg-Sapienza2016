package com.myunit.hw2;

import com.myunit.log.HTMLLogger;
import com.myunit.log.Logger;
import com.myunit.log.LoggerAdapter;
import com.myunit.log.StreamLogger;
import com.myunit.log.gui.GuiLogger;
import com.myunit.test.TestRunner;

import java.lang.reflect.Method;

public class HW2TestSuite {
    /**
     * Specifies SelfTestSuite's running mode
     */
    private enum TestMode {
        STANDARD,
        GUI
    }

    public static void main(String[] args) {
        Class[] testClasses = new Class[] {
                //Homework 1
                com.myunit.hw1.game.board.TestPos.class,
                com.myunit.hw1.game.board.TestPieceModel.class,
                com.myunit.hw1.game.board.TestBoard.class,
                com.myunit.hw1.game.board.TestAction.class,
                com.myunit.hw1.game.board.TestMove.class,
                com.myunit.hw1.game.board.TestGameRuler.class,
                com.myunit.hw1.game.util.TestBoardOct.class,
                com.myunit.hw1.game.util.TestUtils.class,
                com.myunit.hw1.game.util.TestRandPlayer.class,
                com.myunit.hw1.games.TestOthello.class,
                com.myunit.hw1.games.TestOthelloFactory.class,
                //Homework 2
                com.myunit.hw2.game.board.TestAction.class,
                com.myunit.hw2.game.board.TestGameRuler.class,
                com.myunit.hw2.games.TestOthelloFactory.class,
                com.myunit.hw2.games.TestOthello.class
        };
        TestMode testMode = TestMode.GUI;
        switch (testMode) {
            case STANDARD:
                new TestRunner(new HTMLLogger("log.html").openLogAfterTests(true)).run(testClasses);
                break;
            case GUI:
                GuiLogger guiLogger = new GuiLogger(testClasses);
                guiLogger.run(args);
                break;
            default:
                break;
        }
    }
}
