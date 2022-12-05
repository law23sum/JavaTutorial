package packagee.classs.clas.iinterfaceabstract.implement;

import packagee.classs.clas.iinterfaceabstract.Interface;
import packagee.classs.clas.iinterfaceabstract.implement.inherit.polymorph.encapsulate.BaseClass;

public abstract class RootClass implements Interface { //  Highest Order Super Class

    public static void main(String[] args) {
        BaseClass obj = new BaseClass();
        Class cls = obj.getClass().getSuperclass();
        System.out.println(cls.getName());
    }
}