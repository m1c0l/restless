from flask import Flask
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://restless:r3stl355@localhost/restless'

db = SQLAlchemy(app)
