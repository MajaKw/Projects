package uj.wmii.pwj.map2d;

import java.util.*;
import java.util.function.Function;

public class Map2DFactory<R, C, V> implements Map2D<R, C, V> {

    Map<R, Map<C, V>> rowMap;
    Map<C, Map<R, V>> colMap;

    private int size;


    Map2DFactory() {
        rowMap = new HashMap<>();
        colMap = new HashMap<>();
        size = 0;
    }

    @Override
    public V put(R rowKey, C columnKey, V value) {
        if (rowKey == null || columnKey == null) throw new NullPointerException();

        V returnValue = null;
        if (hasKey(rowKey, columnKey)) returnValue = get(rowKey, columnKey);

        rowMap.putIfAbsent(rowKey, new HashMap<>());
        if(!rowMap.get(rowKey).containsKey(columnKey)) ++size;
        rowMap.get(rowKey).put(columnKey, value);
        colMap.putIfAbsent(columnKey, new HashMap<>());
        colMap.get(columnKey).put(rowKey, value);

        return returnValue;
    }

    @Override
    public V get(R rowKey, C columnKey) {
        if (!hasKey(rowKey, columnKey)) return null;
        return rowMap.get(rowKey).get(columnKey);
    }

    @Override
    public V getOrDefault(R rowKey, C columnKey, V defaultValue) {
        if (get(rowKey, columnKey) == null) return defaultValue;
        return get(rowKey, columnKey);
    }

    @Override
    public int size() {
        return size;
    }
    @Override
    public boolean isEmpty(){
        return size == 0;
    }
    @Override
    public  boolean nonEmpty(){
        return !isEmpty();
    }
    @Override
    public  void clear(){
//        valSet.clear();
        rowMap.clear();
        colMap.clear();
        size = 0;
    }

    @Override
    public boolean hasKey(R rowKey, C columnKey) {
        return hasRow(rowKey) && rowMap.get(rowKey).containsKey(columnKey);
    }

    @Override
    public  boolean hasValue(V value){
        return rowMap.values().stream().anyMatch(tuple -> tuple.containsValue(value));
    }

    @Override
    public boolean hasRow(R rowKey) {
        return rowMap.containsKey(rowKey) && !rowMap.get(rowKey).isEmpty();
    }

    @Override
    public boolean hasColumn(C columnKey) {
        return colMap.containsKey(columnKey) && !colMap.get(columnKey).isEmpty();
    }

    @Override
    public Map<C, V> rowView(R rowKey) {
        return  new HashMap<>(rowMap.get(rowKey));
    }

    @Override
    public Map<R, V> columnView(C columnKey) {
        return  new HashMap<>(colMap.get(columnKey));
    }


    public <R2, C2> Map<R2, Map<C2, V>> deepCopy(Map<R2, Map<C2, V>> map){
        Map<R2, Map<C2, V>> copiedMap = new HashMap<>();
        map.forEach((keyTuple, tuple) ->{
            copiedMap.put(keyTuple, new HashMap<>());
            tuple.forEach((key, value)->{
                copiedMap.get(keyTuple).put(key, value);
            });
        });
        return copiedMap;
    }

    @Override
    public Map<R, Map<C, V>> rowMapView() {
        return deepCopy(rowMap);
    }

    @Override
    public Map<C, Map<R, V>> columnMapView() {
        return deepCopy(colMap);
    }


    @Override
    public Map2D<R, C, V> fillMapFromRow(Map<? super C, ? super V> target, R rowKey){
        if(hasRow(rowKey)) target.putAll(rowView(rowKey));
        return this;
    }

    @Override
    public Map2D<R, C, V> fillMapFromColumn(Map<? super R, ? super V> target, C columnKey){
        if(hasColumn(columnKey)) target.putAll(columnView(columnKey));
        return this;
    }
    @Override
    public Map2D<R, C, V>  putAll(Map2D<? extends R, ? extends C, ? extends V> source){
        Map<? extends R, Map<? extends C, ? extends V>> map = new HashMap<>(source.rowMapView());
        map.forEach((rowKey, colTuple)->{
            map.get(rowKey).forEach((colKey, val)->{
                this.put(rowKey, colKey, val);
            });
        });
        return this;
    }

    @Override
    public <R2, C2, V2> Map2D<R2, C2, V2> copyWithConversion(
            Function<? super R, ? extends R2> rowFunction,
            Function<? super C, ? extends C2> columnFunction,
            Function<? super V, ? extends V2> valueFunction){

        Map2D<R2, C2, V2> copiedMap = new Map2DFactory<>();
        rowMap.forEach((keyRow, colTuple)->{
            rowMap.get(keyRow).forEach((keyCol, value)->{
                copiedMap.put(rowFunction.apply(keyRow),columnFunction.apply(keyCol), valueFunction.apply(value));
            });
        });
        return copiedMap;
    }

    @Override
    public Map2D<R, C, V> putAllToRow(Map<? extends C, ? extends V> source, R rowKey) {
        source.forEach((columnKey, val) -> this.put(rowKey, columnKey, val));
        return this;
    }

    @Override
    public Map2D<R, C, V> putAllToColumn(Map<? extends R, ? extends V> source, C columnKey) {
        source.forEach((rowKey, val) -> this.put(rowKey, columnKey, val));
        return this;
    }

    @Override
    public V remove(R rowKey, C columnKey) {
        V returnValue = get(rowKey, columnKey);
        if (hasKey(rowKey,columnKey)) {
            rowMap.get(rowKey).remove(columnKey);
            colMap.get(columnKey).remove(rowKey);
            --size;
        }
        return returnValue;
    }

}
