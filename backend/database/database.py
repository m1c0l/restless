from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy()


from user import User 
from project import Project
from skill import Skill
from devs_to_projects import devs_to_projects
from project_skills import project_skills
from user_skills import user_skills
from swipe import Swipe
from login import Login

def insert_obj(obj):
    db.session.add(obj)
    db.session.commit()

def get_user_by_id(id):
    return User.query.filter_by(id=id).first()

def get_project_by_id(id):
    return Project.query.filter_by(id=id).first()

def get_project_by_pm_id(pm_id):
    return Project.query.filter_by(pm_id=pm_id).first()

def update(obj, **kwargs):
    for key, value in kwargs.items():
        if hasattr(obj, key):
            setattr(obj, key, value)
        else:
            raise AttributeError("%s has no attribute '%s'" % (obj.__class__, key))
    db.session.commit()
