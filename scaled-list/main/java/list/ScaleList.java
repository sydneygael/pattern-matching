package list;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Immutable linked list as found in various functional programming languages (Scala, etc.).
 *
 * @param <T> type of the elements held by the list
 */
public sealed interface ScaleList<T> extends Iterable<T> {

    record Nil<T>() implements ScaleList<T> {};

    record CompositeList<T>(T element, ScaleList<T> tail) implements ScaleList<T> {};

    static final Nil<?> NIL = new Nil<>();

    /**
     * Construct a new list by placing a new element at the head and this list as the tail.
     * @param element should not be null
     * @return a new scale list
     */
    default ScaleList<T> cons(T element) {
        return new CompositeList<T>(element, this);
    }

    /**
     * The fold method
     * @param init the object into which the element of the list should be accumulated
     * @param accumulator the accumulator method that takes the accumulated object and the current
     *                    element as parameter and returns the accumulated result
     * @return the final accumulated result
     * @param <O> the type of the accumulator
     */
    default <O> O fold(O init, BiFunction<O, T, O> accumulator) {
        var current = this;
        var result = init;
        while(true) {
            switch (current) {
                case Nil<?>() -> {
                    return result;
                }
                case CompositeList<T>(var head, var tail) -> {
                    result = accumulator.apply(result, head);
                    current = tail;
                }
            }
        }
    }

    /**
     * The switch is also a way to implement a state machine.
     * @return A new reversed list.
     */
    default ScaleList<T> reverse() {
        ScaleList<T> result = ScaleList.of();
        ScaleList<T> current = this;
        while (true) {
            switch (current) {
                case Nil<T>() -> {
                    return result;
                }
                case CompositeList<T>(T head, var tail) -> {
                    result = result.cons(head);
                    current = tail;
                }
            }
        }
    }

    /**
     * The order is guaranteed.
     * @param mapper should not be null and should avoid state
     * @return a new list
     * @param <R> the type of the elements of the returned list
     */
    default <R> ScaleList<R> map(Function<T, R> mapper) {
        return this.fold(ScaleList.<R>of(), (acc, element) -> acc.cons(mapper.apply(element)))
                   .reverse();
    }

    /**
     * Flatten the list content
     * @param mapper the flat map function
     * @return a new list
     * @param <R> the type of the elements of the returned list
     */
    default <R> ScaleList<R> flatMap(Function<T, Iterable<R>> mapper) {
        return this.fold(ScaleList.<R>of(), (acc, toFlat) -> {
            for (var element : mapper.apply(toFlat)) {
                acc = acc.cons(element);
            };
            return acc;
        })
        .reverse();
    }

    /**
     * The filter method
     * @param filter should not be null
     * @return a filtered list
     */
    default ScaleList<T> filter(Function<T, Boolean> filter) {
        return this.fold(ScaleList.<T>of(), (acc, e) -> filter.apply(e) ? acc.cons(e) : acc)
                   .reverse();
    }

    /**
     * Append an element at the end of the list.
     * @param element should not be null
     * @return a new list with the element given as a parameter placed at the tail of this list
     */
    default ScaleList<T> append(T element) {
        return switch (this) {
            case Nil<?>() -> new CompositeList<>(element, this);
            case CompositeList<T>(T head, ScaleList<T> tail) ->
                new CompositeList<>(head, tail.append(element));
        };
    }

    @Override
    default Iterator<T> iterator() {

        class ScaleListIterator<O> implements Iterator<O> {

            private ScaleList<O> innerList;

            private ScaleListIterator(ScaleList<O> innerList) {
                this.innerList = Objects.requireNonNull(innerList);
            }

            @Override
            public boolean hasNext() {
                return !NIL.equals(this.innerList);
            }

            @Override
            public O next() {
                return switch (this.innerList) {
                    case Nil<?>() -> throw new NoSuchElementException();
                    case CompositeList<O>(O head, ScaleList<O> tail) -> {
                        this.innerList = tail;
                        yield head;
                    }
                };
            }
        }

        return new ScaleListIterator<>(this);
    }

    default Stream<T> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this.iterator(), 0), false);
    }

    @SuppressWarnings("unchecked")
    default ScaleList<T> concat(ScaleList<T> other) {
        return other.stream().reduce(this, ScaleList::append, ScaleList::concat);
    }

    @SuppressWarnings("unchecked")
    static <T> ScaleList<T> of() {
        return (ScaleList<T>) NIL;
    }

    @SafeVarargs
    static <T> ScaleList<T> of(T... args) {
        return Arrays
            .stream(args)
            .reduce(ScaleList.<T>of(), ScaleList::append, ScaleList::concat);
    }
}
