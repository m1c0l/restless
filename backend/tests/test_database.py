import flask, unittest
import setup
from database.models import *
from database.database import *
from database.db import db

class DatabaseTestCase(unittest.TestCase):
    """
    Unit tests for L{database.database}
    """

    def __init__(self,arg):
        """
        Initializes the test case. Constructs the C{unittest.TestCase} base
        class and creates a Flask app.
        @param arg: The name of the unit test function
        @type arg: C{str}
        """
        super(DatabaseTestCase, self).__init__(arg)
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

    def test_insert_obj(self):
        """
        Objects inserted to database correctly
        """
        user = User('user123', '', '', '', '')
        insert_obj(user)
        self.assertIsNotNone(user.id)

        project = Project('title', '', user.id)
        insert_obj(project)
        self.assertIsNotNone(project.id)

    def test_update(self):
        """
        Test updating objects
        """
        user = User('user123', '', '', '', 'test bio')
        insert_obj(user)
        changes = {
            'first_name': 'first',
            'last_name': 'last'
        }
        update(user, **changes)
        self.assertEqual(user.first_name, changes['first_name'])
        self.assertEqual(user.last_name, changes['last_name'])
        self.assertEqual(user.bio, 'test bio')
        
        with self.assertRaises(AttributeError):
            update(user, bad_attr=0)

    def test_delete(self):
        """
        Test deleting objects
        """
        user = User('user123', '', '', '', '')
        insert_obj(user)
        project = Project('title', '', user.id)
        insert_obj(project)
        self.assertIsNotNone(get_project_by_id(project.id))
        delete(project)
        self.assertIsNone(get_project_by_id(project.id))

    def test_get_by(self):
        """
        Test methods like C{get_user_by_id} and C{get_skill_by_name}.
        """
        user = User('user123', '', '', '', '')
        insert_obj(user)
        self.assertEqual(get_user_by_username(user.username).id, user.id)
        self.assertEqual(get_user_by_id(user.id).username, user.username)

        project = Project('title', '', user.id)
        insert_obj(project)
        self.assertEqual(get_project_by_title(project.title).id, project.id)
        self.assertEqual(get_project_by_id(project.id).title, project.title)
        self.assertIn(project, get_projects_by_pm_id(user.id))

        skill = Skill('Python')
        insert_obj(skill)
        self.assertEqual(get_skill_by_name(skill.skill_name).id, skill.id)
        self.assertEqual(get_skill_by_id(skill.id).skill_name, skill.skill_name)

        swipe = Swipe(user.id, project.id, Swipe.RESULT_YES, Swipe.SWIPER_DEV)
        insert_obj(swipe)
        self.assertEqual(get_swipe_by_id(swipe.id).user_id, swipe.user_id)
        self.assertEqual(get_swipe_by_id(swipe.id).result, swipe.result)

    def test_add_swipe(self):
        """
        Test adding a swipe.
        """
        user = User('user1', '', '', '', '')
        insert_obj(user)
        project = Project('title', '', user.id)
        insert_obj(project)

        comp = add_swipe(user.id, project.id, Swipe.RESULT_YES, Swipe.SWIPER_DEV)
        self.assertIsNone(comp)
        comp = add_swipe(user.id, project.id, Swipe.RESULT_YES, Swipe.SWIPER_PM)
        self.assertEqual(comp.user_id, user.id)
        self.assertEqual(comp.project_id, project.id)
        self.assertEqual(comp.result, Swipe.RESULT_YES)
        self.assertEqual(comp.who_swiped, Swipe.SWIPER_DEV)

        user2 = User('user2', '', '', '', '')
        comp = add_swipe(user2.id, project.id, Swipe.RESULT_YES, Swipe.SWIPER_DEV)
        self.assertIsNone(comp)
        comp = add_swipe(user2.id, project.id, Swipe.RESULT_NO, Swipe.SWIPER_PM)
        self.assertIsNone(comp)

        user3 = User('user3', '', '', '', '')
        comp = add_swipe(user2.id, project.id, Swipe.RESULT_NO, Swipe.SWIPER_PM)
        self.assertIsNone(comp)
        comp = add_swipe(user2.id, project.id, Swipe.RESULT_YES, Swipe.SWIPER_DEV)
        self.assertIsNone(comp)

    def test_add_new_user(self):
        """
        Test adding a user.
        """
        id = add_new_user('user1', 'pass123')
        self.assertGreater(id, 0)
        id = add_new_user('user1', 'otherpass')
        self.assertEqual(id, -1)
        id = add_new_user('user2', 'pass', 'firstname')
        self.assertEqual('firstname', get_user_by_id(id).first_name)

    def test_add_new_project(self):
        """
        Test adding a project.
        """
        user_id = add_new_user('user1', 'pass')
        self.assertEqual(add_new_project(None, '', user_id), -1)
        self.assertEqual(add_new_project('p1', '', 0), -1)
        project_id = add_new_project('p1', '', user_id)
        project = get_project_by_id(project_id)
        self.assertEqual(project.title, 'p1')

    def test_login(self):
        """
        Test adding and validating logins
        """
        user = User('user1', '', '', '', '')
        add_user_object(user, 'pass123')
        self.assertTrue(validate_login(Login('user1', 'pass123')))
        self.assertFalse(validate_login(Login('user1', 'wrong pass')))
        self.assertFalse(validate_login(Login('wrong user', 'pass123')))
