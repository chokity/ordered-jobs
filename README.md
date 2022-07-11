## Ordered Jobs
Executing dependent jobs in the right order is important for any business. Develop a class, which plans the execution of jobs, which may depend on each other.  
Each job is represented by a single character. Maybe a job “a” must be completed before job “b”, then job “b” depends on “a”. Any job may depend on any number of other jobs. After registering jobs with their dependencies, the class should compute the order of jobs for execution.

The interface is defined as:
```java
interface OrderedJobs {
  void register(char job);
  void register(char job, char dependentJob);
  String[] sort();
}
```
For example, registering the following jobs:
```java
register('c');
register('b', 'a');
register('c', 'b');
````
```sort()``` should return this result: "abc".

The same job appeared in multiple registrations just occurs once in the result of ```sort()```. Jobs with no dependency can occur in any order as long as they are executed before jobs, which depend on them. Circular dependencies between jobs should be reported through an exception (at least in the method ```sort()```).

Provide unit test(s) to verify the required functionality.
