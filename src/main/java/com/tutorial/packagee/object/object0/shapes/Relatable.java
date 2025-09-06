package com.tutorial.packagee.object.object0.shapes;

public interface Relatable {
    int isLargerThan(Relatable other);

    default Object findLargest(Object object1, Object object2) {
        Relatable obj1 = (Relatable) object1;
        Relatable obj2 = (Relatable) object2;
        if ((obj1).isLargerThan(obj2) > 0) return object1;
        else return object2;
    }

    default Object findSmallest(Object object1, Object object2) {
        Relatable obj1 = (Relatable)object1;
        Relatable obj2 = (Relatable)object2;
        if ((obj1).isLargerThan(obj2) < 0)
            return object1;
        else
            return object2;
    }

    default boolean isEqual(Object object1, Object object2) {
        Relatable obj1 = (Relatable)object1;
        Relatable obj2 = (Relatable)object2;
        return (obj1).isLargerThan(obj2) == 0;
    }
}

