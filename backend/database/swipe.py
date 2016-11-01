from db import db

class Swipe(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    project_id = db.Column(db.Integer, db.ForeignKey('project.id'))
    result = db.Column(db.Integer, nullable=False)
    who_swiped = db.Column(db.Integer, nullable=False)

    def __init__(self, user_id, project_id, who_swiped):
        self.user_id = user_id
        self.project_id = project_id
        self.result = 0
        self.who_swiped = who_swiped

    def __repr__(self):
        return "<Swipe user=%d project=%d id=%r>" % (self.user_id, self.project_id, self.id)
