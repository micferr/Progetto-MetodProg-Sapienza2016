package com.myunit.hw2.games;

import com.myunit.test.Sorted;
import com.myunit.test.Test;
import gapp.ulg.game.Param;
import gapp.ulg.games.OthelloFactory;

import java.util.Arrays;
import java.util.List;

import static com.myunit.assertion.Assert.*;

@SuppressWarnings("unused")
public class TestOthelloFactory {
    @Test
    @Sorted(0)
    public void params_NumeroParametri() {
        List<Param<?>> params = new OthelloFactory().params();
        assertEquals(
                params.size(),
                2,
                "params() ritorna una lista di " + params.size() + "parametri anziché 2"
        );
    }

    @Test
    @Sorted(10)
    public void params_PrimoParametroRisultatiCorretti() {
        testParameter(
                0,
                "Time",
                "Time limit for a move",
                new String[] {"No limit","1s","2s","3s","5s","10s","20s","30s","1m","2m","5m"},
                "No limit"
        );
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(20)
    @SuppressWarnings("unchecked")
    public void params_PrimoParametroValuesImmodificabile() {
        ((List<String>)new OthelloFactory().params().get(0).values()).add("0");
    }

    @Test
    @Sorted(30)
    public void params_SecondoParametroRisultatiCorretti() {
        testParameter(
                1,
                "Board",
                "Board size",
                new String[] {"6x6","8x8","10x10","12x12"},
                "8x8"
        );
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(40)
    @SuppressWarnings("unchecked")
    public void params_SecondoParametroValuesImmodificabile() {
        ((List<String>)new OthelloFactory().params().get(1).values()).add("8x8");
    }

    private void testParameter(int paramIndex, String name, String prompt, String[] values, String defaultValue) {
        List<Param<?>> params = new OthelloFactory().params();
        assertEquals(
                params.get(paramIndex).name(), name,
                "params.get(" + paramIndex + ").name() non ritorna \"" + name + "\" ma " + params.get(paramIndex).name()
        );
        assertEquals(
                params.get(paramIndex).prompt(), prompt,
                "params.get(" + paramIndex + ").prompt() ritorna " + params.get(paramIndex).prompt()
        );
        String values2 = "[";
        for (Object val : params.get(paramIndex).values()) {
            values2 += " " + val + " ";
        }
        values2 += "]";
        assertEquals(
                params.get(paramIndex).values(),
                Arrays.asList(values),
                "params.get(" + paramIndex + ").values() errato, ritorna " + values2
        );
        String defaultParam1Value = (String)new OthelloFactory().params().get(paramIndex).get();
        assertEquals(
                defaultParam1Value,
                defaultValue,
                "params.get(" + paramIndex + ").get() ha default errato: " + defaultParam1Value
        );
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(50)
    public void params_TimeImpostatoNonValido1() {
        OthelloFactory oF = new OthelloFactory();
        oF.params().get(0).set(10);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(60)
    public void params_TimeImpostatoNonValido2() {
        OthelloFactory oF = new OthelloFactory();
        oF.params().get(0).set("10");
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(70)
    public void params_BoardImpostatoNonValido1() {
        OthelloFactory oF = new OthelloFactory();
        oF.params().get(1).set(8);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(80)
    public void params_BoardImpostatoNonValido2() {
        OthelloFactory oF = new OthelloFactory();
        oF.params().get(1).set("8");
    }

    @Test
    @Sorted(90)
    public void params_TimeAggiornatoCorrettamente() {
        OthelloFactory oF = new OthelloFactory();
        oF.params().get(0).set("1s");
        assertEquals(
                oF.params().get(0).get(),
                "1s",
                "First parameter should be \"1s\" but is: \"" + oF.params().get(0).get() +"\""
        );
        oF.params().get(0).set("2m");
        assertEquals(
                oF.params().get(0).get(),
                "2m",
                "First parameter should be \"2m\" but is: \"" + oF.params().get(0).get() + "\""
        );
    }

    @Test
    @Sorted(100)
    public void params_BoardAggiornatoCorrettamente() {
        OthelloFactory oF = new OthelloFactory();
        oF.params().get(1).set("6x6");
        assertEquals(
                oF.params().get(1).get(),
                "6x6"
        );
        oF.params().get(1).set("12x12");
        assertEquals(
                oF.params().get(1).get(),
                "12x12"
        );
    }
}
