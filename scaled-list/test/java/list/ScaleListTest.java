package list;

import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ScaleListTest {

    @Test
    void consShouldAddElementAtTheHeadOfTheList() {
        // Given
        ScaleList<Integer> listOfIntegers = ScaleList.of();

        // When
        for (int i = 0; i < 10; i++) {
            listOfIntegers = listOfIntegers.cons(i);
        }

        // Then
        Assertions.assertEquals(ScaleList.of(9, 8, 7, 6, 5, 4, 3, 2, 1, 0), listOfIntegers);
    }

    @Test
    void reversingAListShouldProduceAnotherList() {
        // Given
        var listOfIntegers = ScaleList.of(5, 4, 3, 2, 1, 0);

        // When
        var reversedList = listOfIntegers.reverse();

        // Then
        Assertions.assertNotSame(listOfIntegers, reversedList);
        Assertions.assertEquals(ScaleList.of(0, 1, 2, 3, 4, 5), reversedList);
    }

    @Test
    void flatMapAListOfListsShouldProduceAListOfElement() {
        // Given
        var listOfLists = ScaleList.of(ScaleList.of(1), ScaleList.of(2, 3), ScaleList.of(4, 5, 6));

        // When
        var flattenedList = listOfLists.flatMap(l -> l);

        // Then
        Assertions.assertEquals(ScaleList.of(1, 2, 3, 4, 5, 6), flattenedList);
    }

    @Test
    void mapShouldConvertAllElementOfAListToAnotherType() {
        // Given
        var listOfIntegers = ScaleList.<Integer>of(1, 2, 3);

        // When
        var listOfStrings = listOfIntegers.map(String::valueOf);

        // Then
        Assertions.assertEquals(ScaleList.of("1", "2", "3"), listOfStrings);
    }

    @Test
    void filteringAListOfIntegersElementShouldResultInAnotherListWithLessElements() {
        // Given
        var listOfIntegers = IntStream
            .rangeClosed(0, 10)
            .mapToObj(ScaleList::of)
            .reduce(ScaleList::concat)
            .orElseThrow();

        // When
        var filteredList = listOfIntegers.filter(i -> i % 2 == 0);

        // Then
        var expectedList = listOfIntegers
            .stream()
            .filter(i -> i % 2 == 0)
            .map(ScaleList::of)
            .reduce(ScaleList::concat)
            .orElseThrow();
        Assertions.assertEquals(expectedList, filteredList);
    }

    @Test
    void filteringOutEntireListShouldReturnAEmptyList() {
        // Given
        var listOfIntegersUnder10 =  IntStream
            .range(0, 10)
            .mapToObj(ScaleList::of)
            .reduce(ScaleList::concat)
            .orElseThrow();

        // When
        var filteredList = listOfIntegersUnder10.filter(i -> i > 10);

        // Then
        Assertions.assertEquals(ScaleList.<Integer>of(), filteredList);
    }

    @Test
    void itShouldBePossibleToSumAllElementsOfAListByUsingFold() {
        // Given
        var listOfIntegersUnder10 =  IntStream
            .range(0, 10)
            .mapToObj(ScaleList::of)
            .reduce(ScaleList::concat)
            .orElseThrow();

        // When
        var sum = listOfIntegersUnder10.fold(0, Integer::sum);

        // Then
        var expected = IntStream.range(0, 10).sum();
        Assertions.assertEquals(expected, sum);
    }
}