from db import db

class Weighted_Skill(db.Model):
    """
    This class is used for L{Project} to assign weights to the needed {Skill}s.
    """

    id = db.Column(db.Integer, primary_key=True)
    """
    ID of this weighted skill; this is a primary key.
    """

    project_id = db.Column(db.Integer, db.ForeignKey('project.id'))

    skill_id = db.Column(db.Integer, db.ForeignKey('skill.id'))
    """
    ID of the skill being weighted.
    @type: C{int}
    @see: L{Skill.id}
    """

    skill_weight = db.Column(db.Float, nullable=False)
    """
    Weight of the skill for the project; can be 0.0-5.0 in increments of 0.5
    @type: C{float}
    """

    def __init__(self, project_id, skill_id, skill_weight):
        """
        Construct a weighted skill.

        @param skill_id: ID of the skill
        @type skill_id: C{int}
        @param skill_weight: weight of the skill
        @type skill_weight: C{float}
        """
        self.skill_id = skill_id
        self.skill_weight = skill_weight

    def __repr__(self):
        return "<Weighted_Skill skill=%d weight=%.2f id=%r>" % (self.skill_id, 
            self.skill_weight, self.id)

    def to_dict(self):
        """
        Return a dictionary to be returned by the API.
        @rtype: C{dict}
        """

        ret = {
            'id': self.id,
            'skill_id': self.skill_id,
            'skill_weight': self.skill_weight
        }
        return ret
