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

def update(obj, **kwargs):
    for key, value in kwargs.items():
        if hasattr(obj, key):
            setattr(obj, key, value)
        else:
            raise AttributeError("%s has no attribute '%s'" % (obj.__class__, key))
    db.session.commit()

def delete(obj):
    db.session.delete(obj)
    db.session.commit()


# TODO: use *_or_404 for stuff

def get_user_by_id(id):
    return User.query.get(id)

def get_project_by_id(id):
    return Project.query.get(id)

def get_skill_by_id(id):
    return Skill.query.get(id)

def get_swipe_by_id(id):
    return Swipe.query.get(id)

def get_projects_by_pm_id(pm_id):
    return Project.query.filter_by(pm_id=pm_id).all()

def add_new_user(user_obj, password):
    insert_obj(user_obj)
    new_login = Login(username=user_obj.username, password=password)
    insert_obj(new_login)

def validate_login(login_obj):
    return Login.query.filter_by(username=login_obj.username).filter_by(password=login_obj.password).first()
