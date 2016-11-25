from db import db
from devs_to_projects import devs_to_projects
from user_skills import user_skills
from datetime import datetime

class User(db.Model):
    """
    This class is the database model for the users of Restless. Users are both
    Developers and Project Managers.
    @note: if you change a field in here, make sure to change the corresponding field in the to_dict() function at the bottom!
    """

    id = db.Column(db.Integer, primary_key=True)
    """
    A user's id. This is User's primary key in the database.
    @type: C{int}
    """

    username = db.Column(db.String(20), unique=True, nullable=False)
    """
    The user's username. This is used for authentication and identification
    and cannot be changed.
    @type: C{str}
    """

    first_name = db.Column(db.Text, nullable=False)
    """
    The user's first name.
    @type: C{str}
    """

    last_name = db.Column(db.Text, nullable=False)
    """
    The user's last name.
    @type: C{str}
    """

    email = db.Column(db.Text, nullable=False)
    """
    The user's email.
    @type: C{str}
    """

    LinkedIn_profile_id = db.Column(db.Text, nullable=True)
    """
    The user's LinkedIn profile id.
    @type: C{str}
    """

    bio = db.Column(db.Text, nullable=False, default="")
    """
    A description about the user.
    @type: C{str}
    """
    
    desired_salary = db.Column(db.Integer, nullable=False, default=0)
    """
    The user's desired (hourly?) salary.
    @type: C{int}
    """

    signup_time = db.Column(db.DateTime, nullable=False)
    """
    When the user signed up.
    @type: C{datetime}
    """

    # foreign relationships to PMs, devs, and skills
    projects_managing = db.relationship('Project', backref='projects_managing', lazy='select')
    """
    The projects this user is a Project Manager for.
    @type: list of L{Project}
    """

    projects_developing = db.relationship('Project', secondary=devs_to_projects, backref='projects_developing', lazy='select')
    """
    The projects this user is a Developer for.
    @type: list of L{Project}
    """

    skill_sets = db.relationship('Skill', secondary=user_skills, backref='skill_sets', lazy='select')
    """
    The skills this user has.
    @type: list of L{Skill}
    """

    def __init__(self, username, first_name, last_name, email, bio):
        """
        Construct a User

        @param username: A unique username, not null
        @type username: C{str}
        @param first_name: First name, not null
        @type first_name: C{str}
        @param last_name: last name
        @type last_name: C{str}
        @param email: Email
        @type email: C{str}
        @param bio: A descriptive bio
        @type bio: C{str}
        """
        self.username = username
        self.first_name = first_name
        self.last_name = last_name
        self.email = email
        self.bio = bio
        self.signup_time = datetime.now()

    def __repr__(self):
        return "<User '%s' id=%r>" % (self.username, self.id)

    def to_dict(self):
        """
        Return a dictionary to be returned by the API.
        @rtype: C{dict}
        """

        ret = {
            'id': self.id,
            'username': self.username,
            'first_name': self.first_name,
            'last_name': self.last_name,
            'email': self.email,
            'LinkedIn_profile_id': self.LinkedIn_profile_id,
            'bio': self.bio,
            'signup_time': str(self.signup_time),
            'projects_managing': [project.id for project in self.projects_managing],
            'projects_developing': [project.id for project in self.projects_developing],
            'skill_sets': [skill.id for skill in self.skill_sets]
        }
        #return {c.name: getattr(self, c.name) for c in self.__table__.columns}
        return ret
