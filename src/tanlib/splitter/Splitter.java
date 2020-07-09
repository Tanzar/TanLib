/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanlib.splitter;

import java.util.ArrayList;
import java.util.List;
import tanlib.random.SeedRandomizer;

/**
 *
 * @author Tanzar
 */
public class Splitter {
    private List<int[]> list;
    private SeedRandomizer randomizer;
    
    public Splitter(SeedRandomizer randomizer){
        this.randomizer = randomizer;
    }
    
    /**
     * Odległość mozna zapisać jako równanie 
     * space = suma (i = min do max) ai * i
     * gdzie
     * ai to ilośc wystąpień wartości i
     * i to liczby całkowite z zakresu <min, max>
     * 
     * zmienne array i arrayPattern reprezentują wartości ai, np 
     * dla min-max 3-6 rozmiary tablic będą równe 4, pola będą representować ilość wystąpień wartości 3, 4, 5, 6
     * 
     * metoda recur wykonuje obliczenia, poprzez "tworzenie drzewa"
     * u szczytu drzewa jest zero oraz startowa wartość space
     * następnie "tworzony" jest poziom poprzez wyznaczenie ile możliwych wystąpień wartości minimalnej może się zmieścić w dostępnej odległości
     * dla kazdej takiej wartości tworzona jest "gałąź drzewa" i zapamiętywana jest pozostała odległość
     * następnie ponownie tworzone jest "drzewo" lecz:
     * zamiast zera brana jest wartość z gałęzi
     * zamiast początkowej artości space brana jest pozostała wartość space
     * a poziom jest dla wartości minimalnej zwiększonej o 1 co każdy poziom (odpowiednio dla poziomu 0 nie ma, dla 1 jest min, dla 2 jest min + 1, dla 3 jest min + 2, ...)
     * Przykład drzewa ( pola w formacie "(ai : remainingSpace)" )
     * space 10, min 4, max 6
     * wartość poziomu (a) }        drzewo
     *                     }        0:10
     *                     }    |-----------------------------------|-----------------------|----|
     * 4                   }  0:10                                 1:6                     2:2  3:-2
     *                     }    |---------------|------------|      |---------|-------|     
     * 5                   }  0:10             1:5          2:0    0:6       1:1     2:-4
     *                     }    |----|----|     |--------|          |----|    |----|    
     * 6                   }  0:10  1:4  2:-2  0:5      1:-1       0:6  1:0  0:1  1:-5
     * 
     * dla tego drzewa układy:
     * 0:2:0
     * 1:0:1
     * są prawidłowym rozwiązaniem
     * jeśli w ostatnim poziomie jest pole dla którego remainingSpace jest różne od zera to nieprawidłowy układ
     * jeśli na jakimkolwiek poziomie jest remaining space mniejszy od następnego poziomu to nieprawidłowy układ
     * 
     * @param space
     * @param min
     * @param max
     * @return 
     */
    public int[] split(int space, int min, int max){
        this.list = new ArrayList<int[]>();
        int a = min;
        int[] arrayPattern = emptyIntArray(max - min + 1); //array which 
        int numberOfSegments = 1; 
        if(space > 2 * (min + max)){
            numberOfSegments = this.calculateNumberOfSegments(space, min, max);
        }
        int spaceSegmentSize = space/numberOfSegments;
        recur(a, max, 0, spaceSegmentSize, arrayPattern);
        int[] array = compressParts(min, max, numberOfSegments);
        if(array != null){
            array = calculateResultArray(min, array);
        }
        return array;
    }
    
    private int calculateNumberOfSegments(int space, int min, int max){
        boolean end = false;
        int number = (int) Math.floor(space/(2 * (max + min)));
        while(!end){
            if(space % number == 0){
                end = true;
            }
            else{
                number--;
                if(number == 0){
                    end = true;
                }
           }
        }
        return number;
    }
    
    /**
     * metoda rozdziela podział dużej odległości na mniejsze.
     * Etapy:
     * 1) wyznaczenie na ile maksymalnie części można podzielić odległość
     * 2) obliczenie pozostałej części
     * 3) jeśli pozostała przestrzeń pozwala umieścić aktualną wartość a (aktualnie sprawdzaną
     * 
     * 
     * 
     * @param a value from min to max
     * @param amax end value, should be sat to max
     * @param index index on previous array
     * @param space space to split, after initial this will be parts to split
     * @param previous previous state
     */
    private void recur(int a, int amax, int index, int space, int[] previous){
        int limit = (int) Math.floor(space/a);
        for(int i = limit; i >= 0; i--){
            int[] current = new int[previous.length];
            for(int j = 0; j < current.length; j++){
                current[j] = previous[j];
                if(j == index){
                    current[j] = i;
                }
            }
            int remainSpace = space - a * i;
            if(remainSpace > a && a + 1 <= amax){
                recur(a + 1, amax, index + 1, remainSpace, current);
            }
            if(remainSpace == 0){
                this.list.add(current);
            }
        }
    }
    
    private int[] compressParts(int min, int max, int numberOfSegments){
        int[] array = emptyIntArray(max - min + 1);
        if(this.list.size() > 0){
            for(int i = 0; i < numberOfSegments; i++){
                int index = this.randomizer.random(0, this.list.size() - 1);
                int[] tmpArray = list.get(index);
                for(int j = 0; j < tmpArray.length; j++){
                    array[j] += tmpArray[j];
                }
            }
        }
        else{
            array = null;
        }
        return array;
    }
    
    private int[] calculateResultArray(int min, int[] compressedResults){
        int[] array = copyArray(compressedResults);
        //change array to list of parts
        IntegerList listTmp = new IntegerList();
        for(int i = 0; i < array.length; i++){
            while(array[i] > 0){
                listTmp.add(i + min);
                array[i]--;
            }
        }
        array = new int[listTmp.size()];
        for(int i = 0; i < array.length; i++){
            int index = this.randomizer.random(0, listTmp.size() - 1);
            array[i] = listTmp.getAndRemove(index);
        }
        return array;
    }
    
    private int[] emptyIntArray(int size){
        int[] array = new int[size];
        for(int i = 0; i < array.length; i++){
            array[i] = 0;
        }
        return array;
    }
    
    private int[] copyArray(int[] array){
        int[] copy = new int[array.length];
        for(int i = 0; i < array.length; i++){
            copy[i] = array[i];
        }
        return copy;
    }
}
