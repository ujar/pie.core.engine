# pie.core.engine
Pie core engine is a Java based  threading library for multitaskig execution inside web or desktop appliction. The engine is built on top of  java.util.concurrent library. There
are some added benefit of using Pie core engine such as follows
1. Dynamically add and remove mutiple task in the core execution thread
2. Each task progress is updated by attaching listener/event with each task
3. Schedule mutiple tasks based on specific time(by Date, day, hour...so on)
4. API friendly or less code implementation by the coder

To test Pie core engine, please check out the Client application   https://github.com/ujar/pie.core.engine.test.ui
Client application is developed to demonstrate following usages of pie core engine

1. Start/Stop Pie core engine
2. Create a Task/Adapter with scheduled time or without scheduled time
3. Push the Task/Adapter into the Pie core engine
4. Add or remove task from the pie core engine 
5. Add event/listener with each task in order to get the progress update

It has been developed as a personal effort in order to  easily run/execute multiple work in a java based application.
