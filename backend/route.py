import json, flask
from flask import Flask, request
from database import database
app = Flask(__name__)

def init_app():
    app.config.from_pyfile("config.py")

    from database import database
    database.db.init_app(app)

    return

def error(msg='Bad Request', status='BAD_REQUEST', code=400): #todo: add logging?
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
    return error(msg='There is no index!')

@app.route("/api/<type>/<id>", methods=['GET', 'POST'])
def retrieve(type,id): # todo: find stuff with the id
    if type == "" or id == "":
        return error(msg='Please fill in your parameters!')
    if type.lower() == 'user':
        try:
            id = int(id)
            response = database.get_user_by_id(id)
            response_dict = response.__dict__
            return flask.jsonify(response_dict)
        except:
            return error(msg='Invalid ID')
    elif type.lower() == 'project':
        pass
    elif type.lower() == 'skill':
        pass
    else:
        return error(msg='Invalid type.')

@app.route("/docs/")
def docs_index():
    return flask.send_from_directory(app.config['DOCS_PATH'], 'index.html')

@app.route("/docs/<path:filename>")
def serve_docs(filename):
    return flask.send_from_directory(app.config['DOCS_PATH'], filename)

if __name__ == "__main__":
    init_app()
    app.app_context().push()
    app.run(host='0.0.0.0', port=80)

