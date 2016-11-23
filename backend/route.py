"""
@newfield api: API Documentation
@api: U{https://github.com/m1c0l/restless/blob/master/backend/README.md}
"""

import json, flask, time, os
from flask import Flask, request
from database import database
from database.login import Login
app = Flask(__name__)

def current_time():
    """
    Get current time, Unix Epoch style.
    @return: Integer of current time
    @rtype: C{int}
    """
    return int(time.time())

def init_app():
    """
    Initializes the app
    @note: only call this one time
    """
    config = os.path.dirname(os.path.realpath(__file__)) + '/config.py'
    app.config.from_pyfile(config)
    from database import database
    database.db.init_app(app)

def error(msg='Bad Request', status='BAD_REQUEST', code=400): #todo: add logging?
    """
    Sends a HTTP message with an error response
    @param msg: The error message
    @type msg: C{str}
    @param status: Description of the HTTP status
    @type status: C{str}
    @param code: The HTTP status
    @type code: C{int}
    @return: A tuple of the JSON response and the status code
    @rtype: (C{str}, C{int})
    """
    try:
        err = {
            'error_message' : msg,
            'status': status
        }
        return flask.jsonify(**err), code
    except:
        return 'Internal server error...', 500
        
@app.route("/")
def index():
    """
    Returns a default error message
    @return: A default error message
    """
    return error(msg='There is no index!')

@app.route("/api/update/<type>/<id>", methods=['POST'])
def update_info(type=None, id=None):
    """
    Updates user data from the mobile app if valid. All other parameters are
    sent via POST request.
    @param id: ID of our data.
    @type id: C{int}
    @param type: The type of data to get (eg. C{user}, C{project}, C{skill})
    @type type: C{str}
    @return: New data if successfully updated, or JSON error
    @rtype: C{str}
    """
    commands = {
        'user' : database.get_user_by_id,
        'project' : database.get_project_by_id,
        'skill' : database.get_skill_by_id,
    }
    if type not in commands:
        return error(msg='Invalid type')
    try:
        id = int(id)
        if id:
            obj = commands[type.lower()](id)
            database.update(obj, **request.form)
            return retrieve(type, id)
    except ValueError:
        return error(msg='Invalid ID')
    except AttributeError as e:
        return repr(e)

@app.route("/api/new_user/", methods=['POST'])
def new_user(username=None, password=None):
    """
    Creates a new user with specified username and password.
    @param username: Username from input, cannot be blank.
    @type username: C{str}
    @param password: Password from input, cannot be blank.
    @type password: C{str}
    @return: Integer describing the ID of the newly created user with specified
             username/password, or -1 if username was already in the database.
    @rtype: C{str}
    """
    if not username:
        username = request.form.get("username")
    if not password:
        password = request.form.get("password")
    if not username or not password:
        return str(-1)
    return str(database.add_new_user(username, password))

@app.route("/api/login/", methods=['POST'])
def login(username=None, password=None):
    """
    Handles login requests from the mobile app.
    @param username: Username from input.
    @type username: C{str}
    @param password: Password from input.
    @type password: C{str}
    @return: Integer describing the ID of the user with specified
             username/password, or -1 if none.
    @rtype: C{int}
    """
    if not username:
        username = request.form.get("username")
    if not password:
        password = request.form.get("password")
    creds = Login(username, password)
    if database.validate_login(creds):
        return str(database.get_user_by_username.id)
    else:
        return str(-1)

@app.route("/api/get/<type>/<id>")
def retrieve(type,id):
    """
    Handles API requests from the mobile app.
    @param type: The type of data to get (eg. C{user}, C{project}, C{skill})
    @type type: C{str}
    @param id: The id of the object to get
    @type id: C{int}
    @return: A JSON response, or a JSON error
    @rtype: C{str}
    """
    if type == "" or id == "":
        return error(msg='Please fill in your parameters!')
    database_commands = {
        'user' : database.get_user_by_id,
        'project' : database.get_project_by_id,
        'skill' : database.get_skill_by_id,
    }
    if type.lower() in database_commands:
        try:
            id = int(id)
            response_dict = database_commands[type.lower()](id).to_dict()
            return flask.jsonify(**response_dict)
        except (AttributeError, ValueError):
            return error(msg='Invalid ID')
    else:
        return error(msg='Invalid type.')

