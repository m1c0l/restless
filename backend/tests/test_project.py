import flask
import unittest
import setup
from database.models import User, Project 
from database.db import db

class ProjectTestCase(unittest.TestCase):
    """
    Unit tests for L{Project}
    """

    def __init__(self,arg):
        """
        Initializes the test case. Constructs the C{unittest.TestCase} base
        class and creates a Flask app.
        @param arg: The name of the unit test function
        @type arg: C{str}
        """
        super(ProjectTestCase, self).__init__(arg)
        self.app = setup.create_app()

    def setUp(self):
        """
        Recreate the database.
        """
        db.drop_all()
        db.create_all()
        self.pm_arr = []
        self.pm_arr.append(User(first_name='u', last_name='1', username='u1',
            email='e1', bio='b'))
        db.session.add(self.pm_arr[0])
        db.session.commit()

    def tearDown(self):
        """
        Close the database session.
        """
        db.session.remove()

    def test_init(self):
        """
        The Project is constructed correctly.
        """
        attrs = {
            'title': 'title',
            'description': 'description',
            'pm_id': self.pm_arr[0]
        }
        project = Project(**attrs)
        for attr, value in attrs.iteritems():
            self.assertEqual(getattr(project, attr), value)

    def test_id_unique(self):
        """
        Two projects should have unique id's.
        """
        p1 = Project(title="p1", description="blah", pm_id=self.pm_arr[0].id)
        p2 = Project(title="p2", description="blah again", pm_id=self.pm_arr[0].id)
        db.session.add(p1)
        db.session.add(p2)
        db.session.commit()
        self.assertNotEqual(p1.id, p2.id)

    def test_title_unique(self):
        """
        Two projects should have unique titles.
        """
        p1 = Project(title="p1", description="blah", pm_id=self.pm_arr[0].id)
        p2 = Project(title="p1", description="blah again", pm_id=self.pm_arr[0].id)
        db.session.add(p1)
        db.session.add(p2)
        with self.assertRaisesRegexp(Exception, 'IntegrityError'):
            db.session.commit()

    def test_title_not_nullable(self):
        """
        Project title shouldn't be null.
        """
        p1 = Project(title=None, description="blah", pm_id=self.pm_arr[0].id)
        db.session.add(p1)
        with self.assertRaisesRegexp(Exception, 'OperationalError'):
            db.session.commit()

    def test_desc_not_nullable(self):
        """
        Project description shouldn't be null.
        """
        p1 = Project(title="p1", description=None, pm_id=self.pm_arr[0].id)
        db.session.add(p1)
        with self.assertRaisesRegexp(Exception, 'OperationalError'):
            db.session.commit()

    def test_state_not_nullable(self):
        """
        Project's current state shouldn't be null.
        """
        p1 = Project(title="p1", description="blah", pm_id=self.pm_arr[0].id)
        db.session.add(p1)
        db.session.commit()
        self.assertIsNotNone(p1.current_state)

    def test_insert_db(self):
        """
        The Projects are inserted into the database correctly.
        """
        count = 10
        for i in range(count):
            p = Project(title=str(i), description="blah", pm_id=self.pm_arr[0].id)
            db.session.add(p)
        db.session.commit()

        # all projects are inserted
        projects = Project.query.all()
        self.assertEqual(count, len(projects))

        # check the title
        for i in range(count):
            q = Project.query.filter_by(title=str(i)).all()
            self.assertEqual(len(q), 1)

    def test_serializable(self):
        """
        A Project can be represented as JSON.
        """
        p1 = Project(title="p1", description="blah", pm_id=self.pm_arr[0].id)
        db.session.add(p1)
        db.session.commit()
        with self.app.test_request_context():
            flask.jsonify(**p1.to_dict())
