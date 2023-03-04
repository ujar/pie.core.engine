Pie core engine is a Java-based threading library for multitasking execution inside web or desktop applications. The engine is built on top of the java.util.concurrent library, providing added benefits such as:

    Dynamically adding and removing multiple tasks in the core execution thread
    Updating each task's progress by attaching a listener/event to each task
    Scheduling multiple tasks based on specific time (by date, day, hour, etc.)
    Easy-to-use API for less code implementation by the coder

To test Pie core engine, please check out the client application at https://github.com/ujar/pie.core.engine.test.ui. This client application is developed to demonstrate the following usages of Pie core engine:

    Starting/stopping Pie core engine
    Creating a task/adapter with scheduled time or without scheduled time
    Pushing the task/adapter into the Pie core engine
    Adding or removing tasks from the Pie core engine
    Adding an event/listener with each task to get progress updates

Pie core engine was developed as a personal effort to easily run/execute multiple tasks in a Java-based application.
