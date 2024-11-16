# Android home assignment for Visa כאל (Visa Cal)

# Project Overview

The project follows a modern Android architecture using Kotlin, with a focus on clean architecture principles. Here is a brief overview of the architecture:

## Summary

The project is structured to separate concerns, making it maintainable and scalable. The presentation layer handles UI and user interactions, the domain layer encapsulates business logic, and the data layer manages data operations. Dependency injection, coroutines, and biometric authentication are key features that enhance the application's functionality and security.

## Layers

### Presentation Layer
- **Fragments**: UI components that handle user interactions and display data. Examples include `HomeFragment` and `RecipeFragment`.
- **ViewModels**: Manage UI-related data and handle business logic. Examples include `HomeViewModel` and `RecipeViewModel`.
- **Adapters**: Bind data to RecyclerView components. Example: `RecipesAdapter`.

### Domain Layer
- **Entities**: Represent the core data structures of the application. Examples include `Recipe` and `EncryptedData`.


### Data Layer
- **Repositories**: Handle data operations and abstract data sources. 
- **Data Sources**: Provide data from various sources 

## Key Components

- **Dependency Injection**: Uses Dagger Hilt for dependency injection, as indicated by the `@HiltViewModel` and `@AndroidEntryPoint` annotations.
- **Coroutines**: Utilizes Kotlin coroutines for asynchronous operations, as seen in the `viewModelScope.launch` calls.
- **Biometric Authentication**: Implements biometric authentication for secure operations, using `BiometricDelegate` and `BiometricDelegateImpl`.
- **Error Handling**: Centralized error handling through `ErrorDialogDelegate` and `ErrorDialogDelegateImpl`.

## Navigation

- **Navigation Component**: Manages navigation between fragments, as seen in the `findNavController().navigate` calls.

## UI Binding

- **View Binding**: Utilizes view binding to interact with UI components, as seen in the `FragHomeBinding` and `ItemRecipeBinding` classes.

