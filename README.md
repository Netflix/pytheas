pytheas
=======

Pytheas is a Guice-Jersey based web application framework designed to allow quick prototyping and creation of web based tools that explore large data sources. Applications built on top of Pytheas consist of one or more loosely coupled modules, each encapsulating a data source and its corresponding UI elements. The framework provides a bundle of commonly used UI components for creating dynamic data exploration/visualization applications.

# Features
* Uses Guice/Governator for application bootstrapping.
* Supports Jersey REST data source integration
* Loosely coupled modules with isolated data resource loading and rendering mechanism.
* Seamless aggregation of modules at runtime with minimal configuration
* Bundles rich UI components based on open source frameworks such as Bootstrap, JQuery-UI, DataTables etc.
* Jersey based REST endpoints for streaming server-side-events (SSE)
* Supports cross domain AJAX data using JSONP

# Getting Started
* checkout code - git clone https://github.com/Netflix/pytheas 
* run ./gradlew jettyRun

You should see a simple hello world application running at http://localhost:8989/pytheas-helloworld.
You can look into pytheas-helloworld subproject in your src to find more about specific code blocks needed to get started on building your own app using Pytheas.
