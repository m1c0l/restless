from flask_sqlalchemy import SQLAlchemy
from database import db
from project_skills import project_skills

class Project(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(50), unique=True, nullable=False)
    current_state = db.Column(db.Integer, nullable=False)
    description = db.Column(db.Text(), nullable=False)
    pm_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    project_i_skill = db.relationship('project_skills', secondary=project_skills, backref='project', lazy='select')
