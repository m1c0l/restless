import unittest, route
from database.db import db
from database.models import Skill, User, Project

class RouteTestCase(unittest.TestCase):
    """
    Unit tests for C{route.py}
    """

    def __init__(self,arg):
        """
        Initializes the test case. Constructs the C{unittest.TestCase} base
        class and creates a Flask app.
        @param arg: The name of the unit test function
        @type arg: C{str}
        """
        super(RouteTestCase, self).__init__(arg)
        self.app = route.app
        route.init_app()
        self.app.app_context().push()
        self.client = self.app.test_client()

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

    def test_404(self):
        """
        Requesting a bad url should give 404.
        """
        resp = self.client.get('/404')
        self.assertEqual(404, resp.status_code)
        return

    def test_mimetype_json(self):
        """
        The API should return data as JSON.
        """
        skill = Skill('python')
        db.session.add(skill)
        db.session.commit()
        resp = self.client.get('/api/skill/' + str(skill.id))
        self.assertEqual('application/json', resp.mimetype)

        user = User(first_name='u', last_name='1', username='u1',
            email='e1', bio='b')
        db.session.add(user)
        db.session.commit()
        resp = self.client.get('/api/user/' + str(user.id))
        self.assertEqual('application/json', resp.mimetype)

        project = Project(title="p1", description="blah", pm_id=user.id)
        db.session.add(project)
        db.session.commit()
        resp = self.client.get('/api/project/' + str(project.id))
        self.assertEqual('application/json', resp.mimetype)
