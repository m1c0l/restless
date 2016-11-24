import unittest, json
import route
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
        self.app.testing = True
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

    def populate_db(self):
        """
        Adds sample data into the database.
        """
        skill = Skill('python')
        db.session.add(skill)
        db.session.commit()

        user = User(first_name='u', last_name='1', username='u1',
            email='e1', bio='b')
        db.session.add(user)
        db.session.commit()

        project = Project(title="p1", description="blah", pm_id=user.id)
        db.session.add(project)
        db.session.commit()

        # Save copies of the objects
        self.skill = Skill.query.filter_by(id=skill.id).first()
        self.user = User.query.filter_by(id=user.id).first()
        self.project = Project.query.filter_by(id=project.id).first()

    def assertJSON(self, endpoint):
        """
        Validates that the response is JSON and has an integer id.
        @param endpoint: The endpoint to send a GET request to
        @type endpoint: C{str}
        """
        resp = self.client.get(endpoint)
        self.assertEqual('application/json', resp.mimetype)
        data = json.loads(resp.get_data())
        if hasattr(data, 'id'):
            self.assertEqual(type(data['id']), int)

    def test_4xx(self):
        """
        Requesting a bad url should give a 4xx error.
        """
        def assert4xx(endpoint):
            resp = self.client.get(endpoint)
            self.assertGreaterEqual(resp.status_code, 400)
            self.assertLess(resp.status_code, 500)

        assert4xx('/404')
        assert4xx('/api///')
        assert4xx('/api/get//')
        assert4xx('/api/get/skill/')
        assert4xx('/api/get/badtype/1')
        assert4xx('/api/get//1')
        assert4xx('/api/get/skill/1a')

    def test_data_json(self):
        """
        The API should return data as JSON.
        """
        self.populate_db()
        self.assertJSON('/api/get/skill/' + str(self.skill.id))
        self.assertJSON('/api/get/user/' + str(self.user.id))
        self.assertJSON('/api/get/project/' + str(self.project.id))

    def test_data_values(self):
        """
        The returned JSON matches the database objects.
        """
        def assert_vals(endpoint, obj):
            resp = self.client.get(endpoint)
            self.assertDictContainsSubset(obj.to_dict(), json.loads(resp.get_data()))

        self.populate_db()
        assert_vals('/api/get/skill/' + str(self.skill.id), self.skill)
        assert_vals('/api/get/user/' + str(self.user.id), self.user)
        assert_vals('/api/get/project/' + str(self.project.id), self.project)

    def test_error_json(self):
        """
        The API should return errors as JSON.
        """
        self.populate_db()
        self.assertJSON('/404')
        self.assertJSON('/api///')
        self.assertJSON('/api/get//')
        self.assertJSON('/api/get/skill/')
        self.assertJSON('/api/get/badtype/1')
        self.assertJSON('/api/get//1')
        self.assertJSON('/api/get/skill/1a')

    def test_new_user(self):
        """
        Creating new users should insert them to the database correctly.
        """
        data = {
            'username': 'user123',
            'password': 'pass123'
        }
        resp = self.client.post('/api/new_user/', data=data)
        id = json.loads(resp.data)['id']
        self.assertGreater(id, 0)

        resp = self.client.get('/api/get/user/' + str(id))
        user = json.loads(resp.data)
        self.assertEqual(user['id'], id)
        self.assertEqual(user['username'], data['username'])

        # duplicate username
        resp = self.client.post('/api/new_user/', data=data)
        id = json.loads(resp.data)['id']
        self.assertEqual(id, -1)

    def test_new_project(self):
        """
        Creating new projects should insert them to the database correctly.
        """
        self.populate_db()
        data = {
            'title': 'project title',
            'description': 'cool project',
            'pm_id': self.user.id
        }
        resp = self.client.post('/api/new_project/', data=data)
        id = json.loads(resp.data)['id']
        self.assertGreater(id, 0)

        resp = self.client.get('/api/get/project/' + str(id))
        project = json.loads(resp.data)
        self.assertEqual(project['id'], id)
        self.assertEqual(project['title'], data['title'])
        self.assertEqual(project['pm_id'], self.user.id)
        self.assertEqual(project['pm_id'], self.user.id)

        # duplicate title
        resp = self.client.post('/api/new_project/', data=data)
        id = json.loads(resp.data)['id']
        self.assertEqual(id, -1)

    def test_update_user(self):
        """
        Updating a user works correctly. The C{username} cannot be changed.
        """
        self.populate_db()
        user_update = {
            'first_name': 'new first name',
            'email': 'email@gmail.com'
        }
        resp = self.client.post('/api/update/user/' + str(self.user.id),
                                data=user_update)
        updated_user = json.loads(resp.data)
        self.assertEqual(updated_user['first_name'], user_update['first_name'])
        self.assertEqual(updated_user['email'], user_update['email'])
        self.assertEqual(updated_user['last_name'], self.user.last_name)

        #resp = self.client.post('/api/update/user/' + str(self.user.id),
        #                        data={'username': 'new username'})
        #print(json.loads(resp.data)['username'])
        #self.assertGreaterEqual(resp.status_code, 400)

    def test_update_project(self):
        """
        Updating a project works correctly. The C{title} cannot be changed.
        """
        self.populate_db()
        project_update = {
            'description': 'new description',
            'current_state': Project.STATE_FINISHED
        }
        resp = self.client.post('/api/update/project/' + str(self.project.id),
                                data=project_update)
        updated_project = json.loads(resp.data)
        self.assertEqual(updated_project['description'],
                          project_update['description'])
        self.assertEqual(updated_project['current_state'],
                          project_update['current_state'])
        self.assertEqual(updated_project['pm_id'], self.project.pm_id)

        #resp = self.client.post('/api/update/project/' + str(self.project.id),
        #                        data={'title': 'new title'})
        #self.assertGreaterEqual(resp.status_code, 400)

    def test_login(self):
        """
        Logging in returns the correct user id, and fails if bad credentials.
        """
        data = {
            'username': 'user123',
            'password': 'pass123'
        }
        resp_new_user = self.client.post('/api/new_user/', data=data)
        id1 = json.loads(resp_new_user.data)['id']
        resp_login = self.client.post('/api/login/', data=data)
        id2 = json.loads(resp_login.data)['id']
        self.assertEqual(id1, id2)

        data['password'] = 'wrongpasswd'
        resp = self.client.post('/api/login/', data=data)
        self.assertEqual(json.loads(resp.data)['id'], -1)

    def test_docs(self):
        """
        The docs should be served correctly.
        """
        resp = self.client.get('/docs/')
        self.assertEqual(200, resp.status_code)
        resp = self.client.get('/docs/backend-module.html')
        self.assertEqual(200, resp.status_code)
