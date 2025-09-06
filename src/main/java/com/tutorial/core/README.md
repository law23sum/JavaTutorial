core/
├─ datastructure/
│  ├─ array/
│  ├─ linkedlist/
│  └─ stackqueue/
├─ algorithms/
│  ├─ sorting/
│  │  ├─ BubbleSort.java
│  │  ├─ QuickSort.java
│  │  └─ MergeSort.java
│  └─ searching/
│     ├─ LinearSearch.java
│     └─ BinarySearch.java
├─ oop/
│  ├─ InheritanceDemo.java
│  ├─ PolymorphismDemo.java
│  ├─ EncapsulationDemo.java
│  └─ AbstractionDemo.java
├─ exceptions/
│  ├─ ExceptionHandlingDemo.java
│  ├─ CustomException.java
│  └─ CheckedUncheckedDemo.java
├─ io/
│  ├─ FileReadWriteDemo.java
│  ├─ SerializationDemo.java
│  └─ NioDemo.java
├─ concurrency/
│  ├─ ThreadDemo.java
│  ├─ ExecutorDemo.java
│  ├─ SynchronizationDemo.java
│  └─ FutureDemo.java
├─ generics/
│  ├─ GenericClass.java
│  ├─ GenericMethod.java
│  └─ CollectionDemo.java
├─ jvm/
│  ├─ MemoryLayoutDemo.java
│  ├─ ClassLoaderDemo.java
│  └─ GarbageCollectionDemo.java
├─ functional/
│  ├─ LambdaDemo.java
│  ├─ StreamDemo.java
│  └─ FunctionalInterfacesDemo.java
└─ networking/
├─ SocketClient.java
├─ SocketServer.java
└─ JdbcDemo.java


# Core Java project structure under com/tutorial/core
mkdir -p src/main/java/com/tutorial/core/{datastructure/{array,linkedlist,stackqueue},algorithms/{sorting,searching},oop,exceptions,io,concurrency,generics,jvm,functional,networking}

# Data Structures
# touch src/main/java/com/tutorial/core/datastructure/array/.keep
# touch src/main/java/com/tutorial/core/datastructure/linkedlist/.keep
# touch src/main/java/com/tutorial/core/datastructure/stackqueue/.keep

# Algorithms - Sorting
touch src/main/java/com/tutorial/core/algorithms/sorting/{BubbleSort.java,QuickSort.java,MergeSort.java}
# Algorithms - Searching
touch src/main/java/com/tutorial/core/algorithms/searching/{LinearSearch.java,BinarySearch.java}

# OOP
touch src/main/java/com/tutorial/core/oop/{InheritanceDemo.java,PolymorphismDemo.java,EncapsulationDemo.java,AbstractionDemo.java}

# Exceptions
touch src/main/java/com/tutorial/core/exceptions/{ExceptionHandlingDemo.java,CustomException.java,CheckedUncheckedDemo.java}

# IO
touch src/main/java/com/tutorial/core/io/{FileReadWriteDemo.java,SerializationDemo.java,NioDemo.java}

# Concurrency
touch src/main/java/com/tutorial/core/concurrency/{ThreadDemo.java,ExecutorDemo.java,SynchronizationDemo.java,FutureDemo.java}

# Generics
touch src/main/java/com/tutorial/core/generics/{GenericClass.java,GenericMethod.java,CollectionDemo.java}

# JVM & Memory
touch src/main/java/com/tutorial/core/jvm/{MemoryLayoutDemo.java,ClassLoaderDemo.java,GarbageCollectionDemo.java}

# Functional Programming
touch src/main/java/com/tutorial/core/functional/{LambdaDemo.java,StreamDemo.java,FunctionalInterfacesDemo.java}

# Networking & JDBC
touch src/main/java/com/tutorial/core/networking/{SocketClient.java,SocketServer.java,JdbcDemo.java}
