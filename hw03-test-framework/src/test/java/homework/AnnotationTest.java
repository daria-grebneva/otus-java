package homework;

import homework.annotations.Test;
import homework.annotations.Before;
import homework.annotations.After;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AnnotationTest {

    @Before
    void init() {
        System.out.println("Before annotation was called on object: " + this);
    }

    @After
    void tearDown() {
        System.out.println("After annotation was called on object: " + this);
    }


    @Test
    void successAssertionTest() {
        String expectedValue = "successAssertionTest";
        String value = "successAssertionTest";

        assertThat(value).isEqualTo(expectedValue);
    }

    @Test
    void failedAssertionTest() {
        String expectedValue = "failedAssertionTest";
        String value = "failedAssertionTest1";

        assertThat(value).isEqualTo(expectedValue);
    }

    @Test
    void exceptionAssertionTest() {
        String expectedValue = "exceptionAssertionTest";
        String value = "exceptionAssertionTest";

        assertThat(value).isEqualTo(expectedValue);
        throw new RuntimeException("Test was failed due to unexpected error");
    }

    void notMarkedWithAnnotationTest() {
        String expectedValue = "notMarkedWithAnnotationTest";
        String value = "notMarkedWithAnnotationTest";

        assertThat(value).isEqualTo(expectedValue);
    }
}