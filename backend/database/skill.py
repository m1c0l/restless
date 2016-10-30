from flask_sqlalchemy import SQLAlchemy
from database import db

class Skill(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    skill_name = db.Column(db.String(64), unique=True, nullable=False)

