# Project-Management-Java

## About
This is a console-based Project Management Platform using OOP concepts in Java. It allows three types of users: Customers, Team Members and Managers, to interact with projects and tasks in a collaborative environment.  

* Clients can request new projects (with or without a preferred manager), check project progress and request invoices.
* Team Members can view and assign themselves to projects, create and manage tasks, update task status and view their assigned projects and tasks.
* Managers have advanced controls: they can view all projects, assign or remove team members, create tasks, generate invoices and filter or manage tasks by various criteria.
  
The application uses a menu-driven interface, object-oriented design and service classes to manage users, projects, tasks, notifications and invoices. It demonstrates practical use of Java collections, exception handling and user input processing for a real-world workflow.  
Moreover, the application will be connected to a local database where information about users, projects and tasks will be stored to be extracted and manipulated (coming soon).

## What I learned
* implementing the sealed class User to determine precisely the classes which could extend it: Customer, Member and Manager
* overloading and overriding methods
* including service classes for an effortless way to work with various objects
* handling specific exceptions in order to prevent the app from closing it automatically
* defining enum classes with private constructors and abstract methods
* adding collections: List, Set
* implementing both aggregation and composition
* including both static and non-static blocks within different classes
* upcasting and downcasting
* using Comparator interface for comparing objects with the same type
* using lambda expressions
* implementing streams
