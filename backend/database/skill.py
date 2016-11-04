from db import db

class Skill(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    skill_name = db.Column(db.String(64), unique=True, nullable=False)

    def __init__(self, skill_name):
        self.skill_name = skill_name

    def __repr__(self):
        return "<Skill '%s' id=%r>" % (self.skill_name, self.id)

    def to_dict(self):
        """
        Return a dictionary to be returned by the API.
        @type: dictionary
        """
        
        ret = {
            'id' : id,
            'skill_name', skill_name,
        }
        return ret
        #return {c.name: getattr(self, c.name) for c in self.__table__.columns}
