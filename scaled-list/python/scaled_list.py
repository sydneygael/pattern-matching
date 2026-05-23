from __future__ import annotations

from dataclasses import dataclass
from typing import Callable, Generic, Iterable, Iterator, Optional, TypeVar

T = TypeVar("T")
R = TypeVar("R")
O = TypeVar("O")


class ScaleList(Generic[T]):
    def cons(self, element: T) -> ScaleList[T]:
        return CompositeList(element=element, tail=self)

    def fold(self, init: O, accumulator: Callable[[O, T], O]) -> O:
        current: ScaleList[T] = self
        result: O = init
        while True:
            if isinstance(current, Nil):
                return result
            if isinstance(current, CompositeList):
                result = accumulator(result, current.element)
                current = current.tail
                continue
            raise TypeError(f"Unknown list node: {type(current)}")

    def reverse(self) -> ScaleList[T]:
        result: ScaleList[T] = of()
        current: ScaleList[T] = self
        while True:
            if isinstance(current, Nil):
                return result
            if isinstance(current, CompositeList):
                result = result.cons(current.element)
                current = current.tail
                continue
            raise TypeError(f"Unknown list node: {type(current)}")

    def map(self, mapper: Callable[[T], R]) -> ScaleList[R]:
        mapped = self.fold(of(), lambda acc, element: acc.cons(mapper(element)))
        return mapped.reverse()

    def flat_map(self, mapper: Callable[[T], Iterable[R]]) -> ScaleList[R]:
        def acc_fn(acc: ScaleList[R], to_flat: T) -> ScaleList[R]:
            for element in mapper(to_flat):
                acc = acc.cons(element)
            return acc

        return self.fold(of(), acc_fn).reverse()

    def filter(self, predicate: Callable[[T], bool]) -> ScaleList[T]:
        filtered = self.fold(of(), lambda acc, e: acc.cons(e) if predicate(e) else acc)
        return filtered.reverse()

    def append(self, element: T) -> ScaleList[T]:
        if isinstance(self, Nil):
            return CompositeList(element=element, tail=self)
        if isinstance(self, CompositeList):
            return CompositeList(element=self.element, tail=self.tail.append(element))
        raise TypeError(f"Unknown list node: {type(self)}")

    def concat(self, other: ScaleList[T]) -> ScaleList[T]:
        result: ScaleList[T] = self
        for element in other:
            result = result.append(element)
        return result

    def __iter__(self) -> Iterator[T]:
        current: ScaleList[T] = self
        while isinstance(current, CompositeList):
            yield current.element
            current = current.tail

    def __eq__(self, other: object) -> bool:
        if not isinstance(other, ScaleList):
            return False
        return list(self) == list(other)

    def __repr__(self) -> str:
        return f"ScaleList({list(self)!r})"


@dataclass(frozen=True)
class Nil(ScaleList[T]):
    pass


@dataclass(frozen=True)
class CompositeList(ScaleList[T]):
    element: T
    tail: ScaleList[T]


NIL: Nil[object] = Nil()


def of(*args: T) -> ScaleList[T]:
    result: ScaleList[T] = NIL  # type: ignore[assignment]
    for element in args:
        result = result.append(element)
    return result

