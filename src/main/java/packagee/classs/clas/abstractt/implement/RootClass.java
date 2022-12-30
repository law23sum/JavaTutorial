// Java Project
// Java Topic (Abstract Class & Interface)
// Java Notes
//               Abstraction Enables less complex designs, code duplication, & more reuse ability, increase security.
//               Abstract & Interfaces cannot be instantiated
//               Abstract Class include abstract & non-abstract method
//                              does not support multiple inheritance
//                              implemented by keyword extend
//                              types include static, non-static, final, non-final vars
//                              class members include private, protected, etc.
//                              requires usage of it be extended and its method be implemented
//                              constructors & static methods are allowed
//                              final methods enforcing subclasses no modifications on the methods body
//                              abstract method is declared without an implementation (no braces or semicolon)
//                              allows for data member, constructor, abstract method, non abstract method, main() method
//
//               Interfaces     include only abstract methods
//                              support multiple inheritance
//                              interface is implemented by key word implemented
//                              interface can extend another Java interface(s)
//                              group of related abstract methods with empty bodies
//                              methods in Interface are public and abstract
//                              class can implement more than one interface
//                              multiple inheritance in case of class not possible.
//                              work around by using interface it can achieve multiple inheritance

package packagee.classs.clas.abstractt.implement;

import packagee.classs.clas.abstractt.Interface;
import packagee.classs.clas.abstractt.implement.inherit.polymorph.encapsulate.BaseClass;


public abstract class RootClass implements Interface { //  Highest Order Super Class

    public static void main(String[] args) {
        BaseClass obj = new BaseClass();
        Class cls = obj.getClass().getSuperclass();
        System.out.println("Package Name " + cls.getName());
    }
}