import json, flask
from flask import Flask, request
from flask_sqlalchemy import SQLAlchemy

def create_app():
    app = Flask(__name__)
    db.init_app(app)
    return app

app = create_app()
app.app_context().push()

def error(msg='Bad Request', status='BAD_REQUEST', code=400): #todo: add logging?
    try:
        er = {
            'error_message' : msg,
            'status': status,
        }
        return flask.jsonify(**er), code
    except:
        return 'Internal server error...', 500
        
@app.route("/")
def index():
    error(msg='There is no index!')

@app.route("/<type>/<id>/", methods=['GET', 'POST']):
def retrieve(type,id): # todo: find stuff with the id
    if type == "" or id == "":
        error(msg='Please fill in your parameters!')
    if type.lower() == 'user':
        pass
    elif type.lower() == 'project':
        pass
    elif type.lower() == 'skill':
        pass
    else:
        error(msg='Invalid type.')

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=80)

