from db import db

class Match(db.Model):
    """
    This class is the database model for the Match between a L{User} and a L{Project}.
    """
    
    id = db.Column(db.Integer, primary_key=True)
    """
    A Match's id. This is Match's primary key in the database.
    @type: C{int}
    """

    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    """
    The id of the User who is involved in this Match.
    @type: C{int}
    @see: L{User}
    """

    project_id = db.Column(db.Integer, db.ForeignKey('project.id'))
    """
    The id of the Project that is involved in this Match.
    @type: C{int}
    @see: L{Project}
    """

    result = db.Column(db.Integer, nullable=False)
    """
    The result of the Match. 0 = match declined, 1 means match initialized
    (both user and PM swiped up), 2 means match has been accepted by one party, 3 means match has been accepted by both parties.
    @type: C{int}
    """
    def __init__(self, user_id, project_id):
        """
        Construct a Match

        @param user_id: User's id
        @type user_id: C{int}
        @param project_id: Project's id
        @type project_id: C{int}
        """
        self.user_id = user_id
        self.project_id = project_id
        self.result = 1
    def __repr__(self):
        return "<Match with id %r between user %r and project %r>" % (self.id, self.user_id, self.project_id)
