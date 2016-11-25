from flask import Flask
from database.db import db
import database.models

def create_app():
    """
    Create the Flask app for testing.
    @rtype: Flask app
    """
    app = Flask(__name__)
    app.config.from_pyfile('../config.py')
    app.config['SQLALCHEMY_DATABASE_URI'] = app.config['TESTING_DATABASE_URI']
    db.init_app(app)
    app.app_context().push()
    return app
