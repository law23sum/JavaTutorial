package com.tutorial.packagee.classs.clas.abstractt.inherit.polymorph;

import com.tutorial.packagee.classs.clas.abstractt.inherit.SuperClass;

import java.util.Arrays;
import java.util.List;

public final class Polymorphismm {
    private Polymorphismm() {
    }

    public static void runDemo() {
        System.out.println("\n=== POLYMORPHISM DEMO START ===\n");

        // 1) Same SUPER reference, different SUB behavior (overriding / dynamic dispatch)
        SubClass superRef = new SubClass.LoggingSubClass();
        superRef.change(3);          // overloading picks change(int)
        superRef.displayCurrentState();           // resolves to LoggingSubClass

        superRef = new SubClass.ValidatingSubClass();
        superRef.change(-10);        // triggers validation override
        superRef.displayCurrentState();           // resolves to ValidatingSubClass

        // 2) Polymorphic collection of a SUPER type
        List<SuperClass> mixed = Arrays.asList(new SubClass.LoggingSubClass(), new SubClass.ValidatingSubClass());

        System.out.println("\n-- For-each over List<SuperClass> (dynamic dispatch in loop) --");
        for (SuperClass sc : mixed) {
            SubClass sub = (SubClass) sc;
            sub.change(5, true);                 // overloading picks (int, boolean)
            sc.displayCurrentState();            // overridden method per actual object
        }

        // 3) Interface-based polymorphism (no knowledge of concrete type needed)
        List<SubClass> viaInterface = Arrays.asList(new SubClass.LoggingSubClass(), new SubClass.ValidatingSubClass());

        System.out.println("\n-- Calls via Stateful interface --");
        for (SubClass s : viaInterface) {
            s.incrementState();
            s.displayCurrentState();
        }

        // 4) Teaching-only: instanceof + cast to reach subclass-specific API
        System.out.println("\n-- instanceof + cast (for demonstration only) --");
        SuperClass maybeLog = new SubClass.LoggingSubClass();
        if (maybeLog instanceof SubClass.LoggingSubClass log) {
            log.setTag("demo-tag");
        }

        SuperClass maybeValidate = new SubClass.ValidatingSubClass();
        if (maybeValidate instanceof SubClass.ValidatingSubClass val) {
            val.setAllowNegative(true);          // relax guard
            maybeValidate.change(-7);
            maybeValidate.displayCurrentState();
        }

        System.out.println("\n=== POLYMORPHISM DEMO END ===\n");
    }
}

