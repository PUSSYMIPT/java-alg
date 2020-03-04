package main.java.ru.sbt.mipt;

import java.util.HashMap;

public class CountMap<T> implements CountMapInt<T> {

    private HashMap<T, Integer> map = new HashMap<>();

    @Override
    public void add(T o) {
        if (map.containsKey(o)) {
            Integer old_value = map.get(o);
            map.replace(o, old_value+1);
        }
        else {
            map.put(o, 1);
        }
    }

    @Override
    public int getCount(T o) {
        return map.get(o);
    }

    @Override
    public int remove(T o) {
        Integer old_value = map.get(o);
        if (old_value > 1) {
            map.replace(o, old_value - 1);
            return old_value;
        } else {
            return map.remove(o);  // probably 0
        }
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public HashMap<T, Integer> toMap() {
        return new HashMap<>(map);
    }

    @Override
    public void toMap(HashMap<? super T, Integer> destination) {
        destination = new HashMap<>(map);
    }

    @Override
    public void addAll(CountMap<T> source) {
        for (T key : source.toMap().keySet()) {
            addToKey(key, source.getCount(key));
        }
    }

    private void addToKey(T key,Integer num_to_add) {
        if (map.containsKey(key)) {
            Integer old_value = map.get(key);
            map.replace(key, old_value + num_to_add);
        }
        else {
            map.put(key, num_to_add);
        }
    }

}