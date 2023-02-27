package ru.otus;

class Demo {
    public static void main(String[] args) {
        TestLoggingInterface testLoggingClass = LogRunner.createTestLoggingClass();

        testLoggingClass.calculation(6);
        testLoggingClass.calculation(6, 8);
        testLoggingClass.calculation(6, 8, "testData");
        testLoggingClass.calculation(6, 8, "testData1", "testData2");
        testLoggingClass.newCalculation(6);
    }
}