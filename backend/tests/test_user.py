import flask
import unittest
import setup
from database.models import User
from database.db import db

class UserTestCase(unittest.TestCase):
    """
    Unit tests for L{User}
    """

    def __init__(self,arg):
        """
        Initializes the test case. Constructs the C{unittest.TestCase} base
        class and creates a Flask app.
        @param arg: The name of the unit test function
        @type arg: C{str}
        """
        super(UserTestCase, self).__init__(arg)
        self.app = setup.create_app()

    def setUp(self):
        """
        Recreate the database.
        """
        db.drop_all()
        db.create_all()

    def tearDown(self):
        """
        Close the database session.
        """
        db.session.remove()

    def test_id_unique(self):
        """
        Two users should have unique id's.
        """
        u1 = User(first_name='u', last_name='1', username='u1',
            email='e1', bio='b')
        u2 = User(first_name='u', last_name='2', username='u2',
            email='e2', bio='b')
        db.session.add(u1)
        db.session.add(u2)
        db.session.commit()
        self.assertNotEqual(u1.id, u2.id)

    def test_username_unique(self):
        """
        Two users should have unique usernames.
        """
        u1 = User(first_name='u', last_name='1', username='u1',
            email='e1', bio='b')
        u2 = User(first_name='u', last_name='2', username='u1',
            email='e2', bio='b')
        db.session.add(u1)
        db.session.add(u2)
        with self.assertRaisesRegexp(Exception, 'IntegrityError'):
            db.session.commit()

    def test_username_not_nullable(self):
        """
        Username is not nullable.
        """
        u1 = User(first_name='u', last_name='1', username=None,
            email='e1', bio='b')
        db.session.add(u1)
        with self.assertRaisesRegexp(Exception, 'OperationalError'):
            db.session.commit()

    def test_first_name_not_nullable(self):
        """
        First name is not nullable.
        """
        u1 = User(first_name=None, last_name='1', username='u1',
            email='e1', bio='b')
        db.session.add(u1)
        with self.assertRaisesRegexp(Exception, 'OperationalError'):
            db.session.commit()

    def test_last_name_not_nullable(self):
        """
        Last name is not nullable.
        """
        u1 = User(first_name='u', last_name=None, username='u1',
            email='e1', bio='b')
        db.session.add(u1)
        with self.assertRaisesRegexp(Exception, 'OperationalError'):
            db.session.commit()

    def test_email_not_nullable(self):
        """
        Email is not nullable.
        """
        u1 = User(first_name='u', last_name='1', username='u1',
            email=None, bio='b')
        db.session.add(u1)
        with self.assertRaisesRegexp(Exception, 'OperationalError'):
            db.session.commit()

    def test_signup_time_not_nullable(self):
        """
        Signup time is not nullable.
        """
        u1 = User(first_name='u', last_name='1', username='u1',
            email='e1', bio='b')
        db.session.add(u1)
        db.session.commit()
        self.assertIsNotNone(u1.signup_time)

    def test_serializable(self):
        """
        A User can be represented as JSON.
        """
        u1 = User(first_name='u', last_name='1', username='u1',
            email='e1', bio='b')
        db.session.add(u1)
        db.session.commit()
        with self.app.test_request_context():
            flask.jsonify(**u1.to_dict())
