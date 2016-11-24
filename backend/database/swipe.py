from db import db
from models import User, Project

class Swipe(db.Model):
    """
    This class is the database model for the history of swipes made by developers
    on projects, and by project managers on developers.
    """

    id = db.Column(db.Integer, primary_key=True)
    """
    A Swipe's id. This is Swipe's primary key in the database.
    @type: C{int}
    """

    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    """
    The id of the User who is involved in this swipe.
    @type: C{int}
    @see: L{User.id}
    """

    project_id = db.Column(db.Integer, db.ForeignKey('project.id'))
    """
    The id of the Project that is involved in this swipe.
    @type: C{int}
    @see: L{Project.id}
    """

    RESULT_NO = 0
    """
    Enum indicating that the swipe was a no
    @type: C{int}
    """

    RESULT_YES = 1
    """
    Enum indicating that the swipe was a yes
    @type: C{int}
    """

    result = db.Column(db.Integer, nullable=False)
    """
    The result of the swipe. Whether the swiper likes the project or developer.
    @type: C{enum}
    @enum: L{Swipe.RESULT_NO}
    @enum: L{Swipe.RESULT_YES}
    """

    SWIPER_PM = 0
    """
    Enum indicating that the swipe was made by a Project Manager
    @type: C{int}
    """

    SWIPER_DEV = 1
    """
    Enum indicating that the swipe was made by a Developer
    @type: C{int}
    """

    who_swiped = db.Column(db.Integer, nullable=False)
    """
    Which party did the swipe.
    @type: C{enum}
    @enum: L{Swipe.SWIPER_PM}
    @enum: L{Swipe.SWIPER_DEV}
    """

    def __init__(self, user_id, project_id, result, who_swiped):
        """
        Construct a Swipe

        @param user_id: The User involved in the swipe
        @type user_id: C{int}
        @see: L{User}
        @param project_id: The Project involved in the swipe
        @type project_id: C{int}
        @see: L{Project}
        @param result: Result of swipe
        @type result: L{Swipe.RESULT_NO} or L{Swipe.RESULT_YES}
        @param who_swiped: Which party did the swipe
        @type who_swiped: L{Swipe.SWIPER_PM} or L{Swipe.SWIPER_DEV}
        """
        self.user_id = user_id
        self.project_id = project_id
        self.result = result
        self.who_swiped = who_swiped

    def __repr__(self):
        return "<Swipe user=%d project=%d id=%r>" % (self.user_id,
                                                     self.project_id, self.id)

    def to_dict(self):
        """
        Return a dictionary to be returned by the API.
        @rtype: C{dict}
        """

        ret = {
            'id': self.id,
            'user_id': self.user_id,
            'project_id': self.project_id,
            'result': self.result,
            'who_swiped': self.who_swiped,
        }
        return ret
        #return {c.name: getattr(self, c.name) for c in self.__table__.columns}
