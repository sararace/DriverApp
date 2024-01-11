# DriverApp
This app displays a list of rides for a given day, as well as the details for each trip when you tap on a trip card in the list.

## Third party libraries
* [Retrofit](https://square.github.io/retrofit/) - For networking
* [OkHttp](https://square.github.io/okhttp/) - For networking
* [Koin](https://insert-koin.io/) - For dependency injection

## APK
The [APK](https://github.com/sararace/DriverApp/blob/main/app-debug.apk) is in the root directory of the repository.

## Notes and Improvements
* There are many instances throughout the app of duplicated code, especially in formatting dates and times. I would create a util class to consolidate the logic.
* Both unit tests and UI tests are missing from the app. I did add in a way to mock the CoroutineDispatcher though.
* Currently the onClick() callback is called in addition to dismiss() in the confirmation alert. For further customizability, there should be an option of whether the dialog is dismissed or not.
### Known bugs
There were several bugs that I noted, but prioritized other functionality instead.
* When on the ride details screen, hitting the system back button closes the app. However, the toolbar's back button works correctly.
* The buttons on the confirmation alert are not full width.
* There are too many dividers in the My Rides list
* The riders/boosters text overlaps with the estimated price on smaller screens. I would need to clarify with a designer what the intended wrapping behavior would be.
* The toolbar text is not centered, although the left-aligned text is more in line with android best practices.
* The map markers are not customized.
