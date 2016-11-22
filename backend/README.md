Flask app that runs its builtin server on port 80. Run `python route.py` to
start server if it's not up.

# API usage

## GET requests
```
/api/get/<type>/<id>
```
`<type>` is one of `user`, `project`, or `skill`. `<id>` is the id of the
object you want.  Will return a JSON of the object from the database.

### Users
```
/api/get/user/<user-id>
```
Currently 29 users with ids 1 to 29
#### Example
Get user with id 1: http://159.203.243.194/api/user/1
```json
{
  "LinkedIn_profile_id": null, 
  "bio": "m4st3r h4x0r", 
  "email": "xyz", 
  "first_name": "John", 
  "id": 1, 
  "last_name": "Dough", 
  "projects_developing": [
    12, 
    18
  ], 
  "projects_managing": [
    1
  ], 
  "signup_time": "Sun, 20 Nov 2016 07:07:01 GMT", 
  "skill_sets": [], 
  "username": "jd"
}
```

### Projects
Currently 29 projects with ids 1 to 29, access it like so: /api/project/[project-id], e.g., http://159.203.243.194/api/project/2

### Skills
Currently 4 projects with ids 1 to 4, access it like so: /api/skill/[skill-id], e.g., http://159.203.243.194/api/skill/3


## POST requests

### Updating stuff
```
POST /api/update/<type>/<id>
```
`<type>` is one of `user`, `project`, or `skill`. `<id>` is the id of the
object you want to update. POST data should be the attributes to update on an
object. Returns a JSON of the updated object.

#### Examples
##### Change the title of project with id 1:
```
POST /api/update/project/1
```
POST data:
```json
{
  "title": "new title"
}
```
Response:
```json
{
  "id": 1,
  ...
  "title": "new title"
}
```

##### Change the first and last name of user with id 1:
```
POST /api/update/user/1
```
POST data:
```json
{
  "first_name": "new first name",
  "last_name": "new last name"
}
```
Response:
```json
{
  "id": 1,
  ...
  "first_name": "new first name",
  "last_name": "new last name"
}
```

### Updating stuff
```
POST /api/new_user/
```
POST data should be the `username` and `password` of the new user. Returns a
JSON with the new user's id.

#### Example
##### Adding a user with username `user123` and password `pass123`
```
POST /api/new_user/
```
POST data:
```json
{
  "username": "user123",
  "password": "pass123"
}
```
Response:
```json
{
  "id": 5
}
```
