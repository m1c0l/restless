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

app.app_context().push()

db.drop_all()
db.create_all()

u = User(first_name="John", last_name="Dough", email="xyz", username="jd", bio="m4st3r h4x0r")
db.session.add(u)
db.session.commit()

s = Skill(skill_name="Python")
db.session.add(s)
db.session.commit()

p = Project(title="H4cks", description="M4st3r h4cks 4 dayz", pm_id=u.id)
db.session.add(p)
db.session.commit() 

sw = Swipe(user_id=u.id, project_id=p.id, who_swiped=0)
db.session.add(sw)
db.session.commit() 
