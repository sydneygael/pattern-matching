from scaled_list import of


def main() -> None:
    source = of(1, 2, 3, 4, 5)
    doubled = source.map(lambda x: x * 2)
    evens = source.filter(lambda x: x % 2 == 0)
    total = source.fold(0, lambda acc, x: acc + x)

    print("source :", list(source))
    print("map x2 :", list(doubled))
    print("evens  :", list(evens))
    print("sum    :", total)


if __name__ == "__main__":
    main()

