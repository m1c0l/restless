from db import db

class Skill(db.Model):
    """
    This class is the database model for the skill sets that a L{User} 
    can have and are used in a L{Project}.
    """
    id = db.Column(db.Integer, primary_key=True)
    """
    A Skill's id. This is Skill's primary key in the database.
    """
    skill_name = db.Column(db.String(64), unique=True, nullable=False)
    """
    The name of the skill.
    @type: C{str}
    """

    def __init__(self, skill_name):
        """
        Construct a Skill

        @param skill_name: The name of the skill being constructed
        @type skill_name: C{str}
        """
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
