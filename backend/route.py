"""
@newfield api: API Documentation
@api: U{https://github.com/m1c0l/restless/blob/master/backend/README.md}
"""

import json, flask, time, os, magic, re
from flask import Flask, request
from database import database
from database.db import db
from database.models import *
app = Flask(__name__)

def current_time():
    """
    Get current time, Unix Epoch style.
    @return: Integer of current time
    @rtype: C{int}
    """
    return int(time.time())

def init_app(testing=False):
    """
    Initializes the app
    @note: only call this one time
    """
    config = os.path.dirname(os.path.realpath(__file__)) + '/config.py'
    app.config.from_pyfile(config)
    if testing:
        app.config['SQLALCHEMY_DATABASE_URI'] = app.config['TESTING_DATABASE_URI']
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
            'error_message': msg,
            'status': status
        }
        return flask.jsonify(**err), code
    except:
        err = {
            'error_message': 'Internal Server Error',
            'status': 'Internal Server Error'
        }
        return flask.jsonify(**err), 500

@app.errorhandler(404)
def handle_404(e):
    """
    Handles 404 errors
    @param: the werkzeug response
    @return: A 404 error
    @rtype: (C{str}. C{int})
    """
    return error(
        'See https://github.com/m1c0l/restless/blob/master/backend/README.md for API usage',
        'NOT_FOUND', 404)

@app.route("/api/update/<type>/<int:id>", methods=['POST'])
def update_info(type, id):
    """
    Updates user data from the mobile app if valid. All other parameters are
    sent via POST request.
    @param type: The type of data to get (eg. C{user}, C{project})
    @type type: C{str}
    @param id: ID of our data.
    @type id: C{int}
    @return: New data if successfully updated, or JSON error
    @rtype: C{str}
    """
    commands = {
        'user' : database.get_user_by_id,
        'project' : database.get_project_by_id,
        #'skill' : database.get_skill_by_id,
    }
    if type not in commands:
        return error(msg='Invalid type')
    try:
        obj = commands[type.lower()](id)
        database.update(obj, **request.form)
        return retrieve(type, id)
    except ValueError:
        return error(msg='Invalid ID')
    except AttributeError as e:
        return error(msg=str(e))

@app.route("/api/new_user/", methods=['POST'])
def new_user(username=None, password=None):
    """
    Creates a new user with specified username and password.
    @param username: Username from input, cannot be blank.
    @type username: C{str}
    @param password: Password from input, cannot be blank.
    @type password: C{str}
    @return: JSON with the ID of the newly created user with specified
             username/password, or -1 if username was already in the database.
    @rtype: C{str}
    """
    if not username:
        username = request.form.get('username')
    if not password:
        password = request.form.get('password')
    id = -1
    if username and password:
        id = database.add_new_user(username, password)
    return flask.jsonify(id=id)

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
        username = request.form.get('username')
    if not password:
        password = request.form.get('password')
    id = -1
    creds = Login(username, password)
    if database.validate_login(creds):
        id = database.get_user_by_username(username).id
    return flask.jsonify(id=id)

@app.route("/api/get/<type>/<int:id>")
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
    database_commands = {
        'user' : database.get_user_by_id,
        'project' : database.get_project_by_id,
        'skill' : database.get_skill_by_id,
    }
    if type.lower() in database_commands:
        try:
            response_dict = database_commands[type.lower()](id).to_dict()
            return flask.jsonify(**response_dict)
        except (AttributeError, ValueError):
            return error(msg='Invalid ID')
    else:
        return error(msg='Invalid type')

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
    id = -1
    if pm_id and title:
        id = database.add_new_project(title=title, description=description, pm_id=pm_id)
    return flask.jsonify(id=id)

@app.route("/api/skill/add/<type>/<skill_name>/<int:id>")
def add_skill(type, skill_name, id):
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
    @return: JSON with skill id if successful, or error JSON
    @rtype: C{str}
    @todo: make this a post request
    """
    if not type or not skill_name or not id:
        return error(msg='missing a parameter')
    skill_obj = database.get_skill_by_name(skill_name)
    if not skill_obj:
        skill_obj = Skill(skill_name)
        database.insert_obj(skill_obj)
    if type == 'user':
        user = database.get_user_by_id(id)
        if not user:
            return error(msg='Invalid user id')
        sets = user.skill_sets
        sets.append(skill_obj)
        database.update(user, skill_sets=sets)
        return flask.jsonify(id=skill_obj.id)
    elif type == 'project':
        project = database.get_project_by_id(id)
        if not project:
            return error(msg='Invalid project id')
        sets = project.skills_needed
        sets.append(skill_obj)
        database.update(project, skills_needed = sets)
        return flask.jsonify(id=skill_obj.id)

@app.route("/api/skill/delete/<type>/<skill_name>/<int:id>")
def delete_skill(type, skill_name, id):
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
    @return: skill id if successful, or error JSON
    @todo: make this a post request
    """
    if not type or not skill_name or not id:
        return error(msg='missing a parameter')
    skill_obj = database.get_skill_by_name(skill_name)
    if not skill_obj:
        return error(msg='skill name does not exist')
    if type == 'user':
        user = database.get_user_by_id(id)
        if not user:
            return error(msg='Invalid user id')
        sets = [s for s in user.skill_sets if s.id != skill_obj.id]
        database.update(user, skill_sets=sets)
        return flask.jsonify(id=skill_obj.id)
    elif type == 'project':
        project = database.get_project_by_id(id)
        if not project:
            return error(msg='Invalid project id')
        sets = [s for s in project.skills_needed if s.id != skill_obj.id]
        database.update(project, skills_needed=sets)
        return flask.jsonify(id=skill_obj.id)

