from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from database import db

class Project(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(50), unique=True)
    current_state = db.Column(db.Integer)
    description = db.Text()

