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

def get_user_by_id(id):
    return User.query.filter_by(id=id).first()

def get_project_by_id(id):
    return Project.query.filter_by(id=id).first()

def get_
