src/main/java/com/tutorial/design/
├─ catalog/
│  ├─ methodchaining/
│  │  ├─ MethodChainingDemo.java
│  │  ├─ Enumeration.java
│  │  ├─ EnumerationBuilder.java
│  │  └─ ExampleBuilder.java
│  ├─ fluentinterface/
│  │  ├─ FluentQuery.java
│  │  ├─ FluentQueryBuilder.java
│  │  └─ FluentDemo.java
│  ├─ dsl/
│  │  ├─ DslContext.java
│  │  ├─ DslBuilder.java
│  │  └─ DslDemo.java
│  ├─ pipeline/
│  │  ├─ Stage.java
│  │  ├─ Pipeline.java
│  │  └─ PipelineDemo.java
│  ├─ specification/
│  │  ├─ Specification.java
│  │  ├─ AndSpecification.java
│  │  ├─ OrSpecification.java
│  │  └─ SpecDemo.java
│  ├─ repository/
│  │  ├─ Repository.java
│  │  ├─ InMemoryRepository.java
│  │  └─ RepositoryDemo.java
│  └─ cqrs/
│     ├─ Command.java
│     ├─ Query.java
│     └─ CqrsDemo.java
└─ patterns/
└─ classs/
├─ behavioral/
│  ├─ interpreter/
│  │  ├─ Expression.java
│  │  ├─ NumberExpression.java
│  │  ├─ AdditionExpression.java
│  │  └─ SubtractionExpression.java
│  ├─ observer/
│  │  ├─ Subject.java
│  │  ├─ Observer.java
│  │  └─ ConcreteObserver.java
│  ├─ strategy/
│  │  ├─ Strategy.java
│  │  ├─ ConcreteStrategyA.java
│  │  └─ ConcreteStrategyB.java
│  ├─ command/
│  │  ├─ Command.java
│  │  ├─ Invoker.java
│  │  └─ Receiver.java
│  ├─ state/
│  │  ├─ State.java
│  │  └─ Context.java
│  ├─ chainofresponsibility/
│  │  ├─ Handler.java
│  │  └─ ConcreteHandler.java
│  ├─ mediator/
│  │  ├─ Mediator.java
│  │  └─ Colleague.java
│  ├─ memento/
│  │  ├─ Memento.java
│  │  ├─ Originator.java
│  │  └─ Caretaker.java
│  ├─ templatemethod/
│  │  ├─ AbstractClass.java
│  │  └─ ConcreteClass.java
│  └─ visitor/
│     ├─ Visitor.java
│     ├─ Element.java
│     └─ ConcreteVisitor.java
├─ creation/
│  ├─ factorymethod/
│  │  ├─ Group.java
│  │  └─ SetsFactory.java
│  ├─ abstractfactory/
│  │  ├─ AbstractFactory.java
│  │  ├─ ConcreteFactoryA.java
│  │  └─ ConcreteFactoryB.java
│  ├─ builder/
│  │  ├─ Builder.java
│  │  ├─ Product.java
│  │  └─ Director.java
│  ├─ prototype/
│  │  ├─ Prototype.java
│  │  └─ ConcretePrototype.java
│  └─ singleton/
│     └─ Singleton.java
└─ structural/
├─ adapter/
│  ├─ Target.java
│  ├─ Adaptee.java
│  └─ Adapter.java
├─ composite/
│  ├─ Component.java
│  ├─ Leaf.java
│  └─ Composite.java
├─ decorator/
│  ├─ Component.java
│  ├─ ConcreteComponent.java
│  └─ Decorator.java
├─ facade/
│  ├─ Facade.java
│  ├─ SubsystemA.java
│  └─ SubsystemB.java
├─ bridge/
│  ├─ Abstraction.java
│  ├─ RefinedAbstraction.java
│  ├─ Implementor.java
│  └─ ConcreteImplementor.java
├─ flyweight/
│  ├─ Flyweight.java
│  ├─ ConcreteFlyweight.java
│  └─ FlyweightFactory.java
└─ proxy/
├─ Subject.java
├─ RealSubject.java
└─ Proxy.java


