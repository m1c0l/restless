from flask_sqlalchemy import SQLAlchemy
from database import db
from devs_to_projects import devs_to_projects
from user_skills import user_skills
from datetime import datetime

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(20), unique=True, nullable=False)
    first_name = db.Column(db.Text, nullable=False)
    last_name = db.Column(db.Text, nullable=False)
    email = db.Column(db.Text, nullable=False)
    LinkedIn_profile_id = db.Column(db.Text, nullable=True)
    bio = db.Column(db.Text, nullable=False, default="")
    signup_time = db.Column(db.DateTime, nullable=False)
    # foreign relationships to PMs, devs, and skills
    projects_managing = db.relationship('Project', backref='projects_managing', lazy='select')
    projects_developing = db.relationship('Project', secondary=devs_to_projects, backref='projects_developing', lazy='select')
    skill_sets = db.relationship('Skill', secondary=user_skills, backref='skill_sets', lazy='select')

    def __init__(self, username, first_name, last_name, email, bio):
        self.username = username
        self.first_name = first_name
        self.last_name = last_name
        self.email = email
        self.bio = bio
        self.signup_time = datetime.now()

    def __repr__(self):
        return "<User '%s' id=%d>" % (self.username, self.id)
