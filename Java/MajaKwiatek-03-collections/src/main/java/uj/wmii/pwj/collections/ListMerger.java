package uj.wmii.pwj.collections;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ListMerger {
    public static List<Object> mergeLists(List<?> l1, List<?> l2) {
        LinkedList<Object> mergedList = new LinkedList<>();

        if(l1 == null && l2 == null)  return Collections.unmodifiableList(mergedList);
        if(l1 == null)  return Collections.unmodifiableList(l2);
        if(l2 == null)  return Collections.unmodifiableList(l1);

        Iterator<?> iter1 = l1.iterator();
        Iterator<?> iter2 = l2.iterator();

        while(iter1.hasNext() || iter2.hasNext()){
            if(iter1.hasNext()) mergedList.addLast(iter1.next());
            if(iter2.hasNext()) mergedList.addLast(iter2.next());
        }
        return Collections.unmodifiableList(mergedList);
    }

}
