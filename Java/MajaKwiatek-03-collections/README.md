## Zadanie 1: łączenie list.
Należy zaimplementować metodę `uj.wmii.pwj.collections.ListMerger.mergeLists` w taki sposób, aby stworzyła nową, niemodyfikowalną listę zawierającą na przemian elementy z pierwszej i drugiej listy. Metoda nie powinna zwracać nigdy wartości null, tylko pustą listę.

## Zadanie 2: generowanie Stringa w formacie json z kolekcji.

Należy zbudować klasę implemententującą intefejs `uj.wmii.pwj.collections.JsonMapper` tak, aby z dostarczonej mapy budowała String w formacie json.

Metoda `defaultInstance` w interfejsie `JsonMapper` jest fabryką - powinna zwracać instancję stworzonej klasy.

Klucze mapy zawsze będą typu `String`.

Dozwolone type wartości:
* `String` - należy zamienić na json string
* `int`, `short`, `long`, `byte`, `boolean`, `float`, `double` - należy zamienić na odpowiednie typy json (liczbowy, boolean)
* `Map` - należy zamienić na zagnieżdżony obiekt
* `List` - należy zamienić na tablicę

## Zadanie 3: zbudować kreator losowej planszy do gry w statki

Należy napisać kreator do losowania poprawnych plansz do gry w statki (zaimplementować `uj.wmii.pwj.collections.collections.BattleshipGenerator.generateMap`).

Metoda `defaultInstance` w interfejsie `BattleshipGenerator` jest fabryką - powinna zwracać instancję stworzonej klasy.

Plansza do gry w statki jest kwadratem 10x10 (API powinno zwracać `String` o rozmiarze 100. indeksy 0-9: pierwszy wiersz, 10-19 drugi wiersz, itd). Każde pole może zawierać element statku (maszt), oznaczony znakiem `*`, lub zawierać wodę oznaczoną przez `.`.

Statki mogą być 1, 2, 3, lub 4 masztowe. Statek to jedno, lub więcej stykających się bokiem, pole zawierające maszt. Maszty stykające się tylko rogami nie są statkiem.

Przykłady prawidłowych statków (w otoczeniu wody):
```
...
.#.  -> jednomasztowiec
...

......
.##.#. -> dwa dwumasztowce
....#.

.....
..#..  -> trójmasztowiec
.##..

.........
......##.
.####.##. -> dwa czteromasztowce
.........
```

Przykłady nieprawidłowych statków:
```
......
..#...  -> nieprawidłowy dwumasztowiec
...#..
......
.......
...#...
..#.#..  -> nieprawidłowy czteromasztowiec
...#...
```

Prawidłowa plansza zawiera: 4 jednomasztowce, 3 dwumasztowce, 2 trójmasztowce, oraz 1 czteromasztowiec. Pomiędzy statkami musi być przynajmniej jedno pole odstępu (statki nie mogą dotykać się rogami).

Przykładowa prawidłowa plansza:
```
..#.......#......#..#..#........##............##...##................#..##...#...##....#.#.......#..
```

Ta sama plansza z dodatkowe znakami końca linii co 10 znaków dla czytelności:
```
..#.......
#......#..
#..#......
..##......
......##..
.##.......
.........#
..##...#..
.##....#.#
.......#..
```
