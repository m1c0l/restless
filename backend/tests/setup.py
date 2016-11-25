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
    app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://restless:r3stl355@localhost/restless_unittest'
    db.init_app(app)
    app.app_context().push()
    return app
