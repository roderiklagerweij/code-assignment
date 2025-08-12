# Coding Assignment

This repository contains the code for the requested assignment. Most of code speaks for itself, but I will highlight a few decisions that I took during the development of the project.

All in all it was fun to do, if you have any questions or comments feel free to reach out!

## Technologies
Regarding technologies, I chose:
* Kotlin
* Compose
* Koin
* Retrofit
* Room

For DI I went with Koin as it was mentioned in the original vacancy, Hilt/Dagger could have been a good alternative choice.

## Code structure

Regarding code structure I went with a simple two-layer approach where the presentation layer is separated from the data layer. The repository in the datalayer abstracts the data source, which can be either a local database or the remote API.

For presentation, I just ViewModels, so MVVM-like. In the viewmodel, I observe multiple flows and map them to a single state which is then exposed to the UI.

## State Hoisting

For the Composables, I took a state hoisting approach and pass the state down to the Composables (*Screen -> *Content). This helps with setting up Previews and makes reusability easier.

## Offline first Approach

To make this an offline-first app, I observe the local database and only fetch the repositories to update the database. This way, the app still shows data if the user is offline, if it has been fetched before.

## Paging

For paging I did a relatively straightforward implementation myself. It works as follows:
* It checks which item becomes visible on the screen
* If the last item is visible, it fetches the next page from the repository
* There is some additional logic to prevent multiple requests at the same and to prevent loading loops in case of an error

Alternatively, I could have used the Paging3 library. However, the last time I used it, it had some issues with it and the implementation was not as straightforward as I would have liked. Granted, this was multiple years ago, and it was still the original Paging library, so things might have changed by now:)

## UI

I did not spend too much time on the UI, though it's functional, choosing rather to spend some more time on including some tests. Also, I did not extract strings to a resource file.

## Testing

I have included a few example tests for the RepoOverviewViewModel.

The approach I took here is to essentially test throughout multiple layers, that is starting from the ViewModel all the way to the API where only the API responses are mocked.

This is slightly different approach what you often see done in Android development, with a key benefit being here that this is allows you to afterwards refactor code more easily, as you can be sure that the entire functionality is still working, all the way through.

I included a few examples for how this could be done that touch the paging functionality.


