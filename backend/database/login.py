from flask_sqlalchemy import SQLAlchemy
from database import db

class Login(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(20), db.ForeignKey('user.username'))
    password = db.Column(db.String(50), nullable=False)

    def __init__(self, username, password):
        self.username = username
        self.password = password

    def __repr__(self):
        return "<Login '%s':'%s' id=%r>" % (self.username, self.password, self.id)
