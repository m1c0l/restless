import json, flask
from flask import Flask, request
from database import database
app = Flask(__name__)

def init_app():
    """
    Initializes the app
    @note: only call this one time
    """
    app.config.from_pyfile("config.py")
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

@app.route("/api/<type>/<id>", methods=['GET', 'POST'])
def retrieve(type,id): # todo: find stuff with the id
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
        except ValueError:
            return error(msg='Invalid ID')
    else:
        return error(msg='Invalid type.')

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

