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
