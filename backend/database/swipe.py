from flask_sqlalchemy import SQLAlchemy
from database import db

class Swipe(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    project_id = db.Column(db.Integer, db.ForeignKey('project.id'))
    result = db.Column(db.Integer, nullable=False)
    who_swiped = db.Column(db.Integer, nullable=False)

