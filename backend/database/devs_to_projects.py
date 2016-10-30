from flask_sqlalchemy import SQLAlchemy
from database import db

devs_to_projects = db.Table('devs_to_projects',
    db.Column('user_id', db.Integer, db.ForeignKey('user.id')),
    db.Column('project_id', db.Integer, db.ForeignKey('project.id'))
)

