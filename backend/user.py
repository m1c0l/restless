from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from database import db

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    first_name = db.Column(db.Text)
    last_name = db.Column(db.Text)
    email = db.Column(db.Text)
    LinkedIn_profile_id = db.Column(db.Text)
    bio = db.Column(db.Text)
    signup_time = db.Column(db.DateTime)
