from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from database import db

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(20), unique=True, nullable=False)
    first_name = db.Column(db.Text, nullable=False)
    last_name = db.Column(db.Text, nullable=False)
    email = db.Column(db.Text, nullable=False)
    LinkedIn_profile_id = db.Column(db.Text, nullable=True)
    bio = db.Column(db.Text, nullable=False, default="")
    signup_time = db.Column(db.DateTime, nullable=False)
    projects_i_pm = db.relationship('Project', backref='user', lazy='select')
    projects_i_dev = db.relationship('Project', backref='user', lazy='select')
