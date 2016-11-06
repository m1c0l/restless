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
    db.init_app(app)
    app.app_context().push()
    return app
