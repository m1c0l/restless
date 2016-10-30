from flask_sqlalchemy import SQLAlchemy
from database import db

project_skills = db.Table('project_skills',
    db.Column('project_id', db.Integer, db.ForeignKey('project.id')),
    db.Column('skill_id', db.Integer, db.ForeignKey('skill.id'))
)

