from db import db

class Login(db.Model):
    """
    This class is the database model for login info of the L{User}s.
    """
    id = db.Column(db.Integer, primary_key=True)
    """
    A login's id. This is Login's primary key in the database.
    @type: C{int}
    """
    username = db.Column(db.String(20), db.ForeignKey('user.username'))
    """
    The username of the {User}.
    @type: C{str}
    """
    password = db.Column(db.String(50), nullable=False)
    """
    The user's login password.
    @type: C{str}
    """

    def __init__(self, username, password):
        """
        Construct a Login.

        @param username: The user's username.
        @type username: c{str}
        @param password: The user's password.
        @type password: c{str}
        """
        self.username = username
        self.password = password

    def __repr__(self):
        return "<Login '%s':'%s' id=%r>" % (self.username, self.password, self.id)
