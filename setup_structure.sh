#!/usr/bin/env bash

# --- Documentation ---
mkdir -p documentation
mkdir -p documentation
touch documentation/ConceptMap.md
touch documentation/Foundations.md
touch documentation/DataStructures.md
touch documentation/OOP.md
touch documentation/SystemDesign.md

# --- Core Foundations (already mostly present, but safe) ---
mkdir -p src/main/java/com/tutorial/foundation/bitwise

# --- Data Structures & Algorithms ---
mkdir -p src/main/java/com/tutorial/core/algorithms/searching
mkdir -p src/main/java/com/tutorial/core/algorithms/sorting
mkdir -p src/main/java/com/tutorial/core/algorithms/graph
mkdir -p src/main/java/com/tutorial/core/algorithms/dp

mkdir -p src/main/java/com/tutorial/core/datastructure/primitivestructure/array
mkdir -p src/main/java/com/tutorial/core/datastructure/primitivestructure/strings
mkdir -p src/main/java/com/tutorial/core/datastructure/collections/lists/arraylist
mkdir -p src/main/java/com/tutorial/core/datastructure/collections/lists/linkedlist
mkdir -p src/main/java/com/tutorial/core/datastructure/collections/sets
mkdir -p src/main/java/com/tutorial/core/datastructure/collections/maps
mkdir -p src/main/java/com/tutorial/core/datastructure/collections/queues/stackqueue
mkdir -p src/main/java/com/tutorial/core/datastructure/advdatastructure/{trees,heaps,graphs,hashtables,tries}

# Example new algorithm/demo stubs
touch src/main/java/com/tutorial/core/algorithms/graph/Bfs.java
touch src/main/java/com/tutorial/core/algorithms/graph/Dfs.java
touch src/main/java/com/tutorial/core/algorithms/graph/Dijkstra.java
touch src/main/java/com/tutorial/core/algorithms/graph/AStar.java
touch src/main/java/com/tutorial/core/algorithms/dp/Knapsack.java
touch src/main/java/com/tutorial/core/algorithms/dp/FibonacciDP.java
touch src/main/java/com/tutorial/core/algorithms/dp/LIS.java

# --- Execution & Memory Model ---
mkdir -p src/main/java/com/library/jvm
mkdir -p src/main/java/com/library/concurrency

touch src/main/java/com/library/jvm/JVMOverview.java
touch src/main/java/com/library/jvm/MemoryLayout.java
touch src/main/java/com/library/jvm/GarbageCollection.java
touch src/main/java/com/library/jvm/PerformanceTuning.java

touch src/main/java/com/library/concurrency/ThreadBasics.java
touch src/main/java/com/library/concurrency/LocksAndSync.java
touch src/main/java/com/library/concurrency/MemoryVisibility.java

# --- Core Libraries & Advanced Features ---
mkdir -p src/main/java/com/library/generics
touch src/main/java/com/library/generics/GenericsIntro.java

mkdir -p src/main/java/com/tutorial/advancedlanguage
touch src/main/java/com/tutorial/advancedlanguage/GenericsExamples.java
touch src/main/java/com/tutorial/advancedlanguage/AnnotationsExamples.java
touch src/main/java/com/tutorial/advancedlanguage/ReflectionExamples.java
touch src/main/java/com/tutorial/advancedlanguage/LambdasAndStreams.java
touch src/main/java/com/tutorial/advancedlanguage/ModulesOverview.java
touch src/main/java/com/tutorial/advancedlanguage/RecordsAndSealedClasses.java

# --- Development Workflow ---
mkdir -p src/main/java/com/tutorial/workflow
touch src/main/java/com/tutorial/workflow/BuildLifecycle.java
touch src/main/java/com/tutorial/workflow/DebuggingAndProfiling.java
touch src/main/java/com/tutorial/workflow/GitWorkflow.java

# --- Ecosystem ---
mkdir -p src/main/java/com/tutorial/ecosystem
touch src/main/java/com/tutorial/ecosystem/MavenIntro.java
touch src/main/java/com/tutorial/ecosystem/GradleIntro.java
touch src/main/java/com/tutorial/ecosystem/IDEProductivity.java
touch src/main/java/com/tutorial/ecosystem/TestingEcosystem.java

# --- Deployment & Integration ---
mkdir -p src/main/java/com/tutorial/deploy
touch src/main/java/com/tutorial/deploy/DockerBasics.java
touch src/main/java/com/tutorial/deploy/KubernetesBasics.java
touch src/main/java/com/tutorial/deploy/CICDPipelines.java
touch src/main/java/com/tutorial/deploy/DatabaseIntegration.java

# --- System Design & Scalability ---
mkdir -p src/main/java/com/tutorial/systemdesign
touch src/main/java/com/tutorial/systemdesign/DistributedSystemsBasics.java
touch src/main/java/com/tutorial/systemdesign/CachingIntro.java
touch src/main/java/com/tutorial/systemdesign/LoadBalancingIntro.java
touch src/main/java/com/tutorial/systemdesign/MessagingQueuesIntro.java
touch src/main/java/com/tutorial/systemdesign/ResiliencePatterns.java
touch src/main/java/com/tutorial/systemdesign/ApiDesignRest.java
touch src/main/java/com/tutorial/systemdesign/SecurityBasics.java

# --- Leadership & Best Practices ---
mkdir -p src/main/java/com/tutorial/leadership
mkdir -p src/main/java/com/tutorial/leadership/adr

touch src/main/java/com/tutorial/leadership/CodeReviewGuidelines.java
touch src/main/java/com/tutorial/leadership/MentorshipGuide.java
touch src/main/java/com/tutorial/leadership/ArchitectureDecisionsIntro.java
touch src/main/java/com/tutorial/leadership/adr/ADR000_Template.md
touch src/main/java/com/tutorial/leadership/adr/ADR001_Example.md

