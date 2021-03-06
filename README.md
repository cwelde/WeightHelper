# WeightHelper
## CS125 Project

## Group 4 
Christopher Welde - cwelde@uci.edu, Alon Maor - maora@uci.edu, Vivian Lu - vlu4@uci.edu, David Sianov - dsianov@uci.edu 

## Source Files:
* FoodActivity.java - Gets Calories/Protein/Carbs/Fats from foods returned from search and that user selects, stores in database, displays them on screen, saves values.
* FoodClient.java - Handles HTTP / API Calls for our food
* FoodLog.java - Helper class that helps store macronutrients and calories of a given food
* FoodNoSearch.java, FoodSearch.java - Helper classes that parse the JSON response from a food search in the USDA database and extracts relevant information
* MainActivity.java - Logic for collecting data from first screen the user sees. Stores that data and passes to other activities. inits DB.
* RecipeClient.java - Handles HTTP / API Calls for our recommended recipes
* Recipes.java - Handles the recommendation of recipes based off macronutrients the user needs more of or less of. Displays to UI.
* SensorActivity.java - All the required code for a step counter based off the Google Fit API. Stores our steps
* UserLocation.java - Handles user location based off of lat/long.
* UserScreen.java - Main user screen. Displays various statistics to user such as calories burned/eaten today. Allows navigation to the 
parts of the app.


## HOW TO RUN:
After successfully building/launching the app, you will see fields that need to be populated with data about you.
Fill out your username, name, age, height (in feet and inches), current weight, and goal weight. Hit Next.

From here, you can see statistics such as BMI, recommended calories per day, etc., as well as average calories consumed per day if you have used this application before.   
There are 3 buttons:    
Food log button - allows you to search for a food, select its measurement, log your food and view your macronutrient breakdown. To start a new log and have your data stored in the database, select the New Log button in the Food Log screen.    
Activity log button - allows you to see your step count   
Recipe button - You will see lists of recipes that are recommended to you based off what you've consumed today. e.g. If you've consumed too many carbs, you will see recipes that do not contain a lot of carbs.  

Recommendations are generated automatically if you eat too many carbs or fats. To trigger this, simply log a bunch of bread (or oil) and view
the macronutrients section, a dialog will pop up and recommend other food items to eat.

Another recommendation can be autogenerated if you eat over your recommended in take or are eating too much without having any activity.
To test this, add a lot of high calorie food items and return to the initial screen that displays statistics/contains other activity buttons.


