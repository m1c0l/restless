from flask_sqlalchemy import SQLAlchemy
from database import db

class Login(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(20), nullable=False, db.ForeignKey('user.username'))
    password = db.Column(db.String(50), nullable=False)

