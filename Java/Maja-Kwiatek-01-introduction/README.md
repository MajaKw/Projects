## Zadania do wykładu 1.

Projekt zawiera cztery zadania.

### Zadanie 1 - Hello World
Klasę `HelloWorld`, należy zaimplementować tak, a była uruchamialna jako aplikacja. Należy wyświetlić wszystkie parametry przekazane aplikacji, każdy paramter w nowej linii. W przypadku braku parametrów, należy wyświetlić komunikat `No input parameters provided`.

### Zadanie 2 - Quadratic Equation
W klasie `QuadraticEquation`, metoda `findRoots` należy zaimplementować rozwiązywanie równań kwadratowych w postaci `ax2+bx+c=0`.

W razie odnalezienia dwóch pierwiastków należy zwrócić tablicę o rozmiarze 2 z pierwiastkami. Przy jednem pierwiastku - tablica o rozmiarze 1, przy 0 - tablica o rozmiarze 0.

### Zadanie 3 - Reverser
W klasie `Reverser` należy zaimplementować 2 metody.

Metoda `reverse` powinna zwrócić przekazany parametr z wszystkimi znakami w odwrotnej kolejności, z usunięciem białych znaków z początku i końca Stringa.

Metoda `reverseWords` powinna zwrócić przekazany parametr z wszystkim słowami w odwrotnej kolejności, z usunięciem białych znaków z początku i końca Stringa. Słowa są oddzielone spacją.

### Zadanie 4 - Banner
W klasie `Banner` należy zaimplementować metodę `toBanner` w taki sposób, aby zwróciła tekst w parametrze `input` w formie ASCII-artu, w ASCII-artowym foncie banner, o rozmiarze 7.
Wyjściowy ciąg powinien zawierać tylko duże litery (upper-case). Jeśli parametr wejściowy ma wartość `null`, należy zwrócić pustą tablicę.

Kolejne elementy w zwracanej tablicy powinny zawierać cały wiersz do wyświetlenia, zaczynając od górnego przy indeksie 0, kończąc na dolnym. Metoda `main` zawiera kod, któr umożliwia prawidłowe wyświetlenie wygenerowanego tekstu.

Sam font można znaleźć tu: http://patorjk.com/software/taag/#p=display&f=Banner&t=.

Wysokość tekstu: 7, szerokość (prawie) każdej litery: 7, odstęp pomiędzy literami w słowie: 1, szerokość spacji: 4.

