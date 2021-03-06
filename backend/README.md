Flask app that runs its builtin server on port 80. Run `python route.py` to
start server if it's not up.

# API usage

## Contents
- [Errors](#errors)
- [Getting data](#getting-data)
    - [Users](#users)
    - [Projects](#projects)
    - [Skills](#skills)
- [Updating stuff](#updating-stuff)
- [Creating users](#creating-users)
- [Logging in](#logging-in)
- [Add a new project](#add-a-new-project)
- [Adding/Deleting skills](#addingdeleting-skills)
    - [Adding](#adding)
    - [Deleting](#deleting)
- [Swiping](#swiping)
- [Images](#images)
    - [Getting an image](#getting-an-image)
    - [Uploading an image](#uploading-an-image)
    - [Deleting an image](#deleting-an-image)
- [Stack](#stack)
    - [Get stack for a user](#get-stack-for-a-user)
    - [Get stack for a project](#get-stack-for-a-project)
- [Matches](#matches)
    - [Get matches for a user or project](#get-matches-for-a-user-or-project)
    - [Accept a match](#accept-a-match)
    - [Decline a match](#decline-a-match)
- [Confirmed devs](#confirmed-devs)

## Errors
Any request that is an error will return a JSON like:
```js
{
      "error_message": "Invalid ID",
      "status": "BAD_REQUEST"
}
```
If the API request is good, the response's status code will be `200`. If it is
a bad request, the status code will be `4xx` or `5xx`.

## Getting data
```
/api/get/<type>/<ids>
```
- `<type>` is one of `user`, `project`, or `skill`.
- `<ids>` is a comma-separated array of id's of the objects you want.
Will return a JSON of the object from the database.

### Users
```
GET /api/get/user/<user-id>
```
Currently 29 users with ids 1 to 29
#### Example
Get users with id's 1 and 3:
```
GET /api/get/user/1,3
```
Response:
```js
{
  "results": [
    {
      "LinkedIn_profile_id": null,
      "bio": "m4st3r h4x0r",
      "city": null,
      "desired_salary": 0,
      "email": "xyz",
      "first_name": "John",
      "github_link": null,
      "id": 1,
      "last_name": "Dough",
      "phone": null,
      "projects_developing": [],
      "projects_managing": [
        1,
        6,
        14
      ],
      "signup_time": "2016-11-27 05:06:50",
      "skill_sets": [
        "C",
        "PHP",
        "Python",
        "SQLAlchemy",
        "Scala"
      ],
      "username": "jd"
    },
    {
      "id": 3,
      // ...
    },
    // ...
  ]
}
```
- `projects_developing` and `projects_managing` are arrays of project id's
- `skill_sets` is an array of skill names.

### Projects
```
GET /api/get/project/<project-id>
```
Currently 29 projects with ids 1 to 29

#### Example
Get project with id 1:
```
GET /api/get/project/1
```
Response:
```js
{
  "results": [
    {
      "current_state": 0,
      "description": "M4st3r h4cks 4 dayz",
      "id": 1,
      "pay_range": 0,
      "pm_id": 1,
      "skills_needed": [
        "PHP",
        "Microsoft",
        "Scala",
        "Python",
        "SQLAlchemy"
      ],
      "title": "H4cks"
    }
  ]
}
```
- `skills_needed` is an array of skill names.
- `current_state` is an enum with values:
    - `STATE_RECRUITING = 0`, the project is recruiting devs
    - `STATE_STARTED = 1`, the project is being worked on
    - `STATE_FINISHED = 2`, the project is no longer active

### Skills
```
GET /api/get/skill/<skill-id>
```
Currently 4 skills with ids 1 to 4

#### Example
Get skill with id 1
```
GET /api/get/skill/1
```
Response:
```js
{
  "results": [
    {
      "id": 1,
      "skill_name": "Python"
    }
  ]
}
```

## Updating stuff
```
POST /api/update/<type>/<id>
```
- `<type>` is one of `user`, `project`, or `skill`
- `<id>` is the id of the object you want to update.

POST data should be the attributes to update on an object. Returns a JSON of
the updated object.

### Examples
#### Change the title of project with id 1:
```
POST /api/update/project/1
```
POST data:
```js
{
  "title": "new title"
}
```
Response:
```js
{
  "id": 1,
  // ...
  "title": "new title"
}
```

#### Change the first and last name of user with id 1:
```
POST /api/update/user/1
```
POST data:
```js
{
  "first_name": "new first name",
  "last_name": "new last name"
}
```
Response:
```js
{
  "id": 1,
  // ...
  "first_name": "new first name",
  "last_name": "new last name"
}
```

#### Change a password for user with id 1:
```
POST /api/update/login/1
```
POST data:
```js
{
  "password": "new password"
}
```
Response:
```js
{
  "username": "jd"
}
```
The new password is intentionally left out of this response.

## Creating users
```
POST /api/new_user/
```
POST data should be the `username` and `password` of the new user. Returns a
JSON with the new user's id. Returns -1 if the username was already in the
database.

To set the rest of the User's data, [update the user](#updating-stuff).

### Example
#### Adding a user with username `user123` and password `pass123`
```
POST /api/new_user/
```
POST data:
```js
{
  "username": "user123",
  "password": "pass123"
}
```
Response:
```js
{
  "id": 2
}
```

## Logging in
```
POST /api/login/
```
POST data should be the username and password of the login. Will return (in
json format) the ID of the user if successful or -1 if it fails.

POST data:
```js
{
  "username": "user123",
  "password": "pass123"
}
```
Response:
```js
{
  "id": 2
}
```

## Add a new project
```
POST /api/new_project/
```
Three fields are required:

- `title`
- `pm_id` (id of the project manager creating the project)
- `description`

Returns (as json) the project ID if created or -1 if the project title already
exists.

POST data:
```js
{
  "title": "Restless",
  "pm_id": 1,
  "description": "Tindr for developers"
}
```
Response:
```js
{
  "id": 1
}
```

## Adding/Deleting skills
### Adding
```
GET /api/skill/add/<type>/<skill_name>/<id>
```
- `<type>` is one of `user` or `project`
- `<skill_name>` is a string with the name of the skill to add
- `<id>` is the id of the user/project

Returns the skill id on success.

#### Example
Add `Python` as a skill for user with id 1:
```
GET /api/skill/add/user/Python/1
```
Response:
```js
{
  "id": 1
}
```
### Deleting
```
GET /api/skill/delete/<type>/<skill_name>/<id>
```
- `<type>` is one of `user` or `project`
- `<skill_name>` is a string with the name of the skill to delete
- `<id>` is the id of the user/project
Returns the skill id on success.
#### Example
Delete `Python` from user with id 1:
```
GET /api/skill/delete/user/Python/1
```
Response:
```js
{
  "id": 1
}
```

### Setting skill weight for a project
```
GET /api/skill/set_weight/<project_id>/<skill_name>/<new_weight>
```
Sets the weight of `skill_name` for project `project_id` to `new_weight`, a float between `0.0` and `5.0`.
Response:
```
{
    "id": 2
    "skill_id": 3
    "skill_weight": 4.0
}
```


## Swiping
Register a swipe:
```
GET /api/swipe/<type>/<swiper_id>/<swipee_id>/<direction>
```
- `<type>` is one of `user` or `project`, and corresponds to the type of
  account that is doing the swiping.
- `swiper_id` is the integer ID of this account.
- `swipee_id` is the integer ID of the *other* account, a.k.a. the one being
  swiped on.
- `direction` indicates whether the account swiped *up* (positive swipe) or
  *down* (negative swipe).
    - 0 corresponds to *down*
    - 1 corresponds to *up*.

### Example
If user with ID `3` swipes *up* on the project with ID `2`, we can register
that as follows:
```
GET /api/swipe/user/3/2/1
```

The server will respond with the ID of the complement swipe in the database. If
there is a match, this will return a positive number.

Otherwise, it will return -1 which indicates that the other party has not
swiped positively yet.

## Images
### Getting an image
```
GET /api/img/get/<type>/<id>
```
- `<type>` is one of `user` or `project`
- `<id>` is the id of the user/project

The response's mimetype is set based on the image (eg. `image/png`), so you
should be able to just this endpoint url as a normal image link.

#### Example
Get the image for the project with id 1:
```
GET /api/img/get/project/1
```
Response: The project's image

### Uploading an image
```
POST /api/img/upload/<type>/<id>
```
- `<type>` is one of `user` or `project`
- `<id>` is the id of the user/project

Add the image file in the `file` field of the POST data. This request replaces
the old image for that user/project if there is one. The server returns an error
if `type` or `id` is invalid, or if the file is not an image.

#### Example
Upload an image for the project with id 1:
```
POST /api/img/get/project/1
```
POST data: Raw image data

### Deleting an image
```
GET/POST /api/img/delete/<type>/<id>
```
- `<type>` is one of `user` or `project`
- `<id>` is the id of the user/project

#### Example
Delete the image for the project with id 1:
```
GET /api/img/delete/project/1
```

## Stack
### Get stack for a user
```
GET /api/stack/user/<id>
```
- `<id>` is the user id
Returns an array of id's of projects in the stack.

#### Special users for testing

- user 1 is desperate and has swiped yes on all projects, so user 1's stack should be empty, and all project's stacks should contain user 1
- user 2 is pro and has swiped yes on all projects and all projects have swiped yes on him (matched yes on all projects), so user 2's stack should be empty
- user 3 is a great developer and has a super high desired salary ($200/hr), so user 3's stack should be empty or small

#### Example
Get stack for user with id 5:
```
GET /api/stack/user/5
```
Response:
```js
{
    "stack": [5, 3, 7, 21, 12, 6, 14]
}
```
### Get stack for a project

```
GET /api/stack/project/<id>
```
- `<id>` is the project id

#### Example
Get stack for project with id 1:
```
GET /api/stack/project/1
```
Response:
```js
{
    "stack": [1, 4, 2, 16, 13, 8, 3]
}
```

## Matches
### Get matches for a user or project
```
GET /api/matches/<who>/<id>/<type>
```
- `<who>` is `0` for a project, and `1` for a developer.
- `<id>` is the id of this object.
- `<type>` is the type of this match.
    - `1` is the default value, which indicates that both user and project have
      swiped positively, and they made a match.
    - `0` means that the match has been declined by either user.
    - `2` or more indicates that a party has accepted the match (a.k.a. sought
      more information about it)

#### Special users for testing
- user 2 is pro and has swiped yes on all projects and all projects have swiped yes on him (matched yes on all projects), so ```/api/matches/1/2/1``` should initially contain all the projects, and all of ```/api/matches/0/<project_id>/1``` should initially contain user 2

#### Example
Get all new matches for project with id 3:
```
GET /api/matches/0/3/1
```
Response:
```js
{
    "results" : [2,5,12]
}
```

### Accept a match
```
GET /api/matches/accept/<user_id>/<project_id>
```
- `user_id` is the id of the user.
- `project_id` is the id of the project.

Use this if either the user or the PM accepts this match.

Returns the new result of the match:
- `2` or greater: the match was accepted successfully
- `0`: the match was previously declined, so the result is still `0` (declined).

#### Example
A project with id 2 accepts a user with id 4.
```
GET /api/matches/accept/4/2
```

### Decline a match
```
GET /api/matches/accept/<user_id>/<project_id>
```
- `user_id` is the id of the user.
- `project_id` is the id of the project.

Use this if either the user or the PM declines this match. If the user/project
match was previously accepted, this call will set the new result of the match
to declined.

Returns the new result of the match:
- `0`: the match was successfully declined
- `-1`: the user and project were never matched


#### Example
Suppose user with id 2 and project with id 3 are matched. A party wants to decline the match.
```
GET /api/matches/decline/2/3
```
Response:
```js
{
    "result" : 0
}
```

## Confirmed devs
```
GET /api/confirmed/<project_id>
```
- `<project_id>` is the project's id

Returns an array of user id's

#### Example
Get the users who are developers on the project with id 1:
```
GET /api/confirmed/1
```
Response:
```js
{
  "results": [ 2, 4 ]
}
```
