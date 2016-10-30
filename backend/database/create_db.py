from database import db
from user import User
from project import Project
from skill import Skill
from devs_to_projects import devs_to_projects
from project_skills import project_skills
from user_skills import user_skills
from swipe import Swipe

db.drop_all()
db.create_all()

# insert data
