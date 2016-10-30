from flask_sqlalchemy import SQLAlchemy
from database import db

class Skill(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    skill_name = db.Column(db.String(64), unique=True, nullable=False)

    def __init__(self, skill_name):
        self.skill_name = skill_name

    def __repr__(self):
        return "<Skill '%s' id=%d>" % (self.skill_name, self.id)
