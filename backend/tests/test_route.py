import unittest, json, os
import route
from database.db import db
from database.models import *

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
        route.init_app(testing=True)
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

    def post_json(self, endpoint, dictionary):
        return self.client.post(endpoint, data=json.dumps(dictionary),
                                content_type='application/json')

    def populate_db(self):
        """
        Adds sample data into the database.
        """
        skill = Skill('Python')
        db.session.add(skill)
        db.session.commit()

        user = User(first_name='u', last_name='1', username='u1',
            email='e1', bio='b')
        user.skill_sets.append(skill)
        db.session.add(user)
        db.session.commit()

        user2 = User(first_name='u', last_name='1', username='unique!',
            email='e1', bio='b')
        user2.skill_sets.append(skill)
        db.session.add(user2)
        db.session.commit()

        login = Login(user.username, 'pass123')
        db.session.add(login)
        db.session.commit()

        project = Project(title="p1", description="blah", pm_id=user.id)
        project.skills_needed.append(skill)
        project.skill_weights.append(Weighted_Skill(project.id, skill.id, 5.0))
        db.session.add(project)
        db.session.commit()

        project2 = Project(title="p_unique", description="blah", pm_id=user2.id)
        project2.skills_needed.append(skill)
        project2.skill_weights.append(Weighted_Skill(project.id, skill.id, 5.0))
        db.session.add(project2)
        db.session.commit()

        swipe = Swipe(user.id, project.id, Swipe.RESULT_YES, Swipe.SWIPER_DEV)
        db.session.add(swipe)
        db.session.commit()

        match = Match(user.id, project.id)
        db.session.add(match)
        db.session.commit()

        # Save copies of the objects
        self.skill = Skill.query.filter_by(id=skill.id).first()
        self.user = User.query.filter_by(id=user.id).first()
        self.login = Login.query.filter_by(username=login.username).first()
        self.project = Project.query.filter_by(id=project.id).first()
        self.swipe = Swipe.query.filter_by(user_id=user.id, project_id=project.id).first()
        self.match = Match.query.filter_by(user_id=user.id, project_id=project.id).first()

    def assertJSON(self, endpoint):
        """
        Validates that the response is JSON and has an integer id.
        @param endpoint: The endpoint to send a GET request to
        @type endpoint: C{str}
        """
        resp = self.client.get(endpoint)
        self.assertEqual('application/json', resp.mimetype)
        data = json.loads(resp.get_data())

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
            data = json.loads(resp.get_data())['results'][0]
            self.assertDictContainsSubset(obj.to_dict(), data)

        self.populate_db()
        assert_vals('/api/get/skill/' + str(self.skill.id), self.skill)
        assert_vals('/api/get/user/' + str(self.user.id), self.user)
        assert_vals('/api/get/project/' + str(self.project.id), self.project)

        s2 = Skill('s2')
        db.session.add(s2)
        db.session.commit()
        u2 = User('u2','','','','')
        db.session.add(u2)
        db.session.commit()
        p2 = Project('p2','',self.user.id)
        db.session.add(p2)
        db.session.commit()

        resp = self.client.get('/api/get/user/'+str(self.user.id)+','+str(u2.id))
        data = json.loads(resp.data)['results']
        self.assertEqual(len(data), 2)
        self.assertTrue(data[0]['id'] == self.user.id or data[0]['id'] == u2.id)
        self.assertTrue(data[1]['id'] == self.user.id or data[1]['id'] == u2.id)

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
        resp = self.post_json('/api/new_user/', data)
        id = json.loads(resp.data)['id']
        self.assertGreater(id, 0)

        resp = self.client.get('/api/get/user/' + str(id))
        user = json.loads(resp.data)['results'][0]
        self.assertEqual(user['id'], id)
        self.assertEqual(user['username'], data['username'])

        # duplicate username
        resp = self.post_json('/api/new_user/', data)
        self.assertGreaterEqual(resp.status_code, 400)

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
        resp = self.post_json('/api/new_project/', data)
        id = json.loads(resp.data)['id']
        self.assertGreater(id, 0)

        resp = self.client.get('/api/get/project/' + str(id))
        project = json.loads(resp.data)['results'][0]
        self.assertEqual(project['id'], id)
        self.assertEqual(project['title'], data['title'])
        self.assertEqual(project['pm_id'], self.user.id)
        self.assertEqual(project['pm_id'], self.user.id)

        # duplicate title
        resp = self.post_json('/api/new_project/', data)
        self.assertGreaterEqual(resp.status_code, 400)

    def test_update_user(self):
        """
        Updating a user works correctly. The C{username} cannot be changed.
        """
        self.populate_db()
        user_update = {
            'first_name': 'new first name',
            'email': 'email@gmail.com'
        }
        resp = self.post_json('/api/update/user/'+str(self.user.id), user_update)
        updated_user = json.loads(resp.data)
        self.assertEqual(updated_user['first_name'], user_update['first_name'])
        self.assertEqual(updated_user['email'], user_update['email'])
        self.assertEqual(updated_user['last_name'], self.user.last_name)

        #resp = self.post_json('/api/update/user/' + str(self.user.id),
        #                        {'username': 'new username'})
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
        resp = self.post_json('/api/update/project/' + str(self.project.id),
                                project_update)
        updated_project = json.loads(resp.data)
        self.assertEqual(updated_project['description'],
                          project_update['description'])
        self.assertEqual(updated_project['current_state'],
                          project_update['current_state'])
        self.assertEqual(updated_project['pm_id'], self.project.pm_id)

        #resp = self.post_json('/api/update/project/' + str(self.project.id),
        #                        {'title': 'new title'})
        #self.assertGreaterEqual(resp.status_code, 400)

    def test_change_password(self):
        """
        Tests changing a password.
        """
        self.populate_db()
        user_id = str(self.user.id)

        resp = self.post_json('/api/update/login/0', {'password':'123'})
        self.assertGreaterEqual(resp.status_code, 400)

        username = self.login.username
        old_password = self.login.password
        new_password = 'new password'
        resp = self.post_json('/api/update/login/' + user_id,
                                        { 'password': new_password })
        data = json.loads(resp.data)
        self.assertEqual(data['username'], username)
        self.assertNotIn('password', data) # leave password out of response

        creds = { 'username': username, 'password': old_password }
        resp = self.post_json('/api/login/', creds)
        self.assertGreaterEqual(resp.status_code, 400)

        creds['password'] = new_password
        resp = self.post_json('/api/login/', creds)
        self.assertEqual(resp.status_code, 200)
        self.assertEqual(json.loads(resp.data)['id'], self.user.id)

    def test_login(self):
        """
        Logging in returns the correct user id, and fails if bad credentials.
        """
        data = {
            'username': 'user123',
            'password': 'pass123'
        }
        resp_new_user = self.post_json('/api/new_user/', data)
        id1 = json.loads(resp_new_user.data)['id']
        resp_login = self.post_json('/api/login/', data)
        id2 = json.loads(resp_login.data)['id']
        self.assertEqual(id1, id2)

        data['password'] = 'wrongpasswd'
        resp = self.post_json('/api/login/', data)
        self.assertGreaterEqual(resp.status_code, 400)

    def test_add_skill(self):
        """
        Tests that adding skills works.
        """
        self.populate_db()

        # User
        user_id = str(self.user.id)
        resp = self.client.get('/api/skill/add/user/C++/'+user_id)
        id_cpp = json.loads(resp.data)['id']
        self.assertGreater(id_cpp, 0)
        resp = self.client.get('/api/skill/add/user/Python/'+user_id)
        id_py = json.loads(resp.data)['id']
        self.assertGreater(id_py, 0)
        self.assertNotEqual(id_cpp, id_py)

        resp = self.client.get('/api/get/user/' + user_id)
        user = json.loads(resp.data)['results'][0]
        self.assertEqual(len(user['skill_sets']), 2)
        self.assertIn('C++', user['skill_sets'])
        self.assertIn('Python', user['skill_sets'])

        self.client.get('/api/skill/add/user/C++/'+user_id)
        resp = self.client.get('/api/get/user/' + user_id)
        user = json.loads(resp.data)['results'][0]
        self.assertEqual(len(user['skill_sets']), 2) # no duplicate skills

        # Project
        project_id = str(self.project.id)
        resp = self.client.get('/api/skill/add/project/C++/'+project_id)
        id_cpp2 = json.loads(resp.data)['id']
        self.assertEqual(id_cpp, id_cpp2)
        resp = self.client.get('/api/skill/add/project/Python/' + project_id)
        id_py2 = json.loads(resp.data)['id']
        self.assertEqual(id_py, id_py2)

        resp = self.client.get('/api/get/project/' + project_id)
        project = json.loads(resp.data)['results'][0]
        self.assertEqual(len(project['skills_needed']), 2)
        self.assertIn('C++', project['skills_needed'])
        self.assertIn('Python', project['skills_needed'])

        self.client.get('/api/skill/add/project/C++/'+project_id)
        resp = self.client.get('/api/get/project/' + project_id)
        project = json.loads(resp.data)['results'][0]
        self.assertEqual(len(project['skills_needed']), 2) # no duplicate skills

    def test_delete_skill(self):
        """
        Tests that deleting skills works.
        """
        self.populate_db()

        # User
        user_id = str(self.user.id)
        resp = self.client.get('/api/skill/add/user/C++/' + user_id)
        id_cpp = json.loads(resp.data)['id']
        resp = self.client.get('/api/skill/add/user/Python/' + user_id)
        id_py = json.loads(resp.data)['id']
        resp = self.client.get('/api/skill/delete/user/Python/' + user_id)
        self.assertEqual(json.loads(resp.data)['id'], id_py)

        resp = self.client.get('/api/get/user/' + user_id)
        user = json.loads(resp.data)['results'][0]
        self.assertEqual(len(user['skill_sets']), 1)
        self.assertIn('C++', user['skill_sets'])
        self.assertNotIn('Python', user['skill_sets'])

        # Project
        project_id = str(self.project.id)
        resp = self.client.get('/api/skill/add/project/C++/' + project_id)
        id_cpp = json.loads(resp.data)['id']
        resp = self.client.get('/api/skill/add/project/Python/' + project_id)
        id_py = json.loads(resp.data)['id']
        resp = self.client.get('/api/skill/delete/project/Python/' + project_id)
        self.assertEqual(json.loads(resp.data)['id'], id_py)

        resp = self.client.get('/api/get/project/' + project_id)
        project = json.loads(resp.data)['results'][0]
        self.assertEqual(len(project['skills_needed']), 1)
        self.assertIn('C++', project['skills_needed'])
        self.assertNotIn('Python', project['skills_needed'])

    def test_images_bad_id(self):
        """
        Test uploading and getting images with bad id's
        """
        self.populate_db()
        user_id = str(self.user.id)
        project_id = str(self.project.id)
        rootdir = os.path.dirname(os.path.realpath(__file__))

        gif_file = os.path.join(rootdir, 'data/pic.gif')
        png_file = os.path.join(rootdir, 'data/pic.png')
        text_file = os.path.join(rootdir, 'data/text.txt')
        gif_bad_ext = os.path.join(rootdir, 'data/actually-gif.unknown')

        resp = self.client.post('/api/img/upload/user/-1',
                                    data={'file': open(gif_file)})
        self.assertGreaterEqual(resp.status_code, 400)
        resp = self.client.post('/api/img/upload/project/-1',
                                    data={'file': open(gif_file)})
        self.assertGreaterEqual(resp.status_code, 400)

        resp = self.client.get('/api/img/get/user/-1')
        self.assertGreaterEqual(resp.status_code, 400)
        resp = self.client.get('/api/img/get/project/-1')
        self.assertGreaterEqual(resp.status_code, 400)

    def test_delete_image(self):
        """
        Tests that images are deleted correctly.
        """
        self.populate_db()
        user_id = str(self.user.id)
        rootdir = os.path.dirname(os.path.realpath(__file__))
        png_file = os.path.join(rootdir, 'data/pic.png')

        self.client.post('/api/img/upload/user/' + user_id,
                            data={'file': open(png_file)})
        resp = self.client.get('/api/img/get/user/' + user_id)
        self.assertEqual(resp.status_code, 200)

        resp = self.client.get('/api/img/delete/user/' + user_id)
        self.assertEqual(resp.status_code, 200)
        resp = self.client.get('/api/img/get/user/' + user_id)
        self.assertGreaterEqual(resp.status_code, 400)
        resp = self.client.get('/api/img/delete/user/' + user_id)
        self.assertGreaterEqual(resp.status_code, 400)

    def test_images_no_pic(self):
        """
        Test getting images with no uploaded pictures
        """
        self.populate_db()
        user_id = str(self.user.id)
        project_id = str(self.project.id)

        self.client.get('/api/img/delete/user/' + user_id)
        self.client.get('/api/img/delete/project/' + project_id)

        resp = self.client.get('/api/img/get/user/' + user_id)
        self.assertGreaterEqual(resp.status_code, 400)
        resp = self.client.get('/api/img/get/project/' + project_id)
        self.assertGreaterEqual(resp.status_code, 400)

    def test_images_bad_input(self):
        """
        Test uploading images with bad input
        """
        self.populate_db()
        user_id = str(self.user.id)
        project_id = str(self.project.id)
        rootdir = os.path.dirname(os.path.realpath(__file__))

        gif_file = os.path.join(rootdir, 'data/pic.gif')
        png_file = os.path.join(rootdir, 'data/pic.png')
        text_file = os.path.join(rootdir, 'data/text.txt')
        gif_bad_ext = os.path.join(rootdir, 'data/actually-gif.unknown')

        resp = self.client.post('/api/img/upload/user/' + user_id,
                                    data={'file': 'not a file'})
        self.assertGreaterEqual(resp.status_code, 400)
        resp = self.client.post('/api/img/upload/project/' + project_id,
                                    data={'nofiles': 0})
        self.assertGreaterEqual(resp.status_code, 400)
        resp = self.client.post('/api/img/upload/project/' + project_id,
                                    data={'file': open(text_file)})
        self.assertGreaterEqual(resp.status_code, 400)

    def test_images_good_input(self):
        """
        Test uploading and getting images with good input
        """
        self.populate_db()
        user_id = str(self.user.id)
        project_id = str(self.project.id)
        rootdir = os.path.dirname(os.path.realpath(__file__))

        gif_file = os.path.join(rootdir, 'data/pic.gif')
        png_file = os.path.join(rootdir, 'data/pic.png')
        text_file = os.path.join(rootdir, 'data/text.txt')
        gif_bad_ext = os.path.join(rootdir, 'data/actually-gif.unknown')

        resp = self.client.post('/api/img/upload/user/' + user_id,
                                data={'file': open(gif_file)})
        self.assertEqual(resp.status_code, 200)
        resp = self.client.get('/api/img/get/user/' + user_id)
        self.assertEqual(resp.status_code, 200)
        self.assertEqual(resp.mimetype, 'image/gif')
        self.assertEqual(resp.data, open(gif_file).read())

        resp = self.client.post('/api/img/upload/user/' + user_id,
                                data={'file': open(png_file)})
        self.assertEqual(resp.status_code, 200)
        resp = self.client.get('/api/img/get/user/' + user_id)
        self.assertEqual(resp.status_code, 200)
        self.assertEqual(resp.mimetype, 'image/png')
        self.assertEqual(resp.data, open(png_file).read())

        resp = self.client.post('/api/img/upload/project/' + project_id,
                                data={'file': open(gif_bad_ext)})
        self.assertEqual(resp.status_code, 200)
        resp = self.client.get('/api/img/get/project/' + project_id)
        self.assertEqual(resp.status_code, 200)
        self.assertEqual(resp.mimetype, 'image/gif')
        self.assertEqual(resp.data, open(gif_bad_ext).read())

    def test_get_stack_for(self):
        """
        Tests API for stack algorithm.
        """
        self.populate_db()
        user_id = str(self.user.id)
        project_id = str(self.project.id)

        resp = self.client.get('/api/stack/user/' + user_id)
        stack = json.loads(resp.data)['stack']
        self.assertGreaterEqual(len(stack), 1)

        resp = self.client.get('/api/stack/project/' + project_id)
        stack = json.loads(resp.data)['stack']
        self.assertGreaterEqual(len(stack), 1)

        # bad type
        resp = self.client.get('/api/stack/badtype/' + project_id)
        self.assertGreaterEqual(resp.status_code, 400)

        # bad id
        resp = self.client.get('/api/stack/user/0')
        self.assertGreaterEqual(resp.status_code, 400)
        resp = self.client.get('/api/stack/project/0')
        self.assertGreaterEqual(resp.status_code, 400)

    def test_confirmed(self):
        """
        Test API for getting confirmed devs.
        """
        self.populate_db()

        resp = self.client.get('/api/confirmed/0')
        self.assertGreaterEqual(resp.status_code, 400)

        resp = self.client.get('/api/confirmed/' + str(self.project.id))
        devs = json.loads(resp.data)['results']
        self.assertEqual(len(devs), 0)

        u2 = User('u2','','','','')
        db.session.add(u2)
        db.session.commit()
        u2.projects_developing.append(self.project)
        u3 = User('u3','','','','')
        db.session.add(u3)
        db.session.commit()
        u3.projects_developing.append(self.project)
        resp = self.client.get('/api/confirmed/' + str(self.project.id))
        devs = json.loads(resp.data)['results']
        self.assertEqual(len(devs), 2)
        self.assertIn(u2.id, devs)
        self.assertIn(u3.id, devs)

    def test_debug(self):
        """
        Just run debug-only code for coverage
        """
        self.populate_db()
        objs = [self.user, self.login, self.project, self.skill, self.swipe,
                self.match]
        for obj in objs:
            obj.__repr__()

    def test_docs(self):
        """
        The docs should be served correctly.
        """
        resp = self.client.get('/docs/')
        self.assertEqual(200, resp.status_code)
        resp = self.client.get('/docs/backend-module.html')
        self.assertEqual(200, resp.status_code)
