from db import db

"""
This creates a helper table that maps the many-to-many dependencies between
developers and the projects they're part of.
"""
devs_to_projects = db.Table('devs_to_projects',
    db.Column('user_id', db.Integer, db.ForeignKey('user.id')),
    db.Column('project_id', db.Integer, db.ForeignKey('project.id'))
)

