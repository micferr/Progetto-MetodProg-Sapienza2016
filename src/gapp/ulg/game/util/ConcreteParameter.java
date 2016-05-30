package gapp.ulg.game.util;

import gapp.ulg.game.Param;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConcreteParameter<T> implements Param<T> {
    protected String name;
    protected String prompt;
    protected T value;
    protected List<T> values;

    public ConcreteParameter(String name, String prompt, T[] values, T defaultValue) {
        this.name = name;
        this.prompt = prompt;
        this.value = defaultValue;
        this.values = Collections.unmodifiableList(Arrays.asList(values));
        if (!this.values.contains(this.value)) {
            // Todo throw IllegalArgument?
        }
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String prompt() {
        return prompt;
    }

    @Override
    public List values() {
        return values;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set(Object v) {
        if (values.contains(v)) {
            this.value = (T)v;
        } else {
            throw new IllegalArgumentException("Invalid value for parameter " + name);
        }
    }

    @Override
    public T get() {
        return this.value;
    }
}
