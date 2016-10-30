from flask_sqlalchemy import SQLAlchemy
from database import db

user_skills = db.Table('user_skills',
    db.Column('user_id', db.Integer, db.ForeignKey('user.id')),
    db.Column('skill_id', db.Integer, db.ForeignKey('skill.id'))
)
