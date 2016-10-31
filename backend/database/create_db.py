from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from database import db
import database

app = Flask(__name__)
app.config.from_pyfile('../config.py')
db.init_app(app)

from user import User 
from project import Project
from skill import Skill
from devs_to_projects import devs_to_projects
from project_skills import project_skills
from user_skills import user_skills
from swipe import Swipe
from login import Login

app.app_context().push()

db.drop_all()
db.create_all()

if app.config['INSERT_TEST_DATA'] == False:
    exit()

user_arr = [
    User(first_name="John", last_name="Dough", email="xyz", username="jd", bio="m4st3r h4x0r"),
    User(first_name="Mike", last_name="Li", email="mail", username="mic", bio="admin"),
    User(first_name="Rich", last_name="Sun", email="rich", username="rich", bio="waffle the bunny"),
    User(first_name="Vince", last_name="Jin", email="jinir", username="vince", bio="flask dev")
]
for u in user_arr:
    database.insert_obj(u)

s = Skill(skill_name="Python")
database.insert_obj(s)

project_arr = [
    Project(title="H4cks", description="M4st3r h4cks 4 dayz", pm_id=user_arr[0].id),
    Project(title="H4cks2", description="M4st3r h4cks again", pm_id=user_arr[3].id),
    Project(title="H4cks3", description="M4st3r h4cks in Minecraft", pm_id=user_arr[3].id),
    Project(title="H4cks4", description="M4st3r h4cks IRL", pm_id=user_arr[2].id)
]
for p in project_arr:
    database.insert_obj(p)

swipe_arr = [
    Swipe(user_id=user_arr[1].id, project_id=project_arr[0].id, who_swiped=0),
    Swipe(user_id=user_arr[2].id, project_id=project_arr[1].id, who_swiped=0),
    Swipe(user_id=user_arr[0].id, project_id=project_arr[2].id, who_swiped=0),
    Swipe(user_id=user_arr[3].id, project_id=project_arr[3].id, who_swiped=0)
]
for sw in swipe_arr:
    database.insert_obj(sw)

