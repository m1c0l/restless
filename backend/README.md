Flask app that runs its builtin server on port 80. Run `python route.py` to
start server if it's not up.

# API usage

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
/api/get/<type>/<id>
```
`<type>` is one of `user`, `project`, or `skill`. `<id>` is the id of the
object you want.  Will return a JSON of the object from the database.

### Users
```
GET /api/get/user/<user-id>
```
Currently 29 users with ids 1 to 29
#### Example
Get user with id 1: http://159.203.243.194/api/get/user/1
```js
{
  "LinkedIn_profile_id": null,
  "bio": "m4st3r h4x0r",
  "email": "xyz",
  "first_name": "John",
  "id": 1,
  "last_name": "Dough",
  "projects_developing": [
    27
  ],
  "projects_managing": [
    1
  ],
  "signup_time": "2016-11-26 10:26:03",
  "skill_sets": [
    "SQLAlchemy",
    "MySQL"
  ],
  "username": "jd"
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
Get project with id 1: http://159.203.243.194/api/get/project/1
```js
{
  "current_state": 0,
  "description": "M4st3r h4cks 4 dayz",
  "id": 1,
  "pm_id": 1,
  "skills_needed": [
    "Django",
    "MySQL"
  ],
  "title": "H4cks"
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
Get skill with id 1: http://159.203.243.194/api/get/skill/1
```js
{
  "id": 1,
  "skill_name": "Python"
}
```

## Updating stuff
```
POST /api/update/<type>/<id>
```
`<type>` is one of `user`, `project`, or `skill`. `<id>` is the id of the
object you want to update. POST data should be the attributes to update on an
object. Returns a JSON of the updated object.

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
```
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
    - `description`.
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
```
{
  "id": 1
}
```

## Adding and removing skills to a user/project
Implemented, docs coming soon...

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
http://159.203.243.194/api/swipe/user/3/2/1

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
POST data:
```js
{
  "file": File('/path/to/file')
}
```
> Currently, the server assumes the data is given as `multipart/form-data`. If
  it is easier to upload the image as raw data (eg. `binary/octet-stream`), ask
  Richard to change this.

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
