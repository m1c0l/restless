from db import db
from project_skills import project_skills

class Project(db.Model):
    """
    This class is the database model for the projects in Restless.
    """
    id = db.Column(db.Integer, primary_key=True)
    """
    A project's id. This is Project's primary key in the database.
    @type: C{int}
    """
    title = db.Column(db.String(50), unique=True, nullable=False)
    """
    The project's title, which is its name.
    @type: C{str}
    """
    STATE_RECRUITING = 0
    """
    Enum indicating that the project is in the Recruiting state
    @type: C{int}
    """
    STATE_STARTED = 1
    """
    Enum indicating that the project is in the Started state
    @type: C{int}
    """
    STATE_FINISHED = 2
    """
    Enum indicating that the project is in the Finished state
    @type: C{int}
    """
    current_state = db.Column(db.Integer, nullable=False)
    """
    The project's current state, i.e., if it's recruiting, starting work,
    or finished.
    @type: C{enum}
    @enum: L{Project.STATE_RECRUITING}
    @enum: L{Project.STATE_STARTED}
    @enum: L{Project.STATE_FINISHED}
    """
    description = db.Column(db.Text(), nullable=False)
    """
    The project's longer description about what it does.
    @type: C{str}
    """
    pay_range = db.Column(db.Integer, nullable=False, default=0)
    """
    The pay for this project (hourly?). Default to be free.
    @type: C{int}
    """
    pm_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    """
    The id of the project manager of this project, which is a L{User}.
    The project manager creates and leads the project, deciding who to
    add as members.
    @type: C{int}
    """
    skills_needed = db.relationship('Skill', secondary=project_skills, backref='skills_needed', lazy='select')
    """
    A set of skills needed to develop the project.
    @type: list of L{Skill}
    """
    skill_weights = db.relationship('Weighted_Skill', backref='project_of_skill', lazy='select')
    """
    A set of weights for the skills needed, quantifying how important the skill is to the project.
    @type: list of L{Weighted_Skill}
    """

    def __init__(self, title, description, pm_id):
        """
        Constuct a project.

        @param title: The title of the project
        @type title: C{str}
        @param description: The description of the project
        @type description: C{str}
        @param pm_id: The id of the developer who is the PM
        @type pm_id: C{int}
        """
        self.title = title
        self.description = description
        self.pm_id = pm_id
        self.current_state = Project.STATE_RECRUITING
        self.pay_range = 0

    def __repr__(self):
        return "<Project '%s' id=%r>" % (self.title, self.id)

    def __hash__(self):
        """
        Make Project hashable so it can be a dictionary key
        """
        return hash(self.id)

    def __eq__(self, other):
        """
        Project comparator so it can be a dictionary key
        """
        return self.id == other.id

    def __ne__(self, other):
        """
        Project comparator so it can be a dictionary key
        """
        return not(self == other)

    def to_dict(self):
        """
        Return a dictionary to be returned by the API.
        @rtype: C{dict}
        """

        ret = {
            'id': self.id,
            'title': self.title,
            'current_state': self.current_state,
            'description': self.description,
            'pm_id': self.pm_id,
            'skills_needed': [skill.skill_name for skill in self.skills_needed],
            'pay_range': self.pay_range
        }
        #return {c.name: getattr(self, c.name) for c in self.__table__.columns}
        return ret
