from db import db

"""
This helper table maps the many-to-many dependencies between the projects
and the skills they need.
"""
project_skills = db.Table('project_skills',
    db.Column('project_id', db.Integer, db.ForeignKey('project.id')),
    db.Column('skill_id', db.Integer, db.ForeignKey('skill.id'))
)

