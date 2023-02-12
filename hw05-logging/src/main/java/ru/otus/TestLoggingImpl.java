package ru.otus;

import ru.otus.annotations.Log;

class TestLoggingImpl implements TestLoggingInterface {
    @Log
    @Override
    public void calculation(int param) {};

    @Log
    @Override
    public void calculation(int param1, int param2) {};

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {};

    @Override
    public void calculation(int param1, int param2, String param3, String param4) {};

    @Override
    public void newCalculation(int param) {};
}