@app.route("/api/swipe/<type>/<int:swiper_id>/<int:swipee_id>/<int:direction>")
def swipe(type, swiper_id, swipee_id, direction):
    """
    Performs a swipe.
    Direction is 0 for down (negative swipe), 1 for up (positive swipe).
    Returns the ID of the swipe's complement if it exists, or -1 if no complement swipe has been found yet.
    A complement swipe represents a swipe between the same user and PM, but with the other party swiping.
    """
    if type == 'user':
        swipe_ret = database.add_swipe(swiper_id, swipee_id, direction, database.Swipe.SWIPER_DEV)
    elif type == 'project':
        swipe_ret = database.add_swipe(swipee_id, swiper_id, direction, database.Swipe.SWIPER_PM)
    else:
        return error(msg='invalid type')
    complement_id = swipe_ret.id if swipe_ret else -1
    return flask.jsonify(id=complement_id)

@app.route("/api/stack/<type>/<int:id>")
def get_stack_for(type, id):
    """
    """
    if type == 'user':
        pass
    elif type == 'pm':
        pass

@app.route("/api/matches/<int:who>/<int:id>/<int:type>")
def get_matches_for(who,id,type=1):
    """
    Get the matches for an ID. Matches are when both parties have swiped each other.
    @param who: 0 for project, 1 for dev.
    @type who: C{int}
    @param id: The id of this person or project.
    @type id: C{int}
    @param type: The type of match. 0 means match declined, 1 means match made, 2 means match accepted.
    @type type: C{int}
    @return: List of IDs of people or projects who we have matched with, with the certain type.
    @rtype: list of L{int}
    """
    return database.get_matches_for(who,id,type)

@app.route("/api/img/get/<type>/<int:id>")
def get_image(type, id):
    """
    Serves the image for a given user or project.
    @param type: One of C{user} or C{project}
    @type type: C{str}
    @param id: The id of the user or project
    @type id: C{int}
    @return: The image for the user/project
    @rtype: An image
    """
    if type not in ['user', 'project']:
        return error('Invalid type')
    if type == 'user' and not database.get_user_by_id(id):
        flask.abort(404)
    if type == 'project' and not database.get_project_by_id(id):
        flask.abort(404)

    img_path = type + '/' + str(id)
    rootdir = os.path.dirname(os.path.realpath(__file__))
    abs_path = os.path.join(rootdir, app.config['IMG_PATH'], img_path)
    try:
        mimetype = magic.from_file(abs_path, mime=True)
        return flask.send_from_directory(app.config['IMG_PATH'],
                                         img_path, mimetype=mimetype)
    except IOError as e:
        return error(msg=str(e))

@app.route("/api/img/upload/<type>/<int:id>", methods=['POST'])
def upload_image(type, id):
    """
    Handles image uploads and saves them on the server. This overwrites the old
    image if there is one.
    @param type: One of C{user} or C{project}
    @type type: C{str}
    @param id: The id of the user or project
    @type id: C{int}
    @return: A HTTP response with status 200 if success, or an error code
    @rtype: C{str}
    @todo: set MAX_CONTENT_LENGTH in config.py?
    """
    if type not in ['user', 'project']:
        return error('Invalid type')
    if type == 'user' and not database.get_user_by_id(id):
        return error('Invalid user id')
    if type == 'project' and not database.get_project_by_id(id):
        return error('Invalid project id')

    # Check that the file is sent
    if 'file' not in request.files:
        return error("No file part on POST data")
    file = request.files['file']
    if file.filename == '':
        return error("No image file selected")

    # Check mimetype to make sure file is an image
    mimetype = magic.from_buffer(file.read(1024), mime=True)
    if not re.match(r'^image\/', mimetype):
        return error("File is not an image")
    file.seek(0)

    # save the file
    img_path = type + '/' + str(id)
    rootdir = os.path.dirname(os.path.realpath(__file__))
    abs_path = os.path.join(rootdir, app.config['IMG_PATH'], img_path)
    file.save(abs_path)
    return error("Success", status="OK", code=200)

@app.route("/api/img/delete/<type>/<int:id>")
def delete_image(type, id):
    """
    Deletes an image from the server.
    @param type: One of C{user} or C{project}
    @type type: C{str}
    @param id: The id of the user or project
    @type id: C{int}
    @return: A HTTP response with status 200 if success, or an error code
    @rtype: C{str}
    """
    img_path = type + '/' + str(id)
    rootdir = os.path.dirname(os.path.realpath(__file__))
    abs_path = os.path.join(rootdir, app.config['IMG_PATH'], img_path)
    try:
        os.unlink(abs_path)
        return error("Success", status="OK", code=200)
    except OSError as e:
        return error(msg=str(e))

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
    app.run(host='0.0.0.0', port=app.config['PORT'])

