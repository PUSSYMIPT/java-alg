package main.java.ru.sbt.mipt;

import java.util.HashMap;

public interface CountMapInt<T> {
    // добавляет элемент в этот контейнер. 
    void add(T o);
    //Возвращает количество добавлений данного элемента
    int getCount(T o);
    //Удаляет элемент и контейнера и возвращает количество его добавлений(до удаления)
    int remove(T o);
    //количество разных элементов   
    int size();
    //Добавить все элементы из source в текущий контейнер, при совпадении ключей, суммировать значения
    void addAll(CountMap<T> source);
    //Вернуть java.util.Map. ключ - добавленный элемент, значение - количество его добавлений
    HashMap<T, Integer> toMap();
    //Тот же самый контракт как и toMap(), только всю информацию записать в destination
    void toMap(HashMap<? super T, Integer> destination);
}