from db import db
from project_skills import project_skills

class Project(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(50), unique=True, nullable=False)
    current_state = db.Column(db.Integer, nullable=False)
    description = db.Column(db.Text(), nullable=False)
    pm_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    skills_needed = db.relationship('Skill', secondary=project_skills, backref='skills_needed', lazy='select')

    def __init__(self, title, description, pm_id):
        self.title = title
        self.description = description
        self.pm_id = pm_id
        self.current_state = 0

    def __repr__(self):
        return "<Project '%s' id=%r>" % (self.title, self.id)

    def to_dict(self):
        """
        Return a dictionary to be returned by the API.
        @type: dictionary
        """
        
        ret = {
            'id' : id,
            'title' : title,
            'current_state' : current_state,
            'description' : description,
            'pm_id' : pm_id,
            #'skills_needed' : skills_needed,
        }
        #return {c.name: getattr(self, c.name) for c in self.__table__.columns}
        return ret