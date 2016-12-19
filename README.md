# Restless

As software developers, we enjoy working on projects, but finding an interesting project is often difficult. Social media platforms like Facebook allow developers and project managers to try to find team members to work with by posting their skills or project ideas in a community with other developers or project managers. However, this method is ineffective, since users are spread out across multiple groups and managers would need to repeatedly post in such groups to find suitable team members.

This is where Restless comes in; we allow developers and project managers to set up their profile only once and start looking for other members and projects that match their interests. RestLess also saves project managers the hassle of filtering through a large amount of applicants. The app shows the developers that are best fit for the project and makes it easy to get in contact with them.

If you are a developer, you can easily find projects that might be interesting to you. You can swipe to express interest on these projects and will be notified if there is a match. These projects are relevant to your skillset and desired salary.

If you are a project manager, you can easily create a project and find developers who may be interested in this project. You can choose developers that match your needs and skillsets and will be notified if there is a match. This allows you to easily get a project up and running.

## Features

Upon login, the user will be allowed to manage their account as either a Project Manager or a Developer. A common feature shared between Project Managers and Developers is the ability to swipe on the developers or the projects that he or she is respectively interested in. Once the developer has swiped, if the owner of the project being swiped on has also swiped on the developer, we will generate a match between project and developer. The more specific features of Project Managers and Developers will be discussed below.

If the user chooses to manage their account as a Project Manager, he or she will be taken to a screen to either select an existing project, or create a new project. Upon selecting a new project, the user will then enter relevant information about the project before creation. Once the user selects an existing project, he or she can modify the project profile, or proceed to swipe on new developers for said project. 

The Developer profiles that appear in the stack for PM selection show basic information about each candidate. Users can swipe on the upper half of the green tab to advance the screen to show different information (basic info to skills to past projects). If a match is generated between the PM and the developer, a more complete profile of the developer will be available to the PM, which includes further information such as resumes and contact information. The developer profiles can be found under the account matches section of the application. 

If the user chooses to manage their account as a Developer, he or she will be able to view past matched projects or swipe on new projects. If the developer decides to swipe on new projects, the display UI will be similar to that of the PM swiping, albeit displaying projects and their relevant information rather than developer profiles and their relevant information. The project display will additionally include some background information on the Project Manager, as well as proposed wages for the differing roles needed on the team. 

## Screenshots

User profile
![User profile image](https://github.com/m1c0l/restless/blob/master/screenshots/user.png?raw=true)

Project profile
![Project profile image](https://github.com/m1c0l/restless/blob/master/screenshots/project.png?raw=true)

Developer matching with project
![Match image](https://github.com/m1c0l/restless/blob/master/screenshots/match.png?raw=true)

## Design

The front end UI is built in native Android using Java classes and xml documents; this code lives in the [frontend](frontend) folder.

The backend is written in Python; this code lives in the [backend](backend) folder with a full documentation of the API. The backend uses Flask as a server and SQLAlchemy to manage a MySQL database. A routing class handles HTTP requests from the mobile app. We support getting data about users, projects, swipes, and other data using a RESTful API. We support adding and updating these classes, as well as pushing swipes and getting the user/project stack and matches. The HTTP responses are sent as JSON, containing the data serialized based on an interface.

## Tests

To test our backend, we decided to use python's unittest module. All test cases are documented in the /docs/ directory of our backend (or see pdf below). We wrote unit tests to cover all of the methods in our Python modules to do basic test cases to make sure all the code works for basic test cases and also do error checking for invalid arguments and other values. The unit tests focused on the code that accessed the database and the API code, since it is most important that the server stays in a consistent state and the mobile app can interact with the server properly.

To measure test coverage of our Python modules, we used a pip library called Coverage.py, which measures the statement coverage of the Python modules. This library shows which lines of code are executed and which are missed by our unit tests. The file [create_db.py](backend/database/create_db.py) is run by the unit tests, but it is only used for creating mock data for testing purposes. 

We aimed for 100% statement coverage of all of the other code. This metric was easiest to measure because coverage.py automatically calculated it.

## Stack generation

For the purposes of the stack algorithm, we consider projects to have 3 different states: recruiting, started, and finished. When a project is recruiting, it is looking for new developers to join the team.  When a project is deemed started by the PM, the project is considered to be under development. When a project is finished, it is either considered to be abandoned after some amount of time or completed.

To generate the developer’s stack, the stack algorithm first queries for all projects that have at least one required skill that matches any of the developer’s skill sets. Then, the algorithm subtracts the projects that the developer is a project manager of, projects that are not in the recruiting state, projects that the developer has already swiped on or matched with, and projects with a lower pay rate than the developer’s desired salary. Out of the remaining projects, the algorithm assigns each project a score to rank the projects by. The score is composed 40% of the project’s pay and 60% of how well the developer matches the project’s skills. For the pay portion of the score, the algorithm divides the project’s pay by a very large salary, $250/hr. For the skill matching portion, the algorithm calculates the sum of the skills’ weights and what fraction of the skill weights the developer’s skill sets satisfies. For example, if there are 2 skills worth 2.0 and 3.0 and the developer satisfies the first skill, the developer satisfies 2.0/5.0 = 40% of the skill weights.

To generate the project’s stack, the stack algorithm first queries the developers who have swiped on the project. Then, the algorithm subtracts the developers for whom the project manager has already swiped on. Out of the remaining developers, the algorithm assigns a score to rank the developers by; it is calculated the same way as how the skill matching portion is calculated for the developer stack.
