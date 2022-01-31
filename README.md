# MyRecipesApp

A sample app that display list of Recipes. This App uses Kotlin and set of Android Jetpack libraries plus Retrofit to display data from REST API(Unsplash).


### Prerequisites

The project has all required dependencies in the gradle files. 
Add the Project to Android Studio and build.All the required dependencies will be downloaded and installed.

## Architecture

The project uses MVVM architecture pattern.

MVVM architecture is a Model-View-ViewModel architecture that removes the tight
coupling between each component. Most importantly, in this architecture, the children
don't have the direct reference to the parent, they only have the reference by
observables.

#### Model: 
It represents the data and the business logic of the Android Application. It
consists of the business logic - local and remote data source, model classes,
repository.

#### View:
 It consists of the UI Code(Activity, Fragment), XML. It sends the user action
to the ViewModel but does not get the response back directly. To get the response,
it has to subscribe to the observables which ViewModel exposes to it.

#### ViewModel:
 It is a bridge between the View and Model(business logic). It does not
have any clue which View has to use it as it does not have a direct reference to the
View. So basically, the ViewModel should not be aware of the view who is
interacting with. It interacts with the Model and exposes the observable that can be
observed by the View.

## Libraries

* LiveData, ViewModel
* Retrofit
* Moshi
* Unit Testing (JUnit), MockK
* Espresso UI Testing
* Repository Pattern
* AndroidX
* Picasso
* Coroutines
* Dagger-hilt (Dependency Injection)
* JetPack Libraries
