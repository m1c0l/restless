from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from database import db

app = Flask(__name__)
app.config.from_pyfile('../config.py')
db.init_app(app)

from database import db 
from user import User 
from project import Project
from skill import Skill
from devs_to_projects import devs_to_projects
from project_skills import project_skills
from user_skills import user_skills
from swipe import Swipe
from login import Login

app.app_context().push()

def insert_db_obj(obj):
    db.session.add(obj)
    db.session.commit()

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
    insert_db_obj(u)

s = Skill(skill_name="Python")
insert_db_obj(s)

p = Project(title="H4cks", description="M4st3r h4cks 4 dayz", pm_id=user_arr[0].id)
insert_db_obj(p)

sw = Swipe(user_id=user_arr[0].id, project_id=p.id, who_swiped=0)
insert_db_obj(sw)

