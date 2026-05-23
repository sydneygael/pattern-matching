import unittest

from scaled_list import ScaleList, of


class ScaleListTest(unittest.TestCase):
    def test_cons_should_add_element_at_head(self) -> None:
        lst: ScaleList[int] = of()
        for i in range(10):
            lst = lst.cons(i)
        self.assertEqual(of(9, 8, 7, 6, 5, 4, 3, 2, 1, 0), lst)

    def test_reversing_list(self) -> None:
        list_of_integers = of(5, 4, 3, 2, 1, 0)
        reversed_list = list_of_integers.reverse()
        self.assertEqual(of(0, 1, 2, 3, 4, 5), reversed_list)

    def test_flat_map_list_of_lists(self) -> None:
        list_of_lists = of(of(1), of(2, 3), of(4, 5, 6))
        flattened = list_of_lists.flat_map(lambda l: l)
        self.assertEqual(of(1, 2, 3, 4, 5, 6), flattened)

    def test_map_should_convert_all_elements(self) -> None:
        list_of_integers = of(1, 2, 3)
        list_of_strings = list_of_integers.map(str)
        self.assertEqual(of("1", "2", "3"), list_of_strings)

    def test_filter_even_elements(self) -> None:
        list_of_integers = of(*range(0, 11))
        filtered = list_of_integers.filter(lambda i: i % 2 == 0)
        self.assertEqual(of(0, 2, 4, 6, 8, 10), filtered)

    def test_filter_all_out_should_return_empty(self) -> None:
        list_of_integers = of(*range(0, 10))
        filtered = list_of_integers.filter(lambda i: i > 10)
        self.assertEqual(of(), filtered)

    def test_fold_sum(self) -> None:
        list_of_integers = of(*range(0, 10))
        summed = list_of_integers.fold(0, lambda acc, e: acc + e)
        self.assertEqual(sum(range(0, 10)), summed)


if __name__ == "__main__":
    unittest.main()