@app.route("/api/new_project/", methods=['POST'])
def new_project(title=None, description="", pm_id = None):
    """
    Creates a new project
    @param title: The title of the new project
    @type title: C{str}
    @param description: The new project's description
    @type description: C{str}
    @param pm_id: User ID of the project manager that manages this project.
    @type pm_id: C{int}
    @return: Project id if user was created, -1 if username already exists
    @rtype: C{str}
    """
    if not title:
        title = request.form.get("title")
    if not pm_id:
        pm_id = request.form.get("pm_id")
    if not description:
        description = request.form.get("description")
    if not pm_id or not title:
        return -1
    return str(database.add_new_project(title=title, description=description, pm_id=pm_id))

@app.route("/api/skill/add/<type>/<skill_name>/<int:id>")
def add_skill(who, skill_name, id):
    """
    Add a skill to this user or project.
    If user, it will indicate that the user has this skill.
    If project, it will indicate that the project wants this skill.
    @param type: 'user' or 'project'.
    @type type: C{str}
    @param skill_name: The skill name.
    @type skill_name: C{str}
    @param id: user or pm id.
    @type id: C{int}
    @return: skill id if successful, or -1 if fail.
    """
    if not type or not skill_name or not id:
        return str(-1)
    skill_obj = database.get_skill_by_name(skill_name)
    if not skill_obj:
        skill_obj = Skill(skill_name)
        database.insert_obj(skill_obj)
    if type == 'user':
        user = database.get_user_by_id(id)
        if not user:
            return str(-1)
        sets = user.skill_sets
        sets.append(skill_obj)
        database.update(user, skill_sets=sets)
        return skill_obj.id
    elif type == 'project':
        project = database.get_project_by_id(id)
        if not user:
            return str(-1)
        sets = project.skills_needed
        sets.append(skill_obj)
        database.update(project, skills_needed = sets)
        return skill.object_id
@app.route("/api/skill/delete/<type>/<skill_name>/<int:id>")
def delete_skill(who, skill_name):
    """
    Delete this skill from the user/project.
    If user, it will delete from the corresponding user id.
    If project, it will delete from the corresponding project id.
    @param type: 'user' or 'project'.
    @type type: C{str}
    @param skill_name: The skill name.
    @type skill_name: C{str}
    @param id: user or pm id.
    @type id: C{int}
    @return: skill id if successful, or -1 if fail.
    """
    if not type or not skill_name or not id:
        return str(-1)
    skill_obj = database.get_skill_by_name(skill_name)
    if not skill_obj:
        return str(-1)
    if type == 'user':
        user = database.get_user_by_id(id)
        if not user:
            return str(-1)
        sets = [s for s in user.skill_sets if s.id != skill_obj.id]
        database.update(user, skill_sets=sets)
        return skill_obj.id
    elif type == 'project':
        project = database.get_project_by_id(id)
        if not user:
            return str(-1)
        sets = [s for s in user.skill_sets if s.id != skill_obj.id]
        database.update(project, skills_needed = sets)
        return skill.object_id
@app.route("/api/swipe/<type>/<int:swiper_id>/<int:swipee_id>/<int:direction>")
def swipe(type, user_id, project_id, direction):
    """
    Performs a swipe.
    Direction is 0 for down (negative swipe), 1 for up (positive swipe).
    If it is a yes swipe, returns 1 if there is a match, 0 if there is not (just a normal swipe).
    """
    if type == 'user':
        swipe_obj = database.add_swipe(swiper_id, swipee_id, direction, 1)
    elif type == 'project':
        swipe_obj = database.add_swipe(swipee_id, swiper_id, direction, 0)
    database.insert_obj(swipe_obj)

@app.route("/api/stack/<type>/<int:id>")
def get_stack_for(type, id):
    """
    """
    if type == 'user':
        pass
    elif type == 'pm':
        pass

@app.route("/api/matches/<type>/<int:id>")
def get_matches_for(type, id):
    """
    """
    if type == 'user':
        pass
    elif type == 'pm':
        pass

@app.route("/docs/")
def docs_index():
    """
    Serves the docs's index page
    @return: The docs's main page
    @rtype: C{str}
    """
    return flask.send_from_directory(app.config['DOCS_PATH'], 'index.html')

@app.route("/docs/<path:filename>")
def serve_docs(filename):
    """
    Serves a specific page of the docs
    @param filename: The doc page to get
    @type filename: C{str}
    @return: A HTML page
    @rtype: C{str}
    """
    return flask.send_from_directory(app.config['DOCS_PATH'], filename)

if __name__ == "__main__":
    init_app()
    app.app_context().push()
    app.run(host='0.0.0.0', port=80)

