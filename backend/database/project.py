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
    current_state = db.Column(db.Integer, nullable=False)
    """
    The project's current state, i.e., if it's recruiting, starting work,
    or finished.
    @type: C{int}
    @todo: make an enum for this
    """
    description = db.Column(db.Text(), nullable=False)
    """
    The project's longer description about what it does.
    @type: C{str}
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
    @type: list of L{skill}
    """

    def __init__(self, title, description, pm_id):
        """
        Constuct a project.

        @param title: The title of the project
        @type title: c{str}
        @param description: The description of the project
        @type description: c{str}
        @param pm_id: The id of the developer who is the PM
        @type pm_id: c{int}
        """
        self.title = title
        self.description = description
        self.pm_id = pm_id
        self.current_state = 0

    def __repr__(self):
        return "<Project '%s' id=%r>" % (self.title, self.id)

    def to_dict(self):
        return {c.name: getattr(self, c.name) for c in self.__table__.columns}