# catalog
mkdir -p src/main/java/com/tutorial/design/catalog/{methodchaining,fluentinterface,dsl,pipeline,specification,repository,cqrs}
touch src/main/java/com/tutorial/design/catalog/methodchaining/{MethodChainingDemo.java,Enumeration.java,EnumerationBuilder.java,ExampleBuilder.java}
touch src/main/java/com/tutorial/design/catalog/fluentinterface/{FluentQuery.java,FluentQueryBuilder.java,FluentDemo.java}
touch src/main/java/com/tutorial/design/catalog/dsl/{DslContext.java,DslBuilder.java,DslDemo.java}
touch src/main/java/com/tutorial/design/catalog/pipeline/{Stage.java,Pipeline.java,PipelineDemo.java}
touch src/main/java/com/tutorial/design/catalog/specification/{Specification.java,AndSpecification.java,OrSpecification.java,SpecDemo.java}
touch src/main/java/com/tutorial/design/catalog/repository/{Repository.java,InMemoryRepository.java,RepositoryDemo.java}
touch src/main/java/com/tutorial/design/catalog/cqrs/{Command.java,Query.java,CqrsDemo.java}

# patterns – behavioral
mkdir -p src/main/java/com/tutorial/design/patterns/classs/behavioral/{interpreter,observer,strategy,command,state,chainofresponsibility,mediator,memento,templatemethod,visitor}
touch src/main/java/com/tutorial/design/patterns/classs/behavioral/interpreter/{Expression.java,NumberExpression.java,AdditionExpression.java,SubtractionExpression.java}
touch src/main/java/com/tutorial/design/patterns/classs/behavioral/observer/{Subject.java,Observer.java,ConcreteObserver.java}
touch src/main/java/com/tutorial/design/patterns/classs/behavioral/strategy/{Strategy.java,ConcreteStrategyA.java,ConcreteStrategyB.java}
touch src/main/java/com/tutorial/design/patterns/classs/behavioral/command/{Command.java,Invoker.java,Receiver.java}
touch src/main/java/com/tutorial/design/patterns/classs/behavioral/state/{State.java,Context.java}
touch src/main/java/com/tutorial/design/patterns/classs/behavioral/chainofresponsibility/{Handler.java,ConcreteHandler.java}
touch src/main/java/com/tutorial/design/patterns/classs/behavioral/mediator/{Mediator.java,Colleague.java}
touch src/main/java/com/tutorial/design/patterns/classs/behavioral/memento/{Memento.java,Originator.java,Caretaker.java}
touch src/main/java/com/tutorial/design/patterns/classs/behavioral/templatemethod/{AbstractClass.java,ConcreteClass.java}
touch src/main/java/com/tutorial/design/patterns/classs/behavioral/visitor/{Visitor.java,Element.java,ConcreteVisitor.java}

# patterns – creational
mkdir -p src/main/java/com/tutorial/design/patterns/classs/creation/{factorymethod,abstractfactory,builder,prototype,singleton}
touch src/main/java/com/tutorial/design/patterns/classs/creation/factorymethod/{Group.java,SetsFactory.java}
touch src/main/java/com/tutorial/design/patterns/classs/creation/abstractfactory/{AbstractFactory.java,ConcreteFactoryA.java,ConcreteFactoryB.java}
touch src/main/java/com/tutorial/design/patterns/classs/creation/builder/{Builder.java,Product.java,Director.java}
touch src/main/java/com/tutorial/design/patterns/classs/creation/prototype/{Prototype.java,ConcretePrototype.java}
touch src/main/java/com/tutorial/design/patterns/classs/creation/singleton/Singleton.java

# patterns – structural
mkdir -p src/main/java/com/tutorial/design/patterns/classs/structural/{adapter,composite,decorator,facade,bridge,flyweight,proxy}
touch src/main/java/com/tutorial/design/patterns/classs/structural/adapter/{Target.java,Adaptee.java,Adapter.java}
touch src/main/java/com/tutorial/design/patterns/classs/structural/composite/{Component.java,Leaf.java,Composite.java}
touch src/main/java/com/tutorial/design/patterns/classs/structural/decorator/{Component.java,ConcreteComponent.java,Decorator.java}
touch src/main/java/com/tutorial/design/patterns/classs/structural/facade/{Facade.java,SubsystemA.java,SubsystemB.java}
touch src/main/java/com/tutorial/design/patterns/classs/structural/bridge/{Abstraction.java,RefinedAbstraction.java,Implementor.java,ConcreteImplementor.java}
touch src/main/java/com/tutorial/design/patterns/classs/structural/flyweight/{Flyweight.java,ConcreteFlyweight.java,FlyweightFactory.java}
touch src/main/java/com/tutorial/design/patterns/classs/structural/proxy/{Subject.java,RealSubject.java,Proxy.java}